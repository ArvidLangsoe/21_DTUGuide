package com.arvid.dtuguide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavigateToDTUActivity extends AppCompatActivity implements View.OnClickListener {

    Button lyngbyNavigate;
    Button ballerupNavigate;
    Button risoNavigate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_to_dtu);

        lyngbyNavigate = (Button)findViewById(R.id.lyngby_button);
        ballerupNavigate = (Button)findViewById(R.id.ballerup_button);
        risoNavigate = (Button)findViewById(R.id.riso_button);

        lyngbyNavigate.setOnClickListener(this);
        ballerupNavigate.setOnClickListener(this);
        risoNavigate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Uri gmmIntentUri;
        if(view.equals(lyngbyNavigate)){
            gmmIntentUri = Uri.parse("geo:0,0?q=55.785858,12.524933(Lyngby+Campus)");
        }else if(view.equals(ballerupNavigate)){
            gmmIntentUri = Uri.parse("geo:0,0?q=55.731133,12.396795(Ballerup+Campus)");
        }else if(view.equals(risoNavigate)){
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
}
