package org.example.Models;

public class Items {
    private int id;
    private int playerId;
    private int resourceId;
    private int count;
    private int level;

    public Items(){

    }

    public Items(int id, int playerId, int resourceId, int count, int level) {
        this.id = id;
        this.playerId = playerId;
        this.resourceId = resourceId;
        this.count = count;
        this.level = level;
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

    public int getCount() {
        return count;
    }

    public int getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", "
                + "playerId: " + getPlayerId() + ", "
                + "resourceId: " + getResourceId() + ", "
                + "count: " + getCount() + ", "
                + "level: " + getLevel();
    }
}
