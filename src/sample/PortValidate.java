package sample;

public class PortValidate {
    public static boolean validatePort(final String Port) {

        //Loop through string
        for (char c : Port.toCharArray())
        {
            //Debug
            //System.out.println(c);
            if (!Character.isDigit(c)) return false;
        }
       try {
           int port = Integer.parseInt(Port);
           //Debugg
           //System.out.println("Try port:" + port);
           if (port < 0 || port > 65535){
               return false;
           }
       }   catch (NumberFormatException Numex) {
           //Exception Handle
           System.out.println(Numex.toString());
           return false;
       }

       return true;

    }
}