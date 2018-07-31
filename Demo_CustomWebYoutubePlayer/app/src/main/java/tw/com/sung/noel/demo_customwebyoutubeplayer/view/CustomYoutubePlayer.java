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


public class CustomYoutubePlayer extends RelativeLayout implements CustomWebView.OnProgressChangeListener, View.OnClickListener, Runnable, CustomWebViewController.OnControllerEventListener {

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
    //目前controller是否為可見 預設為false
    private boolean isControllerVisible;

    private int playerHeightPx;
    private int playerWidthPx;


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
        customWebView.setOnProgressChangeListener(this);
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
    public void onProgressChanged(int newProgress) {
        progressBar.setProgress(newProgress);

    }
    //--------

    /***
     * 完成載入
     */
    @Override
    public void onFinished() {
        removeView(progressBar);
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
        controllerParams = new LayoutParams(playerWidthPx, (int) (playerHeightPx * 0.12));
        controllerParams.addRule(ALIGN_PARENT_BOTTOM);
        controllerParams.addRule(CENTER_HORIZONTAL);
        controllerParams.setMargins((int) (playerWidthPx * 0.15), 0, (int) (playerWidthPx * 0.15), (int) (playerHeightPx * 0.12));
        customWebViewController = new CustomWebViewController(context, controllerParams.width, controllerParams.height);
        customWebViewController.setOnControllerEventListener(this);
        customWebViewController.setLayoutParams(controllerParams);
        addView(customWebViewController);
        customWebViewController.setVisibility(GONE);
        hideController();
    }

    //--------

    /***
     * 當點選控制台的按鈕
     * @param controllerEvent
     */
    @Override
    public void onEventHappened(int controllerEvent) {

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
     * 是否使用控制台 預設為使用
     */
    public void setControllerUsed(boolean isController) {
        this.isController = isController;
    }
    //-------

    /***
     * 控制台是否為可見
     */
    public boolean isControllerVisible() {
        return isControllerVisible;
    }

}
