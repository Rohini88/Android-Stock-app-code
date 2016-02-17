package com.example.avinash.listview;


import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.TableLayout;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;


import static com.example.avinash.listview.R.id.SRefreshBtn;


public class SubActivity  extends AppCompatActivity implements ImageButton.OnClickListener {

    ImageButton Slv_btnRefresh,Slv_btnStop,Slv_btnStart;
    TextView Slv_lbl_aapl,Slv_lbl_appinc,Slv_txtVBid,Slv_TxtVAsk,Slv_TxtVOpen,Slv_TxtVPreClose,Slv_TxtVHigh,Slv_TxtVLow,Slv_TxtVWHigh,Slv_TxtVWLow,Slv_lbl_txtVOne,Slv_lbl_clock,Slv_lbl_txtVTwo,Slv_lbl_txtVThree;
    private SwipeRefreshLayout Slv_swipeContainer;
    Timer cy_timer;
    MyTimerTask cv_myTimerTask;
    String lv_dataStr;
    String dataFromIntent;
    String lv_url="";


    SubGraphView lv_myCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");

        //Canvas Activity
        lv_myCanvas=(SubGraphView) findViewById(R.id.s_canvas);
        lv_myCanvas.setBackgroundColor(Color.WHITE);
        lv_myCanvas.cp_toggleFlag();

        /*
        //To access with position
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lv_temp = (extras.getInt("id"));
        }
        System.out.println("position u clicked in Sub" + lv_temp);
        */


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            dataFromIntent = (extras.getString("ListClickedValue"));
        }


        Slv_btnRefresh=(ImageButton) findViewById(SRefreshBtn);
        Slv_btnRefresh.setOnClickListener(this);

        Slv_btnStop = (ImageButton) findViewById(R.id.SStopBtn);
        Slv_btnStop.setOnClickListener(this);

        Slv_btnStart = (ImageButton) findViewById(R.id.SStartBtn);
        Slv_btnStart.setOnClickListener(this);

        Slv_lbl_aapl = (TextView) findViewById(R.id.Slbl_aapl);
        Slv_lbl_appinc = (TextView) findViewById(R.id.Slbl_appinc);
        Slv_txtVBid = (TextView) findViewById(R.id.StxtVBid);
        Slv_TxtVAsk = (TextView) findViewById(R.id.STxtVAsk);
        Slv_TxtVOpen = (TextView) findViewById(R.id.STxtVOpen);
        Slv_TxtVPreClose = (TextView) findViewById(R.id.STxtVPreClose);
        Slv_TxtVHigh = (TextView) findViewById(R.id.STxtVHigh);
        Slv_TxtVLow = (TextView) findViewById(R.id.STxtVLow);
        Slv_TxtVWHigh = (TextView) findViewById(R.id.STxtVWHigh);
        Slv_TxtVWLow = (TextView) findViewById(R.id.STxtVWLow);
        Slv_lbl_txtVOne = (TextView) findViewById(R.id.Slbl_txtVOne);
        Slv_lbl_clock = (TextView) findViewById(R.id.Slbl_clock);
        Slv_lbl_txtVTwo = (TextView) findViewById(R.id.Slbl_txtVTwo);
        Slv_lbl_txtVThree = (TextView) findViewById(R.id.Slbl_txtVThree);

        SprintQuote();


    }

        public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            //lv_myCanvas.clearCanvas();
            return true;
        }

        else if (id == android.R.id.home)
        {
            cfp_navBack();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        cfp_navBack();
    }

    private void cfp_navBack (){
        // work for passing no data back
        // won't work for sending from child
        Intent lv_it = new Intent(this, MainActivity.class);
        startActivity(lv_it);

        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.SRefreshBtn)
        {
            Slv_btnStop.setEnabled(false);
            SprintQuote();
        }
        else if(v.getId()==R.id.SStartBtn)
        {
            Slv_btnRefresh.setEnabled(false);
            Slv_btnStop.setEnabled(true);

            if(cy_timer!=null)
            {
                cy_timer.cancel();
            }

            cy_timer=new Timer();
            cv_myTimerTask=new MyTimerTask();
            cy_timer.schedule(cv_myTimerTask,0,1000);
        }
        else if(v.getId()==R.id.SStopBtn)
        {
            if(cy_timer!=null)
            {
                cy_timer.cancel();
            }
        }

    }

    private String cfp_downloadHttp(String url)
    {
        String lv_str = "";

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try
        {
            URL lv_url = new URL(url);
            HttpURLConnection lv_con = (HttpURLConnection) lv_url.openConnection();
            lv_str = cfp_readStream(lv_con.getInputStream());
        }

        catch (Exception e)
        {
            e.printStackTrace();
            lv_str = "Connection error: " + e.toString();
        }

        return lv_str;
    }

    private String cfp_readStream(InputStream in)
    {
        BufferedReader lv_reader = null;
        StringBuilder lv_sb = new StringBuilder();
        try
        {
            lv_reader = new BufferedReader(new InputStreamReader(in));
            String nextLine ="";
            while ((nextLine = lv_reader.readLine()) != null)
            {
                lv_sb.append(nextLine+"\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        finally
        {
            if (lv_reader != null)
            {
                try
                {
                    lv_reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return lv_sb.toString();
        }
    }



    class MyTimerTask extends TimerTask
    {
        @Override
        public void run()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    SprintQuote();

                }
            });
        }
    }

    public void SprintQuote()
    {
        String [] lv_SplitData;
        lv_url="http://download.finance.yahoo.com/d/quotes.csv?s="+dataFromIntent+"&f=snbaophgkjl1t1c1p2a5b6&e=.csv";

        lv_dataStr = cfp_downloadHttp(lv_url);
        System.out.println("Inside Sprint: " + lv_dataStr);
        lv_dataStr=lv_dataStr.replace(", Inc.", "?");

        lv_SplitData=lv_dataStr.split(",");
        lv_SplitData[1]=lv_SplitData[1].replace("?", ", Inc.");

        // "+0.44%" --> 0.44
        lv_SplitData[13]=lv_SplitData[13].replace("%", "");
        lv_SplitData[13]=lv_SplitData[13].replace("\"", "");

        printValue(lv_SplitData);
    }

   public void printValue(String [] quoteParse)
    {
        /*System.out.println("Inside print 0: " + quoteParse[0].replace("\"", ""));
        System.out.println("Inside print 1: "+quoteParse[1]);
        System.out.println("Inside print 2: "+quoteParse[2]);
        System.out.println("Inside print 3: "+quoteParse[3]);
        System.out.println("Inside print 4: " + quoteParse[4]);
        System.out.println("Inside print 5: " + quoteParse[5]);
        System.out.println("Inside print 6: " + quoteParse[6]);*/


        Slv_lbl_aapl.setText(quoteParse[0].replace("\"", ""));
        Slv_lbl_appinc.setText(quoteParse[1].replace("\"", ""));

        Slv_txtVBid.setText(String.format("%.2f",Double.parseDouble(quoteParse[2]))+" x "+(quoteParse[14]));

        Slv_TxtVAsk.setText(String.format("%.2f",Double.parseDouble(quoteParse[3])) + " x " +(quoteParse[15]));

        Slv_TxtVOpen.setText(String.format("%.2f",Double.parseDouble(quoteParse[4])));

        Slv_TxtVPreClose.setText(String.format("%.2f",Double.parseDouble(quoteParse[5])));

        Slv_TxtVHigh.setText(String.format("%.2f",Double.parseDouble(quoteParse[6])));

        Slv_TxtVLow.setText(String.format("%.2f",Double.parseDouble(quoteParse[7])));

        Slv_TxtVWHigh.setText(String.format("%.2f", Double.parseDouble(quoteParse[8])));

        Slv_TxtVWLow.setText(String.format("%.2f", Double.parseDouble(quoteParse[9])));

        Slv_lbl_txtVOne.setText(String.format("%.2f", Double.parseDouble(quoteParse[10])));

        Slv_lbl_clock.setText(quoteParse[11].replace("\"", ""));


        String s1,s2="";
        quoteParse[12]=String.format("%.2f", Double.parseDouble(quoteParse[12]));
        Double lv_change=Double.parseDouble(quoteParse[12]);
        if(lv_change>=0.0)
        {
            s1="+"+lv_change;
        }
        else
        {
            s1=""+lv_change;
        }

        if(lv_change>=0.00) {
            TableLayout lv_tblLayout2 = (TableLayout)findViewById(R.id.tblLayout2);
            lv_tblLayout2.setBackgroundResource(R.drawable.rounded_cornerg);
        }
        else {
            TableLayout lv_tblLayout2 = (TableLayout) findViewById(R.id.tblLayout2);
            lv_tblLayout2.setBackgroundResource(R.drawable.rounded_corner);}
        Slv_lbl_txtVTwo.setText(s1);


        quoteParse[13]=String.format("%.2f", Double.parseDouble(quoteParse[13]));
        Double lv_Perchange=Double.parseDouble(quoteParse[13]);
        if(lv_Perchange>=0.0)
        {
            s2="+"+lv_Perchange+"%";
        }
        else
        {
            s2=""+lv_Perchange+"%";
        }
        Slv_lbl_txtVThree.setText(s2);
   }
}


