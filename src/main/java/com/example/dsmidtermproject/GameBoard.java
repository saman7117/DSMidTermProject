package com.example.dsmidtermproject;

import javafx.scene.control.Label;

import java.util.Random;

public class GameBoard {
    Node[] rows;
    Label[][] tileLabels;
    Random random = new Random();
    int score = 0;

    public GameBoard(Label[][] tileLabels) {
        this.tileLabels = tileLabels;
        rows = new Node[4];

        for (int i = 0; i < 4; i++) {
            rows[i] = createRow();
        }
    }

    private Node createRow() {
        Node head = new Node(0);
        Node curr = head;
        for (int i = 1; i < 4; i++) {
            Node newNode = new Node(0);
            curr.next = newNode;
            newNode.prev = curr;
            curr = newNode;
        }
        return head;
    }

    public boolean moveLeft() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            moved |= shiftAndMerge(rows[i]);
        }
        return moved;
    }

    public boolean moveRight() {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            Node reversedRow = reverseRow(rows[i]);
            moved |= shiftAndMerge(reversedRow);
            reverseRow(reversedRow);
        }
        return moved;
    }

    public boolean moveUp() {
        boolean moved = false;
        for (int col = 0; col < 4; col++) {
            Node column = getColumn(col);
            moved |= shiftAndMerge(column);
            updateColumn(col, column);
        }
        return moved;
    }

    public boolean moveDown() {
        boolean moved = false;

        for (int col = 0; col < 4; col++) {
            // Create and reverse the column linked list
            Node column = reverseColumn(getColumn(col));

            // Perform shift and merge on the reversed column
            moved |= shiftAndMerge(column);

            // Reverse the column back and update the game board
            column = reverseColumn(column);
            updateColumn(col, column);
        }

        return moved;
    }


    private boolean shiftAndMerge(Node head) {
        boolean moved = false;

        Node curr = head;
        while (curr != null) {
            Node next = curr.next;

            while (next != null && next.data == 0) {
                next = next.next; // Skip over empty tiles
            }

            if (next != null && curr.data == 0) {
                // Shift tile forward
                curr.data = next.data;
                next.data = 0;
                moved = true;
            }
            curr = curr.next;
        }

        curr = head;
        while (curr != null && curr.next != null) {
            if (curr.data == curr.next.data && curr.data != 0) {
                // Merge tiles
                curr.data *= 2;
                curr.next.data = 0;
                moved = true;
            }
            curr = curr.next;
        }

        curr = head;


        return moved;
    }

    public void addRandomTile() {
        int emptyCount = countEmptyTiles();
        if (emptyCount == 0) return;

        int target = random.nextInt(emptyCount);
        int count = 0;

        for (int i = 0; i < 4; i++) {
            Node curr = rows[i];
            for (int j = 0; j < 4; j++) {
                if (curr.data == 0) {
                    if (count == target) {
                        curr.data = random.nextDouble() < 0.3 ? 4 : 2;
                        return;
                    }
                    count++;
                }
                curr = curr.next;
            }
        }
    }

    public void updateUI() {
        for (int i = 0; i < 4; i++) {
            Node curr = rows[i];
            for (int j = 0; j < 4; j++) {
                Label label = tileLabels[i][j];
                if (curr.data == 0) {
                    label.setText("");
                    label.setStyle("-fx-background-color: lightgray; -fx-border-color: black;");
                } else {
                    label.setText(String.valueOf(curr.data));
                    label.setStyle("-fx-background-color: " + getColor(curr.data) + "; -fx-border-color: black;");
                }
                curr = curr.next;
            }
        }
    }

    // Get the color for a specific tile value
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

    // Get the column as a linked list
    private Node getColumn(int colIndex) {
        Node head = null; // Initialize the head of the column linked list
        Node curr = null;

        for (int row = 0; row < 4; row++) {
            Node rowNode = rows[row];
            for (int i = 0; i < colIndex; i++) {
                rowNode = rowNode.next; // Move to the correct column node
            }

            Node newNode = new Node(rowNode.data); // Create a new node for the column
            if (head == null) {
                head = newNode; // Set the head of the column
                curr = head;
            } else {
                curr.next = newNode; // Link the new node
                newNode.prev = curr;
                curr = newNode;
            }
        }

        return head;
    }


    // Update the column with new data
    private void updateColumn(int colIndex, Node column) {
        Node curr = column;

        for (int row = 0; row < 4; row++) {
            Node rowNode = rows[row];
            for (int i = 0; i < colIndex; i++) {
                rowNode = rowNode.next; // Navigate to the correct node in the row
            }

            if (curr != null) {
                rowNode.data = curr.data; // Update the row node with column data
                curr = curr.next;
            } else {
                rowNode.data = 0; // Default to 0 if the column node is null
            }
        }
    }

    private Node reverseColumn(Node head) {
        Node curr = head, prev = null;

        while (curr != null) {
            Node next = curr.next;
            curr.next = prev;
            curr.prev = next;
            prev = curr;
            curr = next;
        }

        return prev;
    }


    private Node reverseRow(Node head) {
        return reverseColumn(head);
    }

    private Node getNodeAt(int row, int col) {
        Node curr = rows[row];
        for (int i = 0; i < col; i++) {
            curr = curr.next;
        }
        return curr;
    }

    private int countEmptyTiles() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            Node curr = rows[i];
            while (curr != null) {
                if (curr.data == 0) count++;
                curr = curr.next;
            }
        }
        return count;
    }
}
