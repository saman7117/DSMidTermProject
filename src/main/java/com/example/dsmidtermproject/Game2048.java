package com.example.dsmidtermproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static jdk.xml.internal.SecuritySupport.getResourceAsStream;

public class Game2048 extends Application {
    @Override
    public void start(Stage primaryStage) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        File file = new File("C:\\Users\\Sazgar\\IdeaProjects\\DSMidtermProject\\src\\main\\java\\com\\example\\dsmidtermproject\\Morning-Routine-Lofi-Study-Music(chosic.com).wav");

        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        GameBoard board = new GameBoard();
        GridPane grid = new GridPane();

        grid.setHgap(5);
        grid.setVgap(5);

        Label scoreLabel = new Label("Score");
        scoreLabel.setFont(new Font("Roboto", 24));
        scoreLabel.setTextFill(Color.rgb(218, 218, 211));
        scoreLabel.setStyle(
                "-fx-background-color: #564e4a; " +
                        "-fx-background-radius: 5px; " +
                        "-fx-border-radius: 5px;"
        );

        Label label = new Label("2048");
        label.setFont(Font.font("Roboto", FontWeight.BOLD, 36));
        label.setTextFill(Color.BLACK);
        label.setStyle("-fx-background-color: transparent;");

        Label scoreValue = new Label("0");
        scoreValue.setFont(Font.font(24));
        scoreValue.setTextFill(Color.WHITE);
        scoreValue.setFont(new Font("Roboto", 24));

        VBox scoreBox = new VBox(5, label, scoreLabel, scoreValue);
        scoreBox.setBackground(Background.fill(Color.rgb(139, 128, 119)));
        scoreBox.setAlignment(Pos.CENTER);
        label.setTranslateX(-340);
        label.setTranslateY(50);

        scoreBox.setStyle("-fx-background-color: #8B8077;");

        VBox vbox = new VBox(10, scoreBox, grid);
        vbox.setStyle("-fx-background-color: #8B8077;");
        VBox.setMargin(grid, new Insets(25, 0, 0, 30));

        board.addRandomTile();
        board.addRandomTile();
        board.display(grid, scoreValue);

        vbox.setAlignment(Pos.TOP_RIGHT);

        scoreBox.setTranslateX(165);

        Scene scene = new Scene(vbox, 500, 600);

        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case LEFT -> board.moveLeft();
                case RIGHT -> board.moveRight();
                case UP -> board.moveUp();
                case DOWN -> board.moveDown();
                case U -> board.undo();
                case R -> board.redo();
            }
            board.display(grid, scoreValue);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("2048 Game");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
