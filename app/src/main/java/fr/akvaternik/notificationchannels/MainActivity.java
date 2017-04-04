package fr.akvaternik.notificationchannels;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String ARTICLE_CHANNEL_ID = "article_channel";

    private NotificationManager notificationManager;
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button notifyButton = (Button) findViewById(R.id.notify_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        configureNotifications();
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
}
