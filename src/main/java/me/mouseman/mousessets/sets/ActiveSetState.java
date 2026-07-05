package me.mouseman.mousessets.sets;

public class ActiveSetState {

    private MilestoneSet activeSet;

    public MilestoneSet getActiveSet() {
        return activeSet;
    }

    public void setActiveSet(MilestoneSet activeSet) {
        this.activeSet = activeSet;
    }

    public boolean hasSet() {
        return activeSet != null;
    }
}