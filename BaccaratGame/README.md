# các logic chính cần triển khai bao gồm:

1. Tính điểm lá bài: A=1, 2-9=giữ nguyên, 10/J/Q/K=0; tổng điểm chỉ lấy hàng đơn vị.
2. Đặt cược: Hỗ trợ 5 loại cược (Player, Banker, Tie, Player Pair, Banker Pair) với tỷ lệ trả thưởng cụ thể.
3. Quy trình ván đấu: Chia bài, rút lá thứ 3 (theo quy tắc), so điểm, trả thưởng.
4. Quy tắc rút bài: Kiểm tra 8/9 điểm (Natural), Player rút nếu 0-5 điểm, Banker rút dựa trên điểm và lá thứ 3 của Player.
5. Quản lý phase:
   + Phase 1: Đếm ngược 5 giây, cho phép người chơi tham gia.
   + Phase 2: Đặt cược trong 12 giây.
   + Phase 3: Chia bài, so điểm, trả thưởng.