package com.example.tesis_angie.screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tesis_angie.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etLastName, etCedula, etAddress, etPhone, etUsername, etPassword;
    private MaterialButton btnRegister;
    private TextView tvHaveAccount;

    private static final String PREFS = "app_prefs";
    private static final String KEY_USER_PREFIX = "user_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = findViewById(R.id.etRegFirstName);
        etLastName = findViewById(R.id.etRegLastName);
        etCedula = findViewById(R.id.etRegCedula);
        etAddress = findViewById(R.id.etRegAddress);
        etPhone = findViewById(R.id.etRegPhone);
        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvHaveAccount = findViewById(R.id.tvHaveAccount);

        btnRegister.setOnClickListener(v -> attemptRegister());

        tvHaveAccount.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void attemptRegister() {
        String firstName = getText(etFirstName);
        String lastName = getText(etLastName);
        String cedula = getText(etCedula);
        String address = getText(etAddress);
        String phone = getText(etPhone);
        String username = getText(etUsername);
        String password = getText(etPassword);

        // Validaciones básicas
        if (TextUtils.isEmpty(firstName)) { etFirstName.setError("Ingresa el nombre"); return; }
        if (TextUtils.isEmpty(lastName)) { etLastName.setError("Ingresa el apellido"); return; }
        if (TextUtils.isEmpty(cedula)) { etCedula.setError("Ingresa la cédula"); return; }
        if (TextUtils.isEmpty(address)) { etAddress.setError("Ingresa la dirección"); return; }
        if (TextUtils.isEmpty(phone)) { etPhone.setError("Ingresa el celular"); return; }
        if (TextUtils.isEmpty(username)) { etUsername.setError("Ingresa un usuario"); return; }
        if (TextUtils.isEmpty(password) || password.length() < 6) { etPassword.setError("Contraseña de al menos 6 caracteres"); return; }

        // Guardar en SharedPreferences (simulación de registro local)
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Guardamos los datos con prefijo por usuario para permitir multicuenta simple
        String baseKey = KEY_USER_PREFIX + username + "_";
        editor.putString(baseKey + "firstName", firstName);
        editor.putString(baseKey + "lastName", lastName);
        editor.putString(baseKey + "cedula", cedula);
        editor.putString(baseKey + "address", address);
        editor.putString(baseKey + "phone", phone);
        editor.putString(baseKey + "username", username);
        editor.putString(baseKey + "password", password); // NOTA: en producción, no guardar contraseñas en texto plano
        editor.apply();

        Toast.makeText(this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();

        // Volver al login
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

}

