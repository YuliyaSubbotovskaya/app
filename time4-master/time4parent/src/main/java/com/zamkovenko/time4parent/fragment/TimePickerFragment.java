package com.zamkovenko.time4parent.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.zamkovenko.time4parent.R;

import java.util.Calendar;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 17.12.2017
 */

public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                R.style.DialogTheme, listener, hour, minute,
        true);
        tpd.setButton(DatePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.txt_cancel), tpd);
        return tpd;
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }
}