package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview;

import android.support.annotation.IntDef;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CustomWebViewHandler {

    private final String TAG = getClass().getSimpleName();
    public static final int UNSTARTED = 177;
    public static final int ENDED = 178;
    public static final int PLAYING = 179;
    public static final int PAUSED = 180;
    public static final int BUFFERING = 181;
    public static final int CUED = 182;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UNSTARTED, ENDED, PLAYING, PAUSED, BUFFERING, CUED})
    public @interface YoutubePlayerState {

    }

    private @YoutubePlayerState
    int playState = UNSTARTED;
    //---------

    public CustomWebViewHandler( ) { }
    //---------

    private OnYouTubeEventHappenListener onYouTubeEventHappenListener;

    //---------

    public void setOnYouTubeEventHappenListener(OnYouTubeEventHappenListener onYouTubeEventHappenListener) {
        this.onYouTubeEventHappenListener = onYouTubeEventHappenListener;
    }

    //--------

    @JavascriptInterface
    public void onReady(String arg) {
        Log.d(TAG, "onReady(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onReady();
        }
    }

    @JavascriptInterface
    public void onStateChange(String arg) {
        Log.d(TAG, "onStateChange(" + arg + ")");
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
        Log.d(TAG, "onPlaybackQualityChange(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onPlaybackQualityChange(arg);
        }
    }

    @JavascriptInterface
    public void onPlaybackRateChange(String arg) {
        Log.d(TAG, "onPlaybackRateChange(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onPlaybackRateChange(arg);
        }
    }

    @JavascriptInterface
    public void onError(String arg) {
        Log.e(TAG, "onError(" + arg + ")");
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onError(arg);
        }
    }

    @JavascriptInterface
    public void onApiChange(String arg) {
        Log.d(TAG, "onApiChange(" + arg + ")");
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
    private void notifyStateChange(@YoutubePlayerState int state) {
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onStateChange(state);
        }
        this.playState = state;
    }

    //-----------


    /**
     * APP TO WEB
     */
    public void seekToMillis(CustomWebView customWebView, double mil) {
        Log.d(TAG, "seekToMillis : ");
        customWebView.loadUrl("javascript:onSeekTo(" + mil + ")");
    }

    public void pause(CustomWebView customWebView) {
        Log.d(TAG, "pause");
        customWebView.loadUrl("javascript:onVideoPause()");
    }

    public void stop(CustomWebView customWebView) {
        Log.d(TAG, "stop");
        customWebView.loadUrl("javascript:onVideoStop()");
    }

    public @YoutubePlayerState
    int getPlayerState(CustomWebView customWebView) {
        Log.d(TAG, "getPlayerState");
        return playState;
    }

    public void play(final CustomWebView customWebView) {
        Log.d(TAG, "play");
        customWebView.post(new Runnable() {
            @Override
            public void run() {
                customWebView.loadUrl("javascript:onVideoPlay()");            }
        });
//        customWebView.loadUrl("javascript:onVideoPlay()");
    }

    public void onLoadVideo(CustomWebView customWebView, String videoId, float mil) {
        Log.d(TAG, "onLoadVideo : " + videoId + ", " + mil);
        customWebView.loadUrl("javascript:loadVideo('" + videoId + "', " + mil + ")");
    }

    public void onCueVideo(CustomWebView customWebView, String videoId) {
        Log.d(TAG, "onCueVideo : " + videoId);
        customWebView.loadUrl("javascript:cueVideo('" + videoId + "')");
    }
}
