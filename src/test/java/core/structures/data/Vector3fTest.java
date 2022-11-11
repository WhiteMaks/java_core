package core.structures.data;

import core.structures.data.impl.Vector3f;
import core.structures.data.support.Vector3fResolver;
import core.support.ExtTestWatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({Vector3fResolver.class, ExtTestWatcher.class})
public class Vector3fTest {

    @Test
    @DisplayName("Изменение позиции X вектора")
    public void changePositionXTest(Vector3f vector) {
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
    public void changePositionYTest(Vector3f vector) {
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
    @DisplayName("Изменение позиции Z вектора")
    public void changePositionZTest(Vector3f vector) {
        var expectedPositionZ = new Random()
                .nextFloat();

        vector.setZ(expectedPositionZ);

        assertEquals(
                expectedPositionZ,
                vector.getZ(),
                "position Z"
        );
    }

    @Test
    @DisplayName("Длина вектора")
    public void lengthTest(Vector3f vector) {
        assertEquals(
                (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY() + vector.getZ() * vector.getZ()),
                vector.length(),
                "length"
        );
    }
}
