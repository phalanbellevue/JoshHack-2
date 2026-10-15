
package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemTest {

    private Item item;

    @BeforeEach
    void setUp() {
        // Arrange: create a new item with all default statistic values.
        item = new Item(
            '!',
            Color.RED,
            "test item",
            "mysterious test item"
        );
    }

    @Test
    void detailsWithStats() {
        // Arrange
        item.modifyAttackValue(3);
        item.modifyThrownAttackValue(2);
        item.modifyRangedAttackValue(4);
        item.modifyDefenseValue(2);
        item.modifyFoodValue(10);

        // Act
        String actualDetails = item.details();

        // Assert
        assertEquals(
            "  attack:3  thrown:3  ranged:4  defense:2  food:10",
            actualDetails
        );
    }

    @Test
    void detailsWithNegativeRangedValue() {
        // Arrange
        item.modifyRangedAttackValue(-1);

        // Act
        String actualDetails = item.details();

        // Assert
        assertEquals("", actualDetails);
    }

    @Test
    void detailsWithDefaultValues() {
        // Arrange
        // The item created in setUp has attack, ranged, defense, and food
        // values of 0. Its thrown attack value defaults to 1.

        // Act
        String actualDetails = item.details();

        // Assert
        assertEquals("", actualDetails);
    }
}