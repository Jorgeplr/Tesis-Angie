package com.example.tesis_angie.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis_angie.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnvironmentalFactorsActivity extends AppCompatActivity {
    
    // Views principales
    private DrawerLayout drawerLayout;
    private TextView tvCurrentTemperature, tvCurrentHumidity, tvCurrentLight, tvCurrentPh;
    private TextView tvTemperatureStatus, tvHumidityStatus, tvLightStatus, tvPhStatus;
    private TextView tvNoAlerts;
    private RecyclerView rvAlerts;
    private MaterialButton btnHistoricalData, btnExportData;
    
    // Handler para actualizaciones en tiempo real
    private Handler handler;
    private Runnable updateDataRunnable;
    private Random random = new Random();
    
    // Lista de alertas (simuladas)
    private List<EnvironmentalAlert> alertsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmental_factors);
        
        initializeViews();
        initializeTopBar();
        setupAlerts();
        startRealTimeUpdates();
        setupClickListeners();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        
        // TextViews de valores actuales
        tvCurrentTemperature = findViewById(R.id.tv_current_temperature);
        tvCurrentHumidity = findViewById(R.id.tv_current_humidity);
        tvCurrentLight = findViewById(R.id.tv_current_light);
        tvCurrentPh = findViewById(R.id.tv_current_ph);
        
        // TextViews de estado
        tvTemperatureStatus = findViewById(R.id.tv_temperature_status);
        tvHumidityStatus = findViewById(R.id.tv_humidity_status);
        tvLightStatus = findViewById(R.id.tv_light_status);
        tvPhStatus = findViewById(R.id.tv_ph_status);
        
        // RecyclerView y mensaje de no alertas
        rvAlerts = findViewById(R.id.rv_alerts);
        tvNoAlerts = findViewById(R.id.tv_no_alerts);
        
        // Botones
        btnHistoricalData = findViewById(R.id.btn_historical_data);
        btnExportData = findViewById(R.id.btn_export_data);
        
        // Configurar RecyclerView
        rvAlerts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupAlerts() {
        alertsList = new ArrayList<>();
        
        // Simular algunas alertas (en una app real, estas vendrían del servidor/base de datos)
        if (random.nextBoolean()) {
            alertsList.add(new EnvironmentalAlert(
                "Temperatura Alta", 
                "La temperatura ha superado los 30°C", 
                "Advertencia",
                "Hace 5 min"
            ));
        }
        
        if (random.nextBoolean()) {
            alertsList.add(new EnvironmentalAlert(
                "Humedad Baja", 
                "La humedad está por debajo del 40%", 
                "Atención",
                "Hace 15 min"
            ));
        }
        
        updateAlertsDisplay();
    }

    private void updateAlertsDisplay() {
        if (alertsList.isEmpty()) {
            rvAlerts.setVisibility(View.GONE);
            tvNoAlerts.setVisibility(View.VISIBLE);
        } else {
            rvAlerts.setVisibility(View.VISIBLE);
            tvNoAlerts.setVisibility(View.GONE);
            
            // En una implementación real, aquí configurarías el adapter
            // AlertsAdapter adapter = new AlertsAdapter(alertsList);
            // rvAlerts.setAdapter(adapter);
        }
    }

    private void startRealTimeUpdates() {
        handler = new Handler(Looper.getMainLooper());
        updateDataRunnable = new Runnable() {
            @Override
            public void run() {
                updateEnvironmentalData();
                handler.postDelayed(this, 5000); // Actualizar cada 5 segundos
            }
        };
        
        // Iniciar actualizaciones
        updateEnvironmentalData();
        handler.postDelayed(updateDataRunnable, 5000);
    }

    private void updateEnvironmentalData() {
        // Simular datos en tiempo real (en una app real, estos vendrían de sensores/API)
        
        // Temperatura (20-35°C)
        float temperature = 20 + random.nextFloat() * 15;
        tvCurrentTemperature.setText(String.format("%.1f°C", temperature));
        updateTemperatureStatus(temperature);
        
        // Humedad (30-90%)
        int humidity = 30 + random.nextInt(61);
        tvCurrentHumidity.setText(humidity + "%");
        updateHumidityStatus(humidity);
        
        // Luz (300-1200 lux)
        int light = 300 + random.nextInt(901);
        tvCurrentLight.setText(light + " lux");
        updateLightStatus(light);
        
        // pH (5.5-8.5)
        float ph = 5.5f + random.nextFloat() * 3;
        tvCurrentPh.setText(String.format("%.1f", ph));
        updatePhStatus(ph);
    }

    private void updateTemperatureStatus(float temperature) {
        if (temperature < 18) {
            tvTemperatureStatus.setText("Baja");
            tvTemperatureStatus.setBackgroundResource(R.drawable.status_warning_background);
        } else if (temperature <= 28) {
            tvTemperatureStatus.setText("Normal");
            tvTemperatureStatus.setBackgroundResource(R.drawable.status_normal_background);
        } else {
            tvTemperatureStatus.setText("Alta");
            tvTemperatureStatus.setBackgroundResource(R.drawable.status_danger_background);
        }
    }

    private void updateHumidityStatus(int humidity) {
        if (humidity < 40) {
            tvHumidityStatus.setText("Baja");
            tvHumidityStatus.setBackgroundResource(R.drawable.status_warning_background);
        } else if (humidity <= 70) {
            tvHumidityStatus.setText("Normal");
            tvHumidityStatus.setBackgroundResource(R.drawable.status_normal_background);
        } else {
            tvHumidityStatus.setText("Alta");
            tvHumidityStatus.setBackgroundResource(R.drawable.status_danger_background);
        }
    }

    private void updateLightStatus(int light) {
        if (light < 500) {
            tvLightStatus.setText("Baja");
            tvLightStatus.setBackgroundResource(R.drawable.status_warning_background);
        } else if (light <= 1000) {
            tvLightStatus.setText("Buena");
            tvLightStatus.setBackgroundResource(R.drawable.status_normal_background);
        } else {
            tvLightStatus.setText("Intensa");
            tvLightStatus.setBackgroundResource(R.drawable.status_warning_background);
        }
    }

    private void updatePhStatus(float ph) {
        if (ph < 6.0 || ph > 7.5) {
            tvPhStatus.setText("Fuera rango");
            tvPhStatus.setBackgroundResource(R.drawable.status_danger_background);
        } else if (ph < 6.3 || ph > 7.2) {
            tvPhStatus.setText("Aceptable");
            tvPhStatus.setBackgroundResource(R.drawable.status_warning_background);
        } else {
            tvPhStatus.setText("Óptimo");
            tvPhStatus.setBackgroundResource(R.drawable.status_normal_background);
        }
    }

    private void setupClickListeners() {
        btnHistoricalData.setOnClickListener(v -> {
            // En una implementación real, abriría una actividad con gráficos históricos
            Toast.makeText(this, "Mostrando datos históricos...", Toast.LENGTH_SHORT).show();
        });
        
        btnExportData.setOnClickListener(v -> {
            // En una implementación real, exportaría datos a CSV/Excel
            Toast.makeText(this, "Exportando datos...", Toast.LENGTH_SHORT).show();
        });
    }

    // Inicializa el topbar: muestra botón atrás y oculta botón de menú (porque estamos en pantalla hija)
    private void initializeTopBar() {
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        View backBtn = findViewById(R.id.topbar_back_btn);
        TextView title = findViewById(R.id.topbar_title);

        if (menuBtn != null) menuBtn.setVisibility(View.GONE);
        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> finish());
        }
        if (title != null) title.setText("Factores Ambientales");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && updateDataRunnable != null) {
            handler.removeCallbacks(updateDataRunnable);
        }
    }

    // Clase interna para representar alertas ambientales
    private static class EnvironmentalAlert {
        private String title;
        private String description;
        private String severity;
        private String timestamp;

        public EnvironmentalAlert(String title, String description, String severity, String timestamp) {
            this.title = title;
            this.description = description;
            this.severity = severity;
            this.timestamp = timestamp;
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getSeverity() { return severity; }
        public String getTimestamp() { return timestamp; }
    }
}