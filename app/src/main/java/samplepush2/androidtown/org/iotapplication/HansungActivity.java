package samplepush2.androidtown.org.iotapplication;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HansungActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hansung);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();

        webView=(WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // 웹뷰에서 자바스크립트를 허용하기 위해 설정
        webView.loadUrl("http://hansung.ac.kr"); // 이동할 URL 지정
        webView.setWebViewClient(new WebViewClientClass()); //웹뷰 클라이언트를 지정하는 부분
        webView.setVerticalScrollBarEnabled(true); // 세로 스크롤 설정
    }
    private class WebViewClientClass extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    //웹브라우저 뒤로가기 구현
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
