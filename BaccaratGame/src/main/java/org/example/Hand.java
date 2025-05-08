package org.example;

import java.util.ArrayList;
import java.util.List;

// Class đại diện cho tay bài của Player hoặc Banker
public class Hand {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    // Thêm lá bài vào tay
    public void addCard(Card card) {
        cards.add(card);
    }

    // Tính tổng điểm, chỉ lấy hàng đơn vị nếu tổng > 9
    public int getTotalPoints() {
        int total = 0;
        for (Card card : cards) {
            total += card.getPoint();
        }
        return total % 10; // Chỉ lấy hàng đơn vị
    }

    // Kiểm tra xem 2 lá đầu có phải là đôi không (dùng cho Player Pair/Banker Pair)
    public boolean isPair() {
        if (cards.size() >= 2) {
            return cards.get(0).getPoint() == cards.get(1).getPoint();
        }
        return false;
    }

    public List<Card> getCards() {
        return cards;
    }
}
