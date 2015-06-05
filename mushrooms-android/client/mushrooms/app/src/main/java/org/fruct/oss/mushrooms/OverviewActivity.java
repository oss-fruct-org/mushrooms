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

    ListView catalog;

    String baseTableName;

    ListCursorAdapter adapter;

    String recipeType;
    String columnNameChoose;
    int categoryNumChoose;

    boolean mSearchOpened;
    String mSearchQuery;
    Drawable mIconOpenSearch;
    Drawable mIconCloseSearch;
    EditText mSearchEt;
    MenuItem mSearchAction;

    DisplayMetrics metrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

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

        if(getIntent().getStringExtra("type").compareTo("recipe") == 0){
            baseTableName = DataBase.TABLE_RECIPES;
            if(getIntent().getStringExtra("typeR").compareTo("Berries") == 0){
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" +  getResources().getString(R.string.activity_overview_title_recipes_berries) + "</font>"));
                mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_recipes_berries);
                recipeType = "Berries";
            }

            if(getIntent().getStringExtra("typeR").compareTo("Mushrooms") == 0){
                getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" +  getResources().getString(R.string.activity_overview_title_recipes_mushrooms) + "</font>"));
                mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_recipes_mushrooms);
                recipeType = "Mushrooms";
            }
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

        // Get localized column name
        columnNameChoose = "nameEN";
        if (getResources().getConfiguration().locale.getLanguage().compareTo("ru") == 0){
            columnNameChoose = "nameRU";
        }

        if (savedInstanceState == null) {
            selectItem(0);
        }

        mIconOpenSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_search);
        mIconCloseSearch = getResources()
                .getDrawable(android.R.drawable.ic_menu_close_clear_cancel);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
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
            refreshListAdapter(c.toString().toLowerCase());
        }

        @Override
        public void afterTextChanged(Editable editable) {

            mSearchQuery = mSearchEt.getText().toString();
        }

    }

    private void refreshListAdapter(String filterString) {
        Cursor cursor = queryCursor(categoryNumChoose, filterString);

        if (adapter == null) {
            adapter = new ListCursorAdapter(this, cursor, columnNameChoose, false, metrics);
            catalog.setAdapter(adapter);
        } else {
            adapter.changeCursor(cursor);
        }
    }

    private Cursor queryCursor(int categoryNumChoose, String filterString) {
        String categoryCondition;
        String filterCondition;
        String recipeCondition;

        ArrayList<String> args = new ArrayList<>();

        // Create category filter condition
        if (categoryNumChoose != 0) {
            categoryCondition = "(category LIKE ?)";
            args.add("%" + categoryNumChoose + "%");
        } else {
            categoryCondition = "(1=1)";
        }

        // Create string filter condition
        if (filterString != null && !filterString.isEmpty()) {
            String secondStr = Character.toUpperCase(filterString.charAt(0)) + filterString.substring(1);

            filterCondition = "(" + columnNameChoose + " LIKE ? OR " + columnNameChoose + " LIKE ?)";
            args.add("%" + filterString + "%");
            args.add("%" + secondStr + "%");
        } else {
            filterCondition = "(1=1)";
        }

        if (recipeType != null) {
            recipeCondition = "type LIKE ?";
            args.add("%" + recipeType + "%");
        } else {
            recipeCondition = "(1=1)";
        }

        return dataBase.query(baseTableName,
                null,
                categoryCondition + " AND " + filterCondition + " AND " + recipeCondition,
                args.toArray(new String[args.size()]),
                null,
                null,
                columnNameChoose
        );
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
        categoryNumChoose = category;
        refreshListAdapter(null);
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

            if (getIntent().getStringExtra("type").compareTo("recipe") == 0 ) {
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
}
