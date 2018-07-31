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
    private OnProgressChangeListener onProgressChangeListener;

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
        Log.e("loadVideo", "paramsModel");

        setLayerType(View.LAYER_TYPE_NONE, null);
        Log.e("loadVideo", "LAYER_TYPE_NONE");


        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        Log.e("loadVideo", "UNSPECIFIED");

        addJavascriptInterface(customWebViewHandler, _JS_INTERFACE);
        Log.e("loadVideo", "customWebViewHandler");

        loadDataWithBaseURL(_DOMAIN, getYoutubeHTML(), _TYPE, _ENCODING, null);
        Log.e("loadVideo", "loadDataWithBaseURL");

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
            onProgressChangeListener.onFinished();
        } else {
            onProgressChangeListener.onProgressChanged(newProgress);
        }
    }


    //---------

    @Override
    public void onReady() {
        if (paramsModel != null && paramsModel.getAutoPlay().equals("1")) {
            Log.e("play", "play");
            customWebViewHandler.play(this);
        }
    }
    //---------

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

    /***
     * 在調用此Webview時 記得在ondestroy接口call此 method進行清除
     */
    public void clear() {
        super.onDetachedFromWindow();
        this.clearCache(true);
        this.clearHistory();
    }

    //-------
    public interface OnProgressChangeListener {
        void onProgressChanged(int newProgress);

        void onFinished();
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
