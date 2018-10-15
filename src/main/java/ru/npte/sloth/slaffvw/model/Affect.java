package ru.npte.sloth.slaffvw.model;

public class Affect {

    private final String name;
    private final Integer remainingTime;
    private boolean preffered;

    Affect(String name, Integer remainingTime, Boolean preffered) {
        this.name = name;
        this.remainingTime = remainingTime;
        this.preffered = preffered;
    }

    Affect(String name, Boolean preffered) {
        this.name = name;
        this.remainingTime = null;
        this.preffered = preffered;
    }

    public String getName() {
        return name;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public boolean isPreffered() {
        return preffered;
    }

    @Override
    public String toString() {
        return this.name + ":" + this.remainingTime;
    }
}
