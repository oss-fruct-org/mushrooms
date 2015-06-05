package org.fruct.oss.mushrooms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    boolean mSearchOpened;
    String mSearchQuery;
    Drawable mIconOpenSearch;
    Drawable mIconCloseSearch;
    EditText mSearchEt;
    MenuItem mSearchAction;

    DataBase base;
    SQLiteDatabase dataBase;
    ListCursorAdapter adapterMushrooms;
    ListCursorAdapter adapterBerries;
    ListCursorAdapter adapterRecipes;

    Boolean searchClick;

    DisplayMetrics metrics;

    TextView mushroomTitleMain;
    ListView lastMushroomsList;
    Button lastMushroomsButton;
    TextView berriesTitleMain;
    ListView lastBerriesList;
    Button lastBerriesButton;
    TextView recipesTitleMain;
    ListView lastRecipesList;
    Button lastRecipesButton;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // findViewById(R.id.lastLayout).setVisibility(View.GONE);

        base = new DataBase(this);
        dataBase = base.getWritableDatabase();

        searchClick = false;

        Typeface robotoLight = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        int actionBarHeight;

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Загрузка обновления");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        Drawable img;
        RelativeLayout.LayoutParams params;

        Button buttonMushrooms = (Button) findViewById(R.id.buttonMushrooms);
        Button buttonBerries = (Button) findViewById(R.id.buttonBerries);
        Button buttonFirstaid = (Button) findViewById(R.id.buttonFirstaid);
        Button buttonRecipes = (Button) findViewById(R.id.buttonRecipes);
        ImageButton buttonAbout = (ImageButton) findViewById(R.id.buttonAbout);
        ImageButton buttonUpdate = (ImageButton) findViewById(R.id.buttonUpdate);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("TAG", "resolution: " + metrics.widthPixels + " x " + metrics.heightPixels);


        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, actionBarHeight, getResources().getDisplayMetrics());
            metrics.heightPixels -= px;
        }

        /*Main layout params*/
        params = (RelativeLayout.LayoutParams) buttonMushrooms.getLayoutParams();Log.d("TAG", "mmm " + Integer.toString((metrics.widthPixels / 4)) + " | " + Integer.toString(params.leftMargin));
        params.width = ((metrics.widthPixels * 2) / 3);
        params.height = (metrics.heightPixels / 7) ;
        params.topMargin =  (metrics.heightPixels / 7) / 2 ;
        params.leftMargin = (metrics.widthPixels / 4);
        buttonMushrooms.setTextSize(TypedValue.COMPLEX_UNIT_PX,(metrics.heightPixels / 21));
        img = getResources().getDrawable( R.drawable.mushroom);
        img.setBounds( 0, 0, (metrics.heightPixels / 10) , (metrics.heightPixels / 10) );
        buttonMushrooms.setCompoundDrawables(null,null,img,null);
        buttonMushrooms.setTypeface(robotoLight);
        buttonMushrooms.setPadding(metrics.heightPixels / 50,0,metrics.heightPixels / 50,0);

        params = (RelativeLayout.LayoutParams) buttonBerries.getLayoutParams();
        params.width = ((metrics.widthPixels * 2) / 3);
        params.height = (metrics.heightPixels / 7) ;
        params.topMargin =  (metrics.heightPixels / 7) / 5;
        params.leftMargin = (metrics.widthPixels / 4);
        buttonBerries.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 21);
        img = getResources().getDrawable( R.drawable.berries);
        img.setBounds( 0, 0, (metrics.heightPixels / 10) , (metrics.heightPixels / 10) );
        buttonBerries.setCompoundDrawables(null,null,img,null);
        buttonBerries.setTypeface(robotoLight);
        buttonBerries.setPadding(metrics.heightPixels / 50,0,metrics.heightPixels / 50,0);

        params = (RelativeLayout.LayoutParams) buttonFirstaid.getLayoutParams();
        params.width = ((metrics.widthPixels * 2) / 3);
        params.height = (metrics.heightPixels / 7) ;
        params.topMargin =  (metrics.heightPixels / 7) / 5;
        params.leftMargin = (metrics.widthPixels / 4);
        buttonFirstaid.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 21);
        img = getResources().getDrawable( R.drawable.firstaid);
        img.setBounds( 0, 0, (metrics.heightPixels / 10) , (metrics.heightPixels / 10) );
        buttonFirstaid.setCompoundDrawables(null,null,img,null);
        buttonFirstaid.setTypeface(robotoLight);
        buttonFirstaid.setPadding(metrics.heightPixels / 50,0,metrics.heightPixels / 50,0);

        params = (RelativeLayout.LayoutParams) buttonRecipes.getLayoutParams();
        params.width = ((metrics.widthPixels * 2) / 3);
        params.height = (metrics.heightPixels / 7) ;
        params.topMargin =  (metrics.heightPixels / 7) / 5;
        params.leftMargin = (metrics.widthPixels / 4);
        buttonRecipes.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 21);
        img = getResources().getDrawable( R.drawable.recipes);
        //img.setBounds( 0, 0, (metrics.heightPixels / 10) , (metrics.heightPixels / 10) );
        img.setBounds( 0, 0, (metrics.heightPixels / 10) , (metrics.heightPixels / 10) );
        buttonRecipes.setCompoundDrawables(null,null,img,null);
        buttonRecipes.setTypeface(robotoLight);
        buttonRecipes.setPadding(metrics.heightPixels / 50,0,metrics.heightPixels / 50,0);

        params = (RelativeLayout.LayoutParams) buttonAbout.getLayoutParams();
        params.width = (metrics.widthPixels / 4) - (metrics.widthPixels / 80);
        params.height = (metrics.heightPixels / 7)  ;
        params.topMargin =  (metrics.heightPixels / 7) / 5;
        params.leftMargin = (metrics.widthPixels / 4);
        buttonAbout.setPadding(metrics.heightPixels/75, metrics.heightPixels/75,
                                metrics.heightPixels/75, metrics.heightPixels/75);

        params = (RelativeLayout.LayoutParams) buttonUpdate.getLayoutParams();
        params.width = (metrics.widthPixels / 4) - (metrics.widthPixels / 80);
        params.height = (metrics.heightPixels / 7)  ;
        params.topMargin =  (metrics.heightPixels / 7) / 5;
        params.leftMargin = (metrics.widthPixels / 4) + (metrics.widthPixels / 4) + (metrics.widthPixels / 80);
        buttonUpdate.setPadding(metrics.heightPixels/75, metrics.heightPixels/75,
                                    metrics.heightPixels/75, metrics.heightPixels/75);

        /*Last layout params*/

        mushroomTitleMain = (TextView) findViewById(R.id.mushroomTitleMain);
        lastMushroomsList = (ListView) findViewById(R.id.lastMushroomsList);
        lastMushroomsButton = (Button) findViewById(R.id.lastMushroomsButton);
        berriesTitleMain = (TextView) findViewById(R.id.berriesTitleMain);
        lastBerriesList = (ListView) findViewById(R.id.lastBerriesList);
        lastBerriesButton = (Button) findViewById(R.id.lastBerriesButton);
        recipesTitleMain = (TextView) findViewById(R.id.recipesTitleMain);
        lastRecipesList = (ListView) findViewById(R.id.lastRecipesList);
        lastRecipesButton = (Button) findViewById(R.id.lastRecipesButton);

        params = (RelativeLayout.LayoutParams) mushroomTitleMain.getLayoutParams();
        params.height = (metrics.heightPixels / 20);
        params.topMargin =  (metrics.widthPixels / 50);
        mushroomTitleMain.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        img = getResources().getDrawable( R.drawable.mushroo);
        img.setBounds( 0, 0, (metrics.heightPixels / 20) , (metrics.heightPixels / 20) );
        mushroomTitleMain.setCompoundDrawables(img,null,img,null);
        mushroomTitleMain.setTypeface(robotoLight);
        mushroomTitleMain.setPadding(metrics.heightPixels/75, 0, metrics.heightPixels/75, 0);

        params = (RelativeLayout.LayoutParams) lastMushroomsList.getLayoutParams();
        params.height = (metrics.widthPixels / 2) ;
        params.topMargin =  0;

        params = (RelativeLayout.LayoutParams) lastMushroomsButton.getLayoutParams();
        params.height = (metrics.heightPixels / 20);
        params.topMargin = 0;
        lastMushroomsButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        lastMushroomsButton.setTypeface(robotoLight);

        params = (RelativeLayout.LayoutParams) berriesTitleMain.getLayoutParams();
        params.height = (metrics.heightPixels / 20) + (metrics.widthPixels / 25);
        params.topMargin =  (metrics.widthPixels / 50);
        berriesTitleMain.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        img = getResources().getDrawable( R.drawable.berries);
        img.setBounds( 0, 0, (metrics.heightPixels / 20) , (metrics.heightPixels / 20) );
        berriesTitleMain.setCompoundDrawables(img,null,img,null);
        berriesTitleMain.setTypeface(robotoLight);
        berriesTitleMain.setPadding(metrics.heightPixels/75, 0, metrics.heightPixels/75, 0);

        params = (RelativeLayout.LayoutParams) lastBerriesList.getLayoutParams();
        params.height = metrics.widthPixels / 2;
        params.topMargin =  0;
        params = (RelativeLayout.LayoutParams) lastBerriesButton.getLayoutParams();
        params.height = (metrics.heightPixels / 20);
        params.topMargin =  0;
        lastBerriesButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        lastBerriesButton.setTypeface(robotoLight);

        params = (RelativeLayout.LayoutParams) recipesTitleMain.getLayoutParams();
        params.height = (metrics.heightPixels / 20);
        params.topMargin =  (metrics.widthPixels / 50);
        recipesTitleMain.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        img = getResources().getDrawable( R.drawable.recipes);
        img.setBounds( 0, 0, (metrics.heightPixels / 20) , (metrics.heightPixels / 20) );
        recipesTitleMain.setCompoundDrawables(img,null,img,null);
        recipesTitleMain.setTypeface(robotoLight);
        recipesTitleMain.setPadding(metrics.heightPixels/75, 0, metrics.heightPixels/75, 0);

        params = (RelativeLayout.LayoutParams) lastRecipesList.getLayoutParams();
        params.height = metrics.widthPixels / 2;
        params.topMargin =  0;

        params = (RelativeLayout.LayoutParams) lastRecipesButton.getLayoutParams();
        params.height = (metrics.heightPixels / 20);
        params.topMargin =  0;
        lastRecipesButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 25);
        lastRecipesButton.setTypeface(robotoLight);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.oceanblue)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.activity_main_actionbar_title) + "</font>"));

        Log.d("TAG", Integer.toString((metrics.heightPixels / 18) * 4) + " | " + Integer.toString((metrics.heightPixels / 18)));

        mIconOpenSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_search);
        mIconCloseSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeAdapters();
    }

    protected void onStart() {
        super.onStart();

        findViewById(R.id.lastLayout).setVisibility(View.GONE);
        findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
    }

    protected void onPause() {
        super.onPause();
    }

    private void searchStart(String str) {
        closeAdapters();

        String columnNameChoose = "nameEN";
        if (getResources().getConfiguration().locale.getLanguage().compareTo("ru") == 0) {
            columnNameChoose = "nameRU";
        }

        ListView catalog;
        String rowsShow = "3";

        int countRow;
        RelativeLayout.LayoutParams params;
        String secondStr = str;

        if (str.length() > 0) {
            secondStr = Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }

        Cursor cursor = dataBase.query(DataBase.TABLE_MUSHROOMS,
                null,
                columnNameChoose + " LIKE ? OR " + columnNameChoose + " LIKE ?",
                new String[]{"%" + str + "%", "%" + secondStr + "%"},
                null,
                null,
                columnNameChoose,
                rowsShow
        );

        countRow = cursor.getCount();

        switch (countRow) {
        case 2:
            params = (RelativeLayout.LayoutParams) lastMushroomsButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastMushroomsList.getLayoutParams();
            params.height = (metrics.widthPixels / 6) * 2;
            mushroomTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_mushrooms));
            break;

        case 1:
            params = (RelativeLayout.LayoutParams) lastMushroomsButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastMushroomsList.getLayoutParams();
            params.height = (metrics.widthPixels / 6);
            mushroomTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_mushrooms));
            break;

        case 0:
            params = (RelativeLayout.LayoutParams) lastMushroomsButton.getLayoutParams();
            params.height = 0;
            mushroomTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_mushrooms_not));
            params = (RelativeLayout.LayoutParams) lastMushroomsList.getLayoutParams();
            params.height = 0;
            break;

        default:
            params = (RelativeLayout.LayoutParams) lastMushroomsList.getLayoutParams();
            params.height = ((metrics.widthPixels / 6) * 3) + metrics.heightPixels / 100;
            params = (RelativeLayout.LayoutParams) lastMushroomsButton.getLayoutParams();
            params.height = 40;
            mushroomTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_mushrooms));
            break;
        }

        adapterMushrooms = new ListCursorAdapter(this, cursor, columnNameChoose, true, metrics);

        catalog = (ListView) findViewById(R.id.lastMushroomsList);
        catalog.setAdapter(adapterMushrooms);
        catalog.setOnItemClickListener(new CatalogMushroomsItemClickListener());

        cursor = dataBase.query(DataBase.TABLE_BERRIES,
                null,
                columnNameChoose + " LIKE ? OR " + columnNameChoose + " LIKE ?",
                new String[]{"%" + str + "%", "%" + secondStr + "%"},
                null,
                null,
                columnNameChoose,
                rowsShow
        );

        countRow = cursor.getCount();

        switch (countRow) {
        case 2:
            params = (RelativeLayout.LayoutParams) lastBerriesButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastBerriesList.getLayoutParams();
            params.height = (metrics.widthPixels / 6) * 2;
            berriesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_berries));
            break;

        case 1:
            params = (RelativeLayout.LayoutParams) lastBerriesButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastBerriesList.getLayoutParams();
            params.height = (metrics.widthPixels / 6);
            berriesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_berries));
            break;

        case 0:
            params = (RelativeLayout.LayoutParams) lastBerriesButton.getLayoutParams();
            params.height = 0;
            berriesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_berries_not));
            params = (RelativeLayout.LayoutParams) lastBerriesList.getLayoutParams();
            params.height = 0;
            break;

        default:

            params = (RelativeLayout.LayoutParams) lastBerriesList.getLayoutParams();
            params.height = ((metrics.widthPixels / 6) * 3) + metrics.heightPixels / 100;
            params = (RelativeLayout.LayoutParams) lastBerriesButton.getLayoutParams();
            params.height = 40;
            berriesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_berries));
            break;
        }

        adapterBerries = new ListCursorAdapter(this, cursor, columnNameChoose, true, metrics);

        catalog = (ListView) findViewById(R.id.lastBerriesList);
        catalog.setAdapter(adapterBerries);
        catalog.setOnItemClickListener(new CatalogBerriesItemClickListener());

        cursor = dataBase.query(DataBase.TABLE_RECIPES,
                null,
                columnNameChoose + " LIKE ? OR " + columnNameChoose + " LIKE ?",
                new String[]{"%" + str + "%", "%" + secondStr + "%"},
                null,
                null,
                columnNameChoose,
                rowsShow
        );

        countRow = cursor.getCount();

        switch (countRow) {
        case 2:
            params = (RelativeLayout.LayoutParams) lastRecipesButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastRecipesList.getLayoutParams();
            params.height = (metrics.widthPixels / 6) * 2;
            recipesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_recipes));
            break;

        case 1:
            params = (RelativeLayout.LayoutParams) lastRecipesButton.getLayoutParams();
            params.height = 0;
            params = (RelativeLayout.LayoutParams) lastRecipesList.getLayoutParams();
            params.height = (metrics.widthPixels / 6);
            recipesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_recipes));
            break;

        case 0:
            params = (RelativeLayout.LayoutParams) lastRecipesButton.getLayoutParams();
            params.height = 0;
            recipesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_recipes_not));
            params = (RelativeLayout.LayoutParams) lastRecipesList.getLayoutParams();
            params.height = 0;
            break;

        default:

            params = (RelativeLayout.LayoutParams) lastRecipesList.getLayoutParams();
            params.height = ((metrics.widthPixels / 6) * 3) + metrics.heightPixels / 100;
            params = (RelativeLayout.LayoutParams) lastRecipesButton.getLayoutParams();
            params.height = 40;
            recipesTitleMain.setText(getResources().getString
                    (R.string.activity_main_title_recipes));
            break;
        }

        Log.d("TAG", columnNameChoose);
        adapterRecipes = new ListCursorAdapter(this, cursor, columnNameChoose, true, metrics);

        catalog = (ListView) findViewById(R.id.lastRecipesList);
        catalog.setAdapter(adapterRecipes);
        catalog.setOnItemClickListener(new CatalogRecipesItemClickListener());
    }

    private void closeAdapters() {
        ((ListView) findViewById(R.id.lastRecipesList)).setAdapter(null);
        ((ListView) findViewById(R.id.lastBerriesList)).setAdapter(null);
        ((ListView) findViewById(R.id.lastMushroomsList)).setAdapter(null);

        if (adapterBerries != null)
            adapterBerries.close();

        if (adapterMushrooms != null)
            adapterMushrooms.close();

        if (adapterRecipes != null)
            adapterRecipes.close();

        adapterBerries = null;
        adapterMushrooms = null;
        adapterRecipes = null;
    }

    // Кнопка грибы
    public void onButtonMushroomsClick(View view){

        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
        intent.putExtra("type", "mushrooms");
        startActivity(intent);
    }

    // Кнопка ягоды
    public void onButtonBerriesClick(View view){

        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
        intent.putExtra("type", "berries");
        startActivity(intent);
    }

    // Кнопка Первая помощь
    public void onButtonFirstaidClick(View view){

        Intent intent = new Intent(MainActivity.this, FirstaidActivity.class);
        startActivity(intent);
    }

    // Кнопка рецепты
    public void onButtonRecipesClick(View view){

        Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
        startActivity(intent);
    }

    //Кнопка О программе
    public void onButtonAboutClick(View view){

        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    //Кнопка обновления БД
    public void onButtonUpgradeClick(View view){
        Log.d("TAG", "111111111111111111111111111111");
        final DownloadTask downloadTask = new DownloadTask(MainActivity.this);
        downloadTask.execute("http://gets.cs.petrsu.ru/mushrooms/?version=" + DataBase.getVersiion());
Log.d("TAG", "VER: " + Integer.toString(DataBase.getVersiion()) + " | " + Integer.toString(dataBase.getVersion()) + " | " + 0);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });


    }

    //Кнопка Все грибы
    public void onLastMushroomsButtonClick(View view){

        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
        intent.putExtra("type", "mushrooms");
        intent.putExtra("search", mSearchQuery);
        startActivity(intent);
        closeSearchBar();
       // findViewById(R.id.lastLayout).setVisibility(View.GONE);
       // findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
    }

    //Кнопка Все ягоды
    public void onLastBerriesButtonClick(View view){

        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
        intent.putExtra("type", "berries");
        intent.putExtra("search", mSearchQuery);
        startActivity(intent);
        //findViewById(R.id.lastLayout).setVisibility(View.GONE);
       // findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
        closeSearchBar();
    }

    //Кнопка Все рецепты
    public void onLastRecipesButtonClick(View view){

        Intent intent = new Intent(MainActivity.this, RecipesActivity.class);
        intent.putExtra("search", mSearchQuery);
        startActivity(intent);
        closeSearchBar();
    }

    // Создание актион бара (меню)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search_main);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search_main) {
            if (mSearchOpened) {

                mSearchEt = (EditText) findViewById(R.id.etSearch);

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);

                findViewById(R.id.lastLayout).setVisibility(View.GONE);
                findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
                closeSearchBar();
            } else {

                findViewById(R.id.mainLayout).setVisibility(View.GONE);
                findViewById(R.id.lastLayout).setVisibility(View.VISIBLE);
                openSearchBar(mSearchQuery);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {
        searchStart("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        mSearchEt = (EditText) actionBar.getCustomView()
                .findViewById(R.id.etSearch);
        mSearchEt.addTextChangedListener(new SearchWatcher());
        mSearchEt.setText(queryText);
        mSearchEt.requestFocus();

        mSearchAction.setIcon(mIconCloseSearch);
        mSearchOpened = true;

    }

    private void closeSearchBar() {

        getSupportActionBar().setDisplayShowCustomEnabled(false);

        mSearchEt.setText("");
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened = false;

        closeAdapters();
    }

    private class CatalogMushroomsItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView text = ((TextView) view.findViewById(R.id.catalogLabel));
            Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
            intent.putExtra("type", "mushrooms");
            intent.putExtra("name", text.getText().toString());
            startActivity(intent);
        }
    }

    private class CatalogBerriesItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView text = ((TextView) view.findViewById(R.id.catalogLabel));
            Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
            intent.putExtra("type", "berries");
            intent.putExtra("name", text.getText().toString());
            startActivity(intent);
        }
    }

    private class CatalogRecipesItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView text = ((TextView) view.findViewById(R.id.catalogLabel));
            Intent intent = new Intent(MainActivity.this, DescriptionRecipesActivity.class);
            intent.putExtra("name", text.getText().toString());
            startActivity(intent);
        }
    }

    // Нажатие кнопки "Назад"
    public void onBackPressed(){
        if (mSearchOpened) {

            mSearchEt = (EditText) findViewById(R.id.etSearch);

            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);

            findViewById(R.id.lastLayout).setVisibility(View.GONE);
            findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
            closeSearchBar();
        }else{
            super.onBackPressed();
        }
    }

    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

            Log.d("TAG","C: " + c.toString());
            searchStart(c.toString().toLowerCase());

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mSearchQuery = mSearchEt.getText().toString();
            Log.d("TAG",mSearchQuery);
        }
    }

    public static boolean isDownloadManagerAvailable(Context context) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Обновление")
                                .setCancelable(false)
                                .setNegativeButton("ОК",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                        if(!DataBase.insetInDb(dataBase, xpp)){
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
