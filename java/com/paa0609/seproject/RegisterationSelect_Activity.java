package com.paa0609.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class RegisterationSelect_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);

        Button busDriverButton = (Button) findViewById(R.id.BusDriverButton);
        Button userButton = (Button) findViewById(R.id.UserButton);

        busDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent BusDriverIntent = new Intent(RegisterationSelect_Activity.this, BusDriver_Registeration_Avtivity.class);
                RegisterationSelect_Activity.this.startActivity(BusDriverIntent);
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UserIntent = new Intent(RegisterationSelect_Activity.this, User_Registeration_Activity.class);
                RegisterationSelect_Activity.this.startActivity(UserIntent);
            }
        });
    }
}
