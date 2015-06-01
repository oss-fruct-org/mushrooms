package org.fruct.oss.mushrooms;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class RecipesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        RelativeLayout.LayoutParams params;

        Button buttonMushrooms = (Button) findViewById(R.id.buttonRecipesMushrooms);
        Button buttonBerries = (Button) findViewById(R.id.buttonRecipesBerries);

        Typeface robotoLight = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.d("TAG", "resolution: " + metrics.widthPixels + " x " + metrics.heightPixels);


        int size = metrics.widthPixels < metrics.heightPixels / 2 ?
                metrics.widthPixels : metrics.heightPixels / 2;

        size -= (metrics.heightPixels / 50) * 3 ;

        params = (RelativeLayout.LayoutParams) buttonMushrooms.getLayoutParams();
        params.width = size;
        params.height = size ;
        params.topMargin =  metrics.heightPixels / 50;
        buttonMushrooms.setTypeface(robotoLight);
        buttonMushrooms.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 17);
        buttonMushrooms.setPadding(metrics.heightPixels / 50, 0, metrics.heightPixels / 50, 0);

        params = (RelativeLayout.LayoutParams) buttonBerries.getLayoutParams();
        params.width = size;
        params.height = size;
        params.topMargin =  metrics.heightPixels / 50;
        buttonBerries.setTypeface(robotoLight);
        buttonBerries.setTextSize(TypedValue.COMPLEX_UNIT_PX, metrics.heightPixels / 17);
        buttonBerries.setPadding(metrics.heightPixels / 50, 0, metrics.heightPixels / 50, 0);
    }

    public void onButtonRecipesMushroomsClick(View view){

        Intent intent = new Intent(RecipesActivity.this, OverviewRecipesActivity.class);
        intent.putExtra("typeR", "Mushrooms");
        intent.putExtra("search", getIntent().getStringExtra("search"));
        startActivity(intent);
    }

    public void onButtonRecipesBerriesClick(View view){

        Intent intent = new Intent(RecipesActivity.this, OverviewRecipesActivity.class);
        intent.putExtra("typeR", "Berries");
        intent.putExtra("search", getIntent().getStringExtra("search"));
        startActivity(intent);
    }
}
