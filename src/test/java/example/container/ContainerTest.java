package example.container;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Тест для класса {@link Container}
 */
public class ContainerTest {

    private Container container;
    private Item item1, item2, item3;

    @Before
    public void setUp() {
        container = new Container();
        item1 = new Item(1);
        item2 = new Item(2);
        item3 = new Item(3);
    }

    /**
     * Тест метода {@link Container#add(Item)}
     */
    @Test
    public void testAdd() {
        assertTrue(container.add(item1));
        assertTrue(container.add(item2));
        assertEquals(2, container.size());
    }

    /**
     * Тест метода {@link Container#remove(Item)}
     */
    @Test
    public void testRemove() {
        container.add(item1);
        container.add(item2);

        assertTrue(container.remove(item1));
        assertFalse(container.remove(item3));
        assertEquals(1, container.size());
    }

    /**
     * Тест метода {@link Container#get(int)}
     */
    @Test
    public void testGet() {
        container.add(item1);
        container.add(item2);

        assertEquals(item1, container.get(0));
        assertEquals(item2, container.get(1));
    }

    /**
     * Тест метода {@link Container#size()}
     */
    @Test
    public void testSize() {
        assertEquals(0, container.size());

        container.add(item1);
        container.add(item2);

        assertEquals(2, container.size());
    }

    /**
     * Тест метода {@link Container#contains(Item)}
     */
    @Test
    public void testContains() {
        container.add(item1);
        container.add(item2);

        assertTrue(container.contains(item1));
        assertFalse(container.contains(item3));
    }

}
