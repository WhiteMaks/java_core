package core.structures.data;

import core.structures.data.impl.Vector1f;
import core.structures.data.support.Vector1fResolver;
import core.support.ExtTestWatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({Vector1fResolver.class, ExtTestWatcher.class})
public class Vector1fTest {

    @Test
    @DisplayName("Изменение позиции X вектора")
    public void changePositionXTest(Vector1f vector) {
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
    @DisplayName("Длина вектора")
    public void lengthTest(Vector1f vector) {
        assertEquals(
                vector.getX(),
                vector.length(),
                "length"
        );
    }
}
