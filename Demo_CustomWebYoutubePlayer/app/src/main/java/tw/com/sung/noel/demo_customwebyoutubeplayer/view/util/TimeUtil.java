package tw.com.sung.noel.demo_customwebyoutubeplayer.view.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private final int MILLI_SECOND = 1000;
    private SimpleDateFormat simpleDateFormat;
    public TimeUtil() {
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    //------

    /***
     * 秒數轉 分 : 秒
     * @return
     */
    public String convertSecondToMinute(int seconds) {
        return simpleDateFormat.format(seconds * MILLI_SECOND);
    }
}
