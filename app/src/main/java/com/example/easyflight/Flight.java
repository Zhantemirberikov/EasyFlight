package com.example.easyflight;

public class Flight {
    public String airline_name;
    public String flight_number;
    public String departure_airport;
    public String arrival_airport;
    public String departure_scheduled;

    @Override
    public String toString() {
        return airline_name + " | " + flight_number + "\n" +
                departure_airport + " → " + arrival_airport + "\n" +
                "Вылет: " + departure_scheduled;
    }
}
