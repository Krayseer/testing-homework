package example.note;

import org.junit.Assert;
import org.junit.Test;

/**
 * Тесты для класса {@link NoteLogic}
 */
public class NoteLogicTest {

    private final NoteLogic noteLogic = new NoteLogic();

    /**
     * Протестировать команду добавления и получения заметки
     */
    @Test
    public void testAddAndGetNotes() {
        String result = noteLogic.handleMessage("/add Test Note");
        Assert.assertEquals("Note added!", result);

        String expectedNotes = "Your notes:\n1.Test Note";
        String actualNotes = noteLogic.handleMessage("/notes");
        Assert.assertEquals(expectedNotes, actualNotes);
    }

    /**
     * Протестировать команду изменения заметки
     */
    @Test
    public void testEditAndGetNotes() {
        noteLogic.handleMessage("/add Test Note");
        String result = noteLogic.handleMessage("/edit Edited Test Note");

        Assert.assertEquals("Note edited!", result);

        String expectedNotes = "Your notes:\n1.Edited Test Note";
        String actualNotes = noteLogic.handleMessage("/notes");
        Assert.assertEquals(expectedNotes, actualNotes);
    }

    /**
     * Протестировать команду удаления заметки
     */
    @Test
    public void testDeleteNotes() {
        noteLogic.handleMessage("/add Test Note");
        noteLogic.handleMessage("/add Test Note 2");
        String result = noteLogic.handleMessage("/del");

        Assert.assertEquals("Note deleted!", result);

        String expectedNotes = "Your notes:\n1.Test Note 2";
        String actualNotes = noteLogic.handleMessage("/notes");
        Assert.assertEquals(expectedNotes, actualNotes);
    }

}
