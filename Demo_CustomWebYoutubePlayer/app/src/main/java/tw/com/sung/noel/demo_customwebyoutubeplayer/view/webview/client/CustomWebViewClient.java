package tw.com.sung.noel.demo_customwebyoutubeplayer.view.webview.client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import tw.com.sung.noel.demo_customwebyoutubeplayer.R;

public class CustomWebViewClient extends WebViewClient {
    private Context context;

    public CustomWebViewClient(Context context) {
        this.context = context;
    }
    //-----------

    /***
     *  需另加上 才支援https 且需 補上判斷 否則上架會遭拒 _noel
     */
    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.web_view_ssl_title));
        builder.setMessage(context.getString(R.string.web_view_ssl_message));
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
                Toast.makeText(context, context.getString(R.string.web_view_ssl_toast), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
