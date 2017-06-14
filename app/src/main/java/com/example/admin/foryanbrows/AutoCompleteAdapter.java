package com.example.admin.foryanbrows;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import android.widget.Filter;
import android.widget.Filterable;

/**
 * Created by Admin on 14.06.2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    static {
        System.loadLibrary("native-lib");
    }
    ArrayList<String> tips;

    public AutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        tips = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return tips.size();
    }

    @Override
    public String getItem(int index) {
        return tips.get(index);
    }


    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
//
                    try {
                        String[] s  = GetTips(constraint.toString());
                        ArrayList<String> _tips = new ArrayList<>(Arrays.asList(s));
                        tips = _tips;
                    } catch (Exception e) {
//                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = tips;
                    filterResults.count = tips.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };

        return myFilter;

    }

    public native String[] GetTips(String str);

}
