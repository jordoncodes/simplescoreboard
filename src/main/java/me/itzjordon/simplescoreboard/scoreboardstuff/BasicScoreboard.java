package me.itzjordon.simplescoreboard.scoreboardstuff;

import java.util.Map;

public class BasicScoreboard extends AbstractSimpleScoreboard {

    int maxScore = 15;
    int minScore = 0;
    int currentScore = maxScore;

    public BasicScoreboard(String title) {
        super(title);
    }

    public void add(String text) {
        add(text, getThenDecrementScore());
    }

    @Override
    public void update() {
        super.update();
        currentScore=maxScore;
    }

    @Override
    public void reset() {
        super.reset();
        currentScore = maxScore;
    }

    public void removeLast() {
        remove(currentScore, null);
    }

    public void remove(String text) {
        try {
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                if (entry.getKey().equals(text)) {
                    scores.remove(text);
                }
            }
        }catch (Exception ignored) {}
    }

    public void remove(int score) {
        remove(score, null);
    }

    private int getThenDecrementScore() {
        currentScore--;
        if (currentScore<minScore) {
            throw new IllegalArgumentException("You cannot have more than 16 lines!");
        }
        if (currentScore>maxScore) {
            throw new IllegalArgumentException("Something went wrong.");
        }
        return currentScore+1;
    }
}
