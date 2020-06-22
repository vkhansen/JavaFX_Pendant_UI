package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;

//Simple javabean for the each Mcode,
//Holds a boolean property which can be accessed through the included methods

public class MCode implements Serializable {
    private BooleanProperty enabled = new SimpleBooleanProperty();
    //Empty constructor
    public MCode(){
    }
    //Full Constructor
    public MCode(boolean state){
        this.enabled.setValue(state);
    }
    //Set and get methods
    public boolean isEnabled() {
        return enabled.get();
    }
    public BooleanProperty enabledProperty() {
        return enabled;
    }
}