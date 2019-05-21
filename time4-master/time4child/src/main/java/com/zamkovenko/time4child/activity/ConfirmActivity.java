package com.zamkovenko.time4child.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.zamkovenko.time4child.R;
import com.zamkovenko.time4child.SendMessageTask;
import com.zamkovenko.time4child.widgets.MySwipeButton;
import com.zamkovenko.utils.model.Message;

import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 09.01.2018
 */

public class ConfirmActivity extends AppCompatActivity {

    public static String PARAM_NOTIFICATION_ID = "notification_id";

    private short messageId;

    private Ringtone m_ringtone;

    private SeekBar slider;
    private boolean sliderClicked = true;
    private int firstI = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        messageId = getIntent().getExtras().getShort(PARAM_NOTIFICATION_ID, (short) -1);
        if (messageId == -1) {
            Log.d("ConfirmActivity", "Message id == -1");
            finish();
        }

        MySwipeButton mySwipeButton = new MySwipeButton(ConfirmActivity.this,
                null, null);

        mySwipeButton.setOnSwipeButtonListener(new MySwipeButton.SwipeButtonListener() {

            @Override
            public void swipeButtonOn(ImageView slidingButton) {
                   slideConfirm();
            }

            @Override
            public void swipeButtonOff(ImageView slidingButton) {

            }
        });

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        m_ringtone = RingtoneManager.getRingtone(ConfirmActivity.this, notification);
        m_ringtone.play();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SendSms();
    }

    private void SendSms(){
        Message newMessage = new Message().getBuilder().setId(messageId).build();
        new SendMessageTask(ConfirmActivity.this).execute(newMessage);
        m_ringtone.stop();
        finish();
    }

    private void slideConfirm(){
        if (ContextCompat.checkSelfPermission(ConfirmActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(ConfirmActivity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    0);
        } else {
            // permission already granted run sms send
            SendSms();
        }
    }
}
