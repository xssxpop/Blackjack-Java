package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void StartButton(View v){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void QuitButton(View v){
        finish();
        System.exit(0);
    }
}
