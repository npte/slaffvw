package ru.npte.sloth.slaffvw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npte.sloth.slaffvw.model.Affects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MockController implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockController.class);

    private final Affects affects;

    private static Map<String, Integer> mockedAffects = new HashMap<>();

    static {
        mockedAffects.put("detect invisibility", 20);
        mockedAffects.put("bless", 49);
        mockedAffects.put("stone skin", 33);
    }

    public MockController(Affects affects) {
        this.affects = affects;
    }


    @Override
    public void run() {
        while (true) {

            String mockedAffectsString = mockedAffects.toString()
                    .replace("{", "")
                    .replace("}", "")
                    .replace(", ", "|")
                    .replace("=", ":");

            LOGGER.info("Put affects {}", mockedAffectsString);

            this.affects.setAffectsList(mockedAffectsString);

            Iterator<Map.Entry<String, Integer>> it = mockedAffects.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Integer> entry = it.next();
                int time = entry.getValue() - 1;
                if (time >= 0) {
                    entry.setValue(time);
                } else {
                    it.remove();
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
