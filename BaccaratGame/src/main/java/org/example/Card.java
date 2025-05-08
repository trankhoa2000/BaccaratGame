package org.example;

// Class đại diện cho một lá bài
public class Card {
    private int value; // Giá trị lá bài (1-13: A=1, 2-9, 10/J/Q/K)

    public Card(int value) {
        this.value = value;
    }

    // Tính điểm lá bài theo luật Baccarat
    public int getPoint() {
        if (value >= 10) return 0; // 10, J, Q, K tính là 0 điểm
        return value; // A=1, 2-9 giữ nguyên giá trị
    }
}
