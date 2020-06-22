package sample;

public class CState {

    // This program builds the text commands based on parameters passed into the program
    // For jogging, special commands, adding points and Emergency stops


    public static String Jog(int Feed, int Axis, boolean isNeg, Double Increment) {
        String FeedString = Integer.toString(Feed);

        //Variable for sign
        if (isNeg) Increment = Increment *(-1);
        String Inc = Double.toString(Increment);

        //Uses String Array to select the active axis, includes optional MT0 axis
        String AxisS = "";
        String[] AxisArray = {"X", "Y", "Z","A", "B", "C", "MT0","MT1","MT2","MT3","MT4","MT5","MT6"};

        String CS1 = "#ON_USER#FEED#"; //Initial Static part of the command
        String CS2 = "#TO#0#J#"; //TO Static part of the command

        //Checks if axis number is valid
        if (-1 > Axis || Axis > 12) throw new Error("Invalid Axis, mode must be integer from 0-12");
        AxisS =  AxisArray[Axis];

        // returns the command state
        return CS1 + FeedString + CS2 + AxisS + "#" + Inc + "#";
    }

    // Does Special commands; 1-Begin, 2- To Zero, 3- Go Home at Feedrate specified
    public static String SpecialCommand(int Feed, int Mode) {

        String FeedString = Integer.toString(Feed);
        String ModeString = "";

        String CS1 = "#ON_USER#FEED#"; //Initial Static part of the command
        String CS2 = "#TO#"; //TO Static part of the command
        String CS3 = "#J"; //J Static part of the command

        //Input validation of the selected mode
        if (0 > Mode || Mode > 4) throw new Error("Invalid mode, mode must be integer from 1-3");
        ModeString = Integer.toString(Mode);
        return CS1 + FeedString + CS2 + ModeString + CS3;
    }

    //Stops the machine
    public static String Stop() {
        return "#ON_USER#FEED#000#TO#0#J#S#0#0";
    }

    //Adds point to teach the machine at feedrate specified
    public static String AddPoint(int Feed) {
        return "#ADDPOINT#"+ Feed + "#";
    }

    //Sets home position
    public static String SetHome() {
        return "#SETHOME#";
    }
}



