package com.example.avinash.listview;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;
import java.util.ArrayList;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    String[][] lv_AllSplitedData;
    ArrayList<String> lv_ArrayListFrmdb; //Store data from db with select
    ArrayList<MyListData> lv_listItems = new ArrayList<MyListData>();
    ListView lv_listView;
    MyListAdapter lv_adapter;
    Button btnadd, btnDelete;
    MainActivity lv_This = this;
    private SwipeRefreshLayout lv_swipeContainer;
    DbHandler lv_mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.quote_appName);

        lv_mydb = new DbHandler(this);
        lv_mydb.dbf_initRows();

        btnadd = (Button) findViewById(R.id.BtnAdd);
        btnadd.setOnClickListener(this);
        btnDelete = (Button) findViewById(R.id.BtnRemove);
        btnDelete.setOnClickListener(this);

        //SwipeRefersh
        lv_swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        lv_swipeContainer.setColorSchemeColors(Color.GREEN); //Change RefreshIcon color


        lv_swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lv_swipeContainer.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv_swipeContainer.setRefreshing(false);
                        printQuote();
                    }
                }, 0);
            }
        });

        lv_ArrayListFrmdb = lv_mydb.dbf_getAllRecords();
        System.out.println("list of companys store in database: " + lv_ArrayListFrmdb);//list of companys store in database
        printQuote(); //create URL, download data, store 1D array data in 2D array, display data in list.

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listV) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Are you sure to delete..??");
            System.out.println("b4 dialog");
            menu.add(Menu.NONE, 0, 0, "DELETE");

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        System.out.println("Inside onContextItemSelected2");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        lv_listItems.remove(info.position);
        lv_adapter.notifyDataSetChanged();


        lv_mydb.dbf_deletePart(lv_ArrayListFrmdb.get(info.position));
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
            alertMsg.setTitle("Add a New Company to the list");
            alertMsg.setMessage("Enter new Company name");


            final EditText NewCompNm = new EditText(this);
            alertMsg.setView(NewCompNm);

            //System.out.println("\n\nlv_ArrayListFrmdb 0--------------->>>> " + NewCompNm);

            alertMsg.setCancelable(true).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.out.println("\n\n\n\npublic void onClick1--------------->>>> " + which);
                    //System.out.println("\n\n\n\npublic void onClick--------------->>>> " + NewCompToAdd.getText().toString());

                    lv_mydb.dbf_appendPart(NewCompNm.getText().toString());

                    ArrayList<String> lv_AfterAppend_ArrLisFrmdb = lv_mydb.dbf_getAllRecords();
                    lv_ArrayListFrmdb.clear();
                    lv_ArrayListFrmdb.addAll(lv_AfterAppend_ArrLisFrmdb);
                    /*for (int i = 0; i < lv_ArrayListFrmdb.size(); i++) {

                        System.out.println("\n\nlv_ArrayListFrmdb 3--------------->>>> " + lv_ArrayListFrmdb.get(i));

                    }*/

                    printQuote();
                    lv_adapter.notifyDataSetChanged();
                }
            });

            alertMsg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            Dialog dialog = alertMsg.create();
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String cfp_downloadHttp(String url) {
        String lv_str = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL lv_url = new URL(url);
            HttpURLConnection lv_con = (HttpURLConnection) lv_url.openConnection();
            lv_str = cfp_readStream(lv_con.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            lv_str = "Connection error: " + e.toString();
        }
        return lv_str;
    }

    private String cfp_readStream(InputStream in) {
        BufferedReader lv_reader = null;
        StringBuilder lv_sb = new StringBuilder();
        try {
            lv_reader = new BufferedReader(new InputStreamReader(in));
            String nextLine = "";
            while ((nextLine = lv_reader.readLine()) != null) {
                lv_sb.append(nextLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (lv_reader != null) {
                try {
                    lv_reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return lv_sb.toString();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnAdd) {

            AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
            alertMsg.setTitle("MSG:To add new company");
            alertMsg.setMessage("To add new item click on Menu-->Setting");
            /*
            AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
            alertMsg.setTitle("You wants to add a New Company to the list...??");
            alertMsg.setTitle("Enter new Company name");

            final EditText NewCompNm = new EditText(this);
            alertMsg.setView(NewCompNm);

            //System.out.println("\n\nlv_ArrayListFrmdb 0--------------->>>> " + NewCompNm);

            alertMsg.setCancelable(true).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.out.println("\n\n\n\npublic void onClick1--------------->>>> " + which);
                    //System.out.println("\n\n\n\npublic void onClick--------------->>>> " + NewCompToAdd.getText().toString());

                    lv_mydb.dbf_appendPart(NewCompNm.getText().toString());

                    ArrayList<String> lv_AfterAppend_ArrLisFrmdb = lv_mydb.dbf_getAllRecords();
                    lv_ArrayListFrmdb.clear();
                    lv_ArrayListFrmdb.addAll(lv_AfterAppend_ArrLisFrmdb);
                    /*for (int i = 0; i < lv_ArrayListFrmdb.size(); i++) {

                        System.out.println("\n\nlv_ArrayListFrmdb 3--------------->>>> " + lv_ArrayListFrmdb.get(i));

                    }*/

        } else if (v.getId() == R.id.BtnRemove) {
            AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
            alertMsg.setTitle("MSG:To add new company");
            alertMsg.setMessage("To delete item, long press the list item");
        }
        lv_adapter.notifyDataSetChanged();
    }


    public void printQuote() {

        String StrForURL = "";

        for (int i = 0; i < lv_ArrayListFrmdb.size(); i++) {
            StrForURL += lv_ArrayListFrmdb.get(i);
            StrForURL += "+";
        }

        StrForURL = StrForURL.substring(0, StrForURL.length() - 1);
        String FinalAppendedURL = "http://download.finance.yahoo.com/d/quotes.csv?s=" + StrForURL + "&f=snbaophgkjl1t1c1p2a5b6&e=.csv";


        String lv_itemList;
        int flag = 0;
        lv_itemList = cfp_downloadHttp(FinalAppendedURL);

        /*if(lv_itemList.contains("N/A"))
        {
            flag=1;
            AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
            alertMsg.setTitle("Invalid Input");
            alertMsg.setMessage("Please enter valid company name...");
        }

        else { */

                String[] lv_AllData = lv_itemList.split("\n");
                lv_AllSplitedData = new String[lv_AllData.length][13];

                int i;

                //Replace ", Inc." with "?" and split string with ","
                for (i = 0; i < lv_AllData.length; i++) {
                        System.out.println(lv_AllData[i]);

                        lv_AllData[i] = lv_AllData[i].replace(", Inc.", "?");
                        lv_AllSplitedData[i] = lv_AllData[i].split(",");
                }

                //Replace "?" with ", Inc."  and replace "%","\"" from percent change value.
                for (i = 0; i < lv_AllData.length; i++) {
                    lv_AllSplitedData[i][1] = lv_AllSplitedData[i][1].replace("?", ", Inc.");
                    lv_AllSplitedData[i][13] = lv_AllSplitedData[i][13].replace("%", "");
                    lv_AllSplitedData[i][13] = lv_AllSplitedData[i][13].replace("\"", "");
                    System.out.println("lv_AllSplitedData " + i + lv_AllSplitedData[i]);
                }

                lv_listItems = new ArrayList<>(); //Make it empty b4 adding new elements, else it will append new data to the previous data.

                for (i = 0; i < lv_AllSplitedData.length; i++) {
                    String ComNm = lv_AllSplitedData[i][0].replace("\"", "");
                    String CompFulNm = lv_AllSplitedData[i][1].replace("\"", "");
                    String DayVal = (String.format("%.2f", Double.parseDouble(lv_AllSplitedData[i][10])));
                    String ChangeVal = (String.format("%.2f", Double.parseDouble(lv_AllSplitedData[i][12])));
                    String PercentChangeVal = (String.format("%.2f", Double.parseDouble(lv_AllSplitedData[i][13])));
                    //System.out.println("Data b4 Adding to the list "+ComNm + " " + CompFulNm + " " + DayVal + " " + ChangeVal + " " + PercentChangeVal);

                    lv_listItems.add(new MyListData(ComNm, CompFulNm, DayVal, ChangeVal, PercentChangeVal));
                }

                lv_adapter = new MyListAdapter(getApplicationContext(), lv_listItems);
                lv_listView = (ListView) findViewById(R.id.listV);
                lv_listView.setAdapter(lv_adapter);
                registerForContextMenu(lv_listView);

                lv_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                 public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                        Toast.makeText(getApplicationContext(),
                        String.format("Click ListItem ", position), Toast.LENGTH_SHORT).show();

                        Intent lv_it = new Intent(lv_This, SubActivity.class);

                        //Accept clicked data in datForIntent variable which is of MyListData type
                        MyListData datForIntent = (MyListData) parent.getItemAtPosition((int) id);
                        lv_it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        lv_it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        lv_it.putExtra("ListClickedValue", datForIntent.getlbl_aapl());
                        startActivityForResult(lv_it, 0);
                }
                });

            /*if(flag==0) //logic to remove invalid data from the listArray.
            {
                //StrForURL=StrForURL.substring(0,StrForURL.length()-1);
                lv_ArrayListFrmdb.remove(lv_ArrayListFrmdb.size()-1);
                AlertDialog.Builder alertMsg = new AlertDialog.Builder(lv_This);
                alertMsg.setTitle("Success");
                alertMsg.setMessage("List updated...");
            }
            }*/
    }

}

