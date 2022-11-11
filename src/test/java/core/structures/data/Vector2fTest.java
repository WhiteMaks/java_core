package core.structures.data;

import core.structures.data.impl.Vector2f;
import core.structures.data.support.Vector2fResolver;
import core.support.ExtTestWatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({Vector2fResolver.class, ExtTestWatcher.class})
public class Vector2fTest {

    @Test
    @DisplayName("Изменение позиции X вектора")
    public void changePositionXTest(Vector2f vector) {
        var expectedPositionX = new Random()
                .nextFloat();

        vector.setX(expectedPositionX);

        assertEquals(
                expectedPositionX,
                vector.getX(),
                "position X"
        );
    }

    @Test
    @DisplayName("Изменение позиции Y вектора")
    public void changePositionYTest(Vector2f vector) {
        var expectedPositionY = new Random()
                .nextFloat();

        vector.setY(expectedPositionY);

        assertEquals(
                expectedPositionY,
                vector.getY(),
                "position Y"
        );
    }

    @Test
    @DisplayName("Длина вектора")
    public void lengthTest(Vector2f vector) {
        assertEquals(
                (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY()),
                vector.length(),
                "length"
        );
    }
}
