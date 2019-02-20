package ru.npte.sloth.slaffvw.model.draw;

import java.util.ArrayList;
import java.util.List;

public class Row implements Cloneable {

    private List<Letter> letters;

    public Row() {
        this.letters = new ArrayList<>(30);
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    public Row clone() {
        Row row = new Row();
        row.getLetters().addAll(this.letters);
        return row;
    }
}
