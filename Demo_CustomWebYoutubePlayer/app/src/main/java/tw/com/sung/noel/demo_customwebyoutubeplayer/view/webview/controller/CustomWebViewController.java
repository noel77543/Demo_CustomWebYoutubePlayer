package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import tw.com.sung.noel.demo_customwebyoutubeplayer.R;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.util.TimeUtil;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebView;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebViewHandler;

public class CustomWebViewController extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener {

    //按鈕的尺寸倍率
    private final double BUTTON_SIZE = 0.25;
    //快轉 / 倒退 的變動秒數
    private final int PLAYER_TIME_VARIATION = 15;


    private TimeUtil timeUtil;
    private CustomWebViewHandler customWebViewHandler;
    private CustomWebView customWebView;

    private ProgressBar progressBar;
    private RelativeLayout innerLayout;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    //上一部
    private ImageView ivPrevious;
    private int previousId;
    //倒退 15 sec
    private ImageView ivBack;
    private int backId;
    //play / pause
    private ImageView ivPlay;
    private int playId;
    //快轉 15 sec
    private ImageView ivAccelerate;
    private int accelerateId;
    //下一部
    private ImageView ivNext;
    private int nextId;

    private Context context;
    private int layoutWidth;
    private int layoutHeight;
    //按鈕彼此間的間距
    private int ivMargin;
    //影片總時長
    private int intDuration;
    //目前播放時間
    private int intProgress;
    //可否 上一部影片 or 下一部影片
    private boolean isCanLoadOtherVideo;

    //-------
    public CustomWebViewController(Context context, CustomWebView customWebView, CustomWebViewHandler customWebViewHandler, boolean isCanLoadOtherVideo, int layoutWidth, int layoutHeight) {
        super(context);
        this.context = context;
        this.customWebView = customWebView;
        this.customWebViewHandler = customWebViewHandler;
        this.isCanLoadOtherVideo = isCanLoadOtherVideo;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
        ivMargin = (int) (layoutWidth * 0.08);
        timeUtil = new TimeUtil();

        initProgressBar();
        initInnerLayout();
    }


    //-------

    /***
     * 置頂進度條
     */
    private void initProgressBar() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(layoutWidth, (int) (layoutHeight * 0.08));

        progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setProgressDrawable(context.getDrawable(R.drawable.layer_list_controller_progress));
        progressBar.setLayoutParams(params);
        progressBar.setId(View.generateViewId());
        addView(progressBar);
    }
    //-------

    /***
     * 內部layout
     */
    private void initInnerLayout() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(BELOW, progressBar.getId());
        params.addRule(ALIGN_PARENT_START);
        innerLayout = new RelativeLayout(context);
        innerLayout.setBackgroundColor(Color.YELLOW);
        innerLayout.setLayoutParams(params);
        innerLayout.setBackgroundColor(context.getResources().getColor(R.color.controller_bg));
        addView(innerLayout);
        initCurrentTextView();
        initTotalTextView();

        initPlayButton();
        initBackButton();
        initAccelerateButton();
        if (isCanLoadOtherVideo) {
            initPreviousButton();
            initNextButton();
        }

    }
    //-----------

    /***
     * 目前時間 textview
     */
    private void initCurrentTextView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_START);
        params.addRule(ALIGN_PARENT_TOP);
        params.setMargins(20, 10, 0, 0);
        tvCurrentTime = new TextView(context);
        tvCurrentTime.setLayoutParams(params);
        tvCurrentTime.setId(View.generateViewId());
        tvCurrentTime.setTextSize(10);
        innerLayout.addView(tvCurrentTime);
    }

    //--------

    /***
     * 總時間 textview
     */
    private void initTotalTextView() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_END);
        params.addRule(ALIGN_PARENT_TOP);
        params.setMargins(0, 10, 20, 0);
        tvTotalTime = new TextView(context);
        tvTotalTime.setLayoutParams(params);
        tvTotalTime.setId(View.generateViewId());
        tvTotalTime.setTextSize(10);
        innerLayout.addView(tvTotalTime);
    }

    //---------

    /***
     * 　播放 / 暫停  button
     */
    private void initPlayButton() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(CENTER_HORIZONTAL);

        playId = View.generateViewId();
        ivPlay = new ImageView(context);
        ivPlay.setLayoutParams(params);
        ivPlay.setId(playId);
        ivPlay.setFocusable(true);
        ivPlay.setFocusableInTouchMode(true);
        ivPlay.setOnClickListener(this);
        ivPlay.setOnFocusChangeListener(this);
        ivPlay.setImageResource(R.drawable.ic_pause);
        innerLayout.addView(ivPlay);
    }

    //---------

    /***
     * 倒退15秒 button
     */
    private void initBackButton() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(START_OF, ivPlay.getId());
        params.rightMargin = ivMargin;

        backId = View.generateViewId();
        ivBack = new ImageView(context);
        ivBack.setLayoutParams(params);
        ivBack.setId(backId);
        ivBack.setFocusable(true);
        ivBack.setFocusableInTouchMode(true);
        ivBack.setOnFocusChangeListener(this);
        ivBack.setOnClickListener(this);
        ivBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ivBack.setImageResource(R.drawable.ic_back);
        innerLayout.addView(ivBack);
    }

    //---------

    /***
     * 前一部 button
     */
    private void initPreviousButton() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvCurrentTime.getId());
        params.addRule(START_OF, ivBack.getId());
        params.rightMargin = ivMargin;

        previousId = View.generateViewId();
        ivPrevious = new ImageView(context);
        ivPrevious.setId(previousId);
        ivPrevious.setFocusable(true);
        ivPrevious.setFocusableInTouchMode(true);
        ivPrevious.setOnFocusChangeListener(this);
        ivPrevious.setOnClickListener(this);
        ivPrevious.setLayoutParams(params);
        ivPrevious.setImageResource(R.drawable.ic_previous);
        innerLayout.addView(ivPrevious);
    }

    //---------

    /***
     * 快轉15秒 button
     */
    private void initAccelerateButton() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(END_OF, ivPlay.getId());
        params.leftMargin = ivMargin;

        accelerateId = View.generateViewId();
        ivAccelerate = new ImageView(context);
        ivAccelerate.setLayoutParams(params);
        ivAccelerate.setId(accelerateId);
        ivAccelerate.setFocusable(true);
        ivAccelerate.setFocusableInTouchMode(true);
        ivAccelerate.setOnFocusChangeListener(this);
        ivAccelerate.setOnClickListener(this);
        ivAccelerate.setScaleType(ImageView.ScaleType.FIT_XY);
        ivAccelerate.setImageResource(R.drawable.ic_accelerate);
        innerLayout.addView(ivAccelerate);
    }
    //---------

    /***
     * 下一部 button
     */
    private void initNextButton() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(END_OF, ivAccelerate.getId());
        params.leftMargin = ivMargin;

        nextId = View.generateViewId();
        ivNext = new ImageView(context);
        ivNext.setLayoutParams(params);
        ivNext.setId(nextId);
        ivNext.setFocusable(true);
        ivNext.setFocusableInTouchMode(true);
        ivNext.setOnFocusChangeListener(this);
        ivNext.setOnClickListener(this);
        ivNext.setImageResource(R.drawable.ic_next);
        innerLayout.addView(ivNext);
    }
    //----------------

    /***
     * 顯示的時候focus 播放/暫停 按鈕
     */
    public void focusPlayButton() {
        ivPlay.requestFocus();
    }

    //---------------

    /***
     * 更新進度
     */
    public void updateProgress(double progress) {
        int intProgress = (int) progress;
        this.intProgress = intProgress;
        progressBar.setProgress(intProgress);
        Message msg = playerHandler.obtainMessage();
        msg.what = 1;
        msg.sendToTarget();
    }
    //---------------

    /**
     * 總時長
     */
    public void setDuration(double duration) {
        int intDuration = (int) duration;
        this.intDuration = intDuration;
        progressBar.setMax(intDuration);
        Message msg = playerHandler.obtainMessage();
        msg.what = 2;
        msg.sendToTarget();
    }
    //----------

    /***
     * 改變playButton 的icon
     */
    public void changePlayButtonIcon(boolean isPlaying) {
        ivPlay.setImageResource(isPlaying ? R.drawable.ic_play : R.drawable.ic_pause);
    }


    //---------------

    Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tvCurrentTime.setText(timeUtil.convertSecondToMinute(intProgress));
            } else if (msg.what == 2) {
                tvTotalTime.setText(timeUtil.convertSecondToMinute(intDuration));
            }
            super.handleMessage(msg);
        }
    };

    //----------------

    /***
     * 當focus
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ImageView imageView = findViewById(v.getId());
        if (hasFocus) {
            imageView.setBackgroundResource(R.drawable.shape_focus_bg);
        } else {
            imageView.setBackgroundResource(0);
        }
    }
    //----------------

    /***
     * 當click
     * p.s   switcch ..case.. 須為常數  但是ID此處為動態生成故無法使用之
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        //上一部
        if (id == previousId) {

        }
        //倒退 15sec
        else if (id == backId) {
            customWebViewHandler.seekTo(customWebView, intProgress - PLAYER_TIME_VARIATION);
        }
        //快轉 15sec
        else if (id == accelerateId) {
            customWebViewHandler.seekTo(customWebView, intProgress + PLAYER_TIME_VARIATION);
        }
        //下一部
        else if (id == nextId) {

        }
        //play / pause
        else if (id == playId) {
            Log.e("click", ""+customWebViewHandler.getPlayerState());
            switch (customWebViewHandler.getPlayerState()) {
                case CustomWebViewHandler.PLAYING:
                    customWebViewHandler.pause(customWebView);
                    break;
                case CustomWebViewHandler.PAUSED:
                    customWebViewHandler.play(customWebView);
                    break;
            }
        }
    }
}
