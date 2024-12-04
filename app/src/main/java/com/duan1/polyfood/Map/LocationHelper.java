package com.duan1.polyfood.Map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;

    public LocationHelper(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    // Phương thức lấy vị trí và trả về tọa độ
    public void getCurrentLocation(@NonNull final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onError("Permission not granted");
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                callback.onLocationRetrieved(latitude, longitude);
            } else {
                callback.onError("Unable to retrieve location");
            }
        }).addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Callback interface để trả về kết quả
    public interface LocationCallback {
        void onLocationRetrieved(double latitude, double longitude);
        void onError(String errorMessage);
    }
}
