package com.zamkovenko.time4parent.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zamkovenko.time4parent.R;
import com.zamkovenko.time4parent.Utils.SimUtils;
import com.zamkovenko.time4parent.adapter.TaskListItemAdapter;
import com.zamkovenko.time4parent.manager.MessageManager;
import com.zamkovenko.utils.OnMessageRefreshListener;
import com.zamkovenko.utils.model.Message;

import junit.runner.Version;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMessageRefreshListener {

    private MODE mode;

    ImageButton btnAddTask;
    ImageButton btnEdit;
    ImageButton btnDelete;

    private Calendar calendar;

    private TextView txtMonth;

    private ListView listOfTasks;

    List<Message> m_messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetupLog();
        setUALocale();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        MessageManager.getInstance().init(this);
        MessageManager.getInstance().setOnMessageRefreshListener(this);

        calendar = new GregorianCalendar();

        txtMonth = (TextView) findViewById(R.id.txt_month);

        listOfTasks = (ListView) findViewById(R.id.tasks);

        setupDaysNumbers();

        InitButtons();

        RequestPermission();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        CheckForSimSettings();

        CheckForParentNumber();
    }

    private void CheckForSimSettings() {
        if (!SimUtils.IsSimChosen(this)) {
            Intent chooseSimIntent = new Intent(getApplicationContext(), ChooseSimCardActivity.class);
            startActivity(chooseSimIntent);
        }
    }

    private void CheckForParentNumber() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String parentPhone = prefs.getString(EnterParentPhoneActivity.PARAM_PARENT_PHONE, "");
        if (parentPhone.equals("")) {
            Intent parentPhoneIntent = new Intent(getApplicationContext(), EnterParentPhoneActivity.class);
            startActivity(parentPhoneIntent);
        }
    }

    private void RequestPermission() {
        // Request the permission immediately here for the first time run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission_group.SMS,
                    Manifest.permission_group.STORAGE,
                    Manifest.permission.READ_PHONE_STATE,

                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.BROADCAST_SMS,
                    Manifest.permission.RECEIVE_SMS,
            }, 0);
        }
    }

    private void InitButtons() {
        btnAddTask = (ImageButton) findViewById(R.id.btn_add);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskEditActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        btnEdit = (ImageButton) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE.EDIT;
                ResetColor();
            }
        });

        btnDelete = (ImageButton) findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = MODE.DELETE;
                ResetColor();
            }
        });

        ImageButton btn_today = (ImageButton) findViewById(R.id.btn_today);
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageManager.getInstance().clearDatabase();
            }
        });

        ImageButton btnMore = (ImageButton) findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckIpActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void setupTaskView() {
        m_messageList = MessageManager.getInstance().getMessagesForDay(calendar);
        ArrayAdapter adapter = new TaskListItemAdapter(this, m_messageList);

        listOfTasks.setAdapter(adapter);
        listOfTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TaskEditActivity.class);
                final short _id = m_messageList.get(position).getId();
                intent.putExtra(TaskEditActivity.PARAM_TASK_ID, _id);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void setupDaysNumbers() {
        Calendar calendar = new GregorianCalendar(
                this.calendar.get(Calendar.YEAR), this.calendar.get(Calendar.MONTH), this.calendar.get(Calendar.DAY_OF_MONTH)
        );
        calendar.add(Calendar.DAY_OF_MONTH, -3);

        LinearLayout daysOfMonth = (LinearLayout) findViewById(R.id.layout_days);
        LinearLayout daysOfMonthName = (LinearLayout) findViewById(R.id.layout_days_name);

        int daysOfMonthChildCount = daysOfMonth.getChildCount();
//        int daysOfMonthNameChildCount = daysOfMonthName.getChildCount();

        for (int i = 0; i < daysOfMonthChildCount; i++) {
            TextView daysOfMonthChildAt = (TextView) daysOfMonth.getChildAt(i);
            TextView daysOfMonthNameChildAt = (TextView) daysOfMonthName.getChildAt(i);

            daysOfMonthChildAt.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            daysOfMonthNameChildAt.setText(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, getLocale()));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMonth();
        setupTaskView();

        mode = MODE.NONE;

        ResetColor();
    }

    private void setupMonth() {
        txtMonth.setText(new GregorianCalendar().getDisplayName(Calendar.MONTH,
                Calendar.LONG, getLocale()));
    }

    private Locale getLocale() {
        //return getResources().getConfiguration().locale;
        return new Locale("uk","UA");
    }

    @Override
    public void OnRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupTaskView();

            }
        });
    }

    private void ResetColor(){
        btnEdit.getBackground().setColorFilter(
                mode == MODE.EDIT ? Color.GREEN : Color.GRAY, PorterDuff.Mode.MULTIPLY);
        btnDelete.getBackground().setColorFilter(
                mode == MODE.DELETE ? Color.GREEN : Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    private enum MODE{
        NONE, EDIT, DELETE
    }

    private void SetupLog() {
        if ( isExternalStorageWritable() ) {
            File logFile = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                        + String.valueOf(new Date(System.currentTimeMillis())).replace(" ", "_") + "_log.txt");
            }

            Log.d(getClass().getSimpleName(),("LOGGING: " + logFile));

            // clear the previous logcat and then write the new one to the file
            try {
                Runtime.getRuntime().exec( "logcat -c");
                Runtime.getRuntime().exec( "logcat -f " + logFile);

            } catch ( IOException e ) {

                e.printStackTrace();
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals( state ) ) {
            return true;
        }
        return false;
    }

    private void setUALocale(){
        Configuration config = getResources().getConfiguration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        config.setLocale(getLocale());
    }
}
