class WinChecker {
    private Dol[][] board;
    private int size;
    private OmokGame game;

    public WinChecker(Dol[][] board, int size, OmokGame game) {
        this.board = board;
        this.size = size;
        this.game = game;       
    }
    public boolean checkWin(int lastRow, int lastCol) {
        for(int row=0;row<size;row++) {
            for(int col =0; col<size;col++) {
                if(board[row][col] != null) {
                    if(board[row+1][col-1] != null && board[row+1][col-1].getClass() == board[row][col].getClass()) {
                        if(board[row+2][col-2] != null && board[row+2][col-2].getClass() == board[row+1][col-1].getClass()) {
                            if(board[row+3][col-3] != null && board[row+3][col-3].getClass() == board[row+2][col-2].getClass()) {
                                if(board[row+4][col-4] != null && board[row+4][col-4].getClass() == board[row+3][col-3].getClass()) {
                                    return game.isGameOver = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int row=0;row<size;row++) {
            for(int col =0; col<size;col++) {
                if(board[row][col] != null) {
                    if(board[row+1][col+1] != null && board[row+1][col+1].getClass() == board[row][col].getClass()) {
                        if(board[row+2][col+2] != null && board[row+2][col+2].getClass() == board[row+1][col+1].getClass()) {
                            if(board[row+3][col+3] != null && board[row+3][col+3].getClass() == board[row+2][col+2].getClass()) {
                                if(board[row+4][col+4] != null && board[row+4][col+4].getClass() == board[row+3][col+3].getClass()) {
                                    return game.isGameOver = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int row=0;row<size;row++) {
            for(int col =0; col<size;col++) {
                if(board[row][col] != null) {
                    if(board[row+1][col] != null && board[row+1][col].getClass() == board[row][col].getClass()) {
                        if(board[row+2][col] != null && board[row+2][col].getClass() == board[row+1][col].getClass()) {
                            if(board[row+3][col] != null && board[row+3][col].getClass() == board[row+2][col].getClass()) {
                                if(board[row+4][col] != null && board[row+4][col].getClass() == board[row+3][col].getClass()) {
                                    return game.isGameOver = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int row=0;row<size;row++) {
            for(int col =0; col<size;col++) {
                if(board[row][col] != null) {
                    if(board[row][col+1] != null && board[row][col+1].getClass() == board[row][col].getClass()) {
                        if(board[row][col+2] != null && board[row][col+2].getClass() == board[row][col+1].getClass()) {
                            if(board[row][col+3] != null && board[row][col+3].getClass() == board[row][col+2].getClass()) {
                                if(board[row][col+4] != null && board[row][col+4].getClass() == board[row][col+3].getClass()) {
                                    return game.isGameOver = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}