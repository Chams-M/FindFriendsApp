package com.example.findfriends;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationService extends Service {
    String numero;



    public MyLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        numero=intent.getStringExtra("numero");
        //pour récuperer position GPS
        FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                sendSms(location);
            }
        });
        LocationRequest req=LocationRequest.create().setSmallestDisplacement(100).setFastestInterval(60000);

        LocationCallback action=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location=locationResult.getLastLocation();
                sendSms(location);
            }
        };


        mClient.requestLocationUpdates(req,action,null);



        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSms(Location location) {
        if(location!=null) {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(
                    numero,
                    null,
                    "FindFriends:ma position est : #" + location.getLongitude() + "#" + location.getAltitude(),
                    null,
                    null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}