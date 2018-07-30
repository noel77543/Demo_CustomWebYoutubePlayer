package tw.com.sung.noel.demo_customwebyoutubeplayer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import tw.com.sung.noel.demo_customwebyoutubeplayer.util.view.CustomYoutubePlayer;

public class MainActivity extends FragmentActivity {

    private CustomYoutubePlayer customYoutubePlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customYoutubePlayer = (CustomYoutubePlayer) findViewById(R.id.custom_youtube_player);
        customYoutubePlayer.loadVideo("4ZVUmEUFwaY","#000000",true,true,false,false);
    }
}
