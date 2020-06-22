package sample;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpValidate {
    public static boolean validateIP(final String ip) {
        //Accepts String input, compares to a regex via regular expressions to confirm it is valid IPv4 Address
        //returns a boolean true if the address is valid
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
