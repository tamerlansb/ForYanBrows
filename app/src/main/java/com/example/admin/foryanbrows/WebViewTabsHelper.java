package com.example.admin.foryanbrows;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class WebViewTabsHelper {
    private ArrayList<WebView> tabs;
    private int current;
    private LinearLayout contaner;
    private Context context;

    public WebViewTabsHelper(Context context, LinearLayout linearLayout)
    {
        contaner = linearLayout;
        this.context = context;
        tabs = new ArrayList<WebView>();
        addNewWebView();
        current = 0
        getCurrent().setVisibility(View.VISIBLE);
    }

    public WebView getCurrent()
    {
        return tabs.get(current);
    }

    public int getCurrentIndex()
    {
        return current;
    }

    public  int getIndexWebView(WebView w)
    {
        return tabs.indexOf(w);
    }

    public boolean setCurrent(int index)
    {
        if (index < getSize() && index > -1)
        {
            if (current!=index) {
                getCurrent().setVisibility(View.GONE);
                current = index;
                getCurrent().setVisibility(View.VISIBLE);
            }
            return true;
        }
        return  false;
    }

    public int getSize()
    {
        return  tabs.size();
    }

    public WebView addNewWebView()
    {
        WebView webView = new WebView(context);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://www.ya.ru");
        webView.setVisibility(View.GONE);
        ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contaner.addView(webView, lpView);
        tabs.add(webView);
        return webView;
    }

    public void deleteWebView(int index)
    {
        tabs.remove(index);
        contaner.removeAllViews();
        if (index != current)
        {
            for (int i = 0; i < getSize(); ++ i)
            {
                contaner.addView(tabs.get(i));
            }
        }
        else if (getSize()==0)
        {
            addNewWebView();
            current = 0;
            getCurrent().setVisibility(View.VISIBLE);
        }
        else
        {
            for (int i = 0; i < getSize(); ++ i)
            {
                contaner.addView(tabs.get(i));
            }
            if (current > getSize())
            {
                current--;
            }
            getCurrent().setVisibility(View.VISIBLE);
        }
    }

    public  ArrayList<WebView> getList()
    {
        return  tabs;
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }
}


