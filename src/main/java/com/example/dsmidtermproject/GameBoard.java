package com.example.dsmidtermproject;
import java.util.ArrayList;
import java.util.Random;
import com.example.dsmidtermproject.Node;

public class GameBoard {
    private Node[] rows = new Node[4];
    private Node[] cols = new Node[4];
    private int[][] grid = new int[4][4];
    private Random random = new Random();
    public int score = 0;

    public GameBoard() {
        for (int i = 0; i < 4; i++) {
            rows[i] = new Node(-1, -1, -1);
            cols[i] = new Node(-1, -1, -1);
        }
        addRandomTile();
        addRandomTile();
    }

    public void addRandomTile() {
        ArrayList<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 0) emptyCells.add(new int[]{i, j});
            }
        }
        if (emptyCells.isEmpty()) return;

        int[] randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
        int value = random.nextDouble() < 0.9 ? 2 : 4;
        addNode(value, randomCell[1], randomCell[0]);
    }

    public void addNode(int value, int x, int y) {
        grid[y][x] = value;
        Node newNode = new Node(value, x, y);

        // Add to row
        Node rowHead = rows[y];
        while (rowHead.nextCol != null) rowHead = rowHead.nextCol;
        rowHead.nextCol = newNode;

        // Add to column
        Node colHead = cols[x];
        while (colHead.nextRow != null) colHead = colHead.nextRow;
        colHead.nextRow = newNode;
    }

    public void moveLeft() {
        for (int y = 0; y < 4; y++) {
            int[] temp = new int[4];
            int index = 0;
            for (int x = 0; x < 4; x++) {
                if (grid[y][x] != 0) {
                    temp[index++] = grid[y][x];
                }
            }
            for (int i = 0; i < index - 1; i++) {
                if (temp[i] == temp[i + 1]) {
                    temp[i] *= 2;
                    score += temp[i];
                    temp[i + 1] = 0;
                }
            }
            index = 0;
            for (int x = 0; x < 4; x++) {
                grid[y][x] = 0;
                if (temp[index] != 0) grid[y][x] = temp[index++];
            }
        }
        addRandomTile();
    }

    public void moveRight() {
        for (int y = 0; y < 4; y++) {
            int[] temp = new int[4];
            int index = 3;
            for (int x = 3; x >= 0; x--) {
                if (grid[y][x] != 0) {
                    temp[index--] = grid[y][x];
                }
            }
            for (int i = 3; i > 0; i--) {
                if (temp[i] == temp[i - 1]) {
                    temp[i] *= 2;
                    score += temp[i];
                    temp[i - 1] = 0;
                }
            }
            index = 3;
            for (int x = 3; x >= 0; x--) {
                grid[y][x] = 0;
                if (temp[index] != 0) grid[y][x] = temp[index--];
            }
        }
        addRandomTile();
    }

    public void moveUp() {
        for (int x = 0; x < 4; x++) {
            int[] temp = new int[4];
            int index = 0;
            for (int y = 0; y < 4; y++) {
                if (grid[y][x] != 0) {
                    temp[index++] = grid[y][x];
                }
            }
            for (int i = 0; i < index - 1; i++) {
                if (temp[i] == temp[i + 1]) {
                    temp[i] *= 2;
                    score += temp[i];
                    temp[i + 1] = 0;
                }
            }
            index = 0;
            for (int y = 0; y < 4; y++) {
                grid[y][x] = 0;
                if (temp[index] != 0) grid[y][x] = temp[index++];
            }
        }
        addRandomTile();
    }

    public void moveDown() {
        for (int x = 0; x < 4; x++) {
            int[] temp = new int[4];
            int index = 3;
            for (int y = 3; y >= 0; y--) {
                if (grid[y][x] != 0) {
                    temp[index--] = grid[y][x];
                }
            }
            for (int i = 3; i > 0; i--) {
                if (temp[i] == temp[i - 1]) {
                    temp[i] *= 2;
                    score += temp[i];
                    temp[i - 1] = 0;
                }
            }
            index = 3;
            for (int y = 3; y >= 0; y--) {
                grid[y][x] = 0;
                if (temp[index] != 0) grid[y][x] = temp[index--];
            }
        }
        addRandomTile();
    }

    public int[][] getGrid() {
        return grid;
    }
}
