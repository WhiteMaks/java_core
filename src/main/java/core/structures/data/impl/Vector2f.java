package core.structures.data.impl;

public class Vector2f extends Vector1f {
    protected float y;

    public Vector2f(float x, float y) {
        super(x);

        this.y = y;
    }

    public Vector2f() {
        this(
                0.0f,
                0.0f
        );
    }

    @Override
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
