public class Rule {
    private Dol[][] board;
    private int size;

    public Rule(Dol[][] board, int size, OmokGame game) {
        this.board = board;
        this.size = size;
    }

    public boolean SamSam(int row, int col) {
        if (!(board[row][col] instanceof BlackDol)) {
            return false;
        }

        int openThreeCount = 0;

        if (isOpenThree(row, col, 0, 1)) openThreeCount++;     // →
        if (isOpenThree(row, col, 1, 0)) openThreeCount++;     // ↓
        if (isOpenThree(row, col, 1, 1)) openThreeCount++;     // ↘
        if (isOpenThree(row, col, 1, -1)) openThreeCount++;    // ↙

        return openThreeCount >= 2;
    }

    private boolean isOpenThree(int row, int col, int dRow, int dCol) {
        int count = 1; // 현재 돌 포함
        int blanksLeft = 0;
        int blanksRight = 0;
    
        boolean skipUsedLeft = false;
        boolean skipUsedRight = false;
    
        // ← 왼쪽
        int r = row - dRow;
        int c = col - dCol;
        while (isInBounds(r, c)) {
            if (board[r][c] instanceof BlackDol) {
                count++;
            } else if (board[r][c] == null && !skipUsedLeft) {
                skipUsedLeft = true; // 빈칸 1회 허용
            } else {
                break;
            }
            r -= dRow;
            c -= dCol;
        }

        // 열린지 체크 (왼쪽 끝이 빈칸이면 열린 상태)
        r = row - dRow;
        c = col - dCol;
        while (isInBounds(r, c)) {
            if (board[r][c] == null) {
                blanksLeft = 1;
                break;
            } else if (board[r][c] instanceof BlackDol) {
                r -= dRow;
                c -= dCol;
            } else {
                break;
            }
        }

        // → 오른쪽
        r = row + dRow;
        c = col + dCol;
        while (isInBounds(r, c)) {
            if (board[r][c] instanceof BlackDol) {
                count++;
            } else if (board[r][c] == null && !skipUsedRight) {
                skipUsedRight = true;
            } else {
                break;
            }
            r += dRow;
            c += dCol;
        }

        // 열린지 체크 (오른쪽 끝이 빈칸이면 열린 상태)
        r = row + dRow;
        c = col + dCol;
        while (isInBounds(r, c)) {
            if (board[r][c] == null) {
                blanksRight = 1;
                break;
            } else if (board[r][c] instanceof BlackDol) {
                r += dRow;
                c += dCol;
            } else {
                break;
            }
        }

        // 총 돌이 3개이고 양쪽이 비어 있으면 열린삼
        return count == 3 && (blanksLeft + blanksRight == 2);
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }
}
