package com.example.tesis_angie.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.tesis_angie.R;

public class LoginActivity extends AppCompatActivity {

    // Vistas
    private TextInputEditText etUsername, etPassword;
    private TextInputLayout tilUsername, tilPassword;
    private Button btnLogin;
    private CardView btnCard;
    private TextView tvError;
    private ImageView ivLoginLogo;
    private ProgressBar progressBar;
    private CardView loginCard;

    // Handler para operaciones asíncronas
    private Handler handler;

    // Constantes
    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "1234";
    private static final int LOGIN_DELAY = 1000; // 1 segundo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler = new Handler(Looper.getMainLooper());
        initializeViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initializeViews() {
        // Inicializar todas las vistas
        ivLoginLogo = findViewById(R.id.ivLoginLogo);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        btnLogin = findViewById(R.id.btnLogin);
        btnCard = findViewById(R.id.btn_card);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);
        loginCard = findViewById(R.id.login_card);
    }

    private void setupAnimations() {
        // Animaciones de entrada profesionales
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideInFromBottom = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);

        // Aplicar animaciones con delays progresivos
        loginCard.setAlpha(0f);
        loginCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(200)
                .start();

        ivLoginLogo.startAnimation(fadeIn);

        handler.postDelayed(() -> {
            tilUsername.startAnimation(slideInFromBottom);
        }, 300);

        handler.postDelayed(() -> {
            tilPassword.startAnimation(slideInFromBottom);
        }, 400);

        handler.postDelayed(() -> {
            btnCard.startAnimation(fadeIn);
        }, 500);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        // Click en el card del botón para mejor UX
        btnCard.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        // Limpiar errores previos
        clearErrors();

        // Obtener datos de entrada
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validar campos
        if (!validateInputs(username, password)) {
            return;
        }

        // Mostrar estado de carga
        showLoadingState(true);

        // Simular proceso de autenticación
        handler.postDelayed(() -> {
            validateCredentials(username, password);
        }, LOGIN_DELAY);
    }

    private boolean validateInputs(String username, String password) {
        boolean isValid = true;

        // Validar usuario
        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("El usuario es obligatorio");
            tilUsername.setErrorEnabled(true);
            isValid = false;
        } else {
            tilUsername.setError(null);
            tilUsername.setErrorEnabled(false);
        }

        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("La contraseña es obligatoria");
            tilPassword.setErrorEnabled(true);
            isValid = false;
        } else if (password.length() < 3) {
            tilPassword.setError("La contraseña debe tener al menos 3 caracteres");
            tilPassword.setErrorEnabled(true);
            isValid = false;
        } else {
            tilPassword.setError(null);
            tilPassword.setErrorEnabled(false);
        }

        return isValid;
    }

    private void validateCredentials(String username, String password) {
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            // Login exitoso
            showSuccessState();
            handler.postDelayed(this::navigateToMainApp, 800);
        } else {
            // Login fallido
            showErrorState("Usuario o contraseña incorrectos");
        }
    }

    private void showLoadingState(boolean show) {
        if (show) {
            btnLogin.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            btnCard.animate().alpha(0.7f).setDuration(200).start();
        } else {
            btnLogin.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            btnCard.animate().alpha(1f).setDuration(200).start();
        }
    }

    private void showErrorState(String errorMessage) {
        showLoadingState(false);

        // Mostrar mensaje de error con animación
        tvError.setText(errorMessage);
        tvError.setVisibility(View.VISIBLE);
        tvError.setAlpha(0f);
        tvError.animate()
                .alpha(1f)
                .setDuration(400)
                .start();

        // Animación de shake para el card
        Animation shake = AnimationUtils.loadAnimation(this, android.R.anim.cycle_interpolator);
        loginCard.startAnimation(shake);

        // Limpiar campos de entrada
        etPassword.setText("");
        etPassword.requestFocus();
    }

    private void showSuccessState() {
        showLoadingState(false);
        clearErrors();

        // Animación de éxito
        btnCard.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(200)
                .withEndAction(() -> {
                    btnCard.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start();
                })
                .start();

        // Cambiar texto del botón temporalmente
        btnLogin.setText("¡ACCESO CONCEDIDO!");
    }

    private void clearErrors() {
        tvError.setVisibility(View.GONE);
        tilUsername.setError(null);
        tilPassword.setError(null);
        tilUsername.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    private void navigateToMainApp() {
        // Aquí navegarías a tu actividad principal
        // Intent intent = new Intent(this, MainActivity.class);
        // startActivity(intent);
        // finish();

        // Por ahora, mostrar mensaje temporal
        btnLogin.setText("¡Bienvenido!");

        // Animación de salida
        loginCard.animate()
                .alpha(0f)
                .translationY(-50f)
                .setDuration(500)
                .withEndAction(() -> {
                    // Aquí iría la navegación real
                    finish();
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        // Animación suave al salir
        loginCard.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    LoginActivity.super.onBackPressed();
                })
                .start();
    }
}