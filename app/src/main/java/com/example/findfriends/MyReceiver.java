package com.example.findfriends;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast makeText(context);
        String messageBody, phoneNumber;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();

                    Toast.makeText(context,
                            "Message : " + messageBody + "Reçu de la part de;" + phoneNumber,
                            Toast.LENGTH_LONG)
                            .show();

                    if (messageBody.contains("#FindFriends:envoyer moi votre position")) {
                        //affichage de notification

                        // lancer une notification dans le canal myapplication_channel
                        NotificationCompat.Builder mynotif =
                                new NotificationCompat.Builder(
                                        context, "findfriends_canal");
                        mynotif.setContentTitle("demande position reçue");
                        mynotif.setContentText(phoneNumber + "à envoyé sa position");
                        mynotif.setSmallIcon(android.R.drawable.ic_dialog_map);
                        mynotif.setAutoCancel(true);
                        mynotif.setVibrate(new long[]{500, 1000, 200, 2000}); // il faut ajouter la permission VIBRATE dans le manifest
                        Uri son = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        mynotif.setSound(son);

                        // instance du gestionnaire des notifications de l'appareil
                        NotificationManagerCompat manager =
                                NotificationManagerCompat.from(context);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                        {/* creation du canal si la version android de l'appareil est supérieur à
Oreo */
                            NotificationChannel canal=new
                                    NotificationChannel("findfriends_canal",
                                    // l'ID exacte du canal
                                    "canal pour lapplication find me",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                            AudioAttributes attr=new AudioAttributes.Builder()

                                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .build();
                            // ajouter du son pour le canal
                            canal.setSound(son,attr);
                            // creation du canal dans l'appareil
                            manager.createNotificationChannel(canal);
                        }

                        manager.notify(0, mynotif.build());




                    }
                }
            }
        }
    }
}