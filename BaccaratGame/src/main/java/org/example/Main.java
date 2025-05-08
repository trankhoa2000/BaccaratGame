package org.example;

// Main method để thử nghiệm
public class Main {
    public static void main(String[] args) {
        BaccaratGame game = new BaccaratGame();

        // Thêm người chơi
        game.addPlayer("Player007", 1000);
        game.addPlayer("Player008", 500);

        // Giả lập đặt cược (trong vòng 12 giây)
        try {
            Thread.sleep(5000); // Chờ 5 giây để vào vòng đặt cược
            game.placeBet("Player007", "Player", 100);
            game.placeBet("Player007", "PlayerPair", 50);
            game.placeBet("Player008", "Banker", 200);
            game.placeBet("Player008", "Tie", 50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Chờ ván đấu hoàn tất (sẽ tự động chạy sau 12 giây)
        try {
            Thread.sleep(15000); // Chờ để xem kết quả
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}