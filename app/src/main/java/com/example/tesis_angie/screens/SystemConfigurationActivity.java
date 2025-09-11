package com.example.tesis_angie.screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tesis_angie.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class SystemConfigurationActivity extends AppCompatActivity {
    
    // Views principales
    private DrawerLayout drawerLayout;
    private TextInputEditText etSensorInterval, etTempMin, etTempMax;
    private TextInputEditText etHumidityMin, etHumidityMax, etServerUrl;
    private SwitchMaterial switchNotifications;
    private TextView tvConnectionStatus;
    
    // Botones
    private MaterialButton btnUpdateInterval, btnCalibrateTemperature, btnCalibrateHumidity;
    private MaterialButton btnSaveAlertConfig, btnTestConnection, btnSaveNetworkConfig;
    private MaterialButton btnClearCache, btnExportLogs;
    
    // SharedPreferences para guardar configuraciones
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "SystemConfig";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_configuration);
        
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        initializeViews();
        initializeTopBar();
        loadSavedConfigurations();
        setupClickListeners();
        updateConnectionStatus();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        
        // EditTexts
        etSensorInterval = findViewById(R.id.et_sensor_interval);
        etTempMin = findViewById(R.id.et_temp_min);
        etTempMax = findViewById(R.id.et_temp_max);
        etHumidityMin = findViewById(R.id.et_humidity_min);
        etHumidityMax = findViewById(R.id.et_humidity_max);
        etServerUrl = findViewById(R.id.et_server_url);
        
        // Switch
        switchNotifications = findViewById(R.id.switch_notifications);
        
        // TextViews
        tvConnectionStatus = findViewById(R.id.tv_connection_status);
        
        // Botones
        btnUpdateInterval = findViewById(R.id.btn_update_interval);
        btnCalibrateTemperature = findViewById(R.id.btn_calibrate_temperature);
        btnCalibrateHumidity = findViewById(R.id.btn_calibrate_humidity);
        btnSaveAlertConfig = findViewById(R.id.btn_save_alert_config);
        btnTestConnection = findViewById(R.id.btn_test_connection);
        btnSaveNetworkConfig = findViewById(R.id.btn_save_network_config);
        btnClearCache = findViewById(R.id.btn_clear_cache);
        btnExportLogs = findViewById(R.id.btn_export_logs);
    }

    private void loadSavedConfigurations() {
        // Cargar configuraciones guardadas
        etSensorInterval.setText(String.valueOf(preferences.getInt("sensor_interval", 30)));
        etTempMin.setText(String.valueOf(preferences.getFloat("temp_min", 18.0f)));
        etTempMax.setText(String.valueOf(preferences.getFloat("temp_max", 28.0f)));
        etHumidityMin.setText(String.valueOf(preferences.getInt("humidity_min", 40)));
        etHumidityMax.setText(String.valueOf(preferences.getInt("humidity_max", 70)));
        etServerUrl.setText(preferences.getString("server_url", "https://api.cacaomonitor.com"));
        switchNotifications.setChecked(preferences.getBoolean("notifications_enabled", true));
    }

    private void setupClickListeners() {
        
        // Actualizar intervalo de sensores
        btnUpdateInterval.setOnClickListener(v -> {
            try {
                int interval = Integer.parseInt(etSensorInterval.getText().toString());
                if (interval < 5) {
                    Toast.makeText(this, "El intervalo mínimo es 5 segundos", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                preferences.edit().putInt("sensor_interval", interval).apply();
                Toast.makeText(this, "Intervalo actualizado correctamente", Toast.LENGTH_SHORT).show();
                
                // En una implementación real, aquí se enviaría la configuración al dispositivo
                
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese un número válido", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Calibrar sensores
        btnCalibrateTemperature.setOnClickListener(v -> {
            // En una implementación real, esto iniciaría el proceso de calibración
            Toast.makeText(this, "Iniciando calibración de temperatura...", Toast.LENGTH_LONG).show();
            
            // Simular proceso de calibración
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Simular tiempo de calibración
                    runOnUiThread(() -> 
                        Toast.makeText(this, "Calibración de temperatura completada", Toast.LENGTH_SHORT).show()
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        
        btnCalibrateHumidity.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando calibración de humedad...", Toast.LENGTH_LONG).show();
            
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    runOnUiThread(() -> 
                        Toast.makeText(this, "Calibración de humedad completada", Toast.LENGTH_SHORT).show()
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        
        // Guardar configuración de alertas
        btnSaveAlertConfig.setOnClickListener(v -> {
            try {
                float tempMin = Float.parseFloat(etTempMin.getText().toString());
                float tempMax = Float.parseFloat(etTempMax.getText().toString());
                int humidityMin = Integer.parseInt(etHumidityMin.getText().toString());
                int humidityMax = Integer.parseInt(etHumidityMax.getText().toString());
                boolean notificationsEnabled = switchNotifications.isChecked();
                
                // Validaciones
                if (tempMin >= tempMax) {
                    Toast.makeText(this, "La temperatura mínima debe ser menor que la máxima", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if (humidityMin >= humidityMax) {
                    Toast.makeText(this, "La humedad mínima debe ser menor que la máxima", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Guardar configuraciones
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("temp_min", tempMin);
                editor.putFloat("temp_max", tempMax);
                editor.putInt("humidity_min", humidityMin);
                editor.putInt("humidity_max", humidityMax);
                editor.putBoolean("notifications_enabled", notificationsEnabled);
                editor.apply();
                
                Toast.makeText(this, "Configuración de alertas guardada", Toast.LENGTH_SHORT).show();
                
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor ingrese valores válidos", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Probar conexión
        btnTestConnection.setOnClickListener(v -> {
            String serverUrl = etServerUrl.getText().toString().trim();
            
            if (serverUrl.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese una URL válida", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Toast.makeText(this, "Probando conexión...", Toast.LENGTH_SHORT).show();
            
            // Simular prueba de conexión
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Simular tiempo de conexión
                    
                    // Simular resultado (en una app real, haría una petición HTTP)
                    boolean connectionSuccessful = Math.random() > 0.3; // 70% de éxito
                    
                    runOnUiThread(() -> {
                        if (connectionSuccessful) {
                            tvConnectionStatus.setText("Conectado");
                            tvConnectionStatus.setBackgroundResource(R.drawable.status_normal_background);
                            Toast.makeText(this, "Conexión exitosa", Toast.LENGTH_SHORT).show();
                        } else {
                            tvConnectionStatus.setText("Sin conexión");
                            tvConnectionStatus.setBackgroundResource(R.drawable.status_danger_background);
                            Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        
        // Guardar configuración de red
        btnSaveNetworkConfig.setOnClickListener(v -> {
            String serverUrl = etServerUrl.getText().toString().trim();
            
            if (serverUrl.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese una URL válida", Toast.LENGTH_SHORT).show();
                return;
            }
            
            preferences.edit().putString("server_url", serverUrl).apply();
            Toast.makeText(this, "Configuración de red guardada", Toast.LENGTH_SHORT).show();
        });
        
        // Limpiar caché
        btnClearCache.setOnClickListener(v -> {
            // En una implementación real, esto limpiaría archivos de caché, base de datos temporal, etc.
            Toast.makeText(this, "Limpiando caché...", Toast.LENGTH_SHORT).show();
            
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    runOnUiThread(() -> 
                        Toast.makeText(this, "Caché limpiado correctamente", Toast.LENGTH_SHORT).show()
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        
        // Exportar logs
        btnExportLogs.setOnClickListener(v -> {
            // En una implementación real, esto exportaría logs del sistema
            Toast.makeText(this, "Exportando logs del sistema...", Toast.LENGTH_SHORT).show();
            
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(() -> 
                        Toast.makeText(this, "Logs exportados a Downloads/", Toast.LENGTH_SHORT).show()
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    // Inicializa el topbar para la pantalla de configuración
    private void initializeTopBar() {
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        View backBtn = findViewById(R.id.topbar_back_btn);
        TextView title = findViewById(R.id.topbar_title);

        if (menuBtn != null) menuBtn.setVisibility(View.GONE);
        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> finish());
        }
        if (title != null) title.setText("Configuración del Sistema");
    }

    private void updateConnectionStatus() {
        // Verificar estado de conexión inicial
        // En una implementación real, esto verificaría la conectividad real
        boolean isConnected = Math.random() > 0.2; // 80% conectado por defecto
        
        if (isConnected) {
            tvConnectionStatus.setText("Conectado");
            tvConnectionStatus.setBackgroundResource(R.drawable.status_normal_background);
        } else {
            tvConnectionStatus.setText("Sin conexión");
            tvConnectionStatus.setBackgroundResource(R.drawable.status_danger_background);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Guardar cualquier configuración pendiente si es necesario
    }
}