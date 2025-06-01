package com.example.easyflight;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText editFrom, editTo, editDate;
    private Button btnSearchFlights;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences themePrefs = getSharedPreferences("theme", MODE_PRIVATE);
        boolean isDark = themePrefs.getBoolean("dark_theme", false);
        AppCompatDelegate.setDefaultNightMode(isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Поиск авиабилетов");

        editFrom = findViewById(R.id.edit_from);
        editTo = findViewById(R.id.edit_to);
        editDate = findViewById(R.id.edit_date);
        btnSearchFlights = findViewById(R.id.btn_search_flights);
        btnProfile = findViewById(R.id.btn_profile);

        ImageView imgPlane = findViewById(R.id.img_plane);
        Glide.with(this).asGif().load(R.drawable.plane).into(imgPlane);

        btnProfile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserProfileActivity.class)));

        editDate.setOnClickListener(v -> showDatePicker());

        btnSearchFlights.setOnClickListener(v -> {
            String from = editFrom.getText().toString().trim();
            String to = editTo.getText().toString().trim();
            String date = editDate.getText().toString().trim();

            if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to) || TextUtils.isEmpty(date)) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, FlightResultsActivity.class);
            intent.putExtra("from", from);
            intent.putExtra("to", to);
            intent.putExtra("date", date);
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String formatted = year1 + "-" + String.format("%02d", month1 + 1) + "-" + String.format("%02d", dayOfMonth);
                    editDate.setText(formatted);
                }, year, month, day);
        dpd.show();

    }
}
