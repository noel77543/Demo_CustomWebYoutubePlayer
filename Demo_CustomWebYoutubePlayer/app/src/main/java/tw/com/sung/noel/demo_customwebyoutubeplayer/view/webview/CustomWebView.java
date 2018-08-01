package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import tw.com.sung.noel.demo_customwebyoutubeplayer.view.util.AssetsReader;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.client.CustomWebChromeClient;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.client.CustomWebViewClient;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.model.ParamsModel;


public class CustomWebView extends WebView implements CustomWebChromeClient.onProgressChangeListener, CustomWebViewHandler.OnYouTubeEventHappenListener {

    private final String _DOMAIN = "https://www.youtube.com/";
    private final String _TYPE = "text/html";
    private final String _ENCODING = "utf-8";
    private final String _JS_INTERFACE = "NoelInterface";

    private ParamsModel paramsModel;
    private Context context;
    private WebSettings webSettings;
    private CustomWebViewHandler customWebViewHandler;
    private CustomWebViewClient customWebViewClient;
    private CustomWebChromeClient customWebChromeClient;
    private OnWebProgressChangeListener onWebProgressChangeListener;
    private OnPlayerProgressChangeListener onPlayerProgressChangeListener;
    private OnPlayerStateChangeListener onPlayerStateChangeListener;

    public CustomWebView(Context context, CustomWebViewHandler customWebViewHandler) {
        super(context);
        this.context = context;
        this.customWebViewHandler = customWebViewHandler;
        init();

    }

    //-----
    private void init() {
        customWebViewClient = new CustomWebViewClient(context);
        customWebChromeClient = new CustomWebChromeClient(context);
        customWebChromeClient.setOnProgressChangeListener(this);
        customWebViewHandler.setOnYouTubeEventHappenListener(this);

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
    @SuppressLint("JavascriptInterface")
    public void loadVideo(ParamsModel paramsModel) {
        this.paramsModel = paramsModel;
        setLayerType(View.LAYER_TYPE_NONE, null);
        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        addJavascriptInterface(customWebViewHandler, _JS_INTERFACE);
        loadDataWithBaseURL(_DOMAIN, getYoutubeHTML(), _TYPE, _ENCODING, null);
    }

    //----------

    /***
     * HTML 制定
     * @return
     */

    private String getYoutubeHTML() {

        String html = new AssetsReader(context).getStringFromFile()
                .replace("[VIDEO_ID]", paramsModel.getYoutubeId())
                //背景色碼
                .replace("[BG_COLOR]", paramsModel.getBgColor());

        html = html
                // 是否自動撥放
                .replace("[AUTO_PLAY]", paramsModel.getAutoPlay())
                .replace("[AUTO_HIDE]", paramsModel.getAutoHide())
                // 是否取得相關影片
                .replace("[REL]", paramsModel.getRel())
                //是否顯示影片資訊
                .replace("[SHOW_INFO]", paramsModel.getShowInfo())
                //是否允許JavaScript API
                .replace("[ENABLE_JS_API]", paramsModel.getEnableJSApi())
                //是否不允許keyboard
                .replace("[DISABLE_KB]", paramsModel.getDisableKB())
                //語系
                .replace("[CC_LANG_PREF]", paramsModel.getCcLangPref())
                .replace("[CONTROLS]", paramsModel.getControls())
                //是否全螢幕
                .replace("[FS]", paramsModel.getFs());
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
            onWebProgressChangeListener.onWebLoaded();
        } else {
            onWebProgressChangeListener.onWebProgressChanged(newProgress);
        }
    }


    //---------

    @Override
    public void onReady() {
        if (paramsModel != null && paramsModel.getAutoPlay().equals("1")) {
            customWebViewHandler.play(this);
        }
    }
    //---------

    /***
     * 當狀態改變
     * @param state
     */
    @Override
    public void onStateChange(int state) {
        onPlayerStateChangeListener.onPlayerStateChanged(state);
    }

    //-------

    /***
     * 當畫值調整
     * @param arg
     */
    @Override
    public void onPlaybackQualityChange(String arg) {

    }

    //------

    @Override
    public void onPlaybackRateChange(String arg) {

    }
    //------

    /***
     * 發生錯誤
     * @param arg
     */
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
        onPlayerProgressChangeListener.onPlayerCurrent(second);
    }
    //------

    @Override
    public void onDuration(double duration) {
        onPlayerProgressChangeListener.onPlayerDuration(duration);
    }
    //------

    @Override
    public void logs(String log) {

    }

    //-------

    /***
     * 載入網頁資源進度監聽
     */
    public interface OnWebProgressChangeListener {
        void onWebProgressChanged(int newProgress);

        void onWebLoaded();
    }

    public void setOnWebProgressChangeListener(OnWebProgressChangeListener onWebProgressChangeListener) {
        this.onWebProgressChangeListener = onWebProgressChangeListener;
    }

    //---------

    /***
     * 影片播放進度
     */
    public interface OnPlayerProgressChangeListener {
        void onPlayerCurrent(double second);

        void onPlayerDuration(double duration);
    }

    public void setOnPlayerProgressChangeListener(OnPlayerProgressChangeListener onPlayerProgressChangeListener) {
        this.onPlayerProgressChangeListener = onPlayerProgressChangeListener;
    }
    //------

    /***
     * 撥放器狀態改變
     */
    public interface OnPlayerStateChangeListener {
        void onPlayerStateChanged(@CustomWebViewHandler.YoutubePlayerState int newState);
    }

    public void setOnPlayerStateChangeListener(OnPlayerStateChangeListener onPlayerStateChangeListener) {
        this.onPlayerStateChangeListener = onPlayerStateChangeListener;
    }
}
