package com.example.avinash.listview;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.avinash.listview.R.drawable.rounded_corner;
import static com.example.avinash.listview.R.drawable.rounded_cornerg;

public class MyListAdapter extends BaseAdapter
{
    private Context lv_context;
    private ArrayList<MyListData> lv_listItems;



    public MyListAdapter(Context context, ArrayList<MyListData> listItems)
    {
        this.lv_context = context;
        this.lv_listItems = listItems;

    }

    @Override
    public int getCount() {
        return lv_listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return lv_listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    lv_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.mylist, null);
        }

        TextView Malv_lbl_aapl = (TextView) convertView.findViewById(R.id.lbl_aapl);
        Malv_lbl_aapl.setText(lv_listItems.get(position).getlbl_aapl());
        Malv_lbl_aapl.setTextColor(Color.BLACK);

        TextView Malv_lbl_appinc = (TextView) convertView.findViewById(R.id.lbl_appinc);
        Malv_lbl_appinc.setText(lv_listItems.get(position).getlbl_appinc());
        Malv_lbl_appinc.setTextColor(Color.BLACK);

        TextView Malv_lbl_txtVOne = (TextView) convertView.findViewById(R.id.lbl_txtVOne);
        Malv_lbl_txtVOne.setText(lv_listItems.get(position).getlbl_txtVOne());
        //Malv_lbl_txtVOne.setTextColor(Color.BLACK);

        TextView Malv_lbl_txtVTwo = (TextView) convertView.findViewById(R.id.lbl_txtVTwo);
        Malv_lbl_txtVTwo.setText(lv_listItems.get(position).getlbl_txtVTwo());
        //Malv_lbl_txtVTwo.setTextColor(Color.BLACK);

        String change=lv_listItems.get(position).getlbl_txtVTwo();
        System.out.println("change-------------------->>>>>"+change);
        change=change.replace("%", "");
        System.out.println("change.replace-------------------->>>>>"+change);
        //change
        Double lv_change=Double.parseDouble(change);
        System.out.println("lv_change-------------------->>>>>"+lv_change);

        if(lv_change>=0.00) {
            TableLayout lv_tblLayout1 = (TableLayout) convertView.findViewById(R.id.tblLayout1);
            lv_tblLayout1.setBackground(lv_context.getDrawable(R.drawable.rounded_cornerg));
        }

        else {
            TableLayout lv_tblLayout1 = (TableLayout) convertView.findViewById(R.id.tblLayout1);
            lv_tblLayout1.setBackground(lv_context.getDrawable(R.drawable.rounded_corner));

        }


        TextView Malv_lbl_txtVThree = (TextView) convertView.findViewById(R.id.lbl_txtVThree);
        Malv_lbl_txtVThree.setText(lv_listItems.get(position).getlbl_txtVThree());

        return convertView;
    }


}