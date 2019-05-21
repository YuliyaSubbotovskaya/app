package com.zamkovenko.time4parent.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.zamkovenko.time4parent.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 17.12.2017
 */

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance(Locale.getDefault());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                R.style.DialogTheme, listener, year, month, dayOfMonth );
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE,
                getString(R.string.txt_cancel), dpd);
        // Create a new instance of TimePickerDialog and return it
        return dpd;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
}