package com.example.dsmidtermproject;

public class Node {
    int data, x, y;
    Node nextRow, nextCol, prevRow, prevCol;

    public Node(int data, int x, int y) {
        this.data = data;
        this.x = x;
        this.y = y;
        this.nextRow = null;
        this.nextCol = null;
        this.prevRow = null;
        this.prevCol = null;
    }
}



