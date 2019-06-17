package pro.novatech.solutions.app.cicole.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by p.lukengu on 4/5/2017.
 */

public class DateParser {

    public static String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
