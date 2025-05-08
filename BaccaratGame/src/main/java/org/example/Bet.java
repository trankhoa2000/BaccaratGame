package org.example;

// Class đại diện cho một cược của người chơi
public class Bet {
    private String type; // Loại cược: "Player", "Banker", "Tie", "PlayerPair", "BankerPair"
    private double amount; // Số tiền cược

    public Bet(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
