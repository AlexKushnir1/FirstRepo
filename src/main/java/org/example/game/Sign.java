package org.example.game;

public enum Sign {
    X("x"),
    Y("y"),
    NULL(null);

    private String title;

    Sign(String title) {
        this.title = title;
    }

    public Sign getNextSign() {
        return this.equals(X) ? Y : X;
    }

    public String getTitle() {
        return title;
    }
}
