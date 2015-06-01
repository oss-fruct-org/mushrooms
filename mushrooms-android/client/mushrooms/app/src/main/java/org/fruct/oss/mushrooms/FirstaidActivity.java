package org.fruct.oss.mushrooms;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by OEM on 06.03.2015.
 */
public class FirstaidActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstaid);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.oceanblue)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.activity_main_button_firstaid) + "</font>"));

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        TextView fa = (TextView) findViewById(R.id.firstaidtxt);

        fa.setPadding(metrics.heightPixels / 50,0, metrics.heightPixels / 50, metrics.heightPixels / 50);
    }

}


