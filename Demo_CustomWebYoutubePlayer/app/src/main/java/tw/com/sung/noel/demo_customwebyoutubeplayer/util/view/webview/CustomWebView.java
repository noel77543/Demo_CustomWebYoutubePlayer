package tw.com.sung.noel.demo_customwebyoutubeplayer.util.view.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import tw.com.sung.noel.demo_customwebyoutubeplayer.util.AssetsReader;


public class CustomWebView extends WebView implements CustomWebViewHandler.OnYouTubeEventHappenListener, CustomWebChromeClient.onProgressChangeListener {

    private final String _DOMAIN = "https://www.youtube.com";
    private final String _TYPE = "text/html";
    private final String _ENCODING = "utf-8";

    private Context context;
    private WebSettings webSettings;
    private CustomWebViewHandler customWebViewHandler;
    private CustomWebViewClient customWebViewClient;
    private CustomWebChromeClient customWebChromeClient;
    private OnProgressChangeListener onProgressChangeListener;

    public CustomWebView(Context context) {
        super(context);
        this.context = context;
        init();

    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    //-----
    private void init() {
        customWebViewClient = new CustomWebViewClient(context);
        customWebChromeClient = new CustomWebChromeClient(context);
        customWebChromeClient.setOnProgressChangeListener(this);
        customWebViewHandler = new CustomWebViewHandler(this);

        setWebChromeClient(customWebChromeClient);
        setWebViewClient(customWebViewClient);
        webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        setFocusable(false);
    }

    //------

    /***
     * 帶入youtubeID進行載入
     */
    public void loadVideoByYoutubeId(String youtubeId, String backgroundColorStr, boolean isAutoPlay, boolean isAutoHide, boolean isGetRelativeVideo, boolean isShowInfo) {
        this.setLayerType(View.LAYER_TYPE_NONE, null);
        this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        this.addJavascriptInterface(customWebViewHandler, "NoelInterface");
        this.loadDataWithBaseURL(_DOMAIN, getYoutubeHTML(youtubeId, backgroundColorStr, isAutoPlay?1:0, isAutoHide?1:0, isGetRelativeVideo?1:0, isShowInfo?1:0), _TYPE, _ENCODING, null);
    }

    //----------

    /***
     * 建立HTML
     * @param youtubeId
     * @param backgroundColorStr  背景色碼
     * @param isAutoPlay   是否自動撥放
     * @param isAutoHide
     * @param isGetRelativeVideo  是否取得相關影片
     * @param isShowInfo 是否顯示影片資訊
     * @return
     */
    private String getYoutubeHTML(String youtubeId, String backgroundColorStr, int isAutoPlay, int isAutoHide, int isGetRelativeVideo, int isShowInfo) {

        String html = new AssetsReader(context).getStringFromFile()
                .replace("[VIDEO_ID]", youtubeId)
                .replace("[BG_COLOR]", backgroundColorStr);

        html = html
                .replace("[AUTO_PLAY]", String.valueOf(isAutoPlay))
                .replace("[AUTO_HIDE]", String.valueOf(isAutoHide))
                .replace("[REL]", String.valueOf(isGetRelativeVideo))
                .replace("[SHOW_INFO]", String.valueOf(isShowInfo))
                //是否允許JavaScript API
                .replace("[ENABLE_JS_API]", String.valueOf(0))
                //是否不允許keyboard
                .replace("[DISABLE_KB]", String.valueOf(1))
                //語系
                .replace("[CC_LANG_PREF]", "en")
                .replace("[CONTROLS]", String.valueOf(0))
                //是否全螢幕  1 / 0
                .replace("[FS]", String.valueOf(1));
        return html;
    }

    //---------

    /***
     * 網頁載入中
     * @param newProgress
     */
    @Override
    public void onProgressChanged(int newProgress) {
            if (newProgress > 95) {
                onProgressChangeListener.onFinished();
            } else {
                onProgressChangeListener.onProgressChanged(newProgress);
            }
    }


    //---------

    @Override
    public void onReady() {

    }

    @Override
    public void onStateChange(int state) {

    }

    //-------

    @Override
    public void onPlaybackQualityChange(String arg) {

    }

    //------

    @Override
    public void onPlaybackRateChange(String arg) {

    }
    //------

    @Override
    public void onError(String arg) {

    }
    //------

    @Override
    public void onApiChange(String arg) {

    }
    //------

    @Override
    public void onCurrentSecond(double second) {

    }
    //------

    @Override
    public void onDuration(double duration) {

    }
    //------

    @Override
    public void logs(String log) {

    }

    //------
    public interface OnProgressChangeListener {
        void onProgressChanged(int newProgress);

        void onFinished();
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
