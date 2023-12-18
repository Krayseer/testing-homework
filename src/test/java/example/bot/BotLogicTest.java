package example.bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Тесты для класса {@link BotLogic}
 */
public class BotLogicTest {

    private MockBot bot;

    private BotLogic botLogic;

    @Before
    public void setUp() {
        bot = new MockBot();
        botLogic = new BotLogic(bot);
    }

    /**
     * Тестирование обработки команды /test
     * <ol>
     *    <li>Обработка захода в состояние прохождения теста</li>
     *    <li>Обработка правильного ответа на вопрос</li>
     *    <li>Обработка неправильного ответа на вопрос</li>
     *    <li>Обработка завершения теста</li>
     * </ol>
     */
    @Test
    public void testCommandTest() {
        // Подготовка
        User user = new User(1L);
        List<String> botMessages = bot.getMessages();

        // Действие: заход в состояние прохождения теста
        botLogic.processCommand(user, "/test");

        // Проверка
        Assert.assertEquals(State.TEST, user.getState());
        Assert.assertEquals("Вычислите степень: 10^2", botMessages.get(0));

        // Действие: обработка правильного ответа на вопрос
        botLogic.processCommand(user, "100");

        // Проверка
        Assert.assertEquals("Правильный ответ!", botMessages.get(1));
        Assert.assertEquals("Сколько будет 2 + 2 * 2", botMessages.get(2));

        // Действие: обработка неправильного ответа на вопрос
        botLogic.processCommand(user, "8");

        // Проверка
        Assert.assertEquals("Вы ошиблись, верный ответ: 6", botMessages.get(3));
        Assert.assertEquals(1, user.getWrongAnswerQuestions().size());
        Assert.assertEquals("Сколько будет 2 + 2 * 2", user.getWrongAnswerQuestions().get(0).getText());

        // Проверка, что тест завершен
        Assert.assertEquals("Тест завершен", botMessages.get(4));
        Assert.assertEquals(State.INIT, user.getState());
    }

    /**
     * Тестирование обработки команды /notify
     * <ol>
     *     <li>Обработка захода в состояние установки напоминания</li>
     *     <li>Обработка ввода текста напоминания</li>
     *     <li>
     *         Обработка ввода времени, спустя которое нужно отправить напоминание
     *         <ul>
     *             <li>С некорректным значением</li>
     *             <li>С корректным значением</li>
     *         </ul>
     *     </li>
     *     <li>Обработка отправки сообщения спустя время</li>
     * </ol>
     */
    @Test
    public void notifyCommandTest() throws InterruptedException {
        // Подготовка
        User user = new User(1L);
        List<String> botMessages = bot.getMessages();

        // Действие: заход в состояние установки напоминания
        botLogic.processCommand(user, "/notify");

        // Проверка
        Assert.assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        Assert.assertEquals("Введите текст напоминания", bot.getMessages().get(0));

        // Действие: ввод текста напоминания
        botLogic.processCommand(user, "test");

        // Проверка
        Assert.assertTrue(user.getNotification().isPresent());
        Assert.assertEquals("Через сколько секунд напомнить?", bot.getMessages().get(1));
        Assert.assertEquals(State.SET_NOTIFY_DELAY, user.getState());

        // Действие: ввод некорректного времени, спустя которое нужно отправить напоминание
        botLogic.processCommand(user, "abc");
        Assert.assertEquals("Пожалуйста, введите целое число", botMessages.get(2));
        Assert.assertEquals(State.SET_NOTIFY_DELAY, user.getState());

        // Действие: ввод корректного времени
        botLogic.processCommand(user, "1");

        // Проверка
        Assert.assertEquals("Напоминание установлено", bot.getMessages().get(3));
        Assert.assertEquals(State.INIT, user.getState());

        // Проверка отправки сообщения спустя время
        Thread.sleep(1060);
        Assert.assertEquals("Сработало напоминание: 'test'", bot.getMessages().get(4));
    }

    /**
     * Тестирование обработки команды /repeat
     * <ol>
     *     <li>
     *         Тестирование захода в состояние повтора вопросов
     *         <ul>
     *             <li>Когда не имеется вопросов, на которые не был дан неправильный ответ</li>
     *             <li>Когда имеются вопросы, на которые был дан неверный ответ</li>
     *         </ul>
     *     </li>
     *     <li>Обработка правильного ответа на вопрос, который ранее был некорректным</li>
     * </ol>
     */
    @Test
    public void repeatCommandTest() {
        // Подготовка
        User user = new User(1L);
        List<String> botMessages = bot.getMessages();

        // Действие: обработка захода в состояние повтора вопросов, когда нет вопросов для повторения
        botLogic.processCommand(user, "/repeat");

        // Проверка
        Assert.assertEquals("Нет вопросов для повторения", botMessages.get(0));
        Assert.assertEquals(State.INIT, user.getState());

        // Действие: обработка захода в состояние повтора вопросов, на которые был дан неправильный ответ
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "wrongAnswer");
        botLogic.processCommand(user, "/repeat");

        // Проверка
        Assert.assertEquals("Вычислите степень: 10^2", user.getCurrentWrongAnswerQuestion().get().getText());
        Assert.assertEquals(State.REPEAT, user.getState());

        // Действие: правильный ответ на вопрос
        botLogic.processCommand(user, "100");

        // Проверка
        Assert.assertEquals(State.INIT, user.getState());
        Assert.assertEquals(0, user.getWrongAnswerQuestions().size());
    }

}