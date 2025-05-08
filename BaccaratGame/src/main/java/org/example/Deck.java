package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Class đại diện cho bộ bài (8 bộ 52 lá)
public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        // Tạo 8 bộ bài, mỗi bộ có 52 lá (13 giá trị x 4 chất)
        for (int deck = 0; deck < 8; deck++) {
            for (int value = 1; value <= 13; value++) {
                for (int suit = 0; suit < 4; suit++) {
                    cards.add(new Card(value));
                }
            }
        }
        shuffle();
    }

    // Xáo bài
    public void shuffle() {
        Collections.shuffle(cards, new Random());
    }

    // Rút một lá bài từ bộ bài
    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Hết bài để rút!");
        }
        return cards.remove(0);
    }
}
