package sample;

import java.util.Arrays;
import java.util.Observable;

public class ObservablePosition extends Observable {
    private double[] Position;
    private int ErrorState;

    //Constructor  method for the ObservablePosition
    public ObservablePosition(){
    }

    public double[] getPosition() {
            return Position;
    }

    public void setPosition(double[] positionInput){
        //Check if the value has changed
        if(Arrays.equals(Position,positionInput)){
            //System.out.println("Debug: Arrays have NOT Changed");
        } else {
            //System.out.println("Debug: Arrays HAVE Changed");
            //Updates the position to the new position
            Position = positionInput;
            //Notify observers and send out an update
            setChanged();
            notifyObservers();
        }
    }

}
