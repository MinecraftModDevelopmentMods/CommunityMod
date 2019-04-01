package com.mcmoddev.communitymod.gegy.transiconherobrinebutwithbetterpants;

public enum DabDirection {
    LEFT(-1),
    RIGHT(1);

    public final int step;

    DabDirection(int step) {
        this.step = step;
    }
}
