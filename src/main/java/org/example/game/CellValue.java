package org.example.game;

public enum CellValue {
    X("x"),
    Y("y"),
    NULL(null);

    private String title;

    CellValue(String title) {
        this.title = title;
    }

    public CellValue getNextSign() {
        return this.equals(X) ? Y : X;
    }

    public String getTitle() {
        return title;
    }
}
