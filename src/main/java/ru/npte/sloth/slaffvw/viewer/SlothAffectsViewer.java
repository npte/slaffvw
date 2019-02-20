package ru.npte.sloth.slaffvw.viewer;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npte.sloth.slaffvw.model.Affects;
import ru.npte.sloth.slaffvw.model.draw.Color;
import ru.npte.sloth.slaffvw.model.draw.DrawString;
import ru.npte.sloth.slaffvw.model.draw.Screen;

import java.io.IOException;
import java.util.List;

public class SlothAffectsViewer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlothAffectsViewer.class);

    private final Affects affects;
    private TextGraphics textGraphics;
    private TerminalScreen screen;

    private final static String normalColor = "WHITE_1";
    private final static String warningColor = "RED";

    private Screen screenBuffer;


    private final static int MAX_AFFECT_NAME_LENGTH = 20;

    public SlothAffectsViewer(Affects affects) {
        this.affects = affects;
        this.screenBuffer = new Screen();
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            this.screen = new TerminalScreen(terminal);
            this.textGraphics = this.screen.newTextGraphics();
            this.screen.startScreen();
        } catch (IOException e) {
            LOGGER.error("Error", e);
        }
    }

    public void run() {
        try {
            KeyStroke keyStroke = null;
            if (screen != null) {
                screen.clear();
            }
            while (screen != null && keyStroke == null) {
                List<DrawString> drawStrings = screenBuffer.getDrawStrings(affects.getAffects());

                for (DrawString drawString : drawStrings) {
                    LOGGER.debug("Print string {} at row {} col {}", drawString.getDrawString(), drawString.getRow(), drawString.getCol());
                    textGraphics.setForegroundColor(getANSIColor(drawString.getFontColor()));
                    textGraphics.setBackgroundColor(getANSIColor(drawString.getBackGroundColor()));
                    textGraphics.putString(drawString.getCol(), drawString.getRow(), drawString.getDrawString());
                }

                keyStroke = screen.pollInput();
                screen.refresh();
                Thread.sleep(100);
            }
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
        try {
            LOGGER.debug("Stopping screen");
            screen.stopScreen();
        } catch (IOException e) {
            LOGGER.error("Error", e);
        }
    }


    private TextColor.ANSI getANSIColor(Color color) {
        switch (color) {
            case DEFAULT:
                return TextColor.ANSI.DEFAULT;
            case RED:
                return TextColor.ANSI.RED;
            case BLACK:
                return TextColor.ANSI.BLACK;
            default:
                return TextColor.ANSI.DEFAULT;
        }
    }

}
