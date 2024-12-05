package com.example.dsmidtermproject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameBoard {
    private final Node[] rowHeads;
    private final Node[] colHeads;
    private final int size = 4;
    private final int maxHistory = 5;
    private final Random random = new Random();
    private int score = 0;
    private Stack<int[][]> undoStack = new Stack<>();
    private Stack<Integer> undoscoreStack = new Stack<>();
    private Stack<int[][]> redoStack = new Stack<>();
    private Stack<Integer> redoscoreStack = new Stack<>();

    public GameBoard() {
        rowHeads = new Node[size];
        colHeads = new Node[size];
    }

    public int getScore() {
        return score;
    }

    public void addNode(int value, int x, int y) {
        Node newNode = new Node(value, x, y);

        Node row = rowHeads[y];
        if (row == null || row.x > x) {
            newNode.nextCol = rowHeads[y];
            rowHeads[y] = newNode;
        } else {
            while (row.nextCol != null && row.nextCol.x < x) {
                row = row.nextCol;
            }
            newNode.nextCol = row.nextCol;
            row.nextCol = newNode;
        }

        Node col = colHeads[x];
        if (col == null || col.y > y) {
            newNode.nextRow = colHeads[x];
            colHeads[x] = newNode;
        } else {
            while (col.nextRow != null && col.nextRow.y < y) {
                col = col.nextRow;
            }
            newNode.nextRow = col.nextRow;
            col.nextRow = newNode;
        }
    }

    public void deleteNode(int x, int y) {
        Node row = rowHeads[y];
        if (row != null && row.x == x) {
            rowHeads[y] = row.nextCol;
        } else {
            while (row != null && row.nextCol != null) {
                if (row.nextCol.x == x) {
                    row.nextCol = row.nextCol.nextCol;
                    break;
                }
                row = row.nextCol;
            }
        }

        Node col = colHeads[x];
        if (col != null && col.y == y) {
            colHeads[x] = col.nextRow;
        } else {
            while (col != null && col.nextRow != null) {
                if (col.nextRow.y == y) {
                    col.nextRow = col.nextRow.nextRow;
                    break;
                }
                col = col.nextRow;
            }
        }
    }

    public void addRandomTile() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getNode(i, j) == null) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.isEmpty()) return;

        int[] chosen = emptyCells.get(random.nextInt(emptyCells.size()));
        int value = random.nextDouble() < 0.3 ? 4 : 2;
        addNode(value, chosen[0], chosen[1]);
    }

    public Node getNode(int x, int y) {
        Node row = rowHeads[y];
        while (row != null) {
            if (row.x == x)
                return row;
            row = row.nextCol;
        }
        return null;
    }

    private int[][] getBoardState() {
        int[][] boardState = new int[size][size];
        for (int y = 0; y < size; y++) {
            Node row = rowHeads[y];
            while (row != null) {
                boardState[row.y][row.x] = row.value;
                row = row.nextCol;
            }
        }
        return boardState;
    }

    private void setBoardState(int[][] state) {
        clearBoard();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (state[y][x] != 0) {
                    addNode(state[y][x], x, y);
                }
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < size; i++) {
            rowHeads[i] = null;
            colHeads[i] = null;
        }
    }

    private boolean hasBoardChanged(int[][] oldState , int[][] currentState) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (currentState[i][j] != oldState[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveLeft() {
        int[][] oldState = getBoardState();
        undoscoreStack.push(score);
        undoStack.push(oldState);
        for (int y = 0; y < size; y++) {
            shiftLeft(y);
            mergeLeft(y);
            shiftLeft(y);
        }
        int[][] currentState = getBoardState();
        if (hasBoardChanged(oldState , currentState)){
            addRandomTile();
            if (isGameOver()) {
                System.out.println("Game Over!");
            }
        }
    }

    public void moveRight() {
        int[][] oldState = getBoardState();
        undoscoreStack.push(score);
        undoStack.push(oldState);
        for (int y = 0; y < size; y++) {
            shiftRight(y);
            mergeRight(y);
            shiftRight(y);
        }
        int[][] currentState = getBoardState();
        if (hasBoardChanged(oldState , currentState)){
            addRandomTile();
            if (isGameOver()) {
                System.out.println("Game Over!");
            }
        }
    }

    public void moveUp() {
        int[][] oldState = getBoardState();
        undoscoreStack.push(score);
        undoStack.push(oldState);
        for (int x = 0; x < size; x++) {
            shiftUp(x);
            mergeUp(x);
            shiftUp(x);
        }
        int[][] currentState = getBoardState();
        if (hasBoardChanged(oldState , currentState)) {
            addRandomTile();
            if (isGameOver()) {
                System.out.println("Game Over!");
            }
        }
    }

    public void moveDown() {
        int[][] oldState = getBoardState();
        undoscoreStack.push(score);
        undoStack.push(oldState);

        for (int x = 0; x < size; x++) {
            shiftDown(x);
            mergeDown(x);
            shiftDown(x);
        }

        int[][] currentState = getBoardState();
        if (hasBoardChanged(oldState, currentState)) {
            addRandomTile();
        }
    }


    private void shiftLeft(int y) {
        Node row = rowHeads[y];
        int position = 0;

        while (row != null) {
            Node next = row.nextCol;
            if (row.x != position) {
                deleteNode(row.x, y);
                addNode(row.value, position, y);
            }
            position++;
            row = next;
        }
    }

    private void mergeLeft(int y) {
        Node row = rowHeads[y];
        while (row != null && row.nextCol != null) {
            if (row.value == row.nextCol.value) {
                row.value *= 2;
                score += row.value;
                deleteNode(row.nextCol.x, y);
            }
            row = row.nextCol;
        }
    }

    private void shiftRight(int y) {
        Node row = rowHeads[y];
        List<Node> reverseList = new ArrayList<>();

        while (row != null) {
            reverseList.add(row);
            row = row.nextCol;
        }

        int position = size - 1;
        for (int i = reverseList.size() - 1; i >= 0; i--) {
            Node node = reverseList.get(i);
            if (node.x != position) {
                deleteNode(node.x, y);
                addNode(node.value, position, y);
            }
            position--;
        }
    }


    private void mergeRight(int y) {
        Node row = rowHeads[y];
        List<Node> reverseList = new ArrayList<>();

        while (row != null) {
            reverseList.add(row);
            row = row.nextCol;
        }

        for (int i = reverseList.size() - 1; i > 0; i--) {
            Node curr = reverseList.get(i);
            Node prev = reverseList.get(i - 1);

            if (curr.value == prev.value) {
                curr.value *= 2;
                score += curr.value;
                deleteNode(prev.x, y);
                reverseList.remove(i - 1);
                i--;
            }
        }
    }

    private void mergeUp(int x) {
        Node col = colHeads[x];
        List<Node> nodeList = new ArrayList<>();

        while (col != null) {
            nodeList.add(col);
            col = col.nextRow;
        }

        for (int i = 0; i < nodeList.size() - 1; i++) {
            Node curr = nodeList.get(i);
            Node next = nodeList.get(i + 1);

            if (curr.value == next.value) {
                curr.value *= 2;
                score += curr.value;
                deleteNode(x, next.y);
                nodeList.remove(i + 1);
                i--;
            }
        }
    }

    private void shiftUp(int x) {
        Node col = colHeads[x];
        List<Node> nodeList = new ArrayList<>();

        while (col != null) {
            nodeList.add(col);
            col = col.nextRow;
        }

        int position = 0;
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            if (node.y != position) {
                deleteNode(x, node.y);
                addNode(node.value, x, position);
            }
            position++;
        }
    }

    private void mergeDown(int x) {
        Node col = colHeads[x];
        List<Node> reverseList = new ArrayList<>();

        while (col != null) {
            reverseList.add(col);
            col = col.nextRow;
        }

        for (int i = reverseList.size() - 1; i > 0; i--) {
            Node curr = reverseList.get(i);
            Node prev = reverseList.get(i - 1);

            if (curr.value == prev.value) {
                curr.value *= 2;
                score += curr.value;
                deleteNode(x, prev.y);
                reverseList.remove(i - 1);
                i--;
            }
        }
    }


    private void shiftDown(int x) {
        Node col = colHeads[x];
        List<Node> reverseList = new ArrayList<>();

        while (col != null) {
            reverseList.add(col);
            col = col.nextRow;
        }

        int position = size - 1;
        for (int i = reverseList.size() - 1; i >= 0; i--) {
            Node node = reverseList.get(i);
            if (node.y != position) {
                deleteNode(x, node.y);
                addNode(node.value, x, position);
            }
            position--;
        }
    }



    public void undo() {
        if (!undoStack.isEmpty() && !undoscoreStack.isEmpty()) {
            redoStack.push(getBoardState());
            redoscoreStack.push(score);

            setBoardState(undoStack.pop());
            score = undoscoreStack.pop();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty() && !redoscoreStack.isEmpty()) {
            undoStack.push(getBoardState());
            undoscoreStack.push(score);

            setBoardState(redoStack.pop());
            score = redoscoreStack.pop();
        }
    }

    public boolean isGameOver() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Node node = getNode(x, y);
                if (node == null) return false;
                if (x < size - 1 && getNode(x + 1, y) != null && node.value == getNode(x + 1, y).value) return false;
                if (y < size - 1 && getNode(x, y + 1) != null && node.value == getNode(x, y + 1).value) return false;
            }
        }
        return true;
    }

    public void display(GridPane grid, Label scoreText) {
        grid.getChildren().clear();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Rectangle rect = new Rectangle(100, 100);
                rect.setFill(Color.rgb(187, 173, 160));
                rect.setStroke(Color.BLACK);
                rect.setArcWidth(5);
                rect.setArcHeight(5);

                Node node = getNode(x, y);
                Text text = new Text();

                if (node != null) {
                    rect.setFill(Color.web(getColor(node.value)));
                    text.setText(String.valueOf(node.value));
                    text.setFont(new Font("Arial", 28));
                }

                StackPane stackPane = new StackPane();
                stackPane.getChildren().addAll(rect, text);

                grid.add(stackPane, x, y);
            }
        }

        scoreText.setStyle("-fx-text-fill: White");
        scoreText.setText(String.valueOf(score));
        //scoreText.setStyle("-fx-background-color: Red; -fx-border-radius: 10px;");


    }

    private String getColor(int value) {
        if (value > 2048) return "#FF0000";
        return switch (value) {
            case 2 -> "#EEE4DA";
            case 4 -> "#EDE0C8";
            case 8 -> "#F2B179";
            case 16 -> "#F59563";
            case 32 -> "#F67C5F";
            case 64 -> "#F65E3B";
            case 128 -> "#EDCF72";
            case 256 -> "#EDCC61";
            case 512 -> "#EDC850";
            case 1024 -> "#EDC53F";
            case 2048 -> "#EDC22E";
            default -> "#CDC1B4";
        };
    }
}