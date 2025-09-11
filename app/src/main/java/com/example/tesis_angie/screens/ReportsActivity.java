package com.example.tesis_angie.screens;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tesis_angie.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Inicializar vistas localmente
        TextView tvTemp = findViewById(R.id.tv_report_temp);
        TextView tvHumidity = findViewById(R.id.tv_report_humidity);
        TextView tvPrecip = findViewById(R.id.tv_report_precip);

        // Top bar: usar el botón de menú incluido como botón de vuelta en pantallas internas
        // Inicializar topbar interno
        View menuBtn = findViewById(R.id.topbar_menu_btn);
        View backBtn = findViewById(R.id.topbar_back_btn);
        TextView title = findViewById(R.id.topbar_title);
        if (menuBtn != null) menuBtn.setVisibility(View.GONE);
        if (backBtn != null) {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> finish());
        }
        if (title != null) title.setText("Reportes");

        // FAB exportar
        FloatingActionButton fab = findViewById(R.id.fab_export);
        if (fab != null) {
            fab.setOnClickListener(v -> Toast.makeText(this, getString(R.string.export_toast), Toast.LENGTH_SHORT).show());
        }

        // Valores por defecto (placeholder) usando recursos
        if (tvTemp != null) tvTemp.setText(getString(R.string.temperature_placeholder));
        if (tvHumidity != null) tvHumidity.setText(getString(R.string.humidity_placeholder));
        if (tvPrecip != null) tvPrecip.setText(getString(R.string.report_precip_placeholder));
    }
}
