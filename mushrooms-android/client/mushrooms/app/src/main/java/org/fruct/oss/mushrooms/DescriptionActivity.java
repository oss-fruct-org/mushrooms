package org.fruct.oss.mushrooms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;


public class DescriptionActivity extends Activity {

    DataBase base;
    SQLiteDatabase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        RelativeLayout.LayoutParams params;

        base = new DataBase(this);

        dataBase = base.getWritableDatabase();

        String name = getIntent().getStringExtra("name");

        String columnNameChoose = "nameEN";
        String columnDescriptionChoose = "descriptionEN";

        SpannableString descriptionContent = null;

        String baseTable = "";

        String category;

        ImageView photo = (ImageView) findViewById(R.id.descriptionImage);

        TextView mainName = (TextView) findViewById(R.id.descTakeName);

        RelativeLayout mainNameLayout = (RelativeLayout) findViewById(R.id.descTakeNameLayout);

        TextView description = (TextView) findViewById(R.id.descView);

        TextView helpmes = (TextView) findViewById(R.id.helpmes);

        mainName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
        mainName.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 20);
        mainName.setPadding(metrics.heightPixels / 50, 0, metrics.heightPixels / 50, 0);

        params = (RelativeLayout.LayoutParams) mainNameLayout.getLayoutParams();
        params.height = metrics.heightPixels / 28 +  metrics.heightPixels / 25;

        photo.setPadding(metrics.heightPixels / 50, 0, metrics.heightPixels / 50, 0);

        description.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 40);
        description.setPadding(metrics.heightPixels / 50,0, metrics.heightPixels / 50, metrics.heightPixels / 50);

        helpmes.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 40);
        helpmes.setPadding(metrics.heightPixels / 50,0,metrics.heightPixels / 50, metrics.heightPixels / 50);

        Bitmap img = null;

        if (getResources().getConfiguration().locale.getLanguage().compareTo("ru") == 0){

            columnNameChoose = "nameRU";
            columnDescriptionChoose = "descriptionRU";
        }
        Cursor cursor;
        if (getIntent().getStringExtra("click")!= null){

            String id = getIntent().getStringExtra("click");

            if (id.charAt(0) == 'm'){

                baseTable =  DataBase.TABLE_MUSHROOMS;
            }

            if (id.charAt(0) == 'b'){

                baseTable =  DataBase.TABLE_BERRIES;
            }

            cursor = dataBase.query(baseTable,
                    null,
                    "_id = ?",
                    new String[] {id.substring(1)},
                    null,
                    null,
                    null
            );
        }else {

            if (getIntent().getStringExtra("type").compareTo("mushrooms") == 0){

                baseTable =  DataBase.TABLE_MUSHROOMS;
            } else if (getIntent().getStringExtra("type").compareTo("berries") == 0){

                baseTable =  DataBase.TABLE_BERRIES;
            }

            cursor = dataBase.query(baseTable,
                    null,
                    columnNameChoose + " = ?",
                    new String[]{name},
                    null,
                    null,
                    null
            );
        }
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));

            String name1 = cursor.getString(cursor
                    .getColumnIndex(columnNameChoose));

            mainName.setText(name1);
            if (name1.length() > 20 && name1.length() < 40){
                params.height *= 2;
            }

            if (name1.length() > 40 ){
                params.height *= 3;
            }
/*
            category = cursor.getString(cursor
                    .getColumnIndex("category"));*/

            category = "";

            String [] mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_mushrooms);
            if (getIntent().getStringExtra("type").compareTo("mushrooms") == 0){
                switch(cursor.getString(cursor.getColumnIndex("category"))) {
                    case "1":
                        category = mScreenTitles[1]/*"Съедобные"*/;
                        helpmes.setVisibility(View.GONE);
                        break;
                    case "2":
                        category = mScreenTitles[2];
                        break;
                    case "3":
                        category = mScreenTitles[3];
                        break;
                    default:
                        category = "";
                }
            }

            mScreenTitles = getResources().getStringArray(R.array.activity_overview_array_berries);

            if (getIntent().getStringExtra("type").compareTo("berries") == 0){
                switch(cursor.getString(cursor.getColumnIndex("category"))) {
                    case "1":
                        category =  mScreenTitles[1];
                        break;
                    case "2":
                        category =  mScreenTitles[2];
                        break;
                    default:
                        category = "";
                }
                helpmes.setVisibility(View.GONE);
            }

            String name2 = getResources().getString(R.string.activity_description_category_word) + " " + category + "\n" + cursor.getString(cursor
                    .getColumnIndex(columnDescriptionChoose));

            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("image"));
            img = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            img = Bitmap.createScaledBitmap(img, metrics.widthPixels , metrics.widthPixels , false);

            descriptionContent = parsingText(name2);


/*
            category = cursor.getString(cursor
                    .getColumnIndex("category"));*/

            Log.i("LOG_TAG", "ROW " + id + " HAS NAME " + name1 + "DISKR" + name2 + "Cat" + category);

        }
        cursor.close();

        description.setText(descriptionContent);
        description.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString helpmesSS = new SpannableString(getString(R.string.activity_description_helpmes));

        helpmesSS.setSpan(new RelativeSizeSpan(2f), 0, helpmesSS.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "CLIIIIICK");
                Intent intent = new Intent(DescriptionActivity.this, FirstaidActivity.class);
                startActivity(intent);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.RED);
                ds.setUnderlineText(false);

            }
        };

        helpmesSS.setSpan(clickableSpan, 0, helpmesSS.length(),  Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        helpmes.setText(helpmesSS);
        helpmes.setMovementMethod(LinkMovementMethod.getInstance());


        photo.setImageBitmap(img);
    }

    public SpannableString parsingText(String str) {
        LinkedList<Point> hs = new LinkedList<>();
        LinkedList<Point> as = new LinkedList<>();
        LinkedList<Point> rs = new LinkedList<>();
        LinkedList<Point> is = new LinkedList<>();


        LinkedList<String> ids = new LinkedList<>();

        int tagStart = 0;// = in.indexOf('<');
        int tagEnd;

        int tagStartH;
        int tagStartA;
        int tagStartR;
        int tagStartI;

        int tagSep;

        String id;
        String text;

        while (true) {

            tagStartH = str.indexOf("[h]", tagStart);
            tagStartA = str.indexOf("[a]", tagStart);
            tagStartR = str.indexOf("[r]", tagStart);
            tagStartI = str.indexOf("[i]", tagStart);


            tagStart = str.length();

            if(tagStartH >= 0 && tagStart > tagStartH) {
                tagStart = tagStartH;
            }
            if(tagStartA >= 0 && tagStart > tagStartA) {
                tagStart = tagStartA;
            }
            if(tagStartR >= 0 && tagStart > tagStartR) {
                tagStart = tagStartR;
            }
            if(tagStartI >= 0 && tagStart > tagStartI) {
                tagStart = tagStartI;
            }
            if(tagStart == str.length()) break;

            if(tagStart == tagStartH && (tagEnd = str.indexOf("[/h]", tagStart)) > tagStart) {
                Log.d("parse", str.substring(tagStart + 3, tagEnd));
                str = str.substring(0, tagStart) + str.substring(tagStart + 3, tagEnd) + str.substring(tagEnd + 4, str.length());
                tagEnd -= 3;

                hs.add(new Point(tagStart, tagEnd));
                continue;
            }

            if(tagStart == tagStartA && (tagEnd = str.indexOf("[/a]", tagStart)) > tagStart) {
                Log.d("parse", str.substring(tagStart + 3, tagEnd));

                tagSep = str.indexOf("|", tagStart);

                if(tagSep >= tagEnd) continue;

                id = str.substring(tagStart + 3, tagSep);

                str = str.substring(0, tagStart) + str.substring(tagSep + 1, tagEnd) + str.substring(tagEnd + 4, str.length());

                //id | text

                tagEnd -= 4 + id.length();

                ids.add(id);

                as.add(new Point(tagStart, tagEnd));
                continue;
            }

            if(tagStart == tagStartR && (tagEnd = str.indexOf("[/r]", tagStart)) > tagStart) {
                Log.d("parse", str.substring(tagStart + 3, tagEnd));
                str = str.substring(0, tagStart) + str.substring(tagStart + 3, tagEnd) + str.substring(tagEnd + 4, str.length());
                tagEnd -= 3;

                rs.add(new Point(tagStart, tagEnd));
                continue;
            }

            if(tagStart == tagStartI && (tagEnd = str.indexOf("[/i]", tagStart)) > tagStart) {
                Log.d("parse", str.substring(tagStart + 3, tagEnd));
                str = str.substring(0, tagStart) + str.substring(tagStart + 3, tagEnd) + str.substring(tagEnd + 4, str.length());
                tagEnd -= 3;

                is.add(new Point(tagStart, tagEnd));
                continue;
            }

        }

        SpannableString content = new SpannableString(str);
        Log.d("TAG", Integer.toString(1) + " | " +  Integer.toString(hs.size()));
        for(Point p : hs) {
            Log.d("TAG", Integer.toString(p.x) + " | " +  Integer.toString(p.y));
            content.setSpan(new RelativeSizeSpan(1.2f), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setSpan(new StyleSpan(Typeface.BOLD), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        for(Point p : rs) {
            Log.d("TAG", Integer.toString(p.x) + " | " +  Integer.toString(p.y));
            content.setSpan(new RelativeSizeSpan(1.2f), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setSpan(new ForegroundColorSpan(Color.rgb(255, 0, 0)), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        for(Point p : is) {
            Log.d("TAG", Integer.toString(p.x) + " | " + Integer.toString(p.y));
            content.setSpan(new StyleSpan(Typeface.ITALIC), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        for(Point p : as) {

            id = ids.removeFirst();
            Log.d("TAG", Integer.toString(p.x) + " | " +  Integer.toString(p.y) + " |id: " + id);
            final String finalId = id;
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Log.d("TAG", "CLIIIIICK");
                    Intent intent = new Intent(DescriptionActivity.this, DescriptionRecipesActivity.class);
                    intent.putExtra("click", finalId);
                    startActivity(intent);
                }
            };

            content.setSpan(clickableSpan, p.x, p.y,  Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setSpan(new ForegroundColorSpan(Color.rgb(0,0,255)), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setSpan(new UnderlineSpan(), p.x, p.y, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }



        return content;
    }

}
