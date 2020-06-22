package sample;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class FileBox {

    //Create variables
    private static File CurrentSelectedFile;

    protected static File display(File selectedFile) {

        //Basic variables to setup new scene
        String title = "Lead Through Save File";
        String message = "Insert Path to Save file";
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(1280);
        window.setMinHeight(600);

        Label label = new Label();
        label.setText(message);
        CurrentSelectedFile = selectedFile;
        TextField FilePath = new TextField();
        FilePath.setText(selectedFile.getPath());
        FilePath.setMaxSize(400,50);

        FileChooser fileChooser = new FileChooser();

        window.initStyle(StageStyle.UTILITY);         //Removes Minimize and Maximize Buttons
        window.setOnCloseRequest(Event::consume);         //Disables the close button!!

        //Create  buttons
        Button noButton = new Button("Cancel");
        Button BrowseFile = new Button("Browse");
        Button Confirm = new Button("Confirm");

        //Set button size
        noButton.setMinSize(200,50);
        BrowseFile.setMinSize(200,50);
        Confirm.setMinSize(200,50);

        //Adds accepted extensions to the file chooser
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DBNC", "*.dbnc"),
                new FileChooser.ExtensionFilter("Textfile", "*.txt")
        );

        BrowseFile.setOnAction(e -> {
            CurrentSelectedFile = fileChooser.showSaveDialog(window);
            try {
                FilePath.setText(CurrentSelectedFile.getAbsolutePath());
            } catch (Exception Ex) {
                //Debugg for null pointer excpetion
                System.out.println(Ex.toString());
            }
        });

        //Clicking will set answer and close window
        noButton.setOnAction(e -> {
            CurrentSelectedFile = new File("");
            window.close();
        });

        //Clicking will set answer and close window
        Confirm.setOnAction(e -> {
            //Debugg
            //System.out.println(selectedFile.getParent());

            CurrentSelectedFile = new File(FilePath.getText());
            window.close();
        });


        VBox layout = new VBox(10);
        layout.setAlignment(Pos.TOP_CENTER);
        //Add buttons to layout
        layout.setPadding(new Insets(0,0,300,0));
        layout.getChildren().addAll(label,FilePath, BrowseFile,Confirm, noButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);



        //Sets and shows the window
        window.setScene(scene);
        window.setFullScreen(true);
        //VERY IMPORTANT TO ENFORCE CORRECT PROGRAM FLOW
        window.setFullScreenExitHint("");

        // Have to add event handling for F6 key directly to the scene

        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F6) {
                // Debugg
                // System.out.println("key event detected" + keyEvent.toString());
                //If a button is selected fire it
                if (scene.focusOwnerProperty().get() instanceof Button) {
                    Button ActiveB = (Button) scene.focusOwnerProperty().get();
                    ActiveB.fire();
                }
                //If enter box is selected move to confirmbox
                if (scene.focusOwnerProperty().get() instanceof TextField) {
                    Confirm.requestFocus();
                }
            }
        });


        window.showAndWait();
        //Make sure to return answer
        return CurrentSelectedFile;
    }

}