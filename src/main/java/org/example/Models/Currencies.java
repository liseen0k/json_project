package org.example.Models;

public class Currencies {
    private int id;
    private int playerId;
    private int resourceId;
    private String name;
    private int count;

    public Currencies() {

    }

    public Currencies(int id, int playerId, int resourceId, String name, int count) {
        this.id = id;
        this.playerId = playerId;
        this.resourceId = resourceId;
        this.name = name;
        this.count = count;
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

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "id: " + getId() + ", "
                + "playerId: " + getPlayerId() + ", "
                + "resourceId: " + getResourceId() + ", "
                + "name: " + getName() + ", "
                + "count: " + getCount();
    }
}
