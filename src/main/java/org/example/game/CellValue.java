package org.example.game;

public enum CellValue {
    X,
    Y,
    NULL;

    private String title;

    CellValue() {
    }

    public String getTitle() {
        return title;
    }

}
