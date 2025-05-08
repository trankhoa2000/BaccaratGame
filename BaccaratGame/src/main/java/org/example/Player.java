package org.example;

import java.util.ArrayList;
import java.util.List;

// Class đại diện cho người chơi
public class Player {
    private String id; // ID của người chơi
    private double balance; // Số dư của người chơi
    private List<Bet> bets; // Danh sách cược của người chơi

    public Player(String id, double initialBalance) {
        this.id = id;
        this.balance = initialBalance;
        this.bets = new ArrayList<>();
    }

    // Đặt cược: Trừ tiền từ số dư và thêm vào danh sách cược
    public void placeBet(String type, double amount) {
        if (amount <= balance) {
            bets.add(new Bet(type, amount));
            balance -= amount;
        } else {
            throw new IllegalStateException("Không đủ tiền để đặt cược!");
        }
    }

    // Xóa danh sách cược sau mỗi ván
    public void clearBets() {
        bets.clear();
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Bet> getBets() {
        return bets;
    }
}
