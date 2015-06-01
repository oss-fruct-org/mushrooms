package org.fruct.oss.mushrooms;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class OverviewActivity extends ActionBarActivity {

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    DataBase base;
    SQLiteDatabase dataBase;

    List<String> list;
    List<Bitmap> listImg;
    ListView catalog;
    ArrayAdapter<String> adapter;

    String baseTableName;

    String columnNameChoose;
    int categoryNumChoose;

    boolean mSearchOpened;
    String mSearchQuery;
    Drawable mIconOpenSearch;
    Drawable mIconCloseSearch;
    EditText mSearchEt;
    MenuItem mSearchAction;

    DisplayMetrics metrics;

    int columnsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        list = new ArrayList<>();

        listImg = new ArrayList<>();

        base = new DataBase(this);

        dataBase = base.getWritableDatabase();

        catalog = (ListView) findViewById(R.id.catalogList);

        baseTableName = "";

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (getIntent().getStringExtra("type").compareTo("mushrooms") == 0) {

            baseTableName = DataBase.TABLE_MUSHROOMS;
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" +  getResources().getString(R.string.activity_overview_title_mushrooms) + "</font>"));

            mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_mushrooms);
        }

        if (getIntent().getStringExtra("type").compareTo("berries") == 0) {

            baseTableName = DataBase.TABLE_BERRIES;
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" +  getResources().getString(R.string.activity_overview_title_berries) + "</font>"));
            mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_berries);
        }

        catalog.setOnItemClickListener(new CatalogItemClickListener());

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mScreenTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {

                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        mIconOpenSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_search);
        mIconCloseSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_close_clear_cancel);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));

        //RelativeLayout spinner = (RelativeLayout)findViewById(R.id.catalogFooter);
        //spinner.setVisibility(View.VISIBLE);

       // catalog.addFooterView(spinner);

        Cursor cursor = dataBase.query(baseTableName,
                new String[]{"_id"},
                null,
                null,
                null,
                null,
                null
        );

        columnsCount = cursor.getColumnCount();

        final CatalogLoader[] catalogLoader = {new CatalogLoader(OverviewActivity.this)};

        catalog.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                /*if (scrollState == SCROLL_STATE_IDLE){
                    Log.d("TAG","SCROLL_STATE_IDLE");
                }
                if (scrollState == SCROLL_STATE_FLING){
                    Log.d("TAG","SCROLL_STATE_FLING");
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    Log.d("TAG","SCROLL_STATE_TOUCH_SCROLL");
                }*/
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                //Check if the last view is visible


                //Log.d("TAG",firstVisibleItem + " | " + visibleItemCount + " | " + columnsCount + " | " +  totalItemCount + " | " + catalogLoader[0].getStatus());
                if ((++firstVisibleItem + visibleItemCount > totalItemCount - 10) && (columnsCount != totalItemCount)) {

                  //  Log.d("TAG",firstVisibleItem + " | " + visibleItemCount + " | " + totalItemCount);
                  //  Log.d("TAG", "LASTVIEW");

                    if(catalogLoader[0].getStatus() != AsyncTask.Status.RUNNING ) {

                        catalogLoader[0] = new CatalogLoader(OverviewActivity.this);
                        catalogLoader[0].execute(totalItemCount);
                     //   Log.d("TAG", "stt " + catalogLoader[0].getStatus());
                    }
                }
            }
        });
    }

    // Создание актион бара (меню)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        mSearchEt = (EditText) findViewById(R.id.etSearch);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
        }catch(NullPointerException ignored){}

        getSupportActionBar().setDisplayShowCustomEnabled(false);

        mSearchOpened = false;

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        mSearchAction = menu.findItem(R.id.action_search_overview);
        mSearchAction.setVisible(!drawerOpen);

        if (getIntent().getStringExtra("search") != null){
            openSearchBar(getIntent().getStringExtra("search"));
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_search_overview) {
            if (mSearchOpened) {
                closeSearchBar();
            } else {
                openSearchBar(mSearchQuery);
            }
            return true;
        }
        if (id == R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText) {

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

    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {

            //OverviewActivity.this.adapter.getFilter().filter(c);
            search(c.toString().toLowerCase());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            mSearchQuery = mSearchEt.getText().toString();
        }

    }

    public void search(String str){

        listImg.clear();
        list.clear();
        Bitmap img;

        columnNameChoose = "nameEN";

        String  secondStr = str;

        if (str.length() > 0){

            secondStr = Character.toUpperCase(str.charAt(0))+ str.substring(1);
        }

        if (getResources().getConfiguration().locale.getLanguage().compareTo("ru") == 0){

            columnNameChoose = "nameRU";
        }
        Log.d("TAG", columnNameChoose + " | " + " | " + baseTableName);
        list.clear();

        Cursor cursor = dataBase.query(baseTableName,
                    null,
                columnNameChoose + " LIKE ? OR " + columnNameChoose + " LIKE ?",
                    new String[]{"%" + str + "%", "%" + secondStr + "%"},
                    null,
                    null,
                    null
            );


        BitmapFactory.Options opt = new BitmapFactory.Options();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor
                    .getColumnIndex(columnNameChoose));
            Log.i("LOG_TAG", "ROW " + id +" HAS NAME " + name);

            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("image"));
            img = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, opt);
            try {
                img = Bitmap.createScaledBitmap(img, metrics.widthPixels / 3, metrics.widthPixels / 3, false);
            }catch (IllegalArgumentException e){
                Log.d("TAG", "atata");
            }
            listImg.add(img);
            list.add(name);
        }
        cursor.close();

        adapter = new ListAdapter(this, list, listImg, metrics,false);
        catalog.setAdapter( adapter);
    }

    private void closeSearchBar() {

        mSearchEt = (EditText) findViewById(R.id.etSearch);

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);
        }catch(NullPointerException ignored){

        }

        getSupportActionBar().setDisplayShowCustomEnabled(false);

        mSearchEt.setText("");
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened = false;

    }

    // Клик в слайдменю
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
            if(mDrawerLayout.isDrawerOpen(mDrawerList)){

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        }
    }

    public void selectItem(int category){

        listImg.clear();
        list.clear();
        Bitmap img;
        String rowsC = "10";
        categoryNumChoose = category;

        columnNameChoose = "nameEN";

        if (getResources().getConfiguration().locale.getLanguage().compareTo("ru") == 0){

            columnNameChoose = "nameRU";
        }
        Log.d("TAG", columnNameChoose + " | " + category + " | " + baseTableName);
        Cursor cursor;
        if (category == 0){
            cursor = dataBase.query(baseTableName,
                    null,
                    null,
                    null,
                    null,
                    null,
                    columnNameChoose,
                    rowsC
            );

        }else {
            cursor = dataBase.query(baseTableName,
                    null,
                    "category LIKE ?",
                    new String[]{"%" + category + "%"},
                    null,
                    null,
                    columnNameChoose,
                    rowsC
            );
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor
                    .getColumnIndex(columnNameChoose));
            Log.i("LOG_TAG", "ROW " + id +" HAS NAME " + name);

            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("image"));
            img = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, opt);
            try {
                img = Bitmap.createScaledBitmap(img, metrics.widthPixels / 3, metrics.widthPixels / 3, false);
            }catch (IllegalArgumentException e){
                Log.d("TAG", "atata");
            }
            listImg.add(img);
            list.add(name);
        }
        cursor.close();

        adapter = new ListAdapter(this, list, listImg, metrics,false);
        catalog.setAdapter( adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Нажатие кнопки "Назад"
    public void onBackPressed(){

       if(mDrawerLayout.isDrawerOpen(mDrawerList)){

            mDrawerLayout.closeDrawer(mDrawerList);
       }else{
           super.onBackPressed();
       }
    }

    private class CatalogItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView text = ((TextView) view.findViewById(R.id.catalogLabel));

            if (getIntent().getStringExtra("type").compareTo("recipes") == 0 ) {

                Intent intent = new Intent(OverviewActivity.this, DescriptionRecipesActivity.class);
                intent.putExtra("name", text.getText().toString());
                startActivity(intent);
                return;
            }

            Intent intent = new Intent(OverviewActivity.this, DescriptionActivity.class);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            intent.putExtra("name", text.getText().toString());
            startActivity(intent);
        }
    }


    public class CatalogLoader extends AsyncTask<Integer,Void, Void> {

        private Context context;


        public CatalogLoader(Context context) { this.context = context; }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Integer... params) {

            int items = params[0];

           // listImg.clear();
          //  list.clear();
            Bitmap img;
            Cursor cursor;
            if (categoryNumChoose == 0) {
                cursor = dataBase.query(baseTableName,
                        null,
                        null,
                        null,
                        null,
                        null,
                        columnNameChoose,
                        Integer.toString(items + 10)
                );
            }else{
                cursor = dataBase.query(baseTableName,
                        null,
                        "category LIKE ?",
                        new String[]{"%" + categoryNumChoose + "%"},
                        null,
                        null,
                        columnNameChoose,
                        Integer.toString(items + 10)
                );
            }

            BitmapFactory.Options opt = new BitmapFactory.Options();
cursor.move(items);
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor
                        .getColumnIndex(columnNameChoose));
              //  Log.i("LOG_TAG", "ROW " + id +" HAS NAME " + name);

                byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("image"));
                img = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length, opt);
                try {
                    img = Bitmap.createScaledBitmap(img, metrics.widthPixels / 3, metrics.widthPixels / 3, false);
                }catch (IllegalArgumentException e){
               //     Log.d("TAG", "atata");
                }
                listImg.add(img);
                list.add(name);
            }

            cursor.close();


          //  adapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            adapter.notifyDataSetChanged();
        }
    }
}
