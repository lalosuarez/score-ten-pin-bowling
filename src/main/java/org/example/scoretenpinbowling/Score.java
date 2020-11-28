package org.example.scoretenpinbowling;

import java.util.Arrays;

public class Score {
    public static final int MAX_NUMBER_OF_FRAMES_PER_GAME = 10;

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
