package tw.com.sung.noel.demo_customwebyoutubeplayer.view.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AssetsReader {

    private Context context;

    public AssetsReader(Context context) {
        this.context = context;
    }


    //------------

    /***
     *  cast to string
     * @return
     */
    public String getStringFromFile() {
        final String fileName = "youtube.html";
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open(fileName), "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            inputStreamReader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
