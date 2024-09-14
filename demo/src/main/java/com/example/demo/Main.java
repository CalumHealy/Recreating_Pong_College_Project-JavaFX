package com.example.demo;

/**
 * @author Calum Healy
 * This class is the main class, containing most of the game logic
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Set;
import java.util.HashSet;
import java.util.Random;
import javafx.animation.AnimationTimer;
import java.awt.Rectangle;
import javafx.geometry.Pos;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

public class Main extends Application {

    // Declaring global variables
    private Window window; // Window object used to store width and height
    private Stage primaryStage; // Stage used for JavaFX application
    private Scene homeScene; // Main scene, first to show, can go to game scene or settings from here
    private Scene settingsScene; // Settings scene, contains settings options and save button for serialization
    private Scene gameScene; // Game scene, actual game thing with ball and rackets
    private int playerOneXPosition; // Position of player one on X axis, distance from left side of window
    private int playerOneYPosition; // Height of player one
    private int playerTwoXPosition; // Position of player two on X axis, distance from right size of widnow
    private int playerTwoYPosition; // Height of player two
    private Canvas gameCanvas; // Canvas used to display objects
    private int scoreP1 = 0; // Player one score
    private int scoreP2 = 0; // Player two score
    private GraphicsContext gc;
    Player playerOne = new Player(); // Player one object
    Player playerTwo = new Player(); // Player two object
    Ball ball = new Ball(); // Ball object
    private int winningScore = 3; // Default score required to win
    private int ballSpeedIncreaseOption = 2; // Default number of bounces required to increase speed of ball
    private int ball_speed_setting_option = 3;
    private int ballBounceCounter = 0; // Counter for number of bounces of ball for increasing speed
    public Set<KeyCode> pressedKeys = new HashSet<>(); // Hash set containing pressed keys, used for handling key presses
    private boolean isStartMessageVisible = true; // Used in displaying the "Press enter to start" message
    private boolean isPlayerOneScoresMessageVisible = false; // Used to display message when player one scores
    private boolean isPlayerTwoScoresMessageVisible = false; // Used to display message when player two scores
    private boolean isPlayerOneWinsMessageVisible = false; // Used to display message when player one wins
    private boolean isPlayerTwoWinsMessageVisible = false; // Used to display message when player two wins
    private boolean isGameSceneErrorMessageVisible = false; // Used to warn player when there is an error in the code/there is a bug
    private boolean isEnterMessageVisible = false; // Used in displaying "Press enter to continue" message
    private boolean isPauseMenuVisible = false; // Used in displaying pause menu
    private boolean gameLive = false; // States whether the game is running, used in game logic for menus and stuff
    private boolean shouldRoundStartBeCalled = true; // Allows Enter key handling and stuff to know what to do
    private boolean isGameEnded = false; // Allows the code to know that the game is finished, another round should not be started
    private boolean hasGameStarted = false; // Used to know that the game has started, no need to display "Press Enter to start" message
    private boolean hasRoundStarted = false; // Used to continue ball movement when the game in unpaused, instead of requiring Enter key to make ball move
    private boolean settingsScenePredecessor = false; // false = homeScene, true = gameScene
    private boolean loadSettingsFailedMessageVisible = false; // Used in displaying message saying loading the settings has failed
    private boolean loadGameFailedMessageVisible = false; // Used in displaying message saying loading the game has failed


    /**
     * Main method, always required, used to start the application, first thing called
     * @param args
     */

    public static void main(String[] args){
        launch(args);
    }

    /**
     * Start method sets up the game, creates scenes and objects
     * @param primaryStage
     * @throws Exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        ////////////////////////////////
        // Setup
        ////////////////////////////////

        this.primaryStage = primaryStage;

//        Player playerOne = new Player();
//        Player playerTwo = new Player();
//        Ball ball = new Ball();

        playerOne.setName("Player One");
        playerTwo.setName("Player Two");

        window = new Window();
        // window.setWidth(800);
        // window.setHeight(600);
        // Add title to window
        primaryStage.setTitle("Title of the window");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        gameCanvas = new Canvas(window.getWidth(), window.getHeight());
        gc = gameCanvas.getGraphicsContext2D();


        // ball.setXPosition((int) window.getWidth() / 2);
        // ball.setYPosition((int) window.getHeight() / 2);
        ball.initializeVectorSpeed(1,1);



        ////////////////////////////////
        // Deserialization Upon Startup
        ////////////////////////////////

        try {
            deserializeSettings();
        } catch (Exception e){
            loadSettingsFailedMessageVisible = true;
            Thread.sleep(3000);
            loadSettingsFailedMessageVisible = false;
        }



        ////////////////////////////////
        // Creating Scene Stuff
        ////////////////////////////////

        // Create game layout (VBox)
        VBox gameLayout = new VBox(10);
        gameLayout.getChildren().add(gameCanvas);

        // Creating main menu title
        Text titleText = new Text("PONG");
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setStyle("-fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-font-size: 60; -fx-fill: #FFFF00;");

        // Creating exit text
        Text exitText = new Text("Press Esc to quit");

        // Creating loadingError text
        Text loadingErrorText = new Text("Failed to load previous settings");
        loadingErrorText.setTextAlignment(TextAlignment.JUSTIFY);

//        // Settings scene back button
//        Button settingsBackButton = new Button("Back (Esc)");

        // Create buttons
        Button playButton = new Button("Play");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        playButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
        exitButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");

//        // Create game layout
//        VBox gameLayout = new VBox(10);

//        gameCanvas = new Canvas(windowWidth, windowHeight);
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        // Find width of window



        ////////////////////////////////
        // Find And Set Window Width
        ////////////////////////////////

         primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            window.setWidth((double) newWidth);
            System.out.println("New Width: " + window.getWidth());
            // System.out.println("exitButton Height: " + exitText.getBoundsInLocal().getHeight());

            // Calculate titleTextXPosition using windowWidth
            double titleTextXPosition = (window.getWidth() - titleText.getBoundsInLocal().getWidth()) / 2;
            // Use titleTextXPosition
            titleText.setTranslateX(titleTextXPosition);

             // Calculate playButtonXPosition using windowWidth
             double playButtonXPosition = (window.getWidth() - playButton.getBoundsInLocal().getWidth()) / 2;
             // Use playButtonXPosition
             playButton.setTranslateX(playButtonXPosition);

             // Calculate settingsButtonXPosition using windowWidth
             double settingsButtonXPosition = (window.getWidth() - settingsButton.getBoundsInLocal().getWidth()) / 2;
             // Use settingsButtonXPosition
             settingsButton.setTranslateX(settingsButtonXPosition);

             // Calculate exitButtonXPosition using windowWidth
             double exitButtonXPosition = (window.getWidth() - exitButton.getBoundsInLocal().getWidth()) / 2;
             // Use exitButtonXPosition
             exitButton.setTranslateX(exitButtonXPosition);

             // Calculate exitTextXPosition using windowWidth
             // double exitTextXPosition = (windowWidth - exitText.getBoundsInLocal().getWidth()) / 2;
             // Use exitTextXPosition
             // exitText.setTranslateX(exitTextXPosition);

             // Calculate X value for exitText
             String exitText_Style_String = String.format("-fx-font-family: 'COMIC SANS'; -fx-font-weight: normal; -fx-font-size: 10; -fx-fill: #000000; -fx-translate-x: %.2f;", (window.getWidth() / 2) - (exitText.getBoundsInLocal().getWidth() / 2));
             exitText.setStyle(exitText_Style_String);

             // Calculate X value for player one
//             playerOneXPosition = 30;
//             playerTwoXPosition = (int) windowWidth - (PlayerTwo.getXSize() + 30);
//
//             gameCanvas.setWidth(windowWidth);
//             gc.setFill(Color.RED);
//             gc.setFont(Font.font(25));
//             gc.clearRect(0, 0, windowWidth, windowHeight);
//             gc.fillRect(playerOneXPosition, playerOneYPosition, PlayerOne.getXSize(), PlayerOne.getYSize());
//             gc.fillRect(playerTwoXPosition, playerTwoYPosition, PlayerTwo.getXSize(), PlayerTwo.getYSize());
//
//             String scoreText = scoreP1 + "\t\t\t\t" + scoreP2;
//             Text text = new Text();
//             text.setFont(gc.getFont());
//             text.setText(scoreText);
//             double  scoreTextWidth = text.getLayoutBounds().getWidth();
//             gc.fillText(scoreText, (windowWidth /2) - (scoreTextWidth / 2), 100);

             if (!gameLive) {
                 ball.setXPosition((int)window.getWidth() / 2);
             }

             drawGameElements(window.getWidth(), window.getHeight());
        });



        ////////////////////////////////
        // Find And Set Window Height
        ////////////////////////////////

        // Find height of window
        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            window.setHeight((double) newHeight);
            System.out.println("New Height: " + window.getHeight());

            // Calculate titleTextYPosition using windowHeight
            double titleTextYPosition = (window.getHeight() - titleText.getBoundsInLocal().getHeight()) / 4;
            // Use titleTextYPosition
            titleText.setTranslateY(titleTextYPosition);

            // Calculate playButtonYPosition using windowHeight
            double playButtonYPosition = (window.getHeight() - playButton.getBoundsInLocal().getWidth()) / 2;
            // Use playButtonYPosition
            playButton.setTranslateY(playButtonYPosition);

            // Calculate settingsButtonYPosition using windowHeight
            double settingsButtonYPosition = (playButtonYPosition - settingsButton.getBoundsInLocal().getHeight() + 35);
            // Use settingsButtonYPosition
            settingsButton.setTranslateY(settingsButtonYPosition);

            // Calculate exitButtonYPosition using windowHeight
            double exitButtonYPosition = (settingsButtonYPosition - exitButton.getBoundsInLocal().getHeight() + 35);
            // Use exitButtonYPosition
            exitButton.setTranslateY(exitButtonYPosition);

            // Calculate exitTextYPosition using windowHeight
            double exitTextYPosition = (window.getHeight() - 100) / 2;
            // Use exitTextYPosition
            exitText.setTranslateY(exitTextYPosition);
            // System.out.println("Exit Text Y Position: " + exitTextYPosition);

            // Calculate Y value for exitText
            String exitText_Style_String = String.format("-fx-font-family: 'COMIC SANS'; -fx-font-weight: normal; -fx-font-size: 10; -fx-fill: #000000; -fx-translate-x: %.2f;", (window.getWidth() / 2) - (exitText.getBoundsInLocal().getWidth() / 2));
            exitText.setStyle(exitText_Style_String);

            // Update player locations
            playerOneYPosition = (int) (window.getHeight() / 2) - playerOne.getYSize();
            playerTwoYPosition = (int) (window.getHeight() / 2) - playerTwo.getYSize();

            gameCanvas.setHeight(window.getHeight());

//            gc.clearRect(0, 0, windowWidth, windowHeight);
//            gc.fillRect(playerOneXPosition, playerOneYPosition, PlayerOne.getXSize(), PlayerOne.getYSize());
//            gc.fillRect(playerTwoXPosition, playerTwoYPosition, PlayerTwo.getXSize(), PlayerTwo.getYSize());

            if (!gameLive) {
                ball.setYPosition((int)window.getHeight() / 2);
            }

            drawGameElements(window.getWidth(), window.getHeight());
        });

//        gameCanvas = new Canvas(windowWidth, windowHeight);
//        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
//
//        gameLayout.getChildren().add(gameCanvas);

//        // Add title to window
//        primaryStage.setTitle("Title of the window");
//        primaryStage.setMinWidth(800);
//        primaryStage.setMinHeight(600);

//        playButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
//        settingsButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
//        exitButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");

//        settingsBackButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
//        settingsBackButton.setTranslateX(10);
//        settingsBackButton.setTranslateY(10);

        VBox layout = new VBox(20, loadingErrorText, titleText, playButton, settingsButton, exitButton, exitText);
//        VBox settingsLayout = new VBox(10, settingsBackButton);
//         VBox gameLayout = new VBox(10);

        // Display loadingErrorText only for a short while
        loadingErrorText.setVisible(false);
        if (loadSettingsFailedMessageVisible) {
            loadingErrorText.setVisible(true);
            Duration displayDuration = Duration.seconds(3);
            Timeline timeline = new Timeline(new KeyFrame(displayDuration, e -> loadingErrorText.setVisible(false)));
            timeline.play();
        }

        homeScene = new Scene(layout, window.getWidth(), window.getHeight());
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #00FFFF, #00FF44);");
        // sublayout.setStyle("-fx-background-color: linear-gradient(to bottom, #00FFFF, #00FF00);");

//        settingsScene = new Scene(settingsLayout, window.getWidth(), window.getHeight());
//        settingsLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #00FFFF, #00FF44);");

        gameScene = new Scene(gameLayout, window.getWidth(), window.getHeight());
        gameLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #00FFFF, #00FF44);");

        ////////////////////////////////////////////////////////////////



        ////////////////////////////////
        // Creating Settings Menu
        ////////////////////////////////

        // Settings scene back button
        Button settingsBackButton = new Button("Back (Esc)");
        settingsBackButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
        settingsBackButton.setTranslateX(10);
        settingsBackButton.setTranslateY(10);

        VBox settingsLayout = new VBox(10, settingsBackButton);

        // Scrolling functionality
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(settingsLayout);
        scrollPane.setFitToWidth(true);

        settingsScene = new Scene(scrollPane, window.getWidth(), window.getHeight());
        settingsLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #00FFFF, #00FF44);");



        ////////////////////////////////
        // Settings Menu Items
        ////////////////////////////////

        // Player One Name
        Label playerOneNameInputLabel = new Label("Player One Name: ");
        TextField playerOneNameTextField = new TextField();
        Button playerOneNameSubmitButton = new Button("Submit");
        Label playerOneNameLabel = new Label();
        playerOneNameSubmitButton.setOnAction(e -> {
            String enteredName = playerOneNameTextField.getText();
            playerOneNameLabel.setText("Hello, " + enteredName + "!");
            System.out.println(playerOne.getName());
            playerOne.setName(enteredName);
            System.out.println(playerOne.getName());
        });

        settingsLayout.getChildren().add(playerOneNameInputLabel);
        settingsLayout.getChildren().add(playerOneNameTextField);
        settingsLayout.getChildren().add(playerOneNameSubmitButton);
        settingsLayout.getChildren().add(playerOneNameLabel);

        // Player Two Name
        Label playerTwoNameInputLabel = new Label("Player Two Name: ");
        TextField playerTwoNameTextField = new TextField();
        Button playerTwoNameSubmitButton = new Button("Submit");
        Label playerTwoNameLabel = new Label();
        playerTwoNameSubmitButton.setOnAction(e -> {
            String enteredName = playerTwoNameTextField.getText();
            playerTwoNameLabel.setText("Hello, " + enteredName + "!");
            System.out.println(playerTwo.getName());
            playerTwo.setName(enteredName);
            System.out.println(playerTwo.getName());
        });

        settingsLayout.getChildren().add(playerTwoNameInputLabel);
        settingsLayout.getChildren().add(playerTwoNameTextField);
        settingsLayout.getChildren().add(playerTwoNameSubmitButton);
        settingsLayout.getChildren().add(playerTwoNameLabel);

        // Set Ball Speed
        Label numberSpinnerBallSpeedLabel = new Label("Ball Speed: ");
        Spinner<Integer> numberSpinnerBallSpeed = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3);
        numberSpinnerBallSpeed.setValueFactory(valueFactory);
        numberSpinnerBallSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            ball.setStraightSpeed(newValue);
            System.out.println("Ball Speed: " + ball.getStraightSpeed());
            ball_speed_setting_option = newValue;
            System.out.println("ball_speed_setting_option: " + ball_speed_setting_option);
        });

        settingsLayout.getChildren().add(numberSpinnerBallSpeedLabel);
        settingsLayout.getChildren().add(numberSpinnerBallSpeed);

        // Adjusting The Sizes Of Player One

        // Rectangle playerOneRectangle = new Rectangle(playerOne.getXSize(), playerOne.getYSize(), Color.RED);
        // gameLayout.getChildren().add(playerOneRectangle);

        // Adjusting The Position Of Player One

//        // Calculate X value for player one
//        playerOneXPosition = 30;
//        playerOneYPosition = (int) (windowHeight / 2) - (PlayerOne.getYSize() / 2);

//        Canvas canvas = new Canvas(windowWidth, windowHeight);
//        gameLayout.getChildren().add(canvas);
//        GraphicsContext gc = canvas.getGraphicsContext2D();
//        gc.fillRect(playerOneXPosition, playerOneYPosition, PlayerOne.getXSize(), PlayerOne.getYSize());

        // Adjusting The Size Of Player Two

//        // Calculate X value for player two
//        playerTwoXPosition = (int) windowWidth - (PlayerTwo.getXSize() + 30);
        playerTwoYPosition = (int) (window.getHeight() / 2) - (playerTwo.getYSize() / 2);

        // Adjusting The Width Of Player One

        Label numberSpinnerPlayerOneXSizeLabel = new Label("Player One XSize: ");
        Spinner<Integer> numberSpinnerPlayerOneXSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5);
        numberSpinnerPlayerOneXSize.setValueFactory(valueFactory2);
        numberSpinnerPlayerOneXSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerOne.setXSize(newValue);
            System.out.println("Player One X Size: " + playerOne.getXSize());
            System.out.println("Player One Y Size: " + playerOne.getYSize());
            System.out.println("Player Two X Size: " + playerTwo.getXSize());
            System.out.println("Player Two Y Size: " + playerTwo.getYSize());
            System.out.println("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerPlayerOneXSizeLabel);
        settingsLayout.getChildren().add(numberSpinnerPlayerOneXSize);

        // Adjusting The Height Of Player One

        Label numberSpinnerPlayerOneYSizeLabel = new Label("Player One YSize: ");
        Spinner<Integer> numberSpinnerPlayerOneYSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 2);
        numberSpinnerPlayerOneYSize.setValueFactory(valueFactory3);
        numberSpinnerPlayerOneYSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerOne.setYSize(newValue);
            System.out.println("Player One X Size: " + playerOne.getXSize());
            System.out.println("Player One Y Size: " + playerOne.getYSize());
            System.out.println("Player Two X Size: " + playerTwo.getXSize());
            System.out.println("Player Two Y Size: " + playerTwo.getYSize());
            System.out.println("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerPlayerOneYSizeLabel);
        settingsLayout.getChildren().add(numberSpinnerPlayerOneYSize);

        // Adjusting The Width Of Player Two

        Label numberSpinnerPlayerTwoXSizeLabel = new Label("Player Two XSize: ");
        Spinner<Integer> numberSpinnerPlayerTwoXSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5);
        numberSpinnerPlayerTwoXSize.setValueFactory(valueFactory4);
        numberSpinnerPlayerTwoXSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerTwo.setXSize(newValue);
            System.out.println("Player One X Size: " + playerOne.getXSize());
            System.out.println("Player One Y Size: " + playerOne.getYSize());
            System.out.println("Player Two X Size: " + playerTwo.getXSize());
            System.out.println("Player Two Y Size: " + playerTwo.getYSize());
            System.out.println("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerPlayerTwoXSizeLabel);
        settingsLayout.getChildren().add(numberSpinnerPlayerTwoXSize);

        // Adjusting The Height Of Player Two

        Label numberSpinnerPlayerTwoYSizeLabel = new Label("Player Two YSize: ");
        Spinner<Integer> numberSpinnerPlayerTwoYSize = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 2);
        numberSpinnerPlayerTwoYSize.setValueFactory(valueFactory5);
        numberSpinnerPlayerTwoYSize.valueProperty().addListener((observable, oldValue, newValue) -> {
            playerTwo.setYSize(newValue);
            System.out.println("Player One X Size: " + playerOne.getXSize());
            System.out.println("Player One Y Size: " + playerOne.getYSize());
            System.out.println("Player Two X Size: " + playerTwo.getXSize());
            System.out.println("Player Two Y Size: " + playerTwo.getYSize());
            System.out.println("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerPlayerTwoYSizeLabel);
        settingsLayout.getChildren().add(numberSpinnerPlayerTwoYSize);

        // Adjusting Winning Score

        Label numberSpinnerWinningScoreLabel = new Label("Winning Score: ");
        Spinner<Integer> numberSpinnerWinningScore = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory6 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3);
        numberSpinnerWinningScore.setValueFactory(valueFactory6);
        numberSpinnerWinningScore.valueProperty().addListener((observable, oldValue, newValue) -> {
            winningScore = newValue;
            System.out.print("Winning Score: " + winningScore);
            System.out.print("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerWinningScoreLabel);
        settingsLayout.getChildren().add(numberSpinnerWinningScore);

        // Adjusting Ball Speed Increase Threshold

        Label numberSpinnerBallSpeedIncreaseLabel = new Label("After How Many Hits Should The Speed Of The Ball Increase? ");
        Spinner<Integer> numberSpinnerBallSpeedIncrease = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory7 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 2);
        numberSpinnerBallSpeedIncrease.setValueFactory(valueFactory7);
        numberSpinnerBallSpeedIncrease.valueProperty().addListener((observable, oldValue, newValue) -> {
            ballSpeedIncreaseOption = newValue;
            System.out.print("Ball Speed Increases After " + ballSpeedIncreaseOption + " Bounces");
            System.out.print("\n");
        });

        settingsLayout.getChildren().add(numberSpinnerBallSpeedIncreaseLabel);
        settingsLayout.getChildren().add(numberSpinnerBallSpeedIncrease);

        // Save Settings Button

        Button saveSettingsButton = new Button("Save Settings");
        saveSettingsButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-font-size: 20px;");
        saveSettingsButton.setTranslateX(10);
        settingsLayout.getChildren().add(saveSettingsButton);
        saveSettingsButton.setOnAction(e -> serializeSettings());



        ////////////////////////////////
        // Game Scene Stuff
        ////////////////////////////////

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                gameLive = true;
                System.out.println("Enter key press test");
                roundStart();
            }
        });
        

        ////////////////////////////////////////////////////////////////

        // Exit Button
        exitButton.setOnAction(e -> exitApplication()); // Esc to quit

        // Settings Button
        settingsButton.setOnAction(e -> { // Storing whether you go to settings from the home scene or the game scene
            settingsScenePredecessor = false;
            switchToSettingsScene();
        });

        // Play Button
        playButton.setOnAction(e -> switchToGameScene());

        // Settings Back Button
        settingsBackButton.setOnAction(e -> {
            if (!settingsScenePredecessor) {
                switchToHomeScene();
            }
            else {
                primaryStage.setScene(gameScene);
            }
        });

        // Escape key
        homeScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                primaryStage.close();
            }
        });

        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                if (!gameLive) {
                    isPauseMenuVisible = false;
                    gameLive = true;
                }
                else if (gameLive) {
                    gameLive = false;
                    isPauseMenuVisible = true;
                }
            }
        });

        gameScene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());
            handlePressedKeys();
            drawGameElements(window.getWidth(), window.getHeight());
        });

        gameScene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
            handlePressedKeys();
        });

        settingsScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                if (!settingsScenePredecessor) {
                    switchToHomeScene();
                }
                else {
                    primaryStage.setScene(gameScene);
                }
            }
        });

        primaryStage.setTitle("PONG");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }



    ////////////////////////////////
    // Small Functions
    ////////////////////////////////

    /**
     * This method is used to close the application when called
     */

    private void exitApplication() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * This method is used to change to the settings scene
     */

    private void switchToSettingsScene() {
        primaryStage.setScene(settingsScene);
    }

    /**
     * This method is used to change to the home scene
     */

    private void switchToHomeScene() {
        primaryStage.setScene(homeScene);
    }

    /**
     * This method is used to change to the game scene, and initializes stuff, sets ball position, calls drawGameElements
     */

    private void switchToGameScene() {
        ball.setXPosition((int)window.getWidth() / 2);
        ball.setYPosition((int)window.getHeight() / 2);
        initializePosition(); // Initialize player positions
        primaryStage.setScene(gameScene);
        pressedKeys.remove(KeyCode.ENTER);
        isStartMessageVisible = true;
        gameLive = false;
        drawGameElements(window.getWidth(), window.getHeight());
        resetGame();
    }



    ////////////////////////////////
    // Draw Game Elements
    ////////////////////////////////

    /**
     * This method draws the game elements every time it is called, called many times a second to update ball and racket positions, and to update messages and scores, uses windowWidth and windowHeight to position items correctly
     * @param windowWidth
     * @param windowHeight
     */

    private void drawGameElements(double windowWidth, double windowHeight) {

        playerOneXPosition = 30;
        playerTwoXPosition = (int) window.getWidth() - playerTwo.getXSize() - 45;

        gameCanvas.setWidth(windowWidth);
        gameCanvas.setHeight(windowHeight);

        // Display players
        gc.clearRect(0, 0, windowWidth, windowHeight);
        gc.setFill(Color.RED);
        gc.setFont(Font.font(25));
        // gc.fillRect(playerOneXPosition, (playerOneYPosition + ((double) (playerOne.getYSize()) / 2)), playerOne.getXSize(), playerOne.getYSize());
        gc.fillRect(playerOneXPosition, playerOneYPosition, playerOne.getXSize(), playerOne.getYSize());
        gc.fillRect(playerTwoXPosition, playerTwoYPosition, playerTwo.getXSize(), playerTwo.getYSize());

        // Display ball
        // ball.setXPosition((int) windowWidth / 2);
        // ball.setYPosition((int) windowHeight / 2);
        ball.setRadius(20);
        gc.fillOval((ball.getXPosition() - ball.getRadius()), (ball.getYPosition() - ball.getRadius()), ball.getRadius(), ball.getRadius());

        // Display player one name
        String playerOneNameText = playerOne.getName();
        Text nameP1Text = new Text();
        nameP1Text.setFont(gc.getFont());
        nameP1Text.setText(playerOneNameText);
        double playerOneNameTextWidth = nameP1Text.getLayoutBounds().getWidth();
        gc.fillText(playerOneNameText, (windowWidth * 0.25) - (playerOneNameTextWidth / 2), windowHeight * 0.1);

        // Display player two name
        String playerTwoNameText = playerTwo.getName();
        Text nameP2Text = new Text();
        nameP2Text.setFont(gc.getFont());
        nameP2Text.setText(playerTwoNameText);
        double playerTwoNameTextWidth = nameP2Text.getLayoutBounds().getWidth();
        gc.fillText(playerTwoNameText, (windowWidth * 0.75) - (playerTwoNameTextWidth / 2), windowHeight * 0.1);

        // Display player one score
        String scoreOneText = String.format("%d", scoreP1);
        Text scoreP1Text = new Text();
        scoreP1Text.setFont(gc.getFont());
        scoreP1Text.setText(scoreOneText);
        double scoreOneTextWidth = scoreP1Text.getLayoutBounds().getWidth();
        gc.fillText(scoreOneText, (windowWidth * 0.25) - (scoreOneTextWidth / 2), (windowHeight * 0.1) + 50);

        // Display player two score
        String scoreTwoText = String.format("%d", scoreP2);
        Text scoreP2Text = new Text();
        scoreP2Text.setFont(gc.getFont());
        scoreP2Text.setText(scoreTwoText);
        double scoreTwoTextWidth = scoreP2Text.getLayoutBounds().getWidth();
        gc.fillText(scoreTwoText, (windowWidth * 0.75) - (scoreTwoTextWidth / 2), (windowHeight * 0.1) + 50);

        // Display pause menu tip text
        String pauseMenuTip = String.format("Press Esc to pause");
        Text pauseMenuTipText = new Text();
        pauseMenuTipText.setFont(gc.getFont());
        pauseMenuTipText.setText(pauseMenuTip);
        double pauseMenuTipTextWidth = pauseMenuTipText.getLayoutBounds().getWidth();
        gc.fillText(pauseMenuTip, (window.getWidth() / 2) - (pauseMenuTipTextWidth / 2), 30);

        // Display start message
        if (isStartMessageVisible) {
            String startMessage = String.format("Press Enter to start");
            Text startMessageText = new Text();
            startMessageText.setFont(gc.getFont());
            startMessageText.setText(startMessage);
            double startMessageTextWidth = startMessageText.getLayoutBounds().getWidth();
            gc.fillText(startMessage, (windowWidth / 2) - (startMessageTextWidth / 2), (windowHeight / 2) + 50);

            String loadGameMessage = String.format("Press L to load previous game");
            Text loadGameMessageText = new Text();
            loadGameMessageText.setFont(gc.getFont());
            loadGameMessageText.setText(loadGameMessage);
            double loadGameMessageTextWidth = loadGameMessageText.getLayoutBounds().getWidth();
            gc.fillText(loadGameMessage, (windowWidth / 2) - (loadGameMessageTextWidth / 2), (windowHeight / 2) + 100);
        }

        // Display player one scores message
        if (isPlayerOneScoresMessageVisible) {
            String playerOneScoresMessage = String.format("%s Scores!", playerOne.getName());
            Text playerOneScoresMessageText = new Text();
            playerOneScoresMessageText.setFont(gc.getFont());
            playerOneScoresMessageText.setText(playerOneScoresMessage);
            double playerOneScoresMessageTextWidth = playerOneScoresMessageText.getLayoutBounds().getWidth();
            gc.fillText(playerOneScoresMessage, (windowWidth / 2) - (playerOneScoresMessageTextWidth / 2), (windowHeight / 2) + 50);
        }

        // Display player two scores message
        if (isPlayerTwoScoresMessageVisible) {
            String playerTwoScoresMessage = String.format("%s Scores!", playerTwo.getName());
            Text playerTwoScoresMessageText = new Text();
            playerTwoScoresMessageText.setFont(gc.getFont());
            playerTwoScoresMessageText.setText(playerTwoScoresMessage);
            double playerTwoScoresMessageTextWidth = playerTwoScoresMessageText.getLayoutBounds().getWidth();
            gc.fillText(playerTwoScoresMessage, (window.getWidth() / 2) - (playerTwoScoresMessageTextWidth / 2), (window.getHeight() / 2) + 50);
        }

        // Display player one wins message
        if (isPlayerOneWinsMessageVisible) {
            String playerOneWinsMessage = String.format("%s Wins!", playerOne.getName());
            Text playerOneWinsMessageText = new Text();
            playerOneWinsMessageText.setFont(gc.getFont());
            playerOneWinsMessageText.setText(playerOneWinsMessage);
            double playerOneScoresMessageTextWidth = playerOneWinsMessageText.getLayoutBounds().getWidth();
            gc.fillText(playerOneWinsMessage, (window.getWidth() / 2) - (playerOneScoresMessageTextWidth / 2), (window.getHeight() / 2) + 50);
        }

        // Display player two wins message
        if (isPlayerTwoWinsMessageVisible) {
            String playerTwoWinsMessage = String.format("%s Wins!", playerTwo.getName());
            Text playerTwoWinsMessageText = new Text();
            playerTwoWinsMessageText.setFont(gc.getFont());
            playerTwoWinsMessageText.setText(playerTwoWinsMessage);
            double playerTwoScoresMessageTextWidth = playerTwoWinsMessageText.getLayoutBounds().getWidth();
            gc.fillText(playerTwoWinsMessage, (window.getWidth() / 2) - (playerTwoScoresMessageTextWidth / 2), (window.getHeight() / 2) + 50);
        }

        // Display enter to continue message
        if (isEnterMessageVisible) {
            String enterMessage = String.format("Press enter to continue");
            Text enterMessageText = new Text();
            enterMessageText.setFont(gc.getFont());
            enterMessageText.setText(enterMessage);
            double enterMessageTextWidth = enterMessageText.getLayoutBounds().getWidth();
            gc.fillText(enterMessage, (window.getWidth() / 2) - (enterMessageTextWidth / 2), (window.getHeight() / 2) + 100);
        }

        // Display game scene error message
        if (isGameSceneErrorMessageVisible) {
            String gameSceneErrorMessage = String.format("Error! Press Esc.");
            Text gameSceneErrorMessageText = new Text();
            gameSceneErrorMessageText.setFont(gc.getFont());
            gameSceneErrorMessageText.setText(gameSceneErrorMessage);
            double gameSceneErrorMessageTextWidth = gameSceneErrorMessageText.getLayoutBounds().getWidth();
            gc.fillText(gameSceneErrorMessage, (window.getWidth() / 2) - (gameSceneErrorMessageTextWidth / 2), (window.getHeight() / 2) + 50);
        }

        // Display pause menu
        if (isPauseMenuVisible) {
            // Dim the game scene
//            Rectangle dimmingRectangle = new Rectangle();
//            dimmingRectangle.setSize((int)window.getWidth(), (int)window.getHeight());
//            dimmingRectangle.set
            gc.setFill(Color.rgb(0, 0, 0, 0.75));
            gc.setFont(Font.font(50));
            gc.fillRect(0, 0, window.getWidth(), window.getHeight());
            gc.setFill(Color.YELLOW);

            // Paused text
            String pauseMenuPausedMessage = String.format("PAUSED");
            Text pauseMenuPausedMessageText = new Text();
            pauseMenuPausedMessageText.setFont(gc.getFont());
            pauseMenuPausedMessageText.setText(pauseMenuPausedMessage);
            double pauseMenuPausedMessageTextWidth = pauseMenuPausedMessageText.getLayoutBounds().getWidth();
            gc.fillText(pauseMenuPausedMessage, (window.getWidth() / 2) - (pauseMenuPausedMessageTextWidth / 2), (window.getHeight() / 2) - 100);

            gc.setFont(Font.font(25));

            // Enter text
            String pauseMenuEnterMessage = String.format("Press Enter to continue");
            Text pauseMenuEnterMessageText = new Text();
            pauseMenuEnterMessageText.setFont(gc.getFont());
            pauseMenuEnterMessageText.setText(pauseMenuEnterMessage);
            double pauseMenuEnterMessageTextWidth = pauseMenuEnterMessageText.getLayoutBounds().getWidth();
            gc.fillText(pauseMenuEnterMessage, (window.getWidth() / 2) - (pauseMenuEnterMessageTextWidth / 2), (window.getHeight() / 2) + 25);

            // Settings text
            String pauseMenuSettingsMessage = String.format("Press S to go to settings");
            Text pauseMenuSettingsMessageText = new Text();
            pauseMenuSettingsMessageText.setFont(gc.getFont());
            pauseMenuSettingsMessageText.setText(pauseMenuSettingsMessage);
            double pauseMenuSettingsMessageTextWidth = pauseMenuSettingsMessageText.getLayoutBounds().getWidth();
            gc.fillText(pauseMenuSettingsMessage, (window.getWidth() / 2) - (pauseMenuSettingsMessageTextWidth / 2), (window.getHeight() / 2) + 75);

            // Save Game text
            String pauseMenuSaveGameMessage = String.format("Press G to save the game");
            Text pauseMenuSaveGameMessageText = new Text();
            pauseMenuSaveGameMessageText.setFont(gc.getFont());
            pauseMenuSaveGameMessageText.setText(pauseMenuSaveGameMessage);
            double pauseMenuSaveGameMessageTextWidth = pauseMenuSaveGameMessageText.getLayoutBounds().getWidth();
            gc.fillText(pauseMenuSaveGameMessage, (window.getWidth() / 2) - (pauseMenuSaveGameMessageTextWidth / 2), (window.getHeight() / 2) + 125);

            // Exit text
            String pauseMenuExitMessage = String.format("Press Esc to exit");
            Text pauseMenuExitMessageText = new Text();
            pauseMenuExitMessageText.setFont(gc.getFont());
            pauseMenuExitMessageText.setText(pauseMenuExitMessage);
            double pauseMenuExitMessageTextWidth = pauseMenuExitMessageText.getLayoutBounds().getWidth();
            gc.fillText(pauseMenuExitMessage, (window.getWidth() / 2) - (pauseMenuExitMessageTextWidth / 2), (window.getHeight() / 2) + 175);

            gc.setFill(Color.RED);
        }
    }

    /**
     * This method handles key presses, using logic to determine the current state of the application, and what each key should do at the time, different keys have different functions at different times
     */

    private void handlePressedKeys() {
//        if (pressedKeys.contains(KeyCode.ESCAPE)){
//            gameLive = false;
//            primaryStage.setScene(homeScene);
//            pressedKeys.remove(KeyCode.ESCAPE);
//        }
        if (gameLive) {// If the game is running
            if (pressedKeys.contains(KeyCode.W)){ // Player 1 controls
                if (playerOneYPosition > 0) {
                    playerOneYPosition = playerOneYPosition - ((int) window.getHeight() / 40);
                }
                drawGameElements(window.getWidth(), window.getHeight());
            }
            if (pressedKeys.contains(KeyCode.S)){ // Player 1 controls
                if (playerOneYPosition < (window.getHeight() - playerOne.getYSize() - 40)) {
                    playerOneYPosition = playerOneYPosition + ((int) window.getHeight() / 40);
                }
                drawGameElements(window.getWidth(), window.getHeight());
            }
            if (pressedKeys.contains(KeyCode.UP)){ // Player 2 controls
                if (playerTwoYPosition > 0) {
                    playerTwoYPosition = playerTwoYPosition - ((int) window.getHeight() / 40);
                }
                drawGameElements(window.getWidth(), window.getHeight());
            }
            if (pressedKeys.contains(KeyCode.DOWN)){ // Player 2 controls
                if (playerTwoYPosition < (window.getHeight() - playerTwo.getYSize() - 40)) {
                    playerTwoYPosition = playerTwoYPosition + ((int) window.getHeight() / 40);
                }
                drawGameElements(window.getWidth(), window.getHeight());
            }
            if (pressedKeys.contains(KeyCode.ESCAPE)) { // Press ESCAPE to pause the game
                pressedKeys.remove(KeyCode.ESCAPE);
                gameLive = false;
                isPauseMenuVisible = true;
            }

        }
        else { // If the game is not running
            if (!isPauseMenuVisible) { // If the start message or a score/win message is showing
                if (pressedKeys.contains(KeyCode.ESCAPE)) { // Press ESCAPE to go to the main menu
                    pressedKeys.remove(KeyCode.ESCAPE);
                    isPlayerOneScoresMessageVisible = false;
                    isPlayerOneWinsMessageVisible = false;
                    isPlayerTwoScoresMessageVisible = false;
                    isPlayerTwoWinsMessageVisible = false;
                    isGameSceneErrorMessageVisible = false;
                    isEnterMessageVisible = false;
                    resetGame();
                    switchToHomeScene();
                }
            }
            else { // If the pause menu is showing
                if (pressedKeys.contains(KeyCode.ESCAPE)) { // Press ESCAPE to go to the main menu
                    pressedKeys.remove(KeyCode.ESCAPE);
                    isPauseMenuVisible = false;
                    switchToHomeScene();
                    resetGame();
                }
                else if (pressedKeys.contains(KeyCode.ENTER)) { // Press ENTER to unpause
                    pressedKeys.remove(KeyCode.ENTER);
                    isPauseMenuVisible = false;
                    gameLive = true;
                    gameRun();
                }
                else if (pressedKeys.contains(KeyCode.S)) { // Press S to go to settings
                    pressedKeys.remove(KeyCode.S);
                    settingsScenePredecessor = true;
                    switchToSettingsScene();
                }
                else if (pressedKeys.contains(KeyCode.G)) { // Press G to save the game
                    pressedKeys.remove(KeyCode.G);
                    serializeGame();
                }
            }
            if (isStartMessageVisible) {
                if (pressedKeys.contains(KeyCode.L)) {
                    pressedKeys.remove(KeyCode.L);
                    deserializeGame();
                }
            }
        }
        if (pressedKeys.contains(KeyCode.ENTER)){ // If ENTER is pressed
            pressedKeys.remove(KeyCode.ENTER);
            isStartMessageVisible = false;
            if (!gameLive) { // And the game is not live
                shouldRoundStartBeCalled = true; // The start of the round will be called
                hasGameStarted = true; // The game has begun
            }
            else {
                shouldRoundStartBeCalled = false; // If the game is live, RoundStart() will not be called
            }
            gameLive = true; // The game is set to live
            if (shouldRoundStartBeCalled) { // If RoundStart() should be called
                isPlayerOneScoresMessageVisible = false; // All the messages are hidden
                isPlayerOneWinsMessageVisible = false;
                isPlayerTwoScoresMessageVisible = false;
                isPlayerTwoWinsMessageVisible = false;
                isGameSceneErrorMessageVisible = false;
                isEnterMessageVisible = false;
                roundStart(); // Round start is called
            }
            drawGameElements(window.getWidth(), window.getHeight());
        }
        if (pressedKeys.contains(KeyCode.R)) { // Press R at any time to re-render the game elements
            initializePosition();
            drawGameElements(window.getWidth(), window.getHeight());
            pressedKeys.remove(KeyCode.R);
        }
    };

    /**
     * This method initializes the game scene, and prepares for a new round, generates a random direction for the ball to travel in, calls gameRun() function
     */

    private void roundStart() {
        System.out.println("Round Start Test");
        initializePosition(); // Initialize player positions
        ball.setStraightSpeed(ball_speed_setting_option); // Reset ball speed
        Random random = new Random(); // Generate random direction for the ball

        int initial_X_Value_Option_1 = -1;
        int initial_X_Value_Option_2 = 1;
        int initial_Y_Value_Option_1 = -1;
        int initial_Y_Value_Option_2 = 1;

        // Random random_initial_vector = new Random();
        boolean initial_X_Value_Option_3 = random.nextBoolean();
        int initial_X_Value_Option_4;
        if (initial_X_Value_Option_3){
            initial_X_Value_Option_4 = initial_X_Value_Option_1;
        }
        else {
            initial_X_Value_Option_4 = initial_X_Value_Option_2;
        }
        boolean initial_Y_Value_Option_3 = random.nextBoolean();
        int initial_Y_Value_Option_4;
        if (initial_Y_Value_Option_3){
            initial_Y_Value_Option_4 = initial_Y_Value_Option_1;
        }
        else {
            initial_Y_Value_Option_4 = initial_Y_Value_Option_2;
        }

        // ball.setVectorSpeed(initial_X_Value_Option_4, initial_Y_Value_Option_4);
        if (!hasRoundStarted) { // If the round hasn't already started (not just unpausing the game), then the random direction will be applied to the ball
            ball.setVectorSpeed(initial_X_Value_Option_4, initial_Y_Value_Option_4);
            System.out.println(ball.getVectorSpeed());
        }

        hasRoundStarted = true; // The round has started
        gameRun(); // Call the gameRun() loop
    }

    // Game Loop

    /**
     * This method runs the game, running through constantly, many times a second, updating player and ball positions, messages, scores, detecting collisions, etc
     */

    public void gameRun() {

        AnimationTimer gameLoop = new AnimationTimer() {
            public void handle(long now) {

                // Check for collisions
                // Check for collisions - Top
                if (ball.getYPosition() < ball.getRadius()) {
                    ball.setVectorSpeed((int)ball.getVectorSpeedX(), (int)(ball.getVectorSpeedY() * -1));
                }
                // Check for collisions - Bottom
                if (ball.getYPosition() > (window.getHeight() - (ball.getRadius() * 2))) {
                    ball.setVectorSpeed((int)ball.getVectorSpeedX(), (int)(ball.getVectorSpeedY() * -1));
                }
                // Check for collisions - Left racket - Front
                // if (ball X position < player one right side && ball Y is below top of player one && ball Y is above bottom of player one)
                if (((ball.getXPosition() < (playerOneXPosition + playerOne.getXSize() + ball.getRadius())) && (ball.getXPosition() > playerOneXPosition)) && ((ball.getYPosition() > playerOneYPosition) && ball.getYPosition() < (playerOneYPosition + playerOne.getYSize()))) {
                    ball.setVectorSpeed((int)(Math.abs(ball.getVectorSpeedX()) * 1), (int)ball.getVectorSpeedY());
                    // ballSpeedIncreaseCount++;
                    ballBounceCounter++;
                }
                // Check for collisions - Left racket - Front
                if (((ball.getXPosition() > playerTwoXPosition) && ball.getXPosition() < playerTwoXPosition + playerTwo.getXSize()) && ((ball.getYPosition() > playerTwoYPosition) && ball.getYPosition() < (playerTwoYPosition + playerTwo.getYSize()))) {
                    ball.setVectorSpeed((int)(Math.abs(ball.getVectorSpeedX()) * -1), (int)ball.getVectorSpeedY());
                    // ballSpeedIncreaseCount++;
                    ballBounceCounter++;
                }

                // Check to increase ball speed
//                if (ballSpeedIncreaseCount == ballSpeedIncreaseOption) {
//                    ball.setStraightSpeed((ball.getStraightSpeed()) + 1);
//                    ballSpeedIncreaseCount = 0;
//                }
                if (ballBounceCounter >= ballSpeedIncreaseOption) {
                    ball.setStraightSpeed(ball.getStraightSpeed() + 1);
                    ballBounceCounter = 0;
                }

                // Check for score
                // Check for score - Left
                if (ball.getXPosition() < 0) { // If the ball goes "into the goal"
                    scoreP2++; // Increment score
                    gameLive = false; // Stop the game, !live
                    if (scoreP2 < winningScore) { // If the player 2 score is still less than the set winning score
                        isPlayerTwoScoresMessageVisible = true; // Show player 2 score message
                        isEnterMessageVisible = true; // Show enter message
                        ballBounceCounter = 0; // Reset ball bouncing
                    }
                    else if (scoreP2 == winningScore) { // If thr player 2 score is equal to the winning score
                        isPlayerTwoWinsMessageVisible = true; // Show player 2 win message
                        isEnterMessageVisible = true; // Show enter message
                        shouldRoundStartBeCalled = false; // Don't allow another round to be called
                        isGameEnded = true; // The game is finished
                        ballBounceCounter = 0; // Reset ball bouncing
                    }
                    else if (scoreP2 > winningScore) { // If the code has done a little goofy
                        isGameSceneErrorMessageVisible = true; // Show error message
                    }
                    drawGameElements(window.getWidth(), window.getHeight()); // Render yet again
                    ball.setXPosition((int)window.getWidth() / 2);
                    ball.setYPosition((int)window.getHeight() / 2);
                }
                // Check for score - Right
                if (ball.getXPosition() > window.getWidth()) { // If the ball goes "into the other goal"
                    scoreP1++; // Increment score
                    gameLive = false; // Stop the game, !live
                    if (scoreP1 < winningScore) { // If the player 1 score is still less than the set winning score
                        isPlayerOneScoresMessageVisible = true; // Show player 1 score message
                        isEnterMessageVisible = true; // Show enter message
                        ballBounceCounter = 0; // Reset ball counter
                    }
                    else if (scoreP1 == winningScore) { // If the player 1 score is equal to the winning score
                        isPlayerOneWinsMessageVisible = true; // Show player 1 win message
                        isEnterMessageVisible = true; // Show enter message
                        shouldRoundStartBeCalled = false; // Don't allow another round to be called
                        isGameEnded = true; // The game is finished
                        ballBounceCounter = 0; // Reset ball bouncing
                    }
                    else if (scoreP1 > winningScore) { // If the code has done a different little goofy
                        isGameSceneErrorMessageVisible = true; // Show error message
                    }
                    drawGameElements(window.getWidth(), window.getHeight()); // Render yet again
                    ball.setXPosition((int)window.getWidth() / 2);
                    ball.setYPosition((int)window.getHeight() / 2);
                }

                // Update ball position
                ball.setXPosition(ball.getXPosition() + ((int) ball.getVectorSpeedX() * ball.getStraightSpeed()));
                ball.setYPosition(ball.getYPosition() + ((int) ball.getVectorSpeedY() * ball.getStraightSpeed()));

                // Render game elements with updated positions
                drawGameElements(window.getWidth(), window.getHeight());
                if (!gameLive) {
                    stop();
                }

                // Handling the end of the game
                if (isGameEnded) { // If the game is finished
                    gameScene.setOnKeyPressed(event -> {
                        if (event.getCode() == KeyCode.ENTER){ // And the ENTER key is pressed
                            resetGame(); // Reset the game
                            switchToHomeScene(); // And go to the main menu
                        }
                    });
                }

                // Debug printing
                System.out.println("Player One Height: " + playerOneYPosition);
                System.out.println("Ball Height: " + ball.getYPosition());
                System.out.println("Player One Bottom: " + ((int)playerOneYPosition + playerOne.getYSize()));
                System.out.println("Player One X Position: " + playerOneXPosition);
                System.out.println("Ball X Position: " + ball.getXPosition());
                System.out.println("ball_speed_setting_option: " + ball_speed_setting_option);
                System.out.println();
            }
        };
        gameLoop.start();
    }

    // Function for resetting player positions

    /**
     * This method sets the player positions on the Y axis for the start of each round
     */

    public void initializePosition() {
        playerOneYPosition = (int) (window.getHeight() / 2) - (playerOne.getYSize() / 2);
        playerTwoYPosition = (int) (window.getHeight() / 2) - (playerTwo.getYSize() / 2);
    }

    /**
     * This method resets the game, setting scores to 0, reseting positions, booleans, etc
     */


    public void resetGame() {
        scoreP1 = 0;
        scoreP2 = 0;
        ballBounceCounter = 0;
        // ballSpeedIncreaseCount = 2;
        initializePosition();
        ball.setXPosition((int)window.getWidth() / 2);
        ball.setYPosition((int)window.getHeight() / 2);
        isPlayerOneScoresMessageVisible = false;
        isPlayerOneWinsMessageVisible = false;
        isPlayerTwoScoresMessageVisible = false;
        isPlayerTwoWinsMessageVisible = false;
        isEnterMessageVisible = false;
        isGameEnded = false;
        shouldRoundStartBeCalled = true;
    }

    /**
     * This method is used to create a Serialization object which can store settings, and be saved, therefore saving the settings options for later
     */

    public void serializeSettings() { // To save settings to a file
        Serialization serSettings = new Serialization(); // Create object of serialization class
        serSettings.setPlayerOneName(playerOne.getName()); // Settings values to new object
        serSettings.setPlayerTwoName(playerTwo.getName());
        serSettings.setBallSpeed(ball.getStraightSpeed());
        serSettings.setPlayerOneXSize(playerOne.getXSizeForSer());
        serSettings.setPlayerOneYSize(playerOne.getYSizeForSer());
        serSettings.setPlayerTwoXSize(playerTwo.getXSizeForSer());
        serSettings.setPlayerTwoYSize(playerTwo.getYSizeForSer());
        serSettings.setWinningScore(winningScore);
        serSettings.setBallSpeedIncreaseNumber(ballSpeedIncreaseOption);

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("settings.ser"))) {
            oos.writeObject(serSettings);
            System.out.println("Settings serialized successfully");
        } catch (IOException e) {
            System.err.println("Error occurred while serializing settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method loads and sets the saved settings options
     */

    public void deserializeSettings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("settings.ser"))) {
            Serialization deserializedSettings = (Serialization) ois.readObject();
            playerOne.setName(deserializedSettings.getPlayerOneName());
            playerTwo.setName(deserializedSettings.getPlayerTwoName());
            ball.setStraightSpeed(deserializedSettings.getBallSpeed());
            System.out.println("Ball Speed: " + ball.getStraightSpeed());
            playerOne.setXSize(deserializedSettings.getPlayerOneXSize());
            System.out.println("Player One X Size: " + playerOne.getXSize());
            playerOne.setYSize(deserializedSettings.getPlayerOneYSize());
            System.out.println("Player One Y Size: " + playerOne.getYSize());
            playerTwo.setXSize(deserializedSettings.getPlayerTwoXSize());
            System.out.println("Player Two X Size: " + playerTwo.getXSize());
            playerTwo.setYSize(deserializedSettings.getPlayerTwoYSize());
            System.out.println("Player Two Y Size: " + playerTwo.getYSize());
            winningScore = deserializedSettings.getWinningScore();
            ballSpeedIncreaseOption = deserializedSettings.getBallSpeedIncreaseNumber();
            loadSettingsFailedMessageVisible = false;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            loadSettingsFailedMessageVisible = true;
        }
    }

    /**
     * This method saves the game to resume later
     */

    public void serializeGame() {
        SerializeGame serGame = new SerializeGame();
        Vector ballPositionVector = new Vector();
        ballPositionVector.setVector(ball.getXPosition(), ball.getYPosition());
        serGame.setBallPosition(ballPositionVector);
    }

    /**
     * This method loads the last game if present
     */

    public void deserializeGame() {
        try (ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream("game.ser"))) {
            SerializeGame deserializedGame = (SerializeGame) ois2.readObject();
            ball.setXPosition((int)deserializedGame.getBallPosition().getVectorX());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            loadGameFailedMessageVisible = true;
        }
    }
}
