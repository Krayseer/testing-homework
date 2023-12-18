package example.bot;

import java.util.LinkedList;
import java.util.List;

/**
 * {@link Bot Заглушка}
 */
public class MockBot implements Bot {

    private final List<String> messages = new LinkedList<>();

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

}
