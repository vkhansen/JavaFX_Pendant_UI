package sample;

import static java.lang.Double.parseDouble;

public class NumberInputValid {

    // Used to restrict text fields to valid numbers only

    public static boolean Check(String Num) {
        if (!isNumeric(Num)) {
            return false;
        } else {
            try {
                parseDouble(Num);
            } catch (NumberFormatException NumEx) {
                return false;
            }
        }
        return true;
    }


        public static boolean isNumeric(String s){
            return s.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
        }


    //public static boolean isNumeric(String s){
    //    return s.matches("\\\\d{0,4}([\\\\.]\\\\d{0,4})?");
    //}

    }

