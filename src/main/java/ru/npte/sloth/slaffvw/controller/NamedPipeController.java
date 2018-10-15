package ru.npte.sloth.slaffvw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npte.sloth.slaffvw.model.Affects;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class NamedPipeController implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NamedPipeController.class);

    private final Affects affects;
    private RandomAccessFile pipe;

    public NamedPipeController(Affects affects) {

        String pipeName = System.getProperty("pipe.name");
        if (pipeName == null) {
            pipeName = "./slaffvw_named_pipe";
        }

        this.affects = affects;
        try {
            pipe = new RandomAccessFile(pipeName, "rw");
        } catch (FileNotFoundException e) {
            logger.error("Error", e);
        }
    }

    public void run() {
        try {
            while (pipe != null) {
                String affectsLine = pipe.readLine();
                if (affectsLine != null) {
                    logger.debug("Readed string {}", affectsLine);
                    affects.setAffectsList(affectsLine);
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            logger.error("Error", e);
        }
    }
}
