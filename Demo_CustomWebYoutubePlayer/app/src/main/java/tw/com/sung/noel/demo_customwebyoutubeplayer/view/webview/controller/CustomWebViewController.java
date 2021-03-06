package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.lang.ref.WeakReference;

import tw.com.sung.noel.demo_customwebyoutubeplayer.R;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.util.TimeUtil;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebView;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.CustomWebViewHandler;

public class CustomWebViewController extends RelativeLayout implements View.OnClickListener {

    private OnLoadMoreClickListener onLoadMoreClickListener;
    //按鈕的尺寸倍率
    private final double BUTTON_SIZE = 0.5;
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
    //是否為播放中
    private boolean isPlaying;

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

        ivPlay.setNextFocusUpId(playId);
        ivPlay.setNextFocusDownId(playId);
        ivPlay.setNextFocusRightId(accelerateId);
        ivPlay.setNextFocusLeftId(backId);

        ivAccelerate.setNextFocusUpId(accelerateId);
        ivAccelerate.setNextFocusDownId(accelerateId);
        ivAccelerate.setNextFocusLeftId(playId);
        ivAccelerate.setNextFocusRightId(nextId);

        ivNext.setNextFocusUpId(nextId);
        ivNext.setNextFocusDownId(nextId);
        ivNext.setNextFocusLeftId(accelerateId);

        ivBack.setNextFocusUpId(backId);
        ivBack.setNextFocusDownId(backId);
        ivBack.setNextFocusRightId(playId);
        ivBack.setNextFocusLeftId(previousId);

        ivPrevious.setNextFocusUpId(previousId);
        ivPrevious.setNextFocusDownId(previousId);
        ivPrevious.setNextFocusRightId(backId);
    }


    //-------

    /***
     * 置頂進度條
     */
    private void initProgressBar() {
        LayoutParams params = new LayoutParams(layoutWidth, (int) (layoutHeight * 0.08));

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
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        initPreviousButton();
        initNextButton();

        if (!isCanLoadOtherVideo) {
            ivPrevious.setVisibility(INVISIBLE);
            ivNext.setVisibility(INVISIBLE);
        }

    }
    //-----------

    /***
     * 目前時間 textview
     */
    private void initCurrentTextView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
        LayoutParams params = new LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(CENTER_HORIZONTAL);

        playId = View.generateViewId();
        ivPlay = new ImageView(context);
        ivPlay.setLayoutParams(params);
        ivPlay.setId(playId);
        ivPlay.setFocusable(true);
        ivPlay.setFocusableInTouchMode(true);
        ivPlay.setOnClickListener(this);
        ivPlay.setBackground(getCustomBackgroundSelector());
        ivPlay.setImageResource(R.drawable.ic_pause);
        ivPlay.setPadding(15, 15, 15, 15);
        innerLayout.addView(ivPlay);
    }

    //---------

    /***
     * 倒退15秒 button
     */
    private void initBackButton() {
        LayoutParams params = new LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(START_OF, ivPlay.getId());
        params.rightMargin = ivMargin;

        backId = View.generateViewId();
        ivBack = new ImageView(context);
        ivBack.setLayoutParams(params);
        ivBack.setId(backId);
        ivBack.setFocusable(true);
        ivBack.setFocusableInTouchMode(true);
        ivBack.setOnClickListener(this);
        ivBack.setScaleType(ImageView.ScaleType.FIT_XY);
        ivBack.setBackground(getCustomBackgroundSelector());
        ivBack.setImageResource(R.drawable.ic_back);
        ivBack.setPadding(15, 15, 15, 15);
        innerLayout.addView(ivBack);
    }

    //---------

    /***
     * 前一部 button
     */
    private void initPreviousButton() {
        LayoutParams params = new LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvCurrentTime.getId());
        params.addRule(START_OF, ivBack.getId());
        params.rightMargin = ivMargin;

        previousId = View.generateViewId();
        ivPrevious = new ImageView(context);
        ivPrevious.setId(previousId);
        ivPrevious.setFocusable(true);
        ivPrevious.setFocusableInTouchMode(true);
        ivPrevious.setOnClickListener(this);
        ivPrevious.setLayoutParams(params);
        ivPrevious.setBackground(getCustomBackgroundSelector());
        ivPrevious.setImageResource(R.drawable.ic_previous);
        ivPrevious.setPadding(15, 15, 15, 15);
        innerLayout.addView(ivPrevious);
    }

    //---------

    /***
     * 快轉15秒 button
     */
    private void initAccelerateButton() {
        LayoutParams params = new LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(END_OF, ivPlay.getId());
        params.leftMargin = ivMargin;

        accelerateId = View.generateViewId();
        ivAccelerate = new ImageView(context);
        ivAccelerate.setLayoutParams(params);
        ivAccelerate.setId(accelerateId);
        ivAccelerate.setFocusable(true);
        ivAccelerate.setFocusableInTouchMode(true);
        ivAccelerate.setOnClickListener(this);
        ivAccelerate.setScaleType(ImageView.ScaleType.FIT_XY);
        ivAccelerate.setBackground(getCustomBackgroundSelector());
        ivAccelerate.setImageResource(R.drawable.ic_accelerate);
        ivAccelerate.setPadding(15, 15, 15, 15);
        innerLayout.addView(ivAccelerate);
    }
    //---------

    /***
     * 下一部 button
     */
    private void initNextButton() {
        LayoutParams params = new LayoutParams((int) (layoutHeight * BUTTON_SIZE), (int) (layoutHeight * BUTTON_SIZE));
        params.addRule(BELOW, tvTotalTime.getId());
        params.addRule(END_OF, ivAccelerate.getId());
        params.leftMargin = ivMargin;

        nextId = View.generateViewId();
        ivNext = new ImageView(context);
        ivNext.setLayoutParams(params);
        ivNext.setId(nextId);
        ivNext.setFocusable(true);
        ivNext.setFocusableInTouchMode(true);
        ivNext.setOnClickListener(this);
        ivNext.setBackground(getCustomBackgroundSelector());
        ivNext.setImageResource(R.drawable.ic_next);
        ivNext.setPadding(15, 15, 15, 15);
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
        int intProgress = (int) Math.round(progress + 1);
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
        int intDuration = (int) Math.round(duration + 1);
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
        this.isPlaying = isPlaying;
        Message msg = playerHandler.obtainMessage();
        msg.what = 3;
        msg.sendToTarget();
    }

    //----------------

    Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tvCurrentTime.setText(timeUtil.convertSecondToMinute(intProgress));
            } else if (msg.what == 2) {
                tvTotalTime.setText(timeUtil.convertSecondToMinute(intDuration));
            } else if (msg.what == 3) {
                ivPlay.setImageResource(isPlaying ? R.drawable.ic_play : R.drawable.ic_pause);
            }
            super.handleMessage(msg);
        }
    };

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
            if (onLoadMoreClickListener != null) {
                onLoadMoreClickListener.onLoadPreviousVideoClicked();
            }
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
            if (onLoadMoreClickListener != null) {
                onLoadMoreClickListener.onLoadNextVideoClicked();
            }
        }
        //play / pause
        else if (id == playId) {
            switch (customWebViewHandler.getPlayerState()) {
                case CustomWebViewHandler.PLAYING:
                    customWebViewHandler.pause(customWebView);
                    break;
                case CustomWebViewHandler.PAUSED:
                case CustomWebViewHandler.ENDED:
                    customWebViewHandler.play(customWebView);
                    break;
            }
        }
    }


    //----------

    private StateListDrawable getCustomBackgroundSelector() {
        GradientDrawable focusGradientDrawable = new GradientDrawable();
        focusGradientDrawable.setColor(context.getResources().getColor(R.color.focus_bg));
        focusGradientDrawable.setShape(GradientDrawable.OVAL);

        GradientDrawable normalGradientDrawable = new GradientDrawable();
        normalGradientDrawable.setColor(Color.TRANSPARENT);
        normalGradientDrawable.setShape(GradientDrawable.OVAL);

        StateListDrawable selectorDrawable = new StateListDrawable();
        selectorDrawable.addState(new int[]{android.R.attr.state_focused}, focusGradientDrawable);
        selectorDrawable.addState(new int[]{}, normalGradientDrawable);

        return selectorDrawable;
    }


    //---------

    public interface OnLoadMoreClickListener {
        void onLoadNextVideoClicked();

        void onLoadPreviousVideoClicked();
    }

    //----------
    public void setOnLoadMoreClickListener(OnLoadMoreClickListener onLoadMoreClickListener) {
        this.onLoadMoreClickListener = onLoadMoreClickListener;
    }

}
