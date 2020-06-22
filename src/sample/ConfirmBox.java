package sample;

import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ConfirmBox {

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

        //Removes Minimize and Maximize Buttons
        window.initStyle(StageStyle.UTILITY);

        //Disables the close button!!
        window.setOnCloseRequest(Event::consume);

        //Create two buttons
        Button noButton = new Button("No");
        Button yesButton = new Button("Yes");

        noButton.setMinSize(100,50);
        yesButton.setMinSize(100,50);

        //Clicking will set answer and close window

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });


        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });



        VBox layout = new VBox(10);

        //Add buttons to layout
        layout.getChildren().addAll(label, yesButton, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);

        //Sets and shows the window
        window.setScene(scene);


        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F6) {
                // Debugg
                // System.out.println("key event detected" + keyEvent.toString());
                //If a button is selected fire it
                if (scene.focusOwnerProperty().get() instanceof Button) {
                    Button ActiveB = (Button) scene.focusOwnerProperty().get();
                    ActiveB.fire();
                }
            }
        });



        //VERY IMPORTANT TO ENFORCE CORRECT PROGRAM FLOW
        window.setFullScreenExitHint("");
        window.setFullScreen(true);
        window.showAndWait();


        //Make sure to return answer
        return answer;
    }

}