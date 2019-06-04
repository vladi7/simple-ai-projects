import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PuzzleSolver{
	String str = "";
	String goal = "12345678E";

	LinkedList<String> linkedList;

	Map<String, Integer> depth;

	Map<String, String> history;

	int limit = 100; 
	int newValue;
	int a;

	String current;
	boolean solutionFound = false;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.print("Please use the following format: java Proj1 <pathToInputFile>");
			System.exit(1);
		}
		File file = new File(args[0]);
		String[] input = readStringsFromFile(file);
		for (int i = 0; i < input.length; i++) {
			System.out.println("***********************************");
			System.out.println(i + 1);
			System.out.println(input[i]);
			PuzzleSolver bfs = new PuzzleSolver(input[i]);
			bfs.doSearch();
			System.out.println("***********************************");
			System.out.println();
		}

	}

	public static String[] readStringsFromFile(File file) {
		StringBuilder sb = new StringBuilder();
		String[] input = null;
		Scanner s;
		int i = 0;
		try {
			s = new Scanner(file);
			if (s.hasNextLine()) {
				input = new String[Integer.parseInt(s.nextLine())];
			}
			while (s.hasNextLine()) {
				sb = new StringBuilder();
				while (true) {
					String nextLine = s.nextLine();
					if (nextLine.equals("")) {
						break;
					}
					sb.append(nextLine.replaceAll(" ", ""));
				}
				input[i] = sb.toString();
				i++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File was not found");
			System.exit(1);
		}
		return input;
	}

	PuzzleSolver(String str) {
		linkedList = new LinkedList<String>();
		depth = new HashMap<String, Integer>();
		history = new HashMap<String, String>();
		this.str = str;
		addToLinkedList(str, null);
	}

	void doSearch() {

		while (!linkedList.isEmpty()) {

			current = linkedList.removeFirst();

			if (current.equals(goal)) { 
				solutionFound = true;
				print(current);
				break;
			}

			if (depth.get(current) == limit) {
				solutionFound = false;
				print(current);
				break;
			}

			else {						
				a = current.indexOf("E");				
				left();
				up();
				right();
				down();			
			}

		}

		if (solutionFound) {
			System.out.println("Solution found");
		} else {
			System.out.println("Solution not found");
		}

	}
	private void left() {
		while (a != 0 && a != 3 && a != 6) {
			String nextState = current.substring(0, a - 1) + "E" + current.charAt(a - 1)
					+ current.substring(a + 1);
			addToLinkedList(nextState, current);
			break;
		}
	}
	private void up() {
		while (a != 0 && a != 1 && a != 2) {
			String nextState = current.substring(0, a - 3) + "E" + current.substring(a - 2, a)
					+ current.charAt(a - 3) + current.substring(a + 1);
			addToLinkedList(nextState, current);
			break;
		}
	}
	private void right() {
		while (a != 2 && a != 5 && a != 8) {
			String nextState = current.substring(0, a) + current.charAt(a + 1) + "E"
					+ current.substring(a + 2);
			addToLinkedList(nextState, current);
			break;
		}
	}
	private void down() {
		while (a != 6 && a != 7 && a != 8) {
			String nextState = current.substring(0, a) + current.substring(a + 3, a + 4)
					+ current.substring(a + 1, a + 3) + "E" + current.substring(a + 4);
			addToLinkedList(nextState, current);
			break;
		}
	}
	private void addToLinkedList(String newState, String oldState) {
		if (!depth.containsKey(newState)) {
			newValue = oldState == null ? 0 : depth.get(oldState) + 1;
			depth.put(newState, newValue);
			linkedList.add(newState);
			history.put(newState, oldState);
		}

	}

	void print(String current) {
		String traceState = current;
		while (traceState != null) {
		System.out.println(traceState + " at " + depth.get(traceState));
		//this is just to create neat presentation, instead only string
		try{
		for(int z=0;z<9;z++){
		System.out.print( " " + String.valueOf(traceState.charAt(z)) + " " );
		if ((z+1) % 3 == 0){System.out.println();}
		}
		}
		catch (NullPointerException e) {}
		traceState = history.get(traceState);
		}

		}}
