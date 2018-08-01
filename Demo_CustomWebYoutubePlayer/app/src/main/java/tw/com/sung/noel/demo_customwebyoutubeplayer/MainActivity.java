package tw.com.sung.noel.demo_customwebyoutubeplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import tw.com.sung.noel.demo_customwebyoutubeplayer.view.CustomYoutubePlayer;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.model.ParamsModel;

public class MainActivity extends FragmentActivity {

    private CustomYoutubePlayer customYoutubePlayer;

    //影音
    private final String YOUTUBE_ID = "_sQSXwdtxlY";
    //直播
//    private final String YOUTUBE_ID = "4ZVUmEUFwaY";

    private final String LANGUAGE = "en";
    private final String BACKGROUND_COLOR = "#000000";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customYoutubePlayer = (CustomYoutubePlayer) findViewById(R.id.custom_youtube_player);
//        customYoutubePlayer.canLoadOtherVideo(false);
//        customYoutubePlayer.setControllerUsed(false);
        customYoutubePlayer.loadVideo(getParams());
    }

    //--------

    /***
     * 參數設定
     */
    private ParamsModel getParams() {
        ParamsModel paramsModel = new ParamsModel();
        paramsModel.setBgColor(BACKGROUND_COLOR);
        paramsModel.setYoutubeId(YOUTUBE_ID);
        paramsModel.setCcLangPref(LANGUAGE);
        paramsModel.setAutoHide(false);
        paramsModel.setAutoPlay(true);
        paramsModel.setControls(false);
        paramsModel.setDisableKB(true);
        paramsModel.setEnableJSApi(true);
        paramsModel.setFs(true);
        paramsModel.setRel(false);
        paramsModel.setShowInfo(false);
        return paramsModel;
    }

    //--------

    @Override
    public void onBackPressed() {
        if(customYoutubePlayer.isControllerVisible()){
            customYoutubePlayer.hideController();
        }else {
            finish();
        }
    }
}
