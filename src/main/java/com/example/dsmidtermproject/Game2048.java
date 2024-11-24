package com.example.dsmidtermproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game2048 extends Application {

    class Node {
        int value;
        int x, y;
        Node nextRow, nextCol;

        Node(int value, int x, int y) {
            this.value = value;
            this.x = x;
            this.y = y;
        }
    }

    class GameBoard {
        private final Node[] rowHeads;
        private final Node[] colHeads;
        private final int size = 4;
        private final Random random = new Random();
        private int score = 0;

        public GameBoard() {
            rowHeads = new Node[size];
            colHeads = new Node[size];
        }

        public int getScore() {
            return score;
        }

        public void addNode(int value, int x, int y) {
            Node newNode = new Node(value, x, y);

            // Add to row linked list
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

            // Add to column linked list
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
            // Remove from row linked list
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

            // Remove from column linked list
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
                if (row.x == x) return row;
                row = row.nextCol;
            }
            return null;
        }

        public void moveLeft() {
            for (int y = 0; y < size; y++) {
                mergeLeft(y);
                compressLeft(y);
            }
        }

        public void moveRight() {
            for (int y = 0; y < size; y++) {
                mergeRight(y);
                compressRight(y);
            }
        }

        public void moveUp() {
            for (int x = 0; x < size; x++) {
                mergeUp(x);
                compressUp(x);
            }
        }

        public void moveDown() {
            for (int x = 0; x < size; x++) {
                mergeDown(x);
                compressDown(x);
            }
        }

        private void mergeLeft(int y) {
            Node row = rowHeads[y];
            while (row != null && row.nextCol != null) {
                if (row.value == row.nextCol.value) {
                    row.value *= 2;
                    score += row.value; // Update score
                    deleteNode(row.nextCol.x, y);
                }
                row = row.nextCol;
            }
        }

        private void compressLeft(int y) {
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

        private void mergeRight(int y) {
            Node row = rowHeads[y];
            List<Node> reverseList = new ArrayList<>();

            while (row != null) {
                reverseList.add(row);
                row = row.nextCol;
            }

            for (int i = reverseList.size() - 1; i > 0; i--) {
                if (reverseList.get(i).value == reverseList.get(i - 1).value) {
                    reverseList.get(i).value *= 2;
                    score += reverseList.get(i).value;
                    deleteNode(reverseList.get(i - 1).x, y);
                }
            }
        }

        private void compressRight(int y) {
            Node row = rowHeads[y];
            List<Node> reverseList = new ArrayList<>();

            while (row != null) {
                reverseList.add(row);
                row = row.nextCol;
            }

            int position = size - 1;
            for (int i = reverseList.size() - 1; i >= 0; i--) {
                if (reverseList.get(i).x != position) {
                    Node node = reverseList.get(i);
                    deleteNode(node.x, y);
                    addNode(node.value, position, y);
                }
                position--;
            }
        }
        private void mergeUp(int x) {
            Node col = colHeads[x];
            while (col != null && col.nextRow != null) {
                if (col.value == col.nextRow.value) {
                    col.value *= 2;
                    score += col.value; // Update score
                    deleteNode(x, col.nextRow.y);
                }
                col = col.nextRow;
            }
        }

        private void compressUp(int x) {
            Node col = colHeads[x];
            int position = 0;

            while (col != null) {
                Node next = col.nextRow;
                if (col.y != position) {
                    deleteNode(x, col.y);
                    addNode(col.value, x, position);
                }
                position++;
                col = next;
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
                if (reverseList.get(i).value == reverseList.get(i - 1).value) {
                    reverseList.get(i).value *= 2;
                    score += reverseList.get(i).value;
                    deleteNode(x, reverseList.get(i - 1).y);
                }
            }
        }

        private void compressDown(int x) {
            Node col = colHeads[x];
            List<Node> reverseList = new ArrayList<>();

            while (col != null) {
                reverseList.add(col);
                col = col.nextRow;
            }

            int position = size - 1;
            for (int i = reverseList.size() - 1; i >= 0; i--) {
                if (reverseList.get(i).y != position) {
                    Node node = reverseList.get(i);
                    deleteNode(x, node.y);
                    addNode(node.value, x, position);
                }
                position--;
            }
        }

        public void display(GridPane grid, Text scoreText) {
            grid.getChildren().clear();
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    Rectangle rect = new Rectangle(100, 100);
                    rect.setFill(Color.web(getColor(0))); // Default color
                    rect.setStroke(Color.BLACK);

                    Node node = getNode(x, y);
                    if (node != null) {
                        rect.setFill(Color.web(getColor(node.value)));
                        Text text = new Text(String.valueOf(node.value));
                        text.setFont(Font.font(24));
                        grid.add(rect, x, y);
                        grid.add(text, x, y);
                    } else {
                        grid.add(rect, x, y);
                    }
                }
            }

            scoreText.setText("Score: " + score);
        }

        private String getColor(int value) {
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

    @Override
    public void start(Stage primaryStage) {
        GameBoard board = new GameBoard();
        GridPane grid = new GridPane();
        Text scoreText = new Text("Score: 0");
        scoreText.setFont(Font.font(18));

        VBox vbox = new VBox(10, scoreText, grid);
        board.addRandomTile();
        board.addRandomTile();
        board.display(grid, scoreText);

        Scene scene = new Scene(vbox, 400, 450);
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case LEFT -> board.moveLeft();
                case RIGHT -> board.moveRight();
                case UP -> board.moveUp();
                case DOWN -> board.moveDown();
            }
            board.addRandomTile();
            board.display(grid, scoreText);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("2048 Game");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
