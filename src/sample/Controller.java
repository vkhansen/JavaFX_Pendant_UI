package sample;

import com.sun.glass.ui.Size;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.time.Instant;
import java.util.*;

import static java.lang.Double.parseDouble;


public class Controller {
    private Stage MainCStage;
    private File selectedFile;

    //private ObservableList<MCode> MCodeList;
    private MCodeListManager mlistMGR = new MCodeListManager();
    private ObservableList<MCode> observableMList;

    //Placeholder for active variable
    //private MCode mactive;

    private int feedrateint; //Store the last feedrate set with the slider
    private int MstateInt; // Stores the mode of the controller, 0 - Manual mode, 1 -  disabled mode, 2 - Run mode
    private double jogIncrementDouble; // Store the jog increment
    private double[] LastRobotState;
    private String feedratestring;
    private boolean AdminEnable; //Adminstrator mode flag
    private boolean TouchScreenEnable; //Enables or disables touchscreen
    private boolean AxisJogEnable; // Jog Access Screen is enabled
    private boolean JointJogEnable; // Motor Access Screen is enabled
    private final String RedBgStyle = "-fx-background-color: rgba(255, 0, 0, 1);";



    public Controller() {
        //Empty constructor
    }

    @FXML
    public void initialize() {
        // Initialize section of the controller
        // used to set variables and add listeners prior to starting program

        MstateInt = 0; // Pendant starts in manual mode
        MainCStage = Main.getInstance().MainStage;
        feedrateint = 0; //Start at default value
        jogIncrementDouble = 1.0; //Start at default value
        AxisJogEnable = false; //Jog starts disabled
        JointJogEnable = false; //Joint movement starts disabled

        selectedFile = new File("C:\\Default.txt");         //Set default save file path

        TouchScreenEnable = true;
        AdminEnable = false;

        //Defaults to hiding the to position menu
        AxisJogToPosition.setVisible(false);
        JointJogToPosition.setVisible(false);
        LeadThroughParentVBox.setVisible(false);


        //Updates the integer value stored in controller for use by other methods
        FeedRateSlider.valueProperty().addListener(e -> {
            Double temp = FeedRateSlider.getValue();
            feedrateint = temp.intValue();
        });

        //Updates text displayed next to the slider via binding
        FeedRateDisplay.textProperty().bind(
                Bindings.format(
                        "%.0f",
                        FeedRateSlider.valueProperty()
                )
        );


        // Enforce the text field to only accept an ip address
        TargetIP.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!IpValidate.validateIP(newValue)) {
                TargetIP.setText(oldValue);
            }
        });

        // Enforce the text field to only accept an ip address
        SourceIP.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!IpValidate.validateIP(newValue)) {
                SourceIP.setText(oldValue);
            }
        });

        // Enforce the text field to only accept a valid integer port range
        SourcePort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!PortValidate.validatePort(newValue)) {
                SourcePort.setText(oldValue);
            }
        });

        // Enforce the text field to only accept a valid integer port range
        TargetPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!PortValidate.validatePort(newValue)) {
                TargetPort.setText(oldValue);
            }
        });


        //Restricts the jog increment to valid values only
        JogIncrement.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!isNumeric(newValue)) {
                JogIncrement.setText(oldValue);
            } else {
                try {
                    jogIncrementDouble = parseDouble(JogIncrement.textProperty().getValue());
                } catch (NumberFormatException NumEx) {
                    //Debugg error handling
                    //System.out.println(NumEx.toString());
                    //System.out.println("ERROR: Illeagal value set!");
                    JogIncrement.setText(oldValue);
                }
                //Debugg
                //System.out.println("Jog Inc Double set:" + jogIncrementDouble);
            }
        }));

        // Enforce the axis field to only valid numeric values
        TPX.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPX.setText(oldValue);
            }
        }));

        TPY.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPY.setText(oldValue);
            }
        }));

        TPZ.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPZ.setText(oldValue);
            }
        }));

        TPA.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPA.setText(oldValue);
            }
        }));

        TPB.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPB.setText(oldValue);
            }
        }));

        TPC.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPC.setText(oldValue);
            }
        }));

        XAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPX.setText(newValue);
            }
        }));

        YAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPY.setText(newValue);
            }
        }));

        ZAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPZ.setText(newValue);
            }
        }));

        AAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPA.setText(newValue);
            }
        }));

        BAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPB.setText(newValue);
            }
        }));

        CAxis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPC.setText(newValue);
            }
        }));

        //CODE FOR LINEAR AXIS (DISABLED)
        //MT0Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
        //    if (!newValue.equals(oldValue)) {
        //        TPMT0.setText(newValue);
        //    }
        //}));

        MT1Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT1.setText(newValue);
            }
        }));

        MT2Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT2.setText(newValue);
            }
        }));

        MT2Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT2.setText(newValue);
            }
        }));

        MT3Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT3.setText(newValue);
            }
        }));

        MT3Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT3.setText(newValue);
            }
        }));

        MT4Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT4.setText(newValue);
            }
        }));

        MT5Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT5.setText(newValue);
            }
        }));

        MT6Axis.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                TPMT6.setText(newValue);
            }
        }));

        //Unused linear axis
        //TPMT0.textProperty().addListener(((observableValue, oldValue, newValue) -> {
        //    if (!NumberInputValid.Check(newValue)) {
        //        TPMT0.setText(oldValue);
        //    }
        //}));

        TPMT1.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT1.setText(oldValue);
            }
        }));

        TPMT2.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT2.setText(oldValue);
            }
        }));

        TPMT3.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT3.setText(oldValue);
            }
        }));

        TPMT4.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT4.setText(oldValue);
            }
        }));

        TPMT5.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT5.setText(oldValue);
            }
        }));

        TPMT6.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (!NumberInputValid.Check(newValue)) {
                TPMT6.setText(oldValue);
            }
        }));

        //Set Numeric Text Fields to use Numeric keyboard by default
        //Zero Axis is disabled
        //TPMT0.getProperties().put("vkType", "numeric");
        TPMT1.getProperties().put("vkType", "numeric");
        TPMT2.getProperties().put("vkType", "numeric");
        TPMT3.getProperties().put("vkType", "numeric");
        TPMT4.getProperties().put("vkType", "numeric");
        TPMT5.getProperties().put("vkType", "numeric");
        TPMT6.getProperties().put("vkType", "numeric");
        TPX.getProperties().put("vkType", "numeric");
        TPY.getProperties().put("vkType", "numeric");
        TPZ.getProperties().put("vkType", "numeric");
        TPA.getProperties().put("vkType", "numeric");
        TPB.getProperties().put("vkType", "numeric");
        TPC.getProperties().put("vkType", "numeric");
        JogIncrement.getProperties().put("vkType", "numeric");
        SourcePort.getProperties().put("vkType", "numeric");
        SourceIP.getProperties().put("vkType", "numeric");
        TargetIP.getProperties().put("vkType", "numeric");
        TargetPort.getProperties().put("vkType", "numeric");
    }

    @FXML
    private ScrollPane MCodeSCrollPane;
    @FXML
    private StackPane LeftStack, RightStack, TopSPane2;
    @FXML
    private VBox LeftMainVbox, RightMainVbox, JogTopVbox, MainTopVbox,JogHboxParent,LeadThroughParentVBox;
    @FXML
    private BorderPane MainPane;
    @FXML
    private Button BStartCircle, BCircleP1, BCircleP2, BCircleP3, BCircleP4, BEndCircle, BLeadThrough,ExitLeadThrough, AdminModeButton;
    @FXML
    private Button BStartCurve, BCurveP1, BCurveP2, BCurveP3, BEndCurve;
    @FXML
    private Label CP1Label,CP2Label,CP3Label,CP4Label,CP1LabelM,CP2LabelM,CP3LabelM,CP4LabelM;
    @FXML
    private Label CvP1Label,CvP2Label,CvP3Label,CvP1LabelM,CvP2LabelM,CvP3LabelM;
    @FXML
    private Label LineP1Label, LineP2Label, LineP3Label, LineP1LabelM, LineP2LabelM, LineP3LabelM;
    @FXML
    private Label RapidP1Label, RapidP1LabelM, RapidP2Label, RapidP2LabelM, RapidP3Label, RapidP3LabelM, RapidP4Label, RapidP4LabelM;
    @FXML
    private Button BStartLine, BLineP1, BLineP2, BLineP3, BEndLine;
    @FXML
    private Button BStartRapid, BRapidP1, BRapidP2, BRapidP3, BRapidP4, BEndRapid;
    @FXML
    private Tab LTCircleTab, LTCurveTab, LTLineTab, LTRapidTab;
    @FXML
    private ToggleButton JogToggleButton, JointToggleButton;
    @FXML
    private FlowPane AxisJogToPosition, JointJogToPosition;
    @FXML
    private TextField TargetIP, TargetPort, SourceIP, SourcePort, JogIncrement;

    //TextFields to enter values in the to postion menu (Axis)
    @FXML
    private TextField TPX, TPY, TPZ, TPA, TPB, TPC;

    //TextFields to enter values in the to postion menu (Motors)
    @FXML
    private TextField TPMT1, TPMT2, TPMT3, TPMT4, TPMT5, TPMT6;
    @FXML
    private Slider FeedRateSlider;
    @FXML
    private Label FeedRateDisplay;
    @FXML
    private Label XAxis, YAxis, ZAxis, AAxis,BAxis,CAxis;
    @FXML
    private Label MT1Axis, MT2Axis, MT3Axis, MT4Axis, MT5Axis, MT6Axis;
    @FXML
    private Label Error_State, Mode_State, CommandLabelLog,AdminLabel, FilePathLabelDisplay ;

    @FXML
    public void McodeEdit(ActionEvent AE) {
        //Modifies the Arraylist storing all the mcodes when the checkboxes are changed
        int index;
        String indexString;
        indexString = AE.getSource().toString().substring(13, 15);
        //Debugg
        //System.out.println(AE.getSource().toString());

        try {
            index = Integer.parseInt(indexString); // Casting, not the best way but works
            if ( AE.getSource() instanceof CheckBox) {
                CheckBox SelectedBox = (CheckBox) AE.getSource();
                if (SelectedBox.isSelected()) {
                    mlistMGR.get(index).enabledProperty().setValue(true);
                } else {
                    mlistMGR.get(index).enabledProperty().setValue(false);
                }
            }
        } catch (Exception Ex) {
            //Debugg
            System.out.println(Ex.toString());
            System.out.println(Ex.getMessage());
            System.out.println(Ex.getLocalizedMessage());
        }
    }


    //Commands for circle mode in Leadthrough
    @FXML
    public void StartCircle(){
        //Instructs the controller to start circle teach mode
        sendUDP("#CIRCLESTART#");
        //Disables itself and adds the next button
        BStartCircle.disableProperty().setValue(true);
        BCircleP1.disableProperty().setValue(false);
        ExitLeadThrough.disableProperty().setValue(true);

        //Disables other tabs while in leadthrough;
        LTCurveTab.disableProperty().setValue(true);
        LTLineTab.disableProperty().setValue(true);
        LTRapidTab.disableProperty().setValue(true);

        // Clears all labels
        CP1Label.setText("");
        CP1LabelM.setText("");
        CP2Label.setText("");
        CP2LabelM.setText("");
        CP3Label.setText("");
        CP3LabelM.setText("");
        CP4Label.setText("");
        CP4LabelM.setText("");
    }

    @FXML
    public void AddCircleP1(){
        //Disables itself
        BCircleP1.disableProperty().setValue(true);
        BCircleP2.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        CP1Label.setText(RobotAxisPostion());
        CP1LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddCircleP2(){
        //Disables itself
        BCircleP2.disableProperty().setValue(true);
        BCircleP3.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        CP2Label.setText(RobotAxisPostion());
        CP2LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddCircleP3(){
        //Disables itself
        BCircleP3.disableProperty().setValue(true);
        BCircleP4.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        CP3Label.setText(RobotAxisPostion());
        CP3LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddCircleP4(){
        //Disables itself
        BCircleP4.disableProperty().setValue(true);
        BEndCircle.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        CP4Label.setText(RobotAxisPostion());
        CP4LabelM.setText(mlistMGR.toString());
    }


    @FXML
    public void EndCircle(){
        //Instructs the controller to end circle teach mode
        sendUDP("#CIRCLESTOP#");
        //Disables itself and adds the next button
        BStartCircle.disableProperty().setValue(false);
        BEndCircle.disableProperty().setValue(true);
        ExitLeadThrough.disableProperty().setValue(false);

        //Enables other tabs while leaving leadthrough;
        LTCurveTab.disableProperty().setValue(false);
        LTLineTab.disableProperty().setValue(false);
        LTRapidTab.disableProperty().setValue(false);
    }


    //All buttons for Curve leadthrough mode
    @FXML
    public void StartCurve(){
        //Instructs the controller to start curve teach mode
        sendUDP("#CURVESTART#");
        //Disables itself and adds the next button
        BStartCurve.disableProperty().setValue(true);
        BCurveP1.disableProperty().setValue(false);

        ExitLeadThrough.disableProperty().setValue(true);
        //Disables other tabs while in leadthrough;
        LTCircleTab.disableProperty().setValue(true);
        LTLineTab.disableProperty().setValue(true);
        LTRapidTab.disableProperty().setValue(true);

        // Clears all labels
        CvP1Label.setText("");
        CvP1LabelM.setText("");
        CvP2Label.setText("");
        CvP2LabelM.setText("");
        CvP3Label.setText("");
        CvP3LabelM.setText("");
    }

    @FXML
    public void AddCurveP1(){
        //Disables itself, enables next button
        BCurveP1.disableProperty().setValue(true);
        BCurveP2.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        CvP1Label.setText(RobotAxisPostion());
        CvP1LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddCurveP2(){
        //Disables itself, enables next button
        BCurveP2.disableProperty().setValue(true);
        BCurveP3.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        CvP2Label.setText(RobotAxisPostion());
        CvP2LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddCurveP3(){
        //Disables itself, enables next button
        BCurveP3.disableProperty().setValue(true);
        BEndCurve.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        CvP3Label.setText(RobotAxisPostion());
        CvP3LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void EndCurve(){
        //Instructs the controller to end circle teach mode
        sendUDP("#CURVESTOP#");
        //Disables itself and adds the next button
        BStartCurve.disableProperty().setValue(false);
        BEndCurve.disableProperty().setValue(true);
        ExitLeadThrough.disableProperty().setValue(false);

        //Enables other tabs while leaving leadthrough;
        LTCircleTab.disableProperty().setValue(false);
        LTLineTab.disableProperty().setValue(false);
        LTRapidTab.disableProperty().setValue(false);
    }

    public void StartLine(){
        //Instructs the controller to start curve teach mode
        sendUDP("#LINESTART#");
        //Disables itself and adds the next button
        BStartLine.disableProperty().setValue(true);
        BLineP1.disableProperty().setValue(false);

        ExitLeadThrough.disableProperty().setValue(true);
        //Disables other tabs while in leadthrough;
        LTCircleTab.disableProperty().setValue(true);
        LTCurveTab.disableProperty().setValue(true);
        LTRapidTab.disableProperty().setValue(true);

        // Clears all labels
        LineP1Label.setText("");
        LineP1LabelM.setText("");
        LineP2Label.setText("");
        LineP2LabelM.setText("");
        LineP3Label.setText("");
        LineP3LabelM.setText("");
    }

    @FXML
    public void AddLineP1(){
        //Disables itself, enables next button
        BLineP1.disableProperty().setValue(true);
        BLineP2.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        LineP1Label.setText(RobotAxisPostion());
        LineP1LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddLineP2(){
        //Disables itself, enables next button
        BLineP2.disableProperty().setValue(true);
        BLineP3.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        LineP2Label.setText(RobotAxisPostion());
        LineP2LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddLineP3(){
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        LineP3Label.setText(RobotAxisPostion());
        LineP3LabelM.setText(mlistMGR.toString());
        BEndLine.disableProperty().setValue(false);
    }

    @FXML
    public void EndLine(){
        //Disables last button (P3)
        BLineP3.disableProperty().setValue(true);
        //Instructs the controller to end line teach mode
        sendUDP("#LINESTOP#");
        //Disables itself and adds the next button
        BStartLine.disableProperty().setValue(false);
        BEndLine.disableProperty().setValue(true);
        ExitLeadThrough.disableProperty().setValue(false);

        //Enables other tabs while leaving leadthrough;
        LTCircleTab.disableProperty().setValue(false);
        LTCurveTab.disableProperty().setValue(false);
        LTRapidTab.disableProperty().setValue(false);
    }

    public void StartRapid(){
        //Instructs the controller to start curve teach mode
        sendUDP("#RAPIDSTART#");
        //Disables itself and adds the next button
        BStartRapid.disableProperty().setValue(true);
        BRapidP1.disableProperty().setValue(false);

        ExitLeadThrough.disableProperty().setValue(true);
        //Disables other tabs while in leadthrough;
        LTCircleTab.disableProperty().setValue(true);
        LTCurveTab.disableProperty().setValue(true);
        LTLineTab.disableProperty().setValue(true);

        // Clears all labels
        RapidP1Label.setText("");
        RapidP1LabelM.setText("");
        RapidP2Label.setText("");
        RapidP2LabelM.setText("");
        RapidP3Label.setText("");
        RapidP3LabelM.setText("");
    }

    @FXML
    public void AddRapidP1(){
        //Disables itself, enables next button
        BRapidP1.disableProperty().setValue(true);
        BRapidP2.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        RapidP1Label.setText(RobotAxisPostion());
        RapidP1LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddRapidP2(){
        //Disables itself, enables next button
        BRapidP2.disableProperty().setValue(true);
        BRapidP3.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        RapidP2Label.setText(RobotAxisPostion());
        RapidP2LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddRapidP3(){
        //Disables itself, enables next button
        BRapidP3.disableProperty().setValue(true);
        BRapidP4.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        RapidP3Label.setText(RobotAxisPostion());
        RapidP3LabelM.setText(mlistMGR.toString());
    }

    @FXML
    public void AddRapidP4(){
        //Disables itself, enables next button
        BRapidP4.disableProperty().setValue(true);
        BEndRapid.disableProperty().setValue(false);
        //Sends network command
        sendUDP(CState.AddPoint(feedrateint) + mlistMGR.toString());
        //updates labels
        RapidP4Label.setText(RobotAxisPostion());
        RapidP4LabelM.setText(mlistMGR.toString());
    }
    @FXML
    public void EndRapid(){
        //Instructs the controller to end circle teach mode
        sendUDP("#RAPIDSTOP#");
        //Disables itself and adds the next button
        BStartRapid.disableProperty().setValue(false);
        BEndRapid.disableProperty().setValue(true);
        ExitLeadThrough.disableProperty().setValue(false);
        //Enables other tabs while leaving leadthrough;
        LTCircleTab.disableProperty().setValue(false);
        LTCurveTab.disableProperty().setValue(false);
        LTLineTab.disableProperty().setValue(false);
    }



    @FXML
    public void PrintMcodeString(){
        System.out.println(mlistMGR.toString());
    }


    @FXML
    public void LeadThrough() {
        selectedFile = FileBox.display(selectedFile);
        LeadThroughParentVBox.setVisible(true);
        try {
            FilePathLabelDisplay.setText(selectedFile.getPath());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        sendUDP("#ADDFILEPATH#" + selectedFile.getPath() + "#");
        BLeadThrough.disableProperty().setValue(true);         //Disable the leadthrough button
    }

    @FXML
    public void ExitLeadThrough() {
        //Hide the leadthrough menu and set button to enabled again
        LeadThroughParentVBox.setVisible(false);
        BLeadThrough.disableProperty().setValue(false);
        sendUDP("#ENDLEADTHROUGH#");
    }

    @FXML
    public void AdminDialog() {
        AdminEnable = AdminBox.display("ADMIN MODE","SWITCH TO ADMIN MODE?");
        if(AdminEnable){
            AdminLabel.setText("ENABLED");
            MainPane.setStyle(RedBgStyle);             //Set Red color to warn about admin mode
        } else {
            MainPane.setStyle(null);             //Reset color back to normal
            AdminLabel.setText("DISABLED");
        }
    }


    @FXML
    public void JogAxisMode() {
        if (JogToggleButton.isSelected()) {
            AxisJogToPosition.setVisible(true);
            JointJogToPosition.setVisible(false);
            AxisJogEnable = true;
            JointJogEnable = false;
        } else {
            AxisJogEnable = false;
            AxisJogToPosition.setVisible(false);
        }
    }


    @FXML
    public void JointAxisMode() {
        if (JointToggleButton.isSelected()) {
            JointJogEnable = true;
            AxisJogEnable = false;
            AxisJogToPosition.setVisible(false);
            JointJogToPosition.setVisible(true);
        } else {
            JointJogEnable = false;
            JointJogToPosition.setVisible(false);
        }
    }


    @FXML
    public void JointToPosition(){
        // NOTE Zero value for MT0 when it is not in use
        String Command = "JOINTTOPOSITION#" + feedrateint + "#0#" + TPMT1.getCharacters() + "#" + TPMT2.getCharacters() + "#" + TPMT3.getCharacters() + "#" + TPMT4.getCharacters() + "#" + TPMT5.getCharacters()+ "#" + TPMT6.getCharacters();
        sendUDP(Command);
    }

    @FXML
    public void JogToPosition(){
        String Command = "JOGTOPOSITION#" + feedrateint + "#" + TPX.getCharacters() + "#" + TPY.getCharacters() + "#" + TPZ.getCharacters() + "#" + TPA.getCharacters() + "#" + TPB.getCharacters() + "#" + TPC.getCharacters();
        sendUDP(Command);
    }

    @FXML
    public void Estop() {
        sendUDP(CState.Stop());
    }

    @FXML
    public void toBegin() {
        Boolean toBeginChoice = ConfirmBox.display("To Begin", "Go to Begin Point?");
        if (toBeginChoice){
            sendUDP(CState.SpecialCommand(feedrateint, 1));
        }
    }

    @FXML
    public void toZero() {
        Boolean toZeroChoice = ConfirmBox.display("To Zero", "Go to Zero Point?");
        if (toZeroChoice){
            sendUDP(CState.SpecialCommand(feedrateint, 2));
            }
    }

    @FXML
    public void DisplayHome() {
        //Dummy holding variable for choice box selection
        int HomeChoice = 0;
        HomeChoice = HomeBox.display(AdminEnable);
        switch (HomeChoice) {
            case 1:
                System.out.println("Go home selected");
                sendUDP(CState.SpecialCommand(feedrateint, 3));
                break;
            case 2:
                System.out.println("Set home selected");
                sendUDP(CState.SetHome());
                break;
        }
    }

    public void updatePosition(double[] Robot_State) {
        LastRobotState = Arrays.copyOfRange(Robot_State, 0,13);
        // Sets all the labels along bottom of screen
        XAxis.setText(String.valueOf(Robot_State[0]));
        YAxis.setText(String.valueOf(Robot_State[1]));
        ZAxis.setText(String.valueOf(Robot_State[2]));
        AAxis.setText(String.valueOf(Robot_State[3]));
        BAxis.setText(String.valueOf(Robot_State[4]));
        CAxis.setText(String.valueOf(Robot_State[5]));
        // Sets all the labels along bottom of screen
        MT1Axis.setText(String.valueOf(Robot_State[7]));
        MT2Axis.setText(String.valueOf(Robot_State[8]));
        MT3Axis.setText(String.valueOf(Robot_State[9]));
        MT4Axis.setText(String.valueOf(Robot_State[10]));
        MT5Axis.setText(String.valueOf(Robot_State[11]));
        MT6Axis.setText(String.valueOf(Robot_State[12]));
        Double Estate = Robot_State[13];
        int EstateInt = Estate.intValue();
        Error_State.setText(String.valueOf(EstateInt));
        Double Mstate = Robot_State[14];
        MstateInt = Mstate.intValue();
        Mode_State.setText(String.valueOf(MstateInt));
    }



    @FXML
    public void Exit_Program() {
        Platform.exit();
        System.exit(0);
    }


    //General method to send message whatever server is targeted by the UI
    public void sendUDP(String Message) {
        UDPClient Sender = new UDPClient(SourceIP.getCharacters().toString(), TargetIP.getCharacters().toString(), Integer.parseInt(SourcePort.getCharacters().toString()), Integer.parseInt(TargetPort.getCharacters().toString()));
        Sender.UDPSend(Message);
        Sender = null;
        System.gc();
        CommandLabelLog.setText("Last Command @:" + Instant.now() + " : " + Message);
    }


    //Jog programs, which are called by hotkeys from another function
    @FXML
    public void jogXPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 0, false, jogIncrementDouble));
    }

    @FXML
    public void jogYPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 1, false, jogIncrementDouble));
    }

    @FXML
    public void jogZPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 2, false, jogIncrementDouble));
    }

    @FXML
    public void jogAPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 3, false, jogIncrementDouble));
    }

    @FXML
    public void jogBPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 4, false, jogIncrementDouble));
    }

    @FXML
    public void jogCPos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 5, false, jogIncrementDouble));
    }

    @FXML
    public void jogXNeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 0, true, jogIncrementDouble));
    }


    @FXML
    public void jogYNeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 1, true, jogIncrementDouble));
    }

    @FXML
    public void jogZNeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 2, true, jogIncrementDouble));
    }

    @FXML
    public void jogANeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 3, true, jogIncrementDouble));
    }

    @FXML
    public void jogBNeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 4, true, jogIncrementDouble));
    }

    @FXML
    public void jogCNeg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 5, true, jogIncrementDouble));
    }

    //Not in use  for linear Axis MT0
    @FXML
    public void jogMT0Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 6, false, jogIncrementDouble));
    }

    //Not in use for linear Axis MT0
    @FXML
    public void jogMT0Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 6, true, jogIncrementDouble));
    }

    @FXML
    public void jogMT1Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 7, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT1Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 7, true, jogIncrementDouble));
    }


    @FXML
    public void jogMT2Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 8, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT2Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 8, true, jogIncrementDouble));
    }

    @FXML
    public void jogMT3Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 9, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT3Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 9, true, jogIncrementDouble));
    }

    @FXML
    public void jogMT4Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 10, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT4Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 10, true, jogIncrementDouble));
    }


    @FXML
    public void jogMT5Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 11, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT5Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 11, true, jogIncrementDouble));
    }

    @FXML
    public void jogMT6Pos() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 12, false, jogIncrementDouble));
    }

    @FXML
    public void jogMT6Neg() {
        //public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment)
        sendUDP(CState.Jog(feedrateint, 12, true, jogIncrementDouble));
    }



    public static boolean isNumeric(String s) {
        return s.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
    }

    //Builds a string with the robots six axis postion
    public String RobotAxisPostion() {
        String Postion = "X: " + XAxis.getText() + " Y: " + YAxis.getText() + " Z: " + ZAxis.getText() + " A: " + AAxis.getText() + " B: " + BAxis.getText() + " C: " + CAxis.getText();
        return Postion;
    }

    //Builds a string with the robots six axis postion
    // MT0 Axis would need to be added to this function for from versions of this program
    // Using the this line below
    //MT0: " + MT0Axis.getText()
    public String RobotMotorPostion() {
        String Postion = " MT1: " + MT1Axis.getText() + " MT2: " + MT2Axis.getText() + " MT3: " + MT3Axis.getText() + " MT4: " + MT4Axis.getText() + " MT5: " + MT5Axis.getText() + " MT6: " + MT6Axis.getText();
        return Postion;
    }

    @FXML
    public void JogKeyHandler(KeyEvent event) {
        //System.out.println("Event Detected Key: " + event.getCode().getName() + " Name: " + event.getEventType().getName());
        //Debugg: Get Key Press for Switch Statement
        //System.out.println(event.getSource().toString());

        String KeyPressed = event.getCode().getName();
        //System.out.println(KeyPressed);

        //Check for special buttons on the pendant F5-F7
        //Context of these buttons changes depending on the mode

        switch (MstateInt) { //Checks if the pendant is in manual mode
            case 0: //Manual mode
                if (event.getCode() == KeyCode.F6) {
                    System.out.println("F5 Detected in manual mode" + event.getCode().getName());

                    //Since jog and jog joint are toggle buttons...
                    if (MainPane.getScene().focusOwnerProperty().get() instanceof ToggleButton){
                        ToggleButton TActiveB =(ToggleButton) MainPane.getScene().focusOwnerProperty().get();
                        TActiveB.fire();
                        //Debugg
                        //System.out.println("Selected Button to fire" + TActiveB.toString());
                    }

                    //If a button is in focus fires the selected button
                    if (MainPane.getScene().focusOwnerProperty().get() instanceof Button){
                        Button ActiveB =(Button) MainPane.getScene().focusOwnerProperty().get();
                        ActiveB.fire();
                        //Debugg
                        //System.out.println("Selected Button to fire" + ActiveB.toString());
                    }

                    //Debugg
                    //System.out.println("Key Event" + ke.toString());
                    //System.out.println("Focus owner" + MainPane.getScene().focusOwnerProperty().get().toString());
                }
                if (event.getCode() == KeyCode.F7) {
                    System.out.println("F6 Detected in manual mode" + event.getCode().getName());
                    JogToggleButton.fire(); //Enter the jog mode
                }
                if (event.getCode() == KeyCode.F8) {
                    System.out.println("F7 Detected in manual mode" + event.getCode().getName());
                    JointToggleButton.fire(); //Enter the joint jog mode
                }
                break;
            case 1:
                break; // do nothing because pendant is locked
            case 2: // Run mode
                if (event.getCode() == KeyCode.F6) {
                    System.out.println("F5 Detected in Run mode" + event.getCode().getName());
                    //Code to run the program
                    sendUDP("#RUNPROGRAM#");
                }
                if (event.getCode() == KeyCode.F7) {
                    System.out.println("F6 Detected in Run mode" + event.getCode().getName());
                    //Code to Pause the program
                    sendUDP("#PAUSEPROGRAM#");
                }
                if (event.getCode() == KeyCode.F8) {
                    System.out.println("F7 Detected in Run mode" + event.getCode().getName());
                    //Code to stop the program??
                    sendUDP("#STOPPROGRAM#");
                }
                break;
        }


            //For Axis Jog mode only when enabled and in manual mode (0)
        if (AxisJogEnable && MstateInt == 0) {
            switch (KeyPressed) {
                case "A":
                    jogXPos();
                    break;
                case "B":
                    jogXNeg();
                    break;
                case "C":
                    jogYPos();
                    break;
                case "D":
                    jogYNeg();
                    break;
                case "E":
                    jogZPos();
                    break;
                case "F":
                    jogZNeg();
                    break;
                case "G":
                    jogAPos();
                    break;
                case "H":
                    jogANeg();
                    break;
                case "I":
                    jogBPos();
                    break;
                case "J":
                    jogBNeg();
                    break;
                case "K":
                    jogCPos();
                    break;
                case "L":
                    jogCNeg();
                    break;
            }
        }

            //For Joint Jog Mode only when enabled and in manual mode (0)
            if (JointJogEnable && MstateInt == 0) {
                switch (KeyPressed) {
                    case "A":
                        jogMT1Pos();
                        break;
                    case "B":
                        jogMT1Neg();
                        break;
                    case "C":
                        jogMT2Pos();
                        break;
                    case "D":
                        jogMT2Neg();
                        break;
                    case "E":
                        jogMT3Pos();
                        break;
                    case "F":
                        jogMT3Neg();
                        break;
                    case "G":
                        jogMT4Pos();
                        break;
                    case "H":
                        jogMT4Neg();
                        break;
                    case "I":
                        jogMT5Pos();
                        break;
                    case "J":
                        jogMT5Neg();
                        break;
                    case "K":
                        jogMT6Pos();
                        break;
                    case "L":
                        jogMT6Neg();
                        break;
                }
            }
    }
}
