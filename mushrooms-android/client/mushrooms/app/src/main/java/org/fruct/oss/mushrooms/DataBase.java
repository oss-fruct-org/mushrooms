package org.fruct.oss.mushrooms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catalog.db";
    private static /*final*/ int DATABASE_VERSION = 1;
    private static /*final*/ int DATABASE_VERSION_NOW = 1;

    /*Таблица грибов*/
    public static final String TABLE_MUSHROOMS = "mushrooms";
    public static final String MUSHROOMS_UID = "_id";
    public static final String MUSHROOMS_NAME_RU = "nameRU";
    public static final String MUSHROOMS_NAME_EN = "nameEN";
    public static final String MUSHROOMS_DESCRIPTION_RU = "descriptionRU";
    public static final String MUSHROOMS_DESCRIPTION_EN = "descriptionEN";
    public static final String MUSHROOMS_CATEGORY = "category";
    public static final String MUSHROOMS_IMAGE = "image";

    /*Таблица ягод*/
    public static final String TABLE_BERRIES = "berries";
    public static final String BERRIES_UID = "_id";
    public static final String BERRIES_NAME_RU = "nameRU";
    public static final String BERRIES_NAME_EN = "nameEN";
    public static final String BERRIES_DESCRIPTION_RU = "descriptionRU";
    public static final String BERRIES_DESCRIPTION_EN = "descriptionEN";
    public static final String BERRIES_CATEGORY = "category";
    public static final String BERRIES_IMAGE = "image";

    /*Таблица рецептов*/
    public static final String TABLE_RECIPES = "recipes";
    public static final String RECIPES_UID = "_id";
    public static final String RECIPES_NAME_RU = "nameRU";
    public static final String RECIPES_NAME_EN = "nameEN";
    public static final String RECIPES_DESCRIPTION_RU = "descriptionRU";
    public static final String RECIPES_DESCRIPTION_EN = "descriptionEN";
    public static final String RECIPES_TYPE = "type";
    public static final String RECIPES_CATEGORY = "category";
    public static final String RECIPES_IMAGE = "image";


    private static final String SQL_CREATE_TABLE_MUSHROOMS = "CREATE TABLE "
            + TABLE_MUSHROOMS
            + " (" + MUSHROOMS_UID + " INTEGER PRIMARY KEY,"
            + MUSHROOMS_NAME_RU + " VARCHAR(255),"
            + MUSHROOMS_NAME_EN + " VARCHAR(255),"
            + MUSHROOMS_DESCRIPTION_RU + " TEXT,"
            + MUSHROOMS_DESCRIPTION_EN + " TEXT,"
            + MUSHROOMS_CATEGORY + " VARCHAR(255),"
            + MUSHROOMS_IMAGE + " BLOB );";

    private static final String SQL_CREATE_TABLE_BERRIES = "CREATE TABLE "
            + TABLE_BERRIES + " ("
            + BERRIES_UID + " INTEGER PRIMARY KEY,"
            + BERRIES_NAME_RU + " VARCHAR(255),"
            + BERRIES_NAME_EN + " VARCHAR(255),"
            + BERRIES_DESCRIPTION_RU + " TEXT,"
            + BERRIES_DESCRIPTION_EN + " TEXT,"
            + BERRIES_CATEGORY + " VARCHAR(255),"
            + BERRIES_IMAGE + " BLOB );";

    private static final String SQL_CREATE_TABLE_RECIPES = "CREATE TABLE "
            + TABLE_RECIPES + " ("
            + RECIPES_UID + " INTEGER PRIMARY KEY,"
            + RECIPES_NAME_RU + " VARCHAR(255),"
            + RECIPES_NAME_EN + " VARCHAR(255),"
            + RECIPES_DESCRIPTION_RU + " TEXT,"
            + RECIPES_DESCRIPTION_EN + " TEXT,"
            + RECIPES_TYPE+ " VARCHAR(255),"
            + RECIPES_CATEGORY + " VARCHAR(255),"
            + RECIPES_IMAGE + " BLOB );";

    Context context;

    SQLiteDatabase dbUse;
    ProgressDialog mProgressDialog;


    public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
         this.context = context;
        Log.d("TAG", "konstructor " + DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("TAG", " onOpen " + db.getVersion() + " | " + DATABASE_VERSION + " | " + DATABASE_VERSION_NOW);
        if (DATABASE_VERSION_NOW > DATABASE_VERSION) {
            db.setVersion(DATABASE_VERSION_NOW);
            DATABASE_VERSION = DATABASE_VERSION_NOW;
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*Запросы создания таблиц*/
        db.execSQL(SQL_CREATE_TABLE_MUSHROOMS);
        db.execSQL(SQL_CREATE_TABLE_BERRIES);
        db.execSQL(SQL_CREATE_TABLE_RECIPES);

        /*Добавляем в базу*/
        insetInDb(db, context.getResources().getXml(R.xml.startdb));

        /* обновление*/

        dbUse = db;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Загрузка обновления");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(context);
        downloadTask.execute("http://gets.cs.petrsu.ru/mushrooms/?version=" + DataBase.getVersiion());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TAG","UPDATE " + db.getVersion() + " | " + oldVersion + " | " + newVersion);
       // db.setVersion(newVersion);
        DATABASE_VERSION = newVersion;
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.d("TAG", "DOWNGRATE " + db.getVersion() + " | " + oldVersion + " | " + newVersion);
        db.setVersion(oldVersion);
        DATABASE_VERSION = oldVersion;
        DATABASE_VERSION_NOW = oldVersion;
    }

    public static int getVersiion(){
        return DATABASE_VERSION;
    }

    public static boolean insetInDb(SQLiteDatabase db, XmlPullParser xpp){

        String tag = "";
        ContentValues cv = new ContentValues();
        int add = 0;
        String baseTable = null;
        String id = null;

        try {

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("TAG", "START_DOCUMENT");

                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Log.d("TAG", "START_TAG " + xpp.getDepth());

                        tag = xpp.getName();

                        if(xpp.getDepth() == 1 ) {

                            if(getVersiion() >= Integer.parseInt(xpp.getAttributeValue(null, "version"))){
                                return false;
                            }
                        Log.d("TAG","Ver: " + db.getVersion() + "|" + Integer.parseInt(xpp.getAttributeValue(null, "version")) + getVersiion());
                            if(getVersiion() == Integer.parseInt(xpp.getAttributeValue(null, "version"))){
                                return false;
                            }else {
                                DATABASE_VERSION = Integer.parseInt(xpp.getAttributeValue(null, "version"));
                                db.setVersion(Integer.parseInt(xpp.getAttributeValue(null, "version")));
                                Log.d("TAG", Integer.toString(db.getVersion()));
                                Log.d("TAG","Version "+Integer.parseInt(xpp.getAttributeValue(null, "version")));
                            }
                        }

                        if(xpp.getDepth() == 2 ){

                            baseTable = null;

                            if( xpp.getAttributeValue(null, "type").compareTo("m") == 0) {
                                baseTable = DataBase.TABLE_MUSHROOMS;
                            }

                            if( xpp.getAttributeValue(null, "type").compareTo("b") == 0) {
                                baseTable = DataBase.TABLE_BERRIES;
                            }

                            if( xpp.getAttributeValue(null, "type").compareTo("r") == 0) {
                                baseTable = DataBase.TABLE_RECIPES;
                            }

                            cv = new ContentValues();
                            if( xpp.getAttributeValue(null, "ex").compareTo("c") == 0) {
                                add = 0;
                            }

                            if( xpp.getAttributeValue(null, "ex").compareTo("d") == 0) {
                                add = 1;
                            }

                            if( xpp.getAttributeValue(null, "ex").compareTo("u") == 0) {
                                add = 2;
                                id = xpp.getAttributeValue(1).substring(1);
                            }
                        }



                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        Log.d("TAG", "END_TAG: name = " + xpp.getName());



                       /* if (xpp.getName().compareTo("mushroom") == 0) {

                            baseTable = DataBase.TABLE_MUSHROOMS;
                        }

                        if (xpp.getName().compareTo("berrie") == 0) {

                            baseTable = DataBase.TABLE_BERRIES;
                        }

                        if (xpp.getName().compareTo("recipe") == 0) {

                            baseTable = DataBase.TABLE_RECIPES;
                        }*/

                        if (xpp.getName().compareTo("element") == 0) {
                            if (add == 0) {

                                db.insert(baseTable, null, cv);
                                add = 0;
                                break;
                            }

                            if (add == 1) {

                                db.delete(baseTable, tag + " = ?", new String[]{xpp.getText()});
                                add = 0;
                                break;
                            }

                            if (add == 2) {

                                db.update(baseTable, cv, "id = ?", new String[]{id});
                                add = 0;
                                break;
                            }
                        }

                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        Log.d("TAG", "text = " + xpp.getText() + " tag: " + tag + " dep: " + xpp.getDepth());

                        //if(add == 0) {
                            if(xpp.getDepth() == 3 ) {
                                if (tag.compareTo("image") == 0) {

                                    cv.put(tag, Base64.decode(xpp.getText(), Base64.NO_WRAP));
                                } else {

                                    cv.put(tag, xpp.getText());
                                }
                            }

                        //}


                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            Log.d("TAG", "END_DOCUMENT");

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Log.d("TAG", "!!!ERROR!! " + db.getVersion());
           // db.setVersion(3);
            return false;
        }

        return true;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/dd.xml");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    Log.d("TAG", "File: " + fileLength + " | " + total + " | " + count);
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 98 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            Log.d("TAG", "PROGRESS:" +  Integer.toString(progress[0]) );
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            // mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.setProgress(99);
            if (result != null) {
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                XmlPullParserFactory factory = null;
                XmlPullParser xpp = null;
                try {
                    factory = XmlPullParserFactory.newInstance();
                    factory.setNamespaceAware(true);
                    xpp = factory.newPullParser();
                    FileInputStream fis = null;
           /* try {
                fis = new FileInputStream("dd.xml");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            xpp.setInput(new InputStreamReader(fis));*/
                    try {
                        Log.d("TAG", "22222222222222222222222222222222222222222222222222222222222222");
                        xpp.setInput(new InputStreamReader(new FileInputStream("/sdcard/dd.xml")));
                        Log.d("TAG", "33333333333333333333333333333333333333333333333333");
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Обновление")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        if(!DataBase.insetInDb(dbUse, xpp)){
                            builder.setMessage("У вас последняя версия обновления");
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else {
                            builder.setMessage("Вы обновились");
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                mProgressDialog.setProgress(100);
                Log.d("TAG", "7777777777777777777777");
                File file = new File("/sdcard/dd.xml");
                file.delete();
                Log.d("TAG", "888888888888888888888888888888");
                mProgressDialog.dismiss();
            }

        }
    }

 }
