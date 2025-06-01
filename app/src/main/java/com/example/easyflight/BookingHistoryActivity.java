package com.example.easyflight;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BookingHistoryActivity extends AppCompatActivity {

    private TextView historyTextView;
    private Button btnClearHistory;
    private static final String PREFS_NAME = "FlightBookings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("История билетов");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        historyTextView = findViewById(R.id.history_text);
        btnClearHistory = findViewById(R.id.btn_clear_history);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String history = sharedPreferences.getString("tickets", "История пуста.");
        historyTextView.setText(history);

        btnClearHistory.setOnClickListener(v -> {
            sharedPreferences.edit().remove("tickets").apply();
            historyTextView.setText("История пуста.");
        });
    }
}
