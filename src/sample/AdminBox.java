package sample;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AdminBox {

    //Create variable
    static boolean answer;

    protected static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(1280);
        window.setMinHeight(600);
        Label label = new Label();
        label.setText(message);
        String RedBgStyle = "-fx-background-color: rgba(255, 0, 0, 1);";


        PasswordField myPasswordField = new PasswordField();

        myPasswordField.setMaxWidth(200);

        //Removes Minimize and Maximize Buttons
        window.initStyle(StageStyle.UTILITY);

        //Disables the close button!!
        window.setOnCloseRequest(Event::consume);

        //Create two buttons
        Button noButton = new Button("Cancel/Logout");
        Button yesButton = new Button("Login");

        noButton.setMinSize(200,50);
        yesButton.setMinSize(200,50);


        //Clicking will set answer and close window
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        yesButton.setOnAction(e -> {
            //Debug
            //System.out.println(passwordField.getCharacters().toString());
            //Super Secret Password set below
            answer = myPasswordField.getCharacters().toString().contentEquals("12345678");
            window.close();
        });



        VBox layout = new VBox(10);
        //Add buttons to layout
        layout.setStyle(RedBgStyle);
        layout.getChildren().addAll(label, myPasswordField, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);



        //Code for handling F6 key in manual mode
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F6) {
                //Debugg
                //System.out.println("key event detected" + keyEvent.toString());
                //If a button is selected fire it
                if (scene.focusOwnerProperty().get() instanceof Button) {
                    Button ActiveB = (Button) scene.focusOwnerProperty().get();
                    ActiveB.fire();
                }
                //If password box is selected move to confirmation box
                if (scene.focusOwnerProperty().get() instanceof PasswordField) {
                    yesButton.requestFocus();
                }
            }
        });


        //Sets and shows the window
        window.setScene(scene);
        window.setFullScreen(true);
        //VERY IMPORTANT TO ENFORCE CORRECT PROGRAM FLOW
        window.setFullScreenExitHint("");
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}