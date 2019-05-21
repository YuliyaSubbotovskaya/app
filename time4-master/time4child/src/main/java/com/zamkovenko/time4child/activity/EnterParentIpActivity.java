package com.zamkovenko.time4child.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zamkovenko.time4child.R;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 10.12.2017
 */

public class EnterParentIpActivity extends AppCompatActivity {

    public static final String PARAM_PARENT_IP = "parent_number";

    private EditText editIp;
    private Button btnConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_ip);

        editIp = (EditText) findViewById(R.id.txt_ip);
        btnConfirmed = (Button) findViewById(R.id.btn_confirm);

        btnConfirmed.setOnClickListener(new OnConfirmed());
    }

    class OnConfirmed implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String ip = editIp.getText().toString();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EnterParentIpActivity.this);
            prefs.edit().putString(PARAM_PARENT_IP, ip).apply();

            EnterParentIpActivity.this.finish();

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
