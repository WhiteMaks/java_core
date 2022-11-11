package core.structures.data.impl;

public class Vector3f extends Vector2f {
    protected float z;

    public Vector3f(float x, float y, float z) {
        super(
                x,
                y
        );

        this.z = z;
    }

    public Vector3f() {
        this(
                0.0f,
                0.0f,
                0.0f
        );
    }

    @Override
    public float length() {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
