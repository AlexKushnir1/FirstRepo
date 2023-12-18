package org.example.game;

public enum Sign {
    X,
    Y;

    Sign() {
    }
    public Sign getNextSign() {
        return this.equals(X) ? Y : X;
    }

}
