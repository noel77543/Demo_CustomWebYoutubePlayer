package tw.com.sung.noel.demo_customwebyoutubeplayer.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import tw.com.sung.noel.demo_customwebyoutubeplayer.R;
import tw.com.sung.noel.demo_customwebyoutubeplayer.util.view.webview.CustomWebView;


public class CustomYoutubePlayer extends RelativeLayout implements CustomWebView.OnProgressChangeListener, View.OnClickListener {

    private Context context;
    private LayoutParams webViewParams;
    private CustomWebView customWebView;
    private ProgressBar progressBar;

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
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnClickListener(this);

        initPlayer();
        initProgressBar();
    }
    //------

    @Override
    public void onClick(View v) {

    }

    //------

    /***
     * webview 視頻初始化
     */
    private void initPlayer() {
        customWebView = new CustomWebView(context);
        webViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webViewParams.addRule(ALIGN_PARENT_LEFT | ALIGN_PARENT_TOP);
        customWebView.setLayoutParams(webViewParams);
        customWebView.setOnProgressChangeListener(this);
        addView(customWebView);
    }
    //--------

    /***
     * 進度條
     */
    private void initProgressBar() {

        RelativeLayout.LayoutParams params = new LayoutParams(100,100);
        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        params.addRule(CENTER_HORIZONTAL);
        params.addRule(CENTER_VERTICAL);
        progressBar.setProgressDrawable(context.getDrawable(R.drawable.layer_list_circle_loading));
        progressBar.setMax(100);
        progressBar.setLayoutParams(params);

        addView(progressBar);
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
    public void loadVideo(String youtubeId, String backgroundColorStr, boolean isAutoPlay, boolean isAutoHide, boolean isGetRelativeVideo, boolean isShowInfo) {
        customWebView.loadVideoByYoutubeId(youtubeId, backgroundColorStr, isAutoPlay, isAutoHide, isGetRelativeVideo, isShowInfo);
    }
}
