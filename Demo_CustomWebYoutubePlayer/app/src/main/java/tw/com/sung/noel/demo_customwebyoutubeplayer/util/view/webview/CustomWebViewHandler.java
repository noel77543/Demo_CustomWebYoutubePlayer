package tw.com.sung.noel.demo_customwebyoutubeplayer.util.view.webview;

import android.support.annotation.IntDef;
import android.webkit.JavascriptInterface;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CustomWebViewHandler {

    public static final int UNSTARTED = 177;
    public static final int ENDED = 178;
    public static final int PLAYING = 179;
    public static final int PAUSED = 180;
    public static final int BUFFERING = 181;
    public static final int CUED = 182;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UNSTARTED,ENDED,PLAYING,PAUSED,BUFFERING,CUED})
    public @interface YoutubePlayerState {

    }

    private @YoutubePlayerState int playState = UNSTARTED;


    private OnYouTubeEventHappenListener onYouTubeEventHappenListener;

    public CustomWebViewHandler(OnYouTubeEventHappenListener onYouTubeEventHappenListener) {
        this.onYouTubeEventHappenListener = onYouTubeEventHappenListener;
    }
    //--------

    @JavascriptInterface
    public void onReady(String arg) {
//        Log.d(TAG, "onReady(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onReady();
        }
    }

    @JavascriptInterface
    public void onStateChange(String arg) {
//        Log.d(TAG, "onStateChange(" + arg + ")");
        if ("UNSTARTED".equalsIgnoreCase(arg)) {
            notifyStateChange(UNSTARTED);
        } else if ("ENDED".equalsIgnoreCase(arg)) {
            notifyStateChange(ENDED);
        } else if ("PLAYING".equalsIgnoreCase(arg)) {
            notifyStateChange(PLAYING);
        } else if ("PAUSED".equalsIgnoreCase(arg)) {
            notifyStateChange(PAUSED);
        } else if ("BUFFERING".equalsIgnoreCase(arg)) {
            notifyStateChange(BUFFERING);
        } else if ("CUED".equalsIgnoreCase(arg)) {
            notifyStateChange(CUED);
        }
    }

    @JavascriptInterface
    public void onPlaybackQualityChange(String arg) {
//        Log.d(TAG, "onPlaybackQualityChange(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onPlaybackQualityChange(arg);
        }
    }

    @JavascriptInterface
    public void onPlaybackRateChange(String arg) {
//        Log.d(TAG, "onPlaybackRateChange(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onPlaybackRateChange(arg);
        }
    }

    @JavascriptInterface
    public void onError(String arg) {
//        Log.e(TAG, "onError(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onError(arg);
        }
    }

    @JavascriptInterface
    public void onApiChange(String arg) {
//        Log.d(TAG, "onApiChange(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onApiChange(arg);
        }
    }

    @JavascriptInterface
    public void currentSeconds(String seconds) {
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onCurrentSecond(Double.parseDouble(seconds));
        }
    }

    @JavascriptInterface
    public void duration(String seconds) {
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onDuration(Double.parseDouble(seconds));
        }
    }

    @JavascriptInterface
    public void logs(String arg) {
//        Log.d(TAG, "logs(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.logs(arg);
        }
    }

    //-------
    public interface OnYouTubeEventHappenListener {
        void onReady();

        void onStateChange(@YoutubePlayerState int state);

        void onPlaybackQualityChange(String arg);

        void onPlaybackRateChange(String arg);

        void onError(String arg);

        void onApiChange(String arg);

        void onCurrentSecond(double second);

        void onDuration(double duration);

        void logs(String log);
    }

    //-------
    private void notifyStateChange( @YoutubePlayerState int state) {
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onStateChange(state);
        }
        this.playState = state;
    }
}
