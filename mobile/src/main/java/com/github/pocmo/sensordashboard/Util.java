package com.github.pocmo.sensordashboard;


import android.net.Uri;
/**
 * @author: Sangwon
 */
public class Util {

    public static String db="smoKICK";
    public static String table="Daily";
    public static int dbversion=1;

    public static String date="Date";
    public static String wasted="Wasted";
    public static String saved="Saved";
    public static String number="Number";

    public static String query="create table Daily(\n" +
            "Date text," +
            "Saved int," +
            "Wasted int," +
            "Number int" +
            ")";

    public static Uri u=Uri.parse("content://smokick.data/"+table);

}