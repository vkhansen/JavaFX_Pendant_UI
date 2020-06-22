package sample;

import javafx.application.Platform;

import java.util.Observable;
import java.util.Observer;


public class PositionObserver implements Observer {
    private ObservablePosition myPosition;
    private Controller obController;
    //Observer which is attached to the robot position object, when the observed values change
    //Updates are send to the main GUI controller

    public PositionObserver(Controller myController){
        this.obController = myController;
    }
    @Override
    public void update(Observable observable, Object arg){
        myPosition = (ObservablePosition) observable;
        //Debugg
        //System.out.println("Position Observer has been updated" + Arrays.toString(myPosition.getPosition()));
        //Sends the update to the application thread
        Platform.runLater(() -> {obController.updatePosition(myPosition.getPosition()); });
        //Garbage Collect
        System.gc();
    }
}
