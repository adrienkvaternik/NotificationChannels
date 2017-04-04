package fr.akvaternik.notificationchannels;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String ARTICLE_CHANNEL_ID = "article_channel";

    private NotificationManager notificationManager;
    private int notificationId = 0;
    private TextView configurationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();

        configureNotifications();
    }

    private void bindUI() {
        Button notifyButton = (Button) findViewById(R.id.notify_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        Button configurationButton = (Button) findViewById(R.id.configuration_button);
        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayChannelConfiguration();
            }
        });

        configurationTextView = (TextView) findViewById(R.id.configuration_textview);

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChannelSettings();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChannel();
            }
        });
    }

    private void configureNotifications() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel articleChannel = new NotificationChannel(ARTICLE_CHANNEL_ID, getString(R.string.article_channel_name), NotificationManager.IMPORTANCE_DEFAULT);

        articleChannel.enableLights(true);
        articleChannel.setLightColor(Color.BLUE);
        articleChannel.enableVibration(true);
        articleChannel.setVibrationPattern(new long[]{100, 100});

        notificationManager.createNotificationChannel(articleChannel);
    }

    private void sendNotification() {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.new_article_title))
                .setContentText(getString(R.string.new_article_text))
                .setChannel(ARTICLE_CHANNEL_ID)
                .build();

        notificationManager.notify(notificationId, notification);
        notificationId++;
    }

    private void displayChannelConfiguration() {
        NotificationChannel channel = notificationManager.getNotificationChannel(ARTICLE_CHANNEL_ID);
        if (channel != null) {
            CharSequence name = channel.getName();
            int importance = channel.getImportance();
            int lightColor = channel.getLightColor();
            long[] vibrationPattern = channel.getVibrationPattern();

            String configurationText = "Name: " + name + "\n" +
                    "Importance: " + importance + "\n" +
                    "Color: " + String.format("#%06X", 0xFFFFFF & lightColor) + "\n" +
                    "Pattern: " + Arrays.toString(vibrationPattern);

            configurationTextView.setText(configurationText);
        }
    }

    private void goToChannelSettings() {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, ARTICLE_CHANNEL_ID);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void deleteChannel() {
        notificationManager.deleteNotificationChannel(ARTICLE_CHANNEL_ID);
    }
}
