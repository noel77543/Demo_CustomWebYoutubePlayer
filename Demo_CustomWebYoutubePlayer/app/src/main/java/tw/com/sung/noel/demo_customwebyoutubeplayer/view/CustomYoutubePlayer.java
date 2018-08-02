package tw.com.sung.noel.demo_customwebyoutubeplayer.view;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import tw.com.sung.noel.demo_customwebyoutubeplayer.R;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebView;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebViewHandler;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.controller.CustomWebViewController;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.model.ParamsModel;


public class CustomYoutubePlayer extends RelativeLayout implements CustomWebView.OnWebProgressChangeListener, View.OnClickListener, Runnable, CustomWebView.OnPlayerProgressChangeListener, CustomWebView.OnPlayerStateChangeListener {

    private final int _CONTROLLER_ANIMATION_TIME = 300;
    private Context context;
    private CustomWebViewHandler customWebViewHandler;
    private ProgressBar progressBar;
    private CustomWebView customWebView;
    private RelativeLayout.LayoutParams webViewParams;
    private CustomWebViewController customWebViewController;
    private RelativeLayout.LayoutParams controllerParams;

    //是否使用控制台 預設為true
    private boolean isController = true;
    //若有使用控制台 目前controller是否為可見 預設為false
    private boolean isControllerVisible;
    //可否 上一部影片 or 下一部影片 預設為true
    private boolean isCanLoadOtherVideo = true;

    private int playerHeightPx;
    private int playerWidthPx;

    //----------

    public CustomYoutubePlayer(Context context) {
        super(context);
        this.context = context;
        init();
    }
    //---------

    public CustomYoutubePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    //---------
    private void init() {
        customWebViewHandler = new CustomWebViewHandler();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnClickListener(this);
        initPlayer();
        initProgressBar();
    }


    //------

    /***
     * webview 視頻初始化
     */
    private void initPlayer() {
        customWebView = new CustomWebView(context, customWebViewHandler);
        webViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webViewParams.addRule(ALIGN_PARENT_LEFT | ALIGN_PARENT_TOP);
        customWebView.setLayoutParams(webViewParams);
        customWebView.setOnWebProgressChangeListener(this);
        customWebView.setOnPlayerProgressChangeListener(this);
        customWebView.setOnPlayerStateChangeListener(this);

        addView(customWebView);
    }
    //--------

    /***
     * Loading web 進度條
     */
    private void initProgressBar() {
        RelativeLayout.LayoutParams params = new LayoutParams(100, 100);
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_VERTICAL);
        progressBar.setProgressDrawable(context.getDrawable(R.drawable.layer_list_circle_loading));
        progressBar.setMax(100);
        progressBar.setSecondaryProgress(100);
        progressBar.setLayoutParams(params);
        addView(progressBar);
    }

    //------

    /***
     * 當點擊此Layout
     * @param view
     */
    @Override
    public void onClick(View view) {
        showController();
    }


    //--------


    /***
     * 載入進度更新
     * @param newProgress
     */
    @Override
    public void onWebProgressChanged(int newProgress) {
        progressBar.setProgress(newProgress);
    }
    //--------

    /***
     * 完成載入
     */
    @Override
    public void onWebLoaded() {
        removeView(progressBar);
    }

    //--------

    /***
     * 當影片播放進度改變
     * @param second
     */
    @Override
    public void onPlayerCurrent(double second) {
        customWebViewController.updateProgress(second);
    }

    //-------

    /***
     * 當取得影片總時長
     * @param duration
     */
    @Override
    public void onPlayerDuration(double duration) {
        customWebViewController.setDuration(duration);
    }

    @Override
    public void onPlayerStateChanged(int newState) {
        customWebViewController.changePlayButtonIcon(newState == CustomWebViewHandler.PAUSED || newState == CustomWebViewHandler.ENDED);
    }
    //------

    /***
     * load 視頻
     */
    public void loadVideo(ParamsModel paramsModel) {
        if (isController) {
            post(this);
        }
        customWebView.loadVideo(paramsModel);
    }


    //------

    @Override
    public void run() {
        playerHeightPx = getHeight();
        playerWidthPx = getWidth();
        initController();
    }

    //-------

    /***
     *  控制台初始化
     */
    private void initController() {
        controllerParams = new LayoutParams(playerWidthPx, (int) (playerHeightPx * 0.1));
        controllerParams.addRule(ALIGN_PARENT_BOTTOM);
        controllerParams.addRule(CENTER_HORIZONTAL);
        controllerParams.setMargins((int) (playerWidthPx * 0.15), 0, (int) (playerWidthPx * 0.15), (int) (playerHeightPx * 0.15));
        customWebViewController = new CustomWebViewController(context, customWebView, customWebViewHandler, isCanLoadOtherVideo, controllerParams.width, controllerParams.height);
        customWebViewController.setLayoutParams(controllerParams);
        addView(customWebViewController);
        customWebViewController.setVisibility(GONE);
        hideController();
    }

    //--------

    /***
     * show 控制台
     */
    public void showController() {
        if (isController) {
            customWebViewController.setVisibility(VISIBLE);
            customWebViewController
                    .animate()
                    .translationY(0)
                    .setDuration(_CONTROLLER_ANIMATION_TIME)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isControllerVisible = true;
                            customWebViewController.focusPlayButton();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    //----------

    /***
     * hide 控制台
     */
    public void hideController() {
        if (isController) {
            customWebViewController
                    .animate()
                    .translationY(playerHeightPx / 2)
                    .setDuration(_CONTROLLER_ANIMATION_TIME)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isControllerVisible = false;
                            requestFocus();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }


    //------

    /***
     * 釋放webview所佔用記憶體
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }
    //------

    /***
     * 釋放webview所佔用記憶體
     */
    private void clear() {
        super.onDetachedFromWindow();
        removeAllViews();
        if (customWebView != null) {
            customWebView.setTag(null);
            customWebView.clearHistory();
            customWebView.clearCache(true);
            customWebView.removeAllViews();
            customWebView.clearView();
            customWebView.destroy();
            customWebView = null;
        }
    }


    //------

    /***
     * 是否使用控制台 預設為是
     */
    public void setControllerUsed(boolean isController) {
        this.isController = isController;
    }
    //--------

    /***
     * 可否 上一部影片 or 下一部影片
     */
    public void canLoadOtherVideo(boolean isCanLoadOtherVideo) {
        this.isCanLoadOtherVideo = isCanLoadOtherVideo;
    }


    //-------

    /***
     * 控制台是否為可見狀態
     */
    public boolean isControllerVisible() {
        return isControllerVisible;
    }

}
