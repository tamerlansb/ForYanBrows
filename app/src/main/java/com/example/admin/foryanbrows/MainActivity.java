package com.example.admin.foryanbrows;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://www.ya.ru");
        ((Button)findViewById(R.id.goBut)).setOnClickListener(this);
        ((EditText) findViewById(R.id.autoComplete)).setOnEditorActionListener(this);
        ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedHint = (String) adapterView.getItemAtPosition(position);
                try
                {
                    URL url = new URL("http://" + selectedHint);
                    url.toURI()
                    ((WebView) findViewById(R.id.webView)).loadUrl(url.toString());
                }
                catch (MalformedURLException e) {
                    String url = "https://yandex.ru/search/?text=" + selectedHint.replace(' ','+');
                    ((WebView) findViewById(R.id.webView)).loadUrl(url.toString());
                }
                // убрать фокус
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow( ((AutoCompleteTextView) findViewById(R.id.autoComplete)).getWindowToken(), 0);
                ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setText(selectedHint);
            }
        });

        try {
            //DelayAutoCompleteTextView requestField =  (DelayAutoCompleteTextView) findViewById(R.id.autoComplete);
            //requestField.setThreshold(1);
            ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setAdapter(new AutoCompleteAdapter(this, R.layout.support_simple_spinner_dropdown_item));
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
            ((EditText) findViewById(R.id.autoComplete)).setText(ex.getMessage());
        }

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if( event != null && ( event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == '\n') ){
            // обработка нажатия Enter
            ((WebView) findViewById(R.id.webView)).loadUrl("http://"+((EditText) findViewById(R.id.autoComplete)).getText().toString());
            Toast.makeText(this, "Нажата кнопка Enter", Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(this, "Smth written", Toast.LENGTH_LONG).show();
        return false;
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        if (v.getId()== R.id.goBut ) {
            ((WebView) findViewById(R.id.webView)).loadUrl("http://"+((EditText) findViewById(R.id.autoComplete)).getText().toString());
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
;
}
