package sample;

import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class Main extends Application {
    public Scene MainScene;
    public Stage MainStage;

    //Creates an instance of the main method which can be called from the controller class
    private static Main instance;

    public Main() {
        instance = this;
    }
    // static method to get instance of Main
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Load the views FXML file
        FXMLLoader mainloader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
        //Loads the default style sheet for everything
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet("sample/GeneralStyle.css");

        Parent root = mainloader.load();
        MainScene = new Scene(root);
        MainStage = primaryStage;

        //extract controller from FXML file so it can be directly called if needed
        Controller myController = mainloader.getController();

        //Creates observable position object
        ObservablePosition myPosition = new ObservablePosition();
        //Creates observer
        PositionObserver observer = new PositionObserver(myController);
        //Adds observer to position (links the two objects)
        myPosition.addObserver(observer);

        //Creates a new UDP server object, passes observable position to the constructor
        UDPServer  MyUDPServer = new UDPServer(myPosition);
        //Makes UDP Server object runnable
        Runnable r = () -> {MyUDPServer.run();};
        //Spawns a new thread with the runnable object
        Thread UDPServerThread = new Thread(r);
        UDPServerThread.setDaemon(true);
        UDPServerThread.start();

        //Event filter for the entire program which listens for any keys that are pressed
        // And passes them to functions in the controller
        MainScene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            myController.JogKeyHandler(keyEvent);
        });


        primaryStage.setScene(MainScene);
        // Shows the scene
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint(""); //Hides the annoying exit key hint when fullscreen mode is activated
        //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Empty main method used for handling input arguments
        launch(args);
    }
}
