package com.zamkovenko.time4parent.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.time4parent.R;
import com.zamkovenko.time4parent.fragment.DatePickerFragment;
import com.zamkovenko.time4parent.fragment.TimePickerFragment;
import com.zamkovenko.time4parent.manager.MessageManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 10.12.2017
 */

public class TaskEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{
    private static final int PERMISSION_SEND_SMS = 123;
    private static final int PERMISSION_READ_PHONE_STATE = 1234;

    private short m_id = -1;

    public static String PARAM_TASK_ID = "_id";

    private static Calendar calendar;

    private Switch m_switchIsBlink;
    private Switch m_switchIsVibration;

    private TextView txtCurrentTime;
    private TextView txtCurrentDate;
    private TextView txtTitle;

    private Locale locale;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        locale = new Locale("uk","UA");

        calendar = new GregorianCalendar(locale);
        calendar.setTime(Calendar.getInstance(locale).getTime());

        m_switchIsBlink = (Switch) findViewById(R.id.switch_blinking);
        m_switchIsVibration = (Switch) findViewById(R.id.switch_vibration);

        Button btnSetDate = (Button) findViewById(R.id.btn_setDate);
        Button btnSetTime = (Button) findViewById(R.id.btn_setTime);
        Button btnAccept = (Button) findViewById(R.id.btn_accept);
        if(Build.VERSION.SDK_INT < 21){
            btnAccept.setBackgroundResource(R.drawable.btn_style);
        }else {
            btnAccept.setBackgroundResource(R.drawable.btn_ripple);
        }

        txtTitle = (TextView) findViewById(R.id.txt_title);

        txtCurrentTime = (TextView) findViewById(R.id.txt_currentTime);
        txtCurrentDate = (TextView) findViewById(R.id.txt_currentDate);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale.setDefault(new Locale("uk","UA"));
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setListener(TaskEditActivity.this);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.setListener(TaskEditActivity.this);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckPermissionSendSms()) {
                    ActivityCompat.requestPermissions(TaskEditActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            PERMISSION_SEND_SMS);
                } else  if (!CheckPermissionGrantedReadPhoneState()) {
                    ActivityCompat.requestPermissions(TaskEditActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            PERMISSION_READ_PHONE_STATE);
                }
                else {
                    SendSms();
                }
            }
        });

        CheckForEdit();

        OnCalendarChanged();
    }

    private boolean CheckPermissionSendSms() {
        return ContextCompat.checkSelfPermission(TaskEditActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean CheckPermissionGrantedReadPhoneState() {
        return ContextCompat.checkSelfPermission(TaskEditActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void CheckForEdit(){
        final short id = getIntent().getShortExtra(PARAM_TASK_ID, (short) -1);
        if (id != -1) {
            List<Message> all = MessageManager.getInstance().GetAll();
            for (Message message : all) {
                if(message.getId() == id){
                    SetMessageForEdit(message);
                    m_id = id;
                }
            }
        }
    }

    private void SetMessageForEdit(Message message) {
        txtTitle.setText(message.getTitle());
        calendar.setTime(message.getOnDate());

        m_switchIsBlink.setChecked(message.isBlinking());
        m_switchIsVibration.setChecked(message.isVibro());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        OnCalendarChanged();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        OnCalendarChanged();
    }

    public void OnCalendarChanged() {
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy", locale);
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        txtCurrentDate.setText(formatDate.format(calendar.getTime()));
        txtCurrentTime.setText(formatTime.format(calendar.getTime()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && CheckPermissionGrantedReadPhoneState()) {
                    SendSms();
                }
            }
            case PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && CheckPermissionSendSms()) {
                    SendSms();
                }
            }
        }
    }

    private void SendSms(){
        Message newMessage = new Message()
                .getBuilder()
                .setId(m_id)
                .setTitle(txtTitle.getText().toString())
                .setIsBlinking(m_switchIsBlink.isChecked())
                .setIsVibro(m_switchIsVibration.isChecked())
                .setOnDate(calendar.getTime()).build();

        MessageManager messageManager = MessageManager.getInstance();
        if (m_id != -1) {
            messageManager.updateMessage(newMessage);
        } else {
            messageManager.addMessage(newMessage);
        }
        onBackPressed();
    }
}
