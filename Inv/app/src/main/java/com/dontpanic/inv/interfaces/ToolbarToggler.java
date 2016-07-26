package com.dontpanic.inv.interfaces;


public interface ToolbarToggler {

    public static enum State {
        SCROLL,
        EXPANDED,
        EXPANDED_FULL,
        COLLAPSED
    }

    State getToolbarState();

    boolean animateStateChange(State state);
}
