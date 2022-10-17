package org.example.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private int playerId;
    private String nickname;
    private List<Progresses> progresses = new ArrayList<>();
    private List<Currencies> currencies = new ArrayList<>();
    private List<Items> items = new ArrayList<>();

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Progresses> getProgresses() {
        return progresses;
    }

    public void setProgresses(List<Progresses> progresses) {
        this.progresses = progresses;
    }

    public List<Currencies> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currencies> currencies) {
        this.currencies = currencies;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public void addProgress(Progresses prog) {
        progresses.add(prog);
    }

    public void addCurrency(Currencies cur) {
        currencies.add(cur);
    }

    public void addItem(Items item) {
        items.add(item);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("----- Player -----\n");
        sb.append("ID="+getPlayerId()+"\n");
        sb.append("Nickname="+getNickname()+"\n");
        sb.append("Progresses="+ getProgresses().toString() +"\n");
        sb.append("Currencies="+ getCurrencies().toString() +"\n");
        sb.append("Items="+ getItems().toString() +"\n");
        sb.append("---------------------------------\n");

        return sb.toString();
    }
}
