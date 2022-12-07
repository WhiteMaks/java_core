package core.supports;

import java.util.concurrent.ThreadLocalRandom;

public final class Randomizer {

    public static int getPositiveInt() {
        return ThreadLocalRandom.current()
                .nextInt(Integer.MAX_VALUE);
    }

    public static int getNegativeInt() {
        return Math.negateExact(getPositiveInt());
    }
}
