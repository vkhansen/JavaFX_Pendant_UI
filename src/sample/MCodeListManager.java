package sample;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.ListIterator;

public class MCodeListManager extends ArrayList<MCode> {
    //Extends the ArrayList class
    //Extends how the Mcode list is created, so it starts with 99 Mcode objects at index 1-99
    //Adds a toString method with concatenates all the mcode in the list into a formatted string separated by
    //# signs for example M01#M02#M05#M99# etc.

    public MCodeListManager(){
        //mlist = new ArrayList<MCode>(99);
        ListIterator iter = this.listIterator();
        for (int i=1; i < 100 ; i++ ){
            iter.add(new MCode(false));
        }
    }

    public String toString(){
        String McodesString = "";
        for (int i=1; i < 99 ; i++ ){
            boolean active = (this.get(i).isEnabled());
            if(active){
                McodesString = McodesString + "M" + i + "#";
            }
        }
        return McodesString;
    }

}
