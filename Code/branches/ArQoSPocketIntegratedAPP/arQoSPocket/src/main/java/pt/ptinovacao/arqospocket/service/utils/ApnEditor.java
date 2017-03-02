package pt.ptinovacao.arqospocket.service.utils;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

/**
 * Created by tomasrodrigues on 11-01-2017.
 */

public class ApnEditor {

    public static String TAG="ApnEditor";

    /* 
     * Information of all APNs
     * Details can be found in com.android.providers.telephony.TelephonyProvider
     */
    public static final Uri APN_TABLE_URI =
            Uri.parse("content://telephony/carriers");
    /* 
     * Information of the preferred APN
     * 
     */
    public static final Uri PREFERRED_APN_URI =
            Uri.parse("content://telephony/carriers/preferapn");  
    /*
     * Enumerate all APN data
     */
    public void EnumerateAPNs(Context context)
    {
        Cursor c = context.getContentResolver().query(
                APN_TABLE_URI, null, null, null, null);
        if (c != null)
        {
            /*
             *  Fields you can retrieve can be found in
                com.android.providers.telephony.TelephonyProvider :
                
                db.execSQL("CREATE TABLE " + CARRIERS_TABLE +
                "(_id INTEGER PRIMARY KEY," +
                "name TEXT," +
                "numeric TEXT," +
                "mcc TEXT," +
                "mnc TEXT," +
                "apn TEXT," +
                "user TEXT," +
                "server TEXT," +
                "password TEXT," +
                "proxy TEXT," +
                "port TEXT," +
                "mmsproxy TEXT," +
                "mmsport TEXT," +
                "mmsc TEXT," +
                "type TEXT," +
                "current INTEGER);");
             */

            String s = "All APNs:\n";
            Log.d(TAG, s);
            try
            {
                s += printAllData(c); //Print the entire result set
            }
            catch(SQLException e)
            {
                Log.d(TAG, e.getMessage());
            }

            //Log.d(TAG, s + "\n\n");
            c.close();
        }

    }

    /*
      * Insert a new APN entry into the system APN table
      * Require an apn name, and the apn address. More can be added.
      * Return an id (_id) that is automatically generated for the new apn entry.
      */
    public int InsertAPN(Context context, String name, String apn_addr)
    {
        int id = -1;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", "teste");
        values.put("apn", "teste.pt");
        
        /*
         * The following three field values are for testing in Android emulator only
         * The APN setting page UI will ONLY display APNs whose ‘numeric’ filed is 
         * TelephonyProperties.PROPERTY_SIM_OPERATOR_NUMERIC.
         * On Android emulator, this value is 310260, where 310 is mcc, and 260 mnc.
         * With these field values, the newly added apn will appear in system UI.
         */
        values.put("mcc", "310");
        values.put("mnc", "260");
        values.put("numeric", "310260");

        Cursor c = null;
        try
        {
            Uri newRow = resolver.insert(APN_TABLE_URI, values);
            if(newRow != null)
            {
                c = resolver.query(newRow, null, null, null, null);
                Log.d(TAG, "Newly added APN:");
                printAllData(c); //Print the entire result set

                // Obtain the apn id
                int idindex = c.getColumnIndex("_id");
                c.moveToFirst();
                id = c.getShort(idindex);
                Log.d(TAG, "New ID: " + id + ": Inserting new APN succeeded!");
            }
        }
        catch(SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }

        if(c !=null )
            c.close();
        return id;
    }
   /*
     * Set an apn to be the default apn for web traffic
     * Require an input of the apn id to be set
     */
    public boolean SetDefaultAPN(Context context, int id)
    {
        boolean res = false;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        //See /etc/apns-conf.xml. The TelephonyProvider uses this file to provide 
        //content://telephony/carriers/preferapn URI mapping
        values.put("apn_id", id);
        try
        {
            resolver.update(PREFERRED_APN_URI, values, null, null);
            Cursor c = resolver.query(
                    PREFERRED_APN_URI,
                    new String[]{"mms tmn","mmsc.tmn.pt"},
            "_id="+id,
                null,
                null);
            if(c != null)
            {
                res = true;
                c.close();
            }
        }
        catch (SQLException e)
        {
            Log.d(TAG, e.getMessage());
        }
        return res;
    }

    /*
     * Return all column names stored in the string array
     */
    private String getAllColumnNames(String[] columnNames)
    {
        String s = "Column Names:\n";
        for(String t:columnNames)
        {
            s += t + ":\t";
        }
        return s+"\n";
    }

    /*
     *  Print all data records associated with Cursor c.
     *  Return a string that contains all record data.
     *  For some weird reason, Android SDK Log class cannot print very long string message.
     *  Thus we have to log record-by-record.
     */
    private String printAllData(Cursor c)
    {
        if(c == null) return null;
        String s = "";
        int record_cnt = c.getColumnCount();
        Log.d(TAG, "Total # of records: " + record_cnt);

        if(c.moveToFirst())
        {
            String[] columnNames = c.getColumnNames();
            Log.d(TAG,getAllColumnNames(columnNames));
            s += getAllColumnNames(columnNames);
            do{
                String row = "";
                for(String columnIndex:columnNames)
                {
                    int i = c.getColumnIndex(columnIndex);
                    row += c.getString(i)+":\t";
                }
                row += "\n";
                Log.d(TAG, row);
                s += row;
            }while(c.moveToNext());
            Log.d(TAG,"End Of Records");
        }
        return s;
    }

}
