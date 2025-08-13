package com.example.tesis_angie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2500; // 2.5 segundos
    private Handler handler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupWindowInsets();
        startSplashSequence();
    }

    private void initializeViews() {
        // Obtener referencias a las vistas
        ImageView logo = findViewById(R.id.ivLogo);
        TextView appName = findViewById(R.id.tvAppName);
        // Animación de fade in para el logo y nombre
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(300);
        logo.startAnimation(fadeIn);
        appName.startAnimation(fadeIn);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startSplashSequence() {
        handler = new Handler(Looper.getMainLooper());
        splashRunnable = () -> {
            // Animación de fade out antes de cambiar de actividad
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration(300);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    navigateToLogin();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            findViewById(R.id.main).startAnimation(fadeOut);
        };
        handler.postDelayed(splashRunnable, SPLASH_DURATION);
    }

    private void navigateToLogin() {
        try {
            Intent intent = new Intent(MainActivity.this,
                    com.example.tesis_angie.screens.LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // overridePendingTransition está deprecado en Android 14+, pero se mantiene para compatibilidad
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Error al iniciar LoginActivity", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar el handler para evitar memory leaks
        if (handler != null && splashRunnable != null) {
            handler.removeCallbacks(splashRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        // Deshabilitar el botón back en el splash screen
        // No hacer nada para evitar que el usuario regrese
        super.onBackPressed(); // Llamada requerida por el sistema
    }
}