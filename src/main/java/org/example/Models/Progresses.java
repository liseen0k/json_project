package org.example.Models;

public class Progresses {
    private int id;
    private int playerId;
    private int resourceId;
    private int score;
    private int maxScore;

    public Progresses() {

    }

    public Progresses(int id, int playerId, int resourceId, int score, int maxScore) {
        this.id = id;
        this.playerId = playerId;
        this.resourceId = resourceId;
        this.score = score;
        this.maxScore = maxScore;
    }

    public int getId() {
        return id;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getScore() {
        return score;
    }

    public int getMaxScore() {
        return maxScore;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", "
                + "playerId: " + getPlayerId() + ", "
                + "resourceId: " + getResourceId() + ", "
                + "score: " + getScore() + ", "
                + "maxScore: " + getMaxScore();
    }
}
