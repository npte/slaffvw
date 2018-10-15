package ru.npte.sloth.slaffvw.viewer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npte.sloth.slaffvw.model.Affect;
import ru.npte.sloth.slaffvw.model.Affects;

import java.io.IOException;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class SlothAffectsViewer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SlothAffectsViewer.class);

    private final Affects affects;
    private TextGraphics textGraphics;
    private TerminalScreen screen;

    private final static String normalColor = "WHITE";
    private final static String warningColor = "RED";

    private final static int MAX_AFFECT_NAME_LENGTH = 20;

    public SlothAffectsViewer(Affects affects) {
        this.affects = affects;

        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(terminal);
            this.textGraphics = this.screen.newTextGraphics();
            this.screen.startScreen();
            this.screen.clear();
        } catch (IOException e) {
            logger.error("Error", e);
        }
    }

    public void run() {
        try {
            while (screen != null) {
                int row = 0;

                for (Affect affect : affects.getAffects()) {
                    logger.debug("Print string {}:{} at row {}", formatAffectName(affect.getName()), formatAffectDuration(affect.getRemainingTime()), row);
                    setDefaultColors(textGraphics);
                    textGraphics.putString(0, row, formatAffectName(affect.getName()));
                    setColors(textGraphics, affect.getRemainingTime());
                    textGraphics.putString(MAX_AFFECT_NAME_LENGTH + 3, row++, formatAffectDuration(affect.getRemainingTime()));

                }

                screen.refresh();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }

    private String formatAffectName(String affectName) {
        String name = affectName;
        if (affectName.length() > 20) {
            name = affectName.substring(0, MAX_AFFECT_NAME_LENGTH);
        }
        return StringUtils.leftPad(name, MAX_AFFECT_NAME_LENGTH) + " : ";
    }

    private String formatAffectDuration(Integer affectDuration) {
        int min = affectDuration != null ? affectDuration / 60 : 0;
        int sec = affectDuration != null ? affectDuration - min * 60 : 0;
        return String.format("%3d:%02d", min, sec);
    }

    private void setDefaultColors(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(getANSIColor(normalColor));
        textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
    }

    private void setColors(TextGraphics textGraphics, Integer value) {
        if (value == null) {
            textGraphics.setBackgroundColor(getANSIColor(warningColor));
            textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
            return;
        }
        if (value < 60) {
            textGraphics.setForegroundColor(getANSIColor(warningColor));
        }
    }

    private TextColor.ANSI getANSIColor(String colorName) {
        try {
            return TextColor.ANSI.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            return TextColor.ANSI.DEFAULT;
        }
    }

}
