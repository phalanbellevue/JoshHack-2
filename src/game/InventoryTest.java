package game;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for game.Inventory.
 * Target: 100% statement/line and branch coverage of Inventory.java.
 *
 * Items are built via makeItem() so the suite does not depend on Item's
 * exact constructor signature.
 */
@DisplayName("Inventory")
class InventoryTest {

    private Item a;
    private Item b;
    private Item c;

    @BeforeEach
    void setUp() {
        a = makeItem("a");
        b = makeItem("b");
        c = makeItem("c");
    }

    // ---------------------------------------------------------------
    // Item construction helper
    // ---------------------------------------------------------------

    /**
     * Creates a distinct Item instance using Item's first declared
     * constructor, filling any parameters with harmless defaults.
     * Identity (not equals) is all Inventory cares about.
     */
    private static Item makeItem(String name) {
        try {
            Constructor<?> ctor = Item.class.getDeclaredConstructors()[0];
            Class<?>[] types = ctor.getParameterTypes();
            Object[] args = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                args[i] = defaultValue(types[i], name);
            }
            ctor.setAccessible(true);
            return (Item) ctor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Could not construct game.Item - see JOSHHACK_COVERAGE.md", e);
        }
    }

    private static Object defaultValue(Class<?> type, String name) {
        if (type == String.class) return name;
        if (type == int.class)    return 0;
        if (type == long.class)   return 0L;
        if (type == double.class) return 0.0;
        if (type == float.class)  return 0f;
        if (type == boolean.class) return false;
        if (type == char.class)   return 'x';
        return null;
    }

    // ---------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------

    @Nested
    @DisplayName("constructor")
    class ConstructorTests {

        @Test
        @DisplayName("allocates the requested number of empty slots")
        void allocatesEmptySlots() {
            Inventory inv = new Inventory(3);
            assertNotNull(inv.getItems());
            assertEquals(3, inv.getItems().length);
            for (int i = 0; i < 3; i++) {
                assertNull(inv.getItems()[i]);
            }
        }

        @Test
        @DisplayName("zero capacity produces an empty backing array")
        void zeroCapacity() {
            Inventory inv = new Inventory(0);
            assertEquals(0, inv.getItems().length);
        }
    }

    @Nested
    @DisplayName("get")
    class GetTests {

        @Test
        @DisplayName("returns the item stored at an index")
        void returnsStoredItem() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            assertSame(a, inv.get(0));
        }

        @Test
        @DisplayName("throws when index is out of bounds")
        void outOfBounds() {
            Inventory inv = new Inventory(1);
            assertThrows(ArrayIndexOutOfBoundsException.class, () -> inv.get(5));
        }
    }

    @Nested
    @DisplayName("add")
    class AddTests {

        @Test
        @DisplayName("stores into the first free slot and breaks")
        void storesInFirstFreeSlot() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            assertSame(a, inv.get(0));
            assertNull(inv.get(1));
            assertNull(inv.get(2));
        }

        @Test
        @DisplayName("skips occupied slots (if-branch false then true)")
        void skipsOccupiedSlots() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            inv.add(b);
            inv.add(c);
            assertSame(a, inv.get(0));
            assertSame(b, inv.get(1));
            assertSame(c, inv.get(2));
        }

        @Test
        @DisplayName("silently drops the item when the inventory is full")
        void dropsWhenFull() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            inv.add(b);
            inv.add(c); // no free slot -> loop exits without break
            assertSame(a, inv.get(0));
            assertSame(b, inv.get(1));
            assertTrue(inv.isFull());
        }

        @Test
        @DisplayName("adding to a zero-capacity inventory is a no-op")
        void addToZeroCapacity() {
            Inventory inv = new Inventory(0);
            assertDoesNotThrow(() -> inv.add(a));
            assertEquals(0, inv.getItems().length);
        }
    }

    @Nested
    @DisplayName("remove")
    class RemoveTests {

        @Test
        @DisplayName("removes the first matching instance and clears the slot")
        void removesFirstMatch() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            inv.remove(a);
            assertNull(inv.get(0));
        }

        @Test
        @DisplayName("only removes the first occurrence of a duplicated item")
        void removesOnlyFirstOccurrence() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            inv.add(a);
            inv.remove(a);
            assertNull(inv.get(0));
            assertSame(a, inv.get(1));
        }

        @Test
        @DisplayName("no-op when the item is absent (if-branch stays false)")
        void absentItem() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            inv.remove(b);
            assertSame(a, inv.get(0));
        }

        @Test
        @DisplayName("uses identity, not equals()")
        void identityMatch() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            inv.remove(c);
            assertSame(a, inv.get(0));
        }

        @Test
        @DisplayName("no-op on an empty inventory (loop never enters)")
        void removeFromEmpty() {
            Inventory inv = new Inventory(0);
            assertDoesNotThrow(() -> inv.remove(a));
        }
    }

    @Nested
    @DisplayName("isFull")
    class IsFullTests {

        @Test
        @DisplayName("true when every slot is occupied")
        void trueWhenFull() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            inv.add(b);
            assertTrue(inv.isFull());
        }

        @Test
        @DisplayName("false when a null slot remains")
        void falseWhenPartiallyFilled() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            assertFalse(inv.isFull());
        }

        @Test
        @DisplayName("true for a zero-capacity inventory (vacuous)")
        void trueWhenZeroCapacity() {
            Inventory inv = new Inventory(0);
            assertTrue(inv.isFull());
        }
    }

    @Nested
    @DisplayName("contains")
    class ContainsTests {

        @Test
        @DisplayName("true when the instance is present")
        void trueWhenPresent() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            assertTrue(inv.contains(a));
        }

        @Test
        @DisplayName("false when the instance is absent")
        void falseWhenAbsent() {
            Inventory inv = new Inventory(3);
            inv.add(a);
            assertFalse(inv.contains(b));
        }

        @Test
        @DisplayName("false on an empty inventory (loop never enters)")
        void falseWhenEmpty() {
            Inventory inv = new Inventory(0);
            assertFalse(inv.contains(a));
        }

        @Test
        @DisplayName("uses identity, not equals()")
        void identityMatch() {
            Inventory inv = new Inventory(2);
            inv.add(a);
            assertFalse(inv.contains(c));
        }
    }
}