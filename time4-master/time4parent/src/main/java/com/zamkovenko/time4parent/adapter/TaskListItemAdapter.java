package com.zamkovenko.time4parent.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zamkovenko.utils.model.Message;
import com.zamkovenko.time4parent.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 03.12.2017
 */

public class TaskListItemAdapter extends ArrayAdapter<Message> {
    private final Context context;
    private final List<Message> values;

    public TaskListItemAdapter(Context context, List<Message> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_list_item, parent, false);

        TextView txtTime = (TextView) rowView.findViewById(R.id.txt_time);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_title);

        Calendar calendar = new GregorianCalendar();

        Message message = values.get(position);


        if (calendar.getTime().after(message.getOnDate())) {
            txtTime.setTextColor(message.IsDone() ? Color.GREEN : context.getResources()
                    .getColor(R.color.colorAdditional));
        } else {
            txtTime.setTextColor(message.IsDone() ? Color.GREEN : context.getResources()
                    .getColor(R.color.colorAccent));
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(message.getOnDate());

        txtTime.setText(time);
//        txtTitle.setText(String.format("id: %s", message.getId()));
        txtTitle.setText(message.getTitle());

        notifyDataSetChanged();

        return rowView;

    }

}
