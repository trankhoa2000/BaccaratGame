package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Class chính quản lý trò chơi Baccarat
public class BaccaratGame {
    private Deck deck; // Bộ bài
    private Hand playerHand; // Tay bài của Player
    private Hand bankerHand; // Tay bài của Banker
    private boolean gameEnded; // Trạng thái ván đấu (đã kết thúc hay chưa)
    private String result; // Kết quả ván đấu: "Player", "Banker", "Tie"
    private List<Player> players; // Danh sách người chơi
    private ScheduledExecutorService scheduler; // Timer để đếm ngược
    private boolean bettingPhase; // Đang trong giai đoạn đặt cược

    public BaccaratGame() {
        deck = new Deck();
        playerHand = new Hand();
        bankerHand = new Hand();
        gameEnded = false;
        result = null;
        players = new ArrayList<>();
        scheduler = Executors.newScheduledThreadPool(1);
        bettingPhase = false;
    }

    // Thêm người chơi vào trò chơi
    public void addPlayer(String playerId, double initialBalance) {
        players.add(new Player(playerId, initialBalance));
        System.out.println("Người chơi " + playerId + " đã tham gia với số dư: " + initialBalance);
        // Nếu đây là người chơi đầu tiên, bắt đầu đếm ngược
        if (players.size() == 1) {
            startCountdown();
        }
    }

    // Phase 1: Đếm ngược 5 giây để bắt đầu ván
    private void startCountdown() {
        System.out.println("Game bắt đầu trong 5 giây...");
        scheduler.schedule(() -> {
            System.out.println("Đếm ngược kết thúc, bắt đầu vòng đặt cược!");
            startBettingPhase();
        }, 5, TimeUnit.SECONDS);
    }

    // Phase 2: Vòng đặt cược 12 giây
    private void startBettingPhase() {
        bettingPhase = true;
        System.out.println("Vòng đặt cược bắt đầu, bạn có 12 giây để đặt cược!");
        scheduler.schedule(() -> {
            bettingPhase = false;
            System.out.println("Vòng đặt cược kết thúc, tiến hành chia bài!");
            playRound();
        }, 12, TimeUnit.SECONDS);
    }

    // Người chơi đặt cược
    public void placeBet(String playerId, String betType, double amount) {
        if (!bettingPhase) {
            throw new IllegalStateException("Không thể đặt cược ngoài vòng đặt cược!");
        }
        Player player = findPlayer(playerId);
        if (player != null) {
            player.placeBet(betType, amount);
            System.out.println("Người chơi " + playerId + " đặt cược " + betType + " với số tiền: " + amount);
        }
    }

    // Tìm người chơi theo ID
    private Player findPlayer(String playerId) {
        return players.stream()
                      .filter(p -> p.getId().equals(playerId))
                      .findFirst()
                      .orElse(null);
    }

    // Phase 3: Chơi một ván đấu
    private void playRound() {
        // Bước 1: Chia 2 lá bài đầu tiên cho Player và Banker
        dealInitialCards();
        // Bước 2: Xử lý rút lá thứ 3 theo quy tắc
        handleThirdCardRules();
        // Bước 3: Xác định kết quả ván đấu
        determineResult();
        // Bước 4: Trả thưởng cho người chơi
        settleBets();

        // In kết quả ván đấu
        System.out.println("Kết quả ván đấu:");
        System.out.println("Player Points: " + playerHand.getTotalPoints());
        System.out.println("Banker Points: " + bankerHand.getTotalPoints());
        System.out.println("Kết quả: " + result);

        // Reset ván mới
        resetGame();
        if (!players.isEmpty()) {
            startCountdown(); // Bắt đầu ván mới nếu còn người chơi
        }
    }

    // Bước 1: Chia 2 lá đầu tiên cho Player và Banker
    private void dealInitialCards() {
        playerHand.addCard(deck.draw());
        bankerHand.addCard(deck.draw());
        playerHand.addCard(deck.draw());
        bankerHand.addCard(deck.draw());
    }

    // Bước 2: Xử lý rút lá thứ 3 theo quy tắc Baccarat
    private void handleThirdCardRules() {
        int playerPoints = playerHand.getTotalPoints();
        int bankerPoints = bankerHand.getTotalPoints();

        // Kiểm tra nếu một bên có 8 hoặc 9 điểm (Natural)
        if (playerPoints >= 8 || bankerPoints >= 8) {
            return; // Không rút thêm bài, kết thúc
        }

        // Quy tắc rút lá thứ 3 cho Player
        boolean playerDraws = false;
        Card playerThirdCard = null;
        if (playerPoints <= 5) {
            playerDraws = true;
            playerThirdCard = deck.draw();
            playerHand.addCard(playerThirdCard);
        }

        // Quy tắc rút lá thứ 3 cho Banker
        if (!playerDraws) {
            // Trường hợp Player không rút (6 hoặc 7 điểm)
            if (bankerPoints <= 5) {
                bankerHand.addCard(deck.draw());
            }
        } else {
            // Trường hợp Player rút lá thứ 3
            int playerThirdCardValue = playerThirdCard.getPoint();
            if (bankerPoints <= 2) {
                bankerHand.addCard(deck.draw());
            } else if (bankerPoints == 3) {
                if (playerThirdCardValue != 8) {
                    bankerHand.addCard(deck.draw());
                }
            } else if (bankerPoints == 4) {
                if (playerThirdCardValue >= 2 && playerThirdCardValue <= 7) {
                    bankerHand.addCard(deck.draw());
                }
            } else if (bankerPoints == 5) {
                if (playerThirdCardValue >= 4 && playerThirdCardValue <= 7) {
                    bankerHand.addCard(deck.draw());
                }
            } else if (bankerPoints == 6) {
                if (playerThirdCardValue == 6 || playerThirdCardValue == 7) {
                    bankerHand.addCard(deck.draw());
                }
            }
            // Nếu Banker có 7 điểm, không rút
        }
    }

    // Bước 3: Xác định kết quả ván đấu
    private void determineResult() {
        int playerPoints = playerHand.getTotalPoints();
        int bankerPoints = bankerHand.getTotalPoints();

        if (playerPoints > bankerPoints) {
            result = "Player";
        } else if (bankerPoints > playerPoints) {
            result = "Banker";
        } else {
            result = "Tie";
        }
        gameEnded = true;
    }

    // Bước 4: Trả thưởng và hoàn tiền (nếu hòa)
    private void settleBets() {
        for (Player player : players) {
            double totalPayout = 0.0;
            for (Bet bet : player.getBets()) {
                double payout = calculatePayout(bet.getType(), bet.getAmount());
                totalPayout += payout;
                System.out.println("Người chơi " + player.getId() + " cược " + bet.getType() + ": Nhận thưởng " + payout);
            }
            // Nếu hòa, hoàn lại tiền cược Player và Banker
            if (result.equals("Tie")) {
                for (Bet bet : player.getBets()) {
                    if (bet.getType().equals("Player") || bet.getType().equals("Banker")) {
                        totalPayout += bet.getAmount();
                        System.out.println("Người chơi " + player.getId() + " được hoàn tiền cược " + bet.getType() + ": " + bet.getAmount());
                    }
                }
            }
            player.setBalance(player.getBalance() + totalPayout);
            System.out.println("Số dư mới của " + player.getId() + ": " + player.getBalance());
            player.clearBets();
        }
    }

    // Tính tiền thắng cược dựa trên loại cược
    private double calculatePayout(String betType, double betAmount) {
        if (!gameEnded) {
            return 0.0;
        }

        double payout = 0.0;
        switch (betType) {
            case "Player":
                if (result.equals("Player")) {
                    payout = betAmount * 1; // Tỷ lệ 1:1
                }
                break;
            case "Banker":
                if (result.equals("Banker")) {
                    payout = betAmount * 0.95; // Tỷ lệ 1:1, trừ 5% hoa hồng
                }
                break;
            case "Tie":
                if (result.equals("Tie")) {
                    payout = betAmount * 8; // Tỷ lệ 8:1
                }
                break;
            case "PlayerPair":
                if (playerHand.isPair()) {
                    payout = betAmount * 11; // Tỷ lệ 11:1
                }
                break;
            case "BankerPair":
                if (bankerHand.isPair()) {
                    payout = betAmount * 11; // Tỷ lệ 11:1
                }
                break;
        }
        return payout;
    }

    // Reset ván đấu để bắt đầu ván mới
    private void resetGame() {
        playerHand = new Hand();
        bankerHand = new Hand();
        gameEnded = false;
        result = null;
        deck = new Deck();
    }

    // Getter
    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getBankerHand() {
        return bankerHand;
    }

    public String getResult() {
        return result;
    }
}