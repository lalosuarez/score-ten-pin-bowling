package org.example.scoretenpinbowling.calculator;

import java.util.Arrays;

public class Score {
    private Player player;
    private Frame[] frames;

    public Score(Player player, Frame[] frames) {
        this.player = player;
        this.frames = frames;
    }

    public Player getPlayer() {
        return player;
    }

    public Frame[] getFrames() {
        return frames;
    }

    @Override
    public boolean equals(Object obj) {
        return this.player.getName().equals(((Score)obj).getPlayer().getName());
    }

    @Override
    public String toString() {
        return "Score{" +
                "player=" + player +
                ", frames=" + Arrays.toString(frames) +
                '}';
    }
}
