package com.example.dsmidtermproject;

import com.example.dsmidtermproject.GameBoard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private static final int SIZE = 4;
    private Label[][] tileLabels = new Label[SIZE][SIZE];
    private GameBoard gameBoard;

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Label label = new Label();
                label.setPrefSize(100, 100);
                label.setStyle("-fx-border-color: black; -fx-alignment: center;");
                tileLabels[i][j] = label;
                gridPane.add(label, j, i);
            }
        }

        gameBoard = new GameBoard(tileLabels);
        gameBoard.addRandomTile();
        gameBoard.addRandomTile();
        gameBoard.updateUI();

        VBox root = new VBox(gridPane);
        Scene scene = new Scene(root, 400, 500);

        // Handle key events for movement
        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setTitle("2048 Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleKeyPress(KeyEvent event) {
        boolean moved = switch (event.getCode()) {
            case LEFT -> gameBoard.moveLeft();
            case RIGHT -> gameBoard.moveRight();
            case UP -> gameBoard.moveUp();
            case DOWN -> gameBoard.moveDown();
            default -> false;
        };

        if (moved) {
            gameBoard.addRandomTile();
            gameBoard.updateUI();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
