package org.example.scoretenpinbowling;

import java.util.LinkedList;
import java.util.List;

public class BowlingGame {
    private Player player;
    private List<Frame> frames;

    public BowlingGame(Player player) {
        this.player = player;
        this.frames = new LinkedList<>();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Frame> getFrames() {
        return frames;
    }

    @Override
    public boolean equals(Object obj) {
        return this.player.getName().equals(((BowlingGame)obj).getPlayer().getName());
    }
}
