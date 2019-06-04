
public class Board {
	public final int h;
	public final int w;
	private int[][] board;
	private boolean next;
	public static final int EMPTY = 0;
	public static final int P1DISK = 1;
	public static final int P2DISK = 2;
	public static final boolean TURN = true;
	public static final int ON = 0;
	public static final int WIN1 = 1;
	public static final int WIN2 = 2;
	public static final int TIE = 3;

	public Board(int w, int h) {
		this.w = w;
		this.h = h;
		board = new int[h][w];
		next = TURN;
	}

	public Board(int[][] data, boolean next) {
		this(data[0].length, data.length);
		loadContents(data);
		this.next = next;
	}

	public boolean canPlace(int column) {
		return column >= 0 && column < w && board[0][column] == 0;
	}

	public boolean place(int column) {
		int disk = (next == TURN) ? P1DISK : P2DISK;
		if (!canPlace(column))
			return false;
		int diskh = h - 1;
		while (board[diskh][column] != EMPTY)
			diskh--;
		board[diskh][column] = disk;
		next = !next;
		return true;
	}

	public Board getNextState(int column) {
		Board next = this.copy();
		next.place(column);
		return next;
	}

	public int[][] getContents() {
		int[][] dataCopy = new int[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				dataCopy[i][j] = board[i][j];
		return dataCopy;
	}

	public void loadContents(int[][] data) {
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				board[i][j] = data[i][j];
	}

	public Board copy() {
		return new Board(board, this.next);
	}

	public boolean didPlayerWin(int playerDisk) {
		int h = board.length;
		int w = board[0].length;
		if (horizontalCheck(playerDisk, h, w) || verticalCheck(playerDisk, h, w)
				|| diagonalDownRightCheck(playerDisk, h, w) || diagonalDownLeftCheck(playerDisk, h, w)) {
			return true;
		} else {
			return false;
		}

	}

	public boolean horizontalCheck(int playerDisk, int h, int w) {
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w - 3; j++)
				for (int k = j; k < j + 4 && board[i][k] == playerDisk; k++)
					if (k == j + 3)
						return true;
		return false;

	}

	public boolean verticalCheck(int playerDisk, int h, int w) {
		for (int i = 0; i < h - 3; i++)
			for (int j = 0; j < w; j++)
				for (int k = i; k < i + 4 && board[k][j] == playerDisk; k++)
					if (k == i + 3)
						return true;
		return false;

	}

	public boolean diagonalDownRightCheck(int playerDisk, int h, int w) {
		for (int i = 0; i < h - 3; i++)
			for (int j = 0; j < w - 3; j++)
				for (int k = 0; k < 4 && board[i + k][j + k] == playerDisk; k++)
					if (k == 3)
						return true;
		return false;

	}

	public boolean diagonalDownLeftCheck(int playerDisk, int h, int w) {
		for (int i = 0; i < h - 3; i++)
			for (int j = 3; j < w; j++)
				for (int k = 0; k < 4 && board[i + k][j - k] == playerDisk; k++)
					if (k == 3)
						return true;
		return false;

	}

	public boolean isFull() {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j] == EMPTY)
					return false;
		return true;
	}

	public int current() {
		return didPlayerWin(P1DISK) ? WIN1 : didPlayerWin(P2DISK) ? WIN2 : isFull() ? TIE : ON;
	}

	public boolean getNext() {
		return next;
	}

	public String toString() {
		String r = "*-";
		for (int j = 0; j < w; j++) {
			r += "--*-";
		}
		r = r.substring(0, r.length() - 1) + "\n";
		for (int i = 0; i < h; i++) {
			r += "* ";
			for (int j = 0; j < w; j++) {
				r += (board[i][j] == EMPTY ? " " : (board[i][j] == 1 ? "R" : "Y")) + " * ";
			}
			r = r.substring(0, r.length() - 1);
			r += "\n*-";
			for (int j = 0; j < w; j++) {
				r += "--*-";
			}
			r = r.substring(0, r.length() - 1);
			r += "\n";
		}
		r += "  0   1   2   3   4   5   6  ";
		return r.substring(0, r.length() - 1);
	}
}
