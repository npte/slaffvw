package ru.npte.sloth.slaffvw.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Affects {

    private static final Logger logger = LoggerFactory.getLogger(Affects.class);

    private final List<Affect> affects;
    private final String lock = "LOCK";

    private final static Set<String> preferredAffects = new HashSet<>();

    public Affects() {
        String preferredAffectsProperty = System.getProperty("preferred.affects");
        if (StringUtils.isNotBlank(preferredAffectsProperty)) {
            for (String s : preferredAffectsProperty.split(",")) {
                preferredAffects.add(s.trim());
            }
        }

        affects = new LinkedList<>();
    }

    public void setAffectsList(String affectsLine) {
        List<Affect> newAffects = new LinkedList<>();

        Set<String> prefsAffects = new HashSet<>(preferredAffects);

        for (String affLine : affectsLine.split("\\|")) {
            logger.debug("Spliting string {}", affLine);
            String[] aff = affLine.split(":");
            logger.debug("After split have {} tokens", aff.length);
            Integer affSeconds = 0;
            if (aff.length > 1 && StringUtils.isNotBlank(aff[1])) {
                try {
                    affSeconds = Integer.parseInt(aff[1]);
                } catch (NumberFormatException e) {

                }
            }
            newAffects.add(new Affect(aff[0], affSeconds, prefsAffects.contains(aff[0])));
            prefsAffects.remove(aff[0]);
        }

        for (String prefsAffect : prefsAffects) {
            newAffects.add(new Affect(prefsAffect, true));
        }

        logger.debug("Set new affects: \n {}", newAffects.stream().map(Affect::toString).collect(Collectors.joining("\n,")));

        synchronized(lock) {
            affects.clear();
            affects.addAll(newAffects);
        }
    }

    public List<Affect> getAffects() {
        List<Affect> result;
        synchronized (lock) {
            result = new LinkedList<>(affects);
        }

        result.sort((o1, o2) -> {
            if (o1 == null) return 1;
            if (o2 == null) return -1;
            if (o1.isPreffered() && !o2.isPreffered()) return -1;
            if (!o1.isPreffered() && o2.isPreffered()) return 1;
            if (o1.getRemainingTime() == null) return -1;
            if (o2.getRemainingTime() == null) return 1;
            return o1.getName().compareTo(o2.getName());
        });

        return result;
    }
}
