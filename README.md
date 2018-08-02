
###用途-
基於youtube iFrame結合以WebView搭配JavaScript與控制項開發的YoutubePlayer

###環境-
Android TV

###布局-
	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:gravity="center"
	    android:layout_height="match_parent">
	
	    <tw.com.sung.noel.demo_customwebyoutubeplayer.view.CustomYoutubePlayer
	        android:id="@+id/custom_youtube_player"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent" />
	
	</LinearLayout>


###布局說明-
簡單的布局在外層為任一Layout將CustomYoutubePlayer置入其中並使之置中後,命名其ID及寬高,由於控制項是基於Player大小作揖定比例的調整,故這裡的設置將會影響控制項的大小,並不會出現空置項大於介面的情況。


###SampleCode-

	    private CustomYoutubePlayer customYoutubePlayer;
	
	    //影音
	    private final String YOUTUBE_ID = "y_cRDZXc3Hk";
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
	        paramsModel.setAutoHide(true);
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


###程式碼說明-
在Sample比較注意的有幾點:




1. customYoutubePlayer.canLoadOtherVideo(false);
   
	//是否允許顯示上一部影片 / 下一部影片的按鈕 預設為true



2. customYoutubePlayer.setControllerUsed(false);

	//是否允許顯示控制項 預設為true

3. customYoutubePlayer.loadVideo(getParams());

	//帶入自訂參數設定包含youtubeID,是否播放畫面佔滿整個CustomYoutubePlayer等...

4. ParamsModel

	//為自定義參數model, 比較常用的參數如BgColor為撥放器背景色,Fs則是是否使播放畫面佔滿整個CustomYoutubePlayer,以及youtubeID的帶入等都需要在此設定。
