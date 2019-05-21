package com.zamkovenko.time4parent.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zamkovenko.time4parent.R;
import com.zamkovenko.time4parent.Utils.SimUtils;

import java.util.List;

public class ChooseSimCardActivity extends AppCompatActivity {

    LinearLayout m_btn_sim1;
    LinearLayout m_btn_sim2;

    TextView txt_sim1;
    TextView txt_sim2;

    ImageView img_sim1;
    ImageView img_sim2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sim);

        m_btn_sim1 = (LinearLayout) findViewById(R.id.btn_sim_1);
        m_btn_sim2 = (LinearLayout) findViewById(R.id.btn_sim_2);

        txt_sim1 = findViewById(R.id.txt_sim1);
        txt_sim2 = findViewById(R.id.txt_sim2);

        img_sim1 = findViewById(R.id.img_sim1);
        img_sim2 = findViewById(R.id.img_sim2);

        m_btn_sim1.setOnClickListener(new OnChooseSimListener());
        m_btn_sim2.setOnClickListener(new OnChooseSimListener());

        SetupSimButtons();
    }

    private void SetupSimButtons() {
        List<String> networkOperator = SimUtils.getNetworkOperator(this);

        txt_sim1.setText(R.string.txt_empty);
        txt_sim2.setText(R.string.txt_empty);

        for (int i = 0; i < networkOperator.size(); i++) {
            Log.d(getClass().getSimpleName(), networkOperator.get(i));
        }

        for (int i = 0; i < networkOperator.size(); i++) {
            if (i == 0) {
                txt_sim1.setText(networkOperator.get(i));
                img_sim1.setImageResource(R.drawable.ic_sim);
            }
            if (i == 1) {
                txt_sim2.setText(networkOperator.get(i));
                img_sim2.setImageResource(R.drawable.ic_sim);
            }
        }
    }

    private class OnChooseSimListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_sim_1:
                    SimUtils.WriteToSharedPreferences(view.getContext(), 0);
                    break;

                case R.id.btn_sim_2:
                    SimUtils.WriteToSharedPreferences(view.getContext(), 1);
                    break;
            }
            finish();
        }
    }

}
