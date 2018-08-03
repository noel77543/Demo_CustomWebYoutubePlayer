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

    //-----
    //call JavaScript 的method
    private final String SEEK_TO = "javascript:onSeekTo(%f)";
    private final String PAUSE = "javascript:onVideoPause()";
    private final String STOP = "javascript:onVideoStop()";
    private final String PLAY = "javascript:onVideoPlay()";
    private final String CUE = "javascript:cueVideo(%s)";
    private final String LOAD = "javascript:loadVideo(%s,%f)";


    //---------

    public CustomWebViewHandler() {
    }
    //---------

    private OnYouTubeEventHappenListener onYouTubeEventHappenListener;

    //---------

    public void setOnYouTubeEventHappenListener(OnYouTubeEventHappenListener onYouTubeEventHappenListener) {
        this.onYouTubeEventHappenListener = onYouTubeEventHappenListener;
    }

    //--------

    @JavascriptInterface
    public void onReady(String arg) {
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
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onPlaybackQualityChange(arg);
        }
    }

    @JavascriptInterface
    public void onPlaybackRateChange(String arg) {
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
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onApiChange(arg);
        }
    }

    @JavascriptInterface
    public void currentSeconds(String second) {
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onCurrentSecond(Double.parseDouble(second));
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
    private void notifyStateChange(@YoutubePlayerState int playState) {
        this.playState = playState;
        if (onYouTubeEventHappenListener != null) {
            onYouTubeEventHappenListener.onStateChange(playState);
        }
    }

    //-----------


    public void seekTo(CustomWebView customWebView, double second) {
//        Log.d(TAG, "seekToMillis : ");
        customWebView.loadUrl(String.format(SEEK_TO, second));
    }
    //-----------

    public void pause(CustomWebView customWebView) {
//        Log.d(TAG, "pause");
        customWebView.loadUrl(PAUSE);
    }
    //-----------

    public void stop(CustomWebView customWebView) {
//        Log.d(TAG, "stop");
        customWebView.loadUrl(STOP);
    }
    //-----------

    public @YoutubePlayerState
    int getPlayerState() {
//        Log.d(TAG, "getPlayerState");
        return playState;
    }
    //-----------

    public void play(final CustomWebView customWebView) {
//        Log.d(TAG, "play");
        customWebView.post(new Runnable() {
            @Override
            public void run() {
                customWebView.loadUrl(PLAY);
            }
        });
    }
    //-----------

    public void loadVideo(CustomWebView customWebView, String videoId, float second) {
        Log.d(TAG, "onLoadVideo : " + videoId + ", " + second);
        customWebView.loadUrl(String.format(LOAD, videoId, second));
    }
    //-----------

    public void cueVideo(CustomWebView customWebView, String videoId) {
        Log.d(TAG, "onCueVideo : " + videoId);
        customWebView.loadUrl(String.format(CUE, videoId));
    }
}
