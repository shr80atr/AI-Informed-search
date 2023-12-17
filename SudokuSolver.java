import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class SudokuSolver {

    private static class Node implements Comparable<Node> {
        private int[][] board;
        private int cost;
        private int heuristic;

        public Node(int[][] board, int cost, int heuristic) {
            this.board = board;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        public int[][] getBoard() {
            return board;
        }

        public int getCost() {
            return cost;
        }

        public int getHeuristic() {
            return heuristic;
        }

        @Override
        public int compareTo(Node other) {
            int f = cost + heuristic;
            int otherF = other.getCost() + other.getHeuristic();
            return Integer.compare(f, otherF);
        }
    }

    public static void solveSudoku(int[][] board) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        openSet.add(new Node(board, 0, calculateHeuristic(board)));

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            int[][] currentBoard = currentNode.getBoard();
            int currentCost = currentNode.getCost();

            if (isGoal(currentBoard)) {
                printBoard(currentBoard);
                return;
            }

            List<Node> successors = generateSuccessors(currentBoard, currentCost);
            for (Node successor : successors) {
                openSet.add(successor);
            }
        }

        System.out.println("No solution found.");
    }

    private static List<Node> generateSuccessors(int[][] board, int cost) {
        List<Node> successors = new ArrayList<>();

        // Find an empty cell
        int row = -1, col = -1;
        boolean found = false;
        for (int i = 0; i < board.length && !found; i++) {
            for (int j = 0; j < board.length && !found; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    found = true;
                }
            }
        }

        // Generate successors
        for (int num = 1; num <= board.length; num++) {
            if (isValid(board, row, col, num)) {
                int[][] successorBoard = cloneBoard(board);
                successorBoard[row][col] = num;
                successors.add(new Node(successorBoard, cost + 1, calculateHeuristic(successorBoard)));
            }
        }

        return successors;
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check row constraints
        for (int i = 0; i < board.length; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // Check column constraints
        for (int i = 0; i < board.length; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // Check box constraints
        int boxSize = (int) Math.sqrt(board.length);
        int startRow = row - row % boxSize;
        int startCol = col - col % boxSize;
        for (int i = startRow; i < startRow + boxSize; i++) {
            for (int j = startCol; j < startCol + boxSize; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isGoal(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] cloneBoard(int[][] board) {
        int[][] clone = new int[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                clone[i][j] = board[i][j];
            }
        }
        return clone;
    }

    private static int calculateHeuristic(int[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    private static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int [][] board = new int[9][9];

        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < 9; j++) {

                board[i][j] = scanner.nextInt();

            }

        }

        solveSudoku(board);

    }

}
