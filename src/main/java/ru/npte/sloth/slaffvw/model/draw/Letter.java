package ru.npte.sloth.slaffvw.model.draw;

class Letter {
    private final char character;
    private final Color backGroundColor;
    private final Color fontColor;


    private Letter(char character, Color backGroundColor, Color fontColor) {
        this.character = character;
        this.backGroundColor = backGroundColor;
        this.fontColor = fontColor;
    }

    static boolean isEqualStyles(Letter a, Letter b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null && b == null) {
            return false;
        }
        if (a == null) { // backGroundColor != null here
            return false;
        }
        return a.getBackGroundColor() == b.getBackGroundColor()
                && a.getFontColor() == b.getFontColor();
    }


    char getCharacter() {
        return this.character;
    }

    Color getBackGroundColor() {
        return this.backGroundColor;
    }

    Color getFontColor() {
        return fontColor;
    }

    static boolean isEquals(Letter a, Letter b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null && b == null) {
            return false;
        }
        if (a == null) { // backGroundColor != null here
            return false;
        }
        return a.getCharacter() == b.getCharacter()
                && a.getBackGroundColor() == b.getBackGroundColor()
                && a.getFontColor() == b.getFontColor()
                || a.getCharacter() == ' ' && b.getCharacter() == ' ';
    }

    static Letter createAffectNameLetter(char character, Color color) {
        return new Letter(character, color, Color.COMMON);
    }

    static Letter createAffectRemainingTimeLetter(char character, Color color) {
        return new Letter(character, Color.COMMON, color);
    }

    static Letter createCommonLetter(char character) {
        return new Letter(character, Color.COMMON, Color.COMMON);
    }

}


