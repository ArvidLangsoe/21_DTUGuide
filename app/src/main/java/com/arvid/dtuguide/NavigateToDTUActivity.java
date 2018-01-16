package com.arvid.dtuguide;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigateToDTUActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView lyngbyImage, ballerupImage, risooImage, lyngbyCircle, ballerupCircle, risooCircle;
    private TextView lyngbyText, ballerupText, risooText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_to_dtu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_navigate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lyngbyImage = (ImageView)findViewById(R.id.image_lyngby);
        ballerupImage = (ImageView)findViewById(R.id.image_ballerup);
        risooImage = (ImageView)findViewById(R.id.image_risoo);


        lyngbyImage.setOnClickListener(this);
        ballerupImage.setOnClickListener(this);
        risooImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Uri gmmIntentUri;
        if(view.equals(lyngbyImage)){
        gmmIntentUri = Uri.parse("geo:0,0?q=55.785858,12.524933(Lyngby+Campus)");
    } else if(view.equals(ballerupImage)){
            gmmIntentUri = Uri.parse("geo:0,0?q=55.731133,12.396795(Ballerup+Campus)");
        }else if(view.equals(risooImage)){
            gmmIntentUri = Uri.parse("geo:0,0?q=55.692385,12.102599(Risø+Campus)");
        }
        else{
            return;
        }
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
