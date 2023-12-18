package org.example.game;

public enum Sign {
    X,
    Y;

    Sign() {
    }
    public Sign changeSign() {
        return this.equals(X) ? Y : X;
    }

}
