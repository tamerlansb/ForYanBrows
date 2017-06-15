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

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener, View.OnClickListener {

    private  WebViewTabsHelper tabs;
    private WebView current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = new WebViewTabsHelper(this,(LinearLayout) findViewById( R.id.containerWebView));
        ((Button)findViewById(R.id.goBut)).setOnClickListener(this);
        ((EditText) findViewById(R.id.autoComplete)).setOnEditorActionListener(this);

        try
        {
            ((AutoCompleteTextView) findViewById(R.id.autoComplete)).setAdapter(new AutoCompleteAdapter(this, R.layout.support_simple_spinner_dropdown_item));
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(),Toast.LENGTH_LONG).show();
            ((EditText) findViewById(R.id.autoComplete)).setText(ex.getMessage());
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        int index = data.getIntExtra("SelectedItemPosition",0);
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

    private  void  updateCurrentBindings()
    {
        current  = tabs.getCurrent();
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

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if( event != null && ( event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == '\n') ){
            // обработка нажатия Enter
            current.loadUrl("http://"+((EditText) findViewById(R.id.autoComplete)).getText().toString());
            Toast.makeText(this, "Нажата кнопка Enter", Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(this, "Smth written", Toast.LENGTH_LONG).show();
        return false;
    }

    public void onClick(View v) {
        // по id определеяем кнопку, вызвавшую этот обработчик
        if (v.getId()== R.id.goBut ) {
            current.loadUrl("http://"+((EditText) findViewById(R.id.autoComplete)).getText().toString());
        }

    }
    public void onBackPressed(){
        if (current.canGoBack()) {
            current.goBack();
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
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_tabs:
                ArrayList<String> str = new ArrayList<>();
                for (int i = 0; i < tabs.getSize(); ++i)
                {
                    str.add(tabs.getList().get(i).getUrl() );
                }
                Intent intent = new Intent(this, TabsActivity.class);
                intent.putStringArrayListExtra("tabsName", str);
                //startActivity(intent);
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
