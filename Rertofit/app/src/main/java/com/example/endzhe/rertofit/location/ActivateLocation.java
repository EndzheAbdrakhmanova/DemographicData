package com.example.endzhe.rertofit.location;


import android.annotation.SuppressLint;
import android.content.Context;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class ActivateLocation implements LocationListener {
    final String LOG = "location";
    LocationManager locationManager;
    String countryName;

    /**
     * Работа с геолокацией, геокодирование используя класс Geocoder.
     */

    @SuppressLint("MissingPermission")
    public ActivateLocation(Context context) {
        // Получаем LocationManager
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        // Получаем лучший провайдер
        Criteria criteria = new Criteria();

        String bestProvider = locationManager.getBestProvider(criteria, false);
        Log.v(LOG, "Best provider: " + bestProvider);
        if (bestProvider != null) {
            // Получаем последнюю доступную позицию
            Location lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);

            Log.v(LOG, "Last location: " + lastKnownLocation);
            if (lastKnownLocation != null) {
                double LATITUDE = lastKnownLocation.getLatitude();
                double LONGITUDE = lastKnownLocation.getLongitude();

                Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
                try {
                    List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                    if (addresses != null) {
                        countryName = addresses.get(0).getCountryName();
                        //т.к. наш API принимает Russian Federation
                        if (countryName.equals("Russia")) {
                            countryName = "Russian Federation";
                        }
                        //editText_country.setText(countryName);
                        Log.e(LOG, addresses.get(0).getAdminArea());
                    } else {
                        Toast.makeText(context, "No country returned", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(LOG, "error:" + e);
                    //Toast.makeText(context, "Can't get country", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(LOG, "error,lastKnownLocation -null");
                Toast.makeText(context, "Can't use geolocation ", Toast.LENGTH_SHORT).show();
            }
            // Подписываемся на обновления
            locationManager.requestLocationUpdates(
                    bestProvider, // провайдер, на обновления которого мы подписываемся
                    0, // минимальное время в миллисекундах, спустя которое мы будем получать обновления
                    0, // минимальное расстояние в метрах, спустя которое мы получим обновление
                    this//listener, который будет вызываться при обновлении
            );
        }


    }

    public String getCountryName() {
        return countryName;
    }

    public void unregisterLocationListener() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            Log.v(LOG, "unregisterLocationListener");
        }
    }

    /**
     * методы которые будут вызываться, когда происходят какие-то изменения, касающиеся гео
     */
    @Override
    //изменяется геопозиция
    public void onLocationChanged(Location location) {
        Log.v(LOG, "Location changed: " + location);

    }

    @Override
    //изменяется статус провайдера
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(LOG, "Status changed: " + provider + ", status: " + status);
    }

    @Override
    //один из провайдеров стал доступен
    public void onProviderEnabled(String provider) {
        Log.v(LOG, "Provider enabled: " + provider);
    }

    @Override
    //один из провайдеров стал недоступен
    public void onProviderDisabled(String provider) {
        Log.v(LOG, "Provider disabled: " + provider);
    }
}



