package core.structures.data.impl;

import core.structures.data.Vector;

public class Vector1f implements Vector {
    protected float x;

    public Vector1f(float x) {
        this.x = x;
    }

    public Vector1f() {
        this(0.0f);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float length() {
        return x;
    }
}
