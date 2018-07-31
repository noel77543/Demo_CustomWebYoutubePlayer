package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.client;

import android.content.Context;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;


public class CustomWebChromeClient extends WebChromeClient {

    private View loadingView;
    private ProgressBar progressBar;
    private Context context;
    private onProgressChangeListener onProgressChangeListener;
    //-----------

    public CustomWebChromeClient(Context context) {
        this.context = context;
    }
    //-----------
//
//    @Override
//    public View getVideoLoadingProgressView() {
//        if (loadingView == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            loadingView = inflater.inflate(R.layout.view_loading, null);
//            progressBar = loadingView.findViewById(R.id.progress_bar);
//        }
//        return loadingView;
//    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        onProgressChangeListener.onProgressChanged(newProgress);
    }

    //---------
    public interface onProgressChangeListener {
        void onProgressChanged(int newProgress);
        }

    public void setOnProgressChangeListener(onProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }
}
