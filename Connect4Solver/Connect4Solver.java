public class Connect4Solver {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.print("Please use the following format: java Proj2 numberOfTrialsAI1 numberOfTrialsAI2 numberOfGames");
			System.exit(1);
		}

		int p1wins = 0;
		int p2wins = 0;
		int ties = 0;
		int numberOfGames = Integer.parseInt(args[2]);
		int numberOfTrialsAI1 = Integer.parseInt(args[0]);
		int numberOfTrialsAI2 = Integer.parseInt(args[1]);
		for (int i = 0; i < numberOfGames; i++) {
			Board board = new Board(7, 6);
			while (board.current() == Board.ON) {
				System.out.println("\n\n" + board);
				int moveColumn;
				int numberOfTrials;
				do {
					if (board.getNext() == Board.TURN) {
						System.out.printf("AI %d moves: ", 1);
						numberOfTrials = numberOfTrialsAI1;
						AI ai = new AI(board);
						moveColumn = ai.getMove(numberOfTrials, 1);
					} else {
						System.out.printf("AI %d moves: ", 2);
						numberOfTrials = numberOfTrialsAI2;
						AI ai = new AI(board);
						moveColumn = ai.getMove(numberOfTrials, 2);
					}

				} while (!board.canPlace(moveColumn));
				board.place(moveColumn);
			}
			int gameState = board.current();
			System.out.println("\n\n\n\n\n");
			System.out.println(board);
			if (gameState == Board.WIN1) {
				System.out.println("AI 1 won.\n");
				p1wins++;
			} else if (gameState == Board.WIN2) {
				System.out.println("AI 2 won.\n");
				p2wins++;
			} else {
				System.out.println("Tie.\n");
				ties++;
			}
		}
		System.out.println("AI1 won:" + p1wins + "        AI2 won:" + p2wins +"       Ties: " + ties);
	}
}
