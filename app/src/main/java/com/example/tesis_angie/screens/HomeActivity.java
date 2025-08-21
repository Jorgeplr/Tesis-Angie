package com.example.tesis_angie.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.tesis_angie.R;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private TextView tvTemperature, tvHumidity, tvPlants;
    private DrawerLayout drawerLayout;

    // Handler para actualizaciones periódicas
    private Handler handler;
    private Runnable updateStatsRunnable;

    // Datos simulados
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        initializeViews();
        setupGreeting();
        startDataSimulation();
        setupDrawerLogic();
    }

    private void initializeViews() {
        tvGreeting = findViewById(R.id.tv_welcome_title); // Usar el TextView de bienvenida principal
        tvTemperature = findViewById(R.id.tv_temperature_quick); // Actualizado al nuevo ID
        tvHumidity = findViewById(R.id.tv_humidity_quick); // Actualizado al nuevo ID
        tvPlants = findViewById(R.id.tv_plants_quick); // Actualizado al nuevo ID
        findViewById(R.id.card_user).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserAdminActivity.class);
            startActivity(intent);
        });
    }

    private void setupGreeting() {
        if (tvGreeting != null) {
            tvGreeting.setText("Bienvenido a Tesis Angie");
        }
    }

    private void startDataSimulation() {
        handler = new Handler(Looper.getMainLooper());
        updateStatsRunnable = new Runnable() {
            @Override
            public void run() {
                // Simular datos de temperatura, humedad y estado de plantas
                int temperature = random.nextInt(15) + 10; // Temperatura entre 10 y 25 grados
                int humidity = random.nextInt(40) + 30; // Humedad entre 30% y 70%
                int plantsStatus = random.nextInt(100); // Estado de las plantas entre 0 y 100

                // Actualizar las vistas con los datos simulados
                if (tvTemperature != null) tvTemperature.setText("Temp: " + temperature + "°C");
                if (tvHumidity != null) tvHumidity.setText("Humedad: " + humidity + "%");
                if (tvPlants != null) tvPlants.setText("Plantas: " + plantsStatus + "%");

                // Repetir la simulación cada 5 segundos
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(updateStatsRunnable);
    }

    private void setupDrawerLogic() {
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        if (menuBtn != null && drawerLayout != null) {
            menuBtn.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.left_drawer)));
        }
        // Opciones del Drawer
        findViewById(R.id.drawer_btn_user).setOnClickListener(v -> {
            // Abrir la ventana de administración de usuarios
            Intent intent = new Intent(HomeActivity.this, UserAdminActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.drawer_btn_clima).setOnClickListener(v -> {
            // Aquí iría la lógica para la opción Clima
        });
        findViewById(R.id.drawer_btn_reportes).setOnClickListener(v -> {
            // Aquí iría la lógica para la opción Reportes
        });
        findViewById(R.id.drawer_btn_settings).setOnClickListener(v -> {
            // Aquí iría la lógica para la opción Configuración
        });
        findViewById(R.id.drawer_btn_logout).setOnClickListener(v -> {
            // Cerrar sesión: volver al Login
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener la simulación al destruir la actividad
        if (handler != null && updateStatsRunnable != null) {
            handler.removeCallbacks(updateStatsRunnable);
        }
    }
}
