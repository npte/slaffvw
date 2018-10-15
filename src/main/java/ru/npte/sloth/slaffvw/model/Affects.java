package ru.npte.sloth.slaffvw.model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Affects {

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
            String[] aff = affLine.split(":");
            Integer affSeconds = 0;
            if (StringUtils.isNotBlank(aff[1])) {
                affSeconds = Integer.parseInt(aff[1]);
            }
            newAffects.add(new Affect(aff[0], affSeconds, prefsAffects.contains(aff[0])));
            prefsAffects.remove(aff[0]);
        }

        for (String prefsAffect : prefsAffects) {
            newAffects.add(new Affect(prefsAffect, true));
        }

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
