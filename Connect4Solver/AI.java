
import java.util.ArrayList;

public class AI {
	private Node root;
	private final int w;

	public AI(Board b) {
		this.w = b.w;
		root = new Node(null, b.copy());
	}

	public int getMove(int numberOfTrials, int AIPlayerNumber) {
		for (int i = 0; i < numberOfTrials; i++) {
			Node selectedNode = select(root);
			if (selectedNode == null)
				continue;
			Node node = expand(selectedNode);
			double result = sim(node);
			backprop(node, result);
		}

		int maxIndex = -1;
		for (int i = 0; i < w; i++) {
			if (root.children[i] != null) {
				if (maxIndex == -1 || root.children[i].visits > root.children[maxIndex].visits)
					maxIndex = i;
			}
		}
		if (AIPlayerNumber == 1) {
			System.out.printf(
					"\nMove selected: %d column; Estimated wins: %f ; Estimated probability this is the best move: %f",
					maxIndex, root.children[maxIndex].playerWins,
					root.children[maxIndex].playerWins / root.children[maxIndex].visits);
		} else if (AIPlayerNumber == 2) {
			System.out.printf(
					"\nMove selected: %d column; Estimated wins: %f ; Estimated probability this is the best move: %f",
					maxIndex, root.children[maxIndex].visits + root.children[maxIndex].playerWins,
					1 - (root.children[maxIndex].playerWins / root.children[maxIndex].visits));
		}
		return maxIndex;
	}

	private Node select(Node parent) {
		for (int i = 0; i < w; i++) {
			if (parent.children[i] == null && parent.b.canPlace(i)) {
				return parent;
			}
		}
		double maxSelectionVal = -1;
		int maxIndex = -1;
		for (int i = 0; i < w; i++) {
			if (!parent.b.canPlace(i))
				continue;
			Node currentChild = parent.children[i];
			double wins = parent.b.getNext() == Board.TURN ? currentChild.playerWins
					: (currentChild.visits - currentChild.playerWins);
			double selectionVal = wins / currentChild.visits
					+ Math.sqrt(2) * Math.sqrt(Math.log(parent.visits) / currentChild.visits);
			if (selectionVal > maxSelectionVal) {
				maxSelectionVal = selectionVal;
				maxIndex = i;
			}
		}
		if (maxIndex == -1)
			return null;
		return select(parent.children[maxIndex]);
	}

	private Node expand(Node selectedNode) {
		ArrayList<Integer> unvisitedChildrenIndices = new ArrayList<Integer>(w);
		for (int i = 0; i < w; i++) {
			if (selectedNode.children[i] == null && selectedNode.b.canPlace(i)) {
				unvisitedChildrenIndices.add(i);
			}
		}

		int selectedIndex = unvisitedChildrenIndices.get((int) (Math.random() * unvisitedChildrenIndices.size()));
		selectedNode.children[selectedIndex] = new Node(selectedNode, selectedNode.b.getNextState(selectedIndex));
		return selectedNode.children[selectedIndex];
	}

	private double sim(Node node) {
		Board simulationBoard = node.b.copy();
		while (simulationBoard.current() == Board.ON) {
			simulationBoard.place((int) (Math.random() * w));
		}
		if (simulationBoard.current() == Board.WIN1) {
			return 1;
		} else if (simulationBoard.current() == Board.WIN2) {
			return 0;
		} else {
			return 0.5;
		}
	}

	private void backprop(Node node, double simResult) {
		Node currentNode = node;
		while (currentNode != null) {
			currentNode.incrementVisits();
			currentNode.incrementWins(simResult);
			currentNode = currentNode.parent;
		}
	}

	private class Node {
		private Node parent;
		private Node[] children;
		private int visits;
		private double playerWins;
		private final Board b;

		public Node(Node parent, Board b) {
			this.parent = parent;
			this.b = b;
			this.visits = 0;
			this.playerWins = 0;
			children = new Node[w];
		}

		public int incrementVisits() {
			return ++visits;
		}

		public double incrementWins(double r) {
			playerWins += r;
			return playerWins;
		}
	}
}
