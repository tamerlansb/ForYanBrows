package com.example.admin.foryanbrows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://www.ya.ru");
        ((Button)findViewById(R.id.goBut)).setOnClickListener(this);
        ((EditText) findViewById(R.id.editText)).setOnEditorActionListener(this);;
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if( event != null && ( event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == '\n') ){
            // обработка нажатия Enter
            ((WebView) findViewById(R.id.webView)).loadUrl("http://"+((EditText) findViewById(R.id.editText)).getText().toString());
            Toast.makeText(this, "Нажата кнопка Enter", Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(this, "Smth written", Toast.LENGTH_LONG).show();
        return false;
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        if (v.getId()== R.id.goBut ) {
            //Toast.makeText(this, ((EditText) findViewById(R.id.editText)).getText(), Toast.LENGTH_LONG).show();
            //Toast.makeText(this, stringFromJNI(((EditText) findViewById(R.id.editText)).getText().toString()), Toast.LENGTH_LONG).show();
            String[] request = GetTips(((EditText) findViewById(R.id.editText)).getText().toString());
            String conslole = "";
            for (String str:request) {
                conslole += str+" ";
            }
            Toast.makeText(this, conslole, Toast.LENGTH_LONG).show();
        }
    }
    public void onBackPressed(){
        if (((WebView) findViewById(R.id.webView)).canGoBack()) {
            ((WebView) findViewById(R.id.webView)).goBack();
        }
        else  {
            this.finish();
        }
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String[] GetTips(String str);
}
