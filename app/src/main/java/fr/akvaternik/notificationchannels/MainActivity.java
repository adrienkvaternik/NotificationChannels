package fr.akvaternik.notificationchannels;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ARTICLE_CHANNEL_ID = "article_channel";
    public static final String PERSONAL_ARTICLE_CHANNEL_ID = "personal_article_channel";
    public static final String BUSINESS_ARTICLE_CHANNEL_ID = "business_article_channel";

    public static final String PERSONAL_GROUP_ID = "personal_group";
    public static final String BUSINESS_GROUP_ID = "business_group";

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
                sendNotification(ARTICLE_CHANNEL_ID);
            }
        });

        Button configurationButton = (Button) findViewById(R.id.configuration_button);
        configurationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayChannelConfiguration(ARTICLE_CHANNEL_ID);
            }
        });

        configurationTextView = (TextView) findViewById(R.id.configuration_textview);

        Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToChannelSettings(ARTICLE_CHANNEL_ID);
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChannel(ARTICLE_CHANNEL_ID);
            }
        });

        Button notifyPersonalGroupButton = (Button) findViewById(R.id.notify_personal_group_button);
        notifyPersonalGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification(PERSONAL_ARTICLE_CHANNEL_ID);
            }
        });
    }

    private void configureNotifications() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannelGroups();
        createNotificationChannels();
    }

    private void createNotificationChannelGroups() {
        NotificationChannelGroup personalGroup = new NotificationChannelGroup(PERSONAL_GROUP_ID, getString(R.string.personal_group_name));
        NotificationChannelGroup businessGroup = new NotificationChannelGroup(BUSINESS_GROUP_ID, getString(R.string.business_group_name));

        List<NotificationChannelGroup> groups = Arrays.asList(personalGroup, businessGroup);

        notificationManager.createNotificationChannelGroups(groups);
    }

    private void createNotificationChannels() {
        NotificationChannel articleChannel = new NotificationChannel(ARTICLE_CHANNEL_ID, getString(R.string.article_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        articleChannel.enableLights(true);
        articleChannel.setLightColor(Color.BLUE);
        articleChannel.enableVibration(true);
        articleChannel.setVibrationPattern(new long[]{100, 100});

        NotificationChannel personalArticleChannel = new NotificationChannel(PERSONAL_ARTICLE_CHANNEL_ID, getString(R.string.article_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        personalArticleChannel.setGroup(PERSONAL_GROUP_ID);

        NotificationChannel businessArticleChannel = new NotificationChannel(BUSINESS_ARTICLE_CHANNEL_ID, getString(R.string.article_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
        businessArticleChannel.setGroup(BUSINESS_GROUP_ID);

        List<NotificationChannel> channels = Arrays.asList(articleChannel, personalArticleChannel, businessArticleChannel);

        notificationManager.createNotificationChannels(channels);
    }

    private void sendNotification(String channelId) {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.new_article_title))
                .setContentText(getString(R.string.new_article_text))
                .setChannel(channelId)
                .build();

        notificationManager.notify(notificationId, notification);
        notificationId++;
    }

    private void displayChannelConfiguration(String channelId) {
        NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
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

    private void goToChannelSettings(String channelId) {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        startActivity(intent);
    }

    private void deleteChannel(String channelId) {
        notificationManager.deleteNotificationChannel(channelId);
    }
}
