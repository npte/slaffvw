package ru.npte.sloth.slaffvw.model.draw;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npte.sloth.slaffvw.model.Affect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Screen {

    private static final Logger LOGGER = LoggerFactory.getLogger(Screen.class);

    private List<Row> rows;

    private List<Letter> delimiter;

    private List<Letter> emptyString;

    private final static int MAX_AFFECT_NAME_LENGTH = 20;
    private final static int MAX_AFFECT_ROW_LENGTH = 30;

    public Screen() {
        this.rows = new ArrayList<>();

        delimiter = new ArrayList<>(3);
        for (char c : " : ".toCharArray()) {
            delimiter.add(Letter.createCommonLetter(c));
        }
        emptyString = new ArrayList<>(30);
        for (int i = 0; i < MAX_AFFECT_ROW_LENGTH; i++) {
            emptyString.add(Letter.createCommonLetter(' '));
        }
    }

    public List<DrawString> getDrawStrings(List<Affect> affects) {
        List<DrawString> drawStrings = new ArrayList<>();

        List<Row> newRows = createNewRowsList(affects);

        if (CollectionUtils.isEmpty(rows)) {
            drawStrings.addAll(createDrawStringsFirstTime(newRows));
        } else {
            int size = newRows.size() > rows.size() ? rows.size() : newRows.size();
            for (int i = 0; i < size; i++) {
                drawStrings.addAll(createDrawStrings(rows.get(i), newRows.get(i), i));
            }
            for (int i = size; i < newRows.size(); i++) {
                drawStrings.addAll(createDrawStrings(newRows.get(i).getLetters(), i, 0));
            }
            for (int i = size; i < rows.size(); i++) {
                drawStrings.addAll(createDrawStrings(emptyString, i, 0));
            }
        }

        rows = newRows;
        return drawStrings;
    }

    private Collection<? extends DrawString> createDrawStringsFirstTime(List<Row> rows) {
        List<DrawString> drawStrings = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            drawStrings.addAll(createDrawStrings(rows.get(i).getLetters(), i, 0));
        }

        return drawStrings;
    }

    private List<DrawString> createDrawStrings(List<Letter> letters, int rownum, int colnum) {
        List<DrawString> drawStrings = new ArrayList<>();

        int begin = 0;

        for (int i = 1; i < letters.size(); i++) {
            if (!Letter.isEqualStyles(letters.get(i), letters.get(i - 1))) {
                drawStrings.add(
                        new DrawString(
                                letters.get(i - 1).getFontColor(),
                                letters.get(i - 1).getBackGroundColor(),
                                createString(letters.subList(begin, i)),
                                rownum,
                                colnum + begin
                        )
                );
                begin = i;
            }
        }

        drawStrings.add(
                new DrawString(
                        letters.get(begin).getFontColor(),
                        letters.get(begin).getBackGroundColor(),
                        createString(letters.subList(begin, letters.size())),
                        rownum,
                        colnum + begin
                )
        );

        return drawStrings;
    }

    private Collection<? extends DrawString> createDrawStrings(Row oldRow, Row newRow, int row) {
        List<DrawString> drawStrings = new ArrayList<>();

        List<Letter> oldLetters = oldRow.getLetters();
        List<Letter> newLetters = newRow.getLetters();

        int col = -1;

        List<Letter> tmpLetters = new ArrayList<>();

        for (int i = 0; i < oldLetters.size(); i++) {
            Letter oldLetter = oldLetters.get(i);
            Letter newLetter = newLetters.get(i);

            if (Letter.isEquals(oldLetter, newLetter)) {
                if (CollectionUtils.isNotEmpty(tmpLetters)) {
                    drawStrings.addAll(createDrawStrings(tmpLetters, row, col));
                    tmpLetters = new ArrayList<>();
                    col = -1;
                }
            } else {
                if (col < 0) {
                    col = i;
                }
                tmpLetters.add(newLetter);
            }
        }

        if (CollectionUtils.isNotEmpty(tmpLetters)) {
            drawStrings.addAll(createDrawStrings(tmpLetters, row, col));
        }

        return drawStrings;
    }

    private String createString(List<Letter> tmpLetters) {
        return tmpLetters.stream().map(it -> String.valueOf(it.getCharacter())).collect(Collectors.joining(""));
    }

    private List<Row> createNewRowsList(List<Affect> affects) {
        return affects.stream().map(affect -> {
            Row row = new Row();

            for (char c : formatAffectName(affect.getName()).toCharArray()) {
                row.getLetters().add(Letter.createAffectNameLetter(c, isAbsentPrefferedAffect(affect)));
            }

            for (Letter letter : row.getLetters()) {
                if (letter.getCharacter() == ' ') {
                    letter.setDefaultColor();
                } else {
                    break;
                }
            }

            row.getLetters().addAll(delimiter);

            Row row1 = new Row();

            for (char c : formatAffectDuration(affect.getRemainingTime()).toCharArray()) {
                row1.getLetters().add(Letter.createAffectRemainingTimeLetter(c, isAbsentPrefferedAffect(affect), isAlmostOutAffect(affect)));
            }

            for (Letter letter : row1.getLetters()) {
                if (letter.getCharacter() == ' ') {
                    letter.setDefaultColor();
                } else {
                    break;
                }
            }

            row.getLetters().addAll(row1.getLetters());

            return row;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean isAlmostOutAffect(Affect affect) {
        return affect.getRemainingTime() != null && affect.getRemainingTime() < 60;
    }

    private boolean isAbsentPrefferedAffect(Affect affect) {
        return affect.isPreffered() && affect.getRemainingTime() == null;
    }

    private String formatAffectName(String affectName) {
        String name = affectName;
        if (affectName.length() > 20) {
            name = affectName.substring(0, MAX_AFFECT_NAME_LENGTH);
        }
        return StringUtils.leftPad(name, MAX_AFFECT_NAME_LENGTH);
    }

    private String formatAffectDuration(Integer affectDuration) {
        int min = affectDuration != null ? affectDuration / 60 : 0;
        int sec = affectDuration != null ? affectDuration - min * 60 : 0;
        return min < 10 ? String.format(" %02d:%02d", min, sec) : String.format("%3d:%02d", min, sec);
    }
}
