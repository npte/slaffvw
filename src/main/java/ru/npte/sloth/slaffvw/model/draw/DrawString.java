package ru.npte.sloth.slaffvw.model.draw;

public class DrawString {

    private Color fontColor;
    private Color backGroundColor;
    private String drawString;
    private Integer row;
    private Integer col;

    DrawString(Color fontColor, Color backGroundColor, String drawString, Integer row, Integer col) {
        this.fontColor = fontColor;
        this.backGroundColor = backGroundColor;
        this.drawString = drawString;
        this.row = row;
        this.col = col;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public Color getBackGroundColor() {
        return backGroundColor;
    }

    public String getDrawString() {
        return drawString;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }
}
