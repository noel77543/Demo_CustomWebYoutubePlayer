package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import tw.com.sung.noel.demo_customwebyoutubeplayer.R;

public class CustomWebViewController extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener {


    private OnControllerEventListener onControllerEventListener;

    private ProgressBar progressBar;
    private RelativeLayout innerLayout;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    //前一個
    private ImageView ivPrevious;
    private int previousEvent;
    //倒退 15 sec
    private ImageView ivBack;
    private int backEvent;
    //play / pause
    private ImageView ivPlay;
    private int playEvent;
    //快轉 15 sec
    private ImageView ivAccelerate;
    private int accelerateEvent;
    //下一個
    private ImageView ivNext;
    private int nextEvent;

    private Context context;
    private int layoutWidth;
    private int layoutHeight;
    //按鈕彼此間的間距
    private int ivMargin;
    //按鈕的尺寸倍率
    private final double BUTTON_SIZE = 0.3;

    //-------
    public CustomWebViewController(Context context , int layoutWidth, int layoutHeight) {
        super(context);
        this.context = context;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
        ivMargin = (int) (layoutWidth * 0.08);

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

        progressBar.setSecondaryProgress(80);
        progressBar.setProgress(30);
        progressBar.setMax(100);

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
        initPreviousButton();
        initAccelerateButton();
        initNextButton();
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

        tvCurrentTime.setText("103:24");
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
        tvTotalTime.setText("169:00");
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
        playEvent = View.generateViewId();

        ivPlay = new ImageView(context);
        ivPlay.setLayoutParams(params);
        ivPlay.setId(playEvent);
        ivPlay.setFocusable(true);
        ivPlay.setFocusableInTouchMode(true);
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

        backEvent = View.generateViewId();
        ivBack = new ImageView(context);
        ivBack.setLayoutParams(params);
        ivBack.setId(backEvent);
        ivBack.setFocusable(true);
        ivBack.setFocusableInTouchMode(true);
        ivBack.setOnFocusChangeListener(this);
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

        previousEvent = View.generateViewId();
        ivPrevious = new ImageView(context);
        ivPrevious.setId(previousEvent);
        ivPrevious.setFocusable(true);
        ivPrevious.setFocusableInTouchMode(true);
        ivPrevious.setOnFocusChangeListener(this);
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

        accelerateEvent = View.generateViewId();
        ivAccelerate = new ImageView(context);
        ivAccelerate.setLayoutParams(params);
        ivAccelerate.setId(accelerateEvent);
        ivAccelerate.setFocusable(true);
        ivAccelerate.setFocusableInTouchMode(true);
        ivAccelerate.setOnFocusChangeListener(this);
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

        nextEvent = View.generateViewId();
        ivNext = new ImageView(context);
        ivNext.setLayoutParams(params);
        ivNext.setId(nextEvent);
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
    public void updateProgress(int max, int progress) {
        progressBar.setMax(max);
        progressBar.setProgress(progress);

    }

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
     * @param view
     */
    @Override
    public void onClick(View view) {
        onControllerEventListener.onEventHappened(view.getId());
    }

    //----------------

    public interface OnControllerEventListener {
        void onEventHappened(int controllerEvent);
    }

    public void setOnControllerEventListener(OnControllerEventListener onControllerEventListener) {
        this.onControllerEventListener = onControllerEventListener;
    }
}
