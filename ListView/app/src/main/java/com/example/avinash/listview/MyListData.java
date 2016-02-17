package com.example.avinash.listview;

/**
 * Created by rshinde on 10/5/15.
 */
public class MyListData
{
    private String Myl_lv_lbl_Nm,Myl_lv_lbl_inc;
    private String Myl_lv_lbl_txtVOne,Myl_lv_lbl_txtVTwo,Myl_lv_lbl_txtVThree;


    public MyListData()
    {
    }

    public MyListData(String data_lbl_aapl, String data_lv_lbl_appinc,String data_lbl_txtVOne, String data_lbl_txtVTwo,String data_lbl_txtVThree )
    {
        String s1,s2="";

        Double lv_change=Double.parseDouble(data_lbl_txtVTwo);
        System.out.println("lv_change inside MyListData-------------------->>>>>"+lv_change);
        if(lv_change>=0.0)
        {
            s1="+"+lv_change;
            System.out.println("if--s1 inside MyListData-------------------->>>>>"+s1);
        }
        else
        {
            s1=""+lv_change;
            System.out.println("Else--s1 inside MyListData-------------------->>>>>"+s1);
        }
        Double lv_Perchange=Double.parseDouble(data_lbl_txtVThree);

        if(lv_Perchange>=0.0)
        {
            s2="+"+lv_Perchange+"%";
        }
        else
        {
            s2=""+lv_Perchange+"%";
        }

        this.Myl_lv_lbl_Nm = data_lbl_aapl;
        this.Myl_lv_lbl_inc =data_lv_lbl_appinc;
        this.Myl_lv_lbl_txtVOne=data_lbl_txtVOne;
        this.Myl_lv_lbl_txtVTwo=s1;
        this.Myl_lv_lbl_txtVThree=s2;
    }

    public String getlbl_aapl(){
        return this.Myl_lv_lbl_Nm;
    }

    public String getlbl_appinc(){
        return this.Myl_lv_lbl_inc;
    }

    public String getlbl_txtVOne(){
        return this.Myl_lv_lbl_txtVOne;
    }

    public String getlbl_txtVTwo(){
        return this.Myl_lv_lbl_txtVTwo;
    }

    public String getlbl_txtVThree(){
        return this.Myl_lv_lbl_txtVThree;
    }

    public void setlbl_aapl(String data_lbl_aapl){
        this.Myl_lv_lbl_Nm=data_lbl_aapl;
    }

    public void setlbl_appinc(String data_lv_lbl_appinc){
        this.Myl_lv_lbl_inc=data_lv_lbl_appinc;
    }

    public void setlbl_txtVOne(String data_lbl_txtVOne){
        this.Myl_lv_lbl_txtVOne=data_lbl_txtVOne;
    }

    public void setlbl_txtVTwo(String data_lbl_txtVTwo){

        String s;
        s = String.format("%.2f", Double.parseDouble(data_lbl_txtVTwo));
        this.Myl_lv_lbl_txtVTwo=s;
    }

    public void setlbl_txtVThree(String data_lbl_txtVThree){
        String s="";

        s = data_lbl_txtVThree;
        Myl_lv_lbl_txtVThree=s;
    }

}
