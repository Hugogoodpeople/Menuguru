package pt.menuguru.menuguru6.Utils;

/**
 * Created by hugocosta on 12/08/14.
 */
import android.location.Location;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


    public static String getDistance(Location loc1, Location loc2)
    {
        float distance =  loc1.distanceTo(loc2);
        if (loc1.getLatitude()!=0 && loc2.getLatitude() !=0) {


            if (distance > 1000 * 100)
                return String.format("%.0f Km", distance / 1000);
            if (distance > 1000 * 10)
                return String.format("%.1f Km", distance / 1000);

            if (distance > 1000)
                return String.format("%.1f Km", distance / 1000);


            return String.format("%.0f m", distance);

        }
        return "ND";
    }

}