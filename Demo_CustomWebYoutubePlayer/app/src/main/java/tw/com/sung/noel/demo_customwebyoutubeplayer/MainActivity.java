package tw.com.sung.noel.demo_customwebyoutubeplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import tw.com.sung.noel.demo_customwebyoutubeplayer.view.CustomYoutubePlayer;
import tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.model.ParamsModel;

public class MainActivity extends FragmentActivity {

    private CustomYoutubePlayer customYoutubePlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customYoutubePlayer = (CustomYoutubePlayer) findViewById(R.id.custom_youtube_player);
//        customYoutubePlayer.setControllerUsed(false);
        customYoutubePlayer.loadVideo(getParams());
    }

    //--------

    /***
     *
     * @return
     */
    private ParamsModel getParams() {
        ParamsModel paramsModel = new ParamsModel();
        paramsModel.setBgColor("#000000");
        paramsModel.setYoutubeId("4ZVUmEUFwaY");
        paramsModel.setCcLangPref("en");
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


    @Override
    public void onBackPressed() {
        if(customYoutubePlayer.isControllerVisible()){
            customYoutubePlayer.hideController();
        }else {
            finish();
        }
    }
}
