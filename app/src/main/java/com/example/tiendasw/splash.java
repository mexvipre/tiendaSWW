package com.example.tiendasw;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Redirige al Login después de 3 segundos
                Intent intent = new Intent(splash.this, login.class);
                startActivity(intent);
                finish(); // Cierra el splash para que no regrese atrás
            }
        }, SPLASH_DELAY);
    }
}
