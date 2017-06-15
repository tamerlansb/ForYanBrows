package com.example.admin.foryanbrows;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {

    private  WebViewTabsHelper tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = new WebViewTabsHelper(this,(LinearLayout) findViewById( R.id.containerWebView));
        ((Button)findViewById(R.id.goBut)).setOnClickListener(this);
        ((EditText) findViewById(R.id.autoComplete)).setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean consumed = false;
                if (keyCode == KEYCODE_ENTER) {
                    String field =  ((AutoCompleteTextView) findViewById(R.id.autoComplete)).getText().toString();
                    try {
                        URL url = new URL("http://" + field);
                        tabs.getCurrent().loadUrl(url.toString());
                    } catch (MalformedURLException e) {
                        String url = "https://yandex.ru/search/?text=" + field.replace(' ', '+');
                        tabs.getCurrent().loadUrl(url);
                    }
                    consumed = true;
                }
                return consumed;
            }
        });
        updateCurrentBindings();

        try
        {
            ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setAdapter(new AutoCompleteAdapter(this, R.layout.support_simple_spinner_dropdown_item));
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        int index = data.getIntExtra("SelectedItemPosition",0);
        ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setText("");
        if (index == -1)
        {
            WebView w = tabs.addNewWebView();
            tabs.setCurrent(tabs.getIndexWebView(w));
        }
        else {
            tabs.setCurrent(index);
        }
        this.updateCurrentBindings();
    }

    private void updateCurrentBindings()
    {
        ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedHint = (String) adapterView.getItemAtPosition(position);
                try {
                    URL url = new URL("http://" + selectedHint);
                    //((WebView) findViewById(R.id.webView)).loadUrl(url.toString());
                    // так как не бросает Exception пока так
                    String urlSearch = "https://yandex.ru/search/?text=" + selectedHint.replace(' ', '+');
                    tabs.getCurrent().loadUrl(urlSearch);
                }
                catch (MalformedURLException e) {
                    String url = "https://yandex.ru/search/?text=" + selectedHint.replace(' ', '+');
                    tabs.getCurrent().loadUrl(url);
                }
                // убрать фокус
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(((AutoCompleteTextView) findViewById(R.id.autoComplete)).getWindowToken(), 0);
                ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setText(selectedHint);
            }
        });
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        if (v.getId()== R.id.goBut ) {
            tabs.getCurrent().loadUrl("http://"+((EditText) findViewById(R.id.autoComplete)).getText().toString());
        }

    }
    public void onBackPressed(){
        if ( tabs.getCurrent().canGoBack()) {
            tabs.getCurrent().goBack();
        }
        else  {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_tabs:
                ArrayList<String> str = new ArrayList<>();
                for (int i = 0; i < tabs.getSize(); ++i)
                {
                    str.add(tabs.getList().get(i).getUrl() );
                }
                Intent intent = new Intent(this, TabsActivity.class);
                intent.putStringArrayListExtra("tabsName", str);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete:
                tabs.deleteWebView(tabs.getCurrentIndex());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
