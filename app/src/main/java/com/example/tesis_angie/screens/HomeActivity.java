package com.example.tesis_angie.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.tesis_angie.R;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private TextView tvGreeting;
    private TextView tvTemperature, tvHumidity, tvPlants;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

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
        // Abrir Reportes desde la tarjeta
        findViewById(R.id.card_reports).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, com.example.tesis_angie.screens.ReportsActivity.class);
            startActivity(intent);
        });
        // Abrir Factores Ambientales desde la tarjeta
        findViewById(R.id.card_weather).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EnvironmentalFactorsActivity.class);
            startActivity(intent);
        });
        // Abrir Configuración del Sistema desde la tarjeta
        findViewById(R.id.card_settings).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SystemConfigurationActivity.class);
            startActivity(intent);
        });
    }

    private void setupGreeting() {
        if (tvGreeting != null) {
            tvGreeting.setText(getString(R.string.welcome_message));
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
                if (tvTemperature != null) tvTemperature.setText(getString(R.string.temp_format, temperature));
                if (tvHumidity != null) tvHumidity.setText(getString(R.string.humidity_format, humidity));
                if (tvPlants != null) tvPlants.setText(getString(R.string.plants_format, plantsStatus));

                // Repetir la simulación cada 5 segundos
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(updateStatsRunnable);
    }

    private void setupDrawerLogic() {
        // Inicializar NavigationView
        navigationView = findViewById(R.id.navigation_view);

        // Configurar ActionBarDrawerToggle para que el topbar abra/cierre el drawer
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        if (menuBtn != null) {
            menuBtn.setOnClickListener(v -> {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            });
        }

        // Actualizar header con datos (si están disponibles)
        if (navigationView != null) {
            View header = navigationView.getHeaderView(0);
            TextView name = header.findViewById(R.id.nav_header_name);
            TextView email = header.findViewById(R.id.nav_header_email);
            // Aquí podrías cargar el nombre real desde SharedPreferences o perfil
            name.setText("Usuario Demo");
            email.setText("usuario@ejemplo.com");

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    int id = item.getItemId();
                    Intent intent;
                    switch (id) {
                        case R.id.nav_user:
                            intent = new Intent(HomeActivity.this, UserAdminActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_environment:
                            intent = new Intent(HomeActivity.this, EnvironmentalFactorsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_reports:
                            intent = new Intent(HomeActivity.this, com.example.tesis_angie.screens.ReportsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_settings:
                            intent = new Intent(HomeActivity.this, SystemConfigurationActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_logout:
                            Intent logout = new Intent(HomeActivity.this, LoginActivity.class);
                            logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(logout);
                            finish();
                            break;
                    }
                    drawerLayout.closeDrawer(navigationView);
                    return true;
                }
            });
        }
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
