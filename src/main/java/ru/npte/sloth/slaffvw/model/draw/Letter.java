package ru.npte.sloth.slaffvw.model.draw;

class Letter {
    private final char character;
    private Color backGroundColor;
    private Color fontColor;


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

    void setDefaultColor() {
        fontColor = Color.DEFAULT;
        backGroundColor = Color.DEFAULT;
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
                && a.getFontColor() == b.getFontColor();
    }

    static Letter createAffectNameLetter(char character, boolean isAbsentPrefferedAffect) {
        return new Letter(character, isAbsentPrefferedAffect ? Color.RED : Color.DEFAULT, isAbsentPrefferedAffect ? Color.BLACK : Color.DEFAULT);
    }

    static Letter createAffectRemainingTimeLetter(char character, boolean isAbsentPrefferedAffect, boolean isAlmostOut) {
        Color fontColor = Color.DEFAULT;
        Color bgColor = Color.DEFAULT;
        if (isAlmostOut) {
            fontColor = Color.RED;
        }
        if (isAbsentPrefferedAffect) {
            fontColor = Color.BLACK;
            bgColor = Color.RED;
        }
        return new Letter(character, bgColor, fontColor);
    }

    static Letter createCommonLetter(char character) {
        return new Letter(character, Color.DEFAULT, Color.DEFAULT);
    }

}


