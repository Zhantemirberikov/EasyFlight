package com.example.easyflight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightResultsActivity extends AppCompatActivity {

    private ListView flightListView;
    private TextView searchInfo;
    private final String API_KEY = "8d4bbf361c79023f7553e20a7bd9eab2";
    private List<FlightResponse.Data> flightDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Результаты");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        flightListView = findViewById(R.id.flight_list_view);
        searchInfo = findViewById(R.id.search_info);

        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");

        searchInfo.setText("Рейсы: " + from + " → " + to);

        loadFlights(from, to);
    }

    private void loadFlights(String from, String to) {
        FlightApi api = FlightApiService.getApi();
        Call<FlightResponse> call = api.getFlights(API_KEY, from, to);

        call.enqueue(new Callback<FlightResponse>() {
            @Override
            public void onResponse(Call<FlightResponse> call, Response<FlightResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flightDataList = response.body().data;
                    List<String> flightStrings = new ArrayList<>();
                    for (FlightResponse.Data flight : flightDataList) {
                        flightStrings.add(flight.toString() + "\n[Нажмите, чтобы забронировать]");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FlightResultsActivity.this,
                            android.R.layout.simple_list_item_1, flightStrings);
                    flightListView.setAdapter(adapter);

                    if (flightStrings.isEmpty()) {
                        Toast.makeText(FlightResultsActivity.this, "Рейсы не найдены", Toast.LENGTH_SHORT).show();
                    }

                    flightListView.setOnItemClickListener((parent, view, position, id) -> {
                        FlightResponse.Data selectedFlight = flightDataList.get(position);
                        Intent intent = new Intent(FlightResultsActivity.this, BookingConfirmationActivity.class);
                        intent.putExtra("flight", selectedFlight);
                        startActivity(intent);
                    });

                } else {
                    Toast.makeText(FlightResultsActivity.this, "Ошибка загрузки рейсов", Toast.LENGTH_SHORT).show();
                    Log.e("API", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<FlightResponse> call, Throwable t) {
                Toast.makeText(FlightResultsActivity.this, "Ошибка подключения к серверу", Toast.LENGTH_SHORT).show();
                Log.e("API", "Failure: " + t.getMessage(), t);
            }
        });
    }
}
