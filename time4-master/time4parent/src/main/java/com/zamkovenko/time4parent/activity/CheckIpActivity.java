package com.zamkovenko.time4parent.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zamkovenko.time4parent.R;
import com.zamkovenko.time4parent.Utils.NetworkUtils;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 31.12.2017
 */

public class CheckIpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ip);

        TextView ip = (TextView) findViewById(R.id.txt_ip);
        Button ok = (Button) findViewById(R.id.btn_ok);
        if(Build.VERSION.SDK_INT < 21){
            ok.setBackgroundResource(R.drawable.btn_style);
        }else {
            ok.setBackgroundResource(R.drawable.btn_ripple);
        }

        ip.setText(NetworkUtils.getIPAddress(true));

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
