package sample;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeBox {

    //Create variable
    static int answer;

    protected static int display(boolean AdminMode) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Home");
        window.setMinWidth(1280);
        window.setMinHeight(600);

        //Removes Minimize and Maximize Buttons
        window.initStyle(StageStyle.UTILITY);

        //Disables the close button!!
        window.setOnCloseRequest(Event::consume);

        //Create two buttons
        Label Instructions1 =  new Label("Set Home to Current Position?");
        Label Instructions2 =  new Label("Warning overwrites old value!");

        Button SetHome = new Button("Set Home");
        Button GoHome = new Button("Go Home");
        Button Cancel = new Button("Cancel");



        SetHome.setMinSize(200,50);
        GoHome.setMinSize(200,50);
        Cancel.setMinSize(200,50);

        //Disables the set home button, unless admin mode is on
        if (!AdminMode){
            SetHome.setDisable(true);
        }

        //Clicking will set answer and close window
        Cancel.setOnAction(e -> {
            answer = 0;
            window.close();
        });

        GoHome.setOnAction(e -> {
            answer = 1;
            window.close();
        });

        SetHome.setOnAction(e -> {
            answer = 2;
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons to layout
        layout.getChildren().addAll(Instructions1,Instructions2,SetHome,GoHome, Cancel);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        //Sets and shows the window
        window.setScene(scene);
        window.setFullScreen(true);

        //Code for handling F6 key in manual mode
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F6) {
                // Debugg
                // System.out.println("key event detected" + keyEvent.toString());
                // If a button is selected fire it
                if (scene.focusOwnerProperty().get() instanceof Button) {
                    Button ActiveB = (Button) scene.focusOwnerProperty().get();
                    ActiveB.fire();
                }
            }
        });

        //VERY IMPORTANT TO ENFORCE CORRECT PROGRAM FLOW
        window.setFullScreenExitHint("");
        window.showAndWait();

        return answer;//Make sure to return answer
    }

}