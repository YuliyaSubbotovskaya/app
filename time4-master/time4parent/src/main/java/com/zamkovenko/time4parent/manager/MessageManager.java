package com.zamkovenko.time4parent.manager;

import android.content.Context;
import android.util.Log;

import com.zamkovenko.time4parent.database.ParentTaskDbHelper;
import com.zamkovenko.time4parent.task.SendMessageTask;
import com.zamkovenko.utils.OnMessageRefreshListener;
import com.zamkovenko.utils.model.Message;
import com.zamkovenko.utils.model.MessageState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * User: Yevgeniy Zamkovenko
 * Date: 17.12.2017
 */

public class MessageManager {

    private Context m_context;

    private ParentTaskDbHelper m_dbHelper;

    public void setOnMessageRefreshListener(OnMessageRefreshListener onMessageRefreshListener) {
        this.onMessageRefreshListener = onMessageRefreshListener;
    }

    private OnMessageRefreshListener onMessageRefreshListener;

    private static MessageManager instance;

    public static MessageManager getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private MessageManager() {
        //InitWithTestValues();
    }

    private void InitWithTestValues() {

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 9);
        addMessage(new Message().getBuilder().setTitle("Louis Pills").setOnDate(calendar.getTime()).build());

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        addMessage(new Message().getBuilder().setTitle("Bill M16").setOnDate(calendar.getTime()).build());

        calendar.set(Calendar.HOUR_OF_DAY, 15);
        addMessage(new Message().getBuilder().setTitle("Francis Hunting").setOnDate(calendar.getTime()).build());

        calendar.set(Calendar.HOUR_OF_DAY, 18);
        addMessage(new Message().getBuilder().setTitle("Zoey Hair").setOnDate(calendar.getTime()).build());

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        addMessage(new Message().getBuilder().setTitle("Drink Water").setOnDate(calendar.getTime()).build());

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        addMessage(new Message().getBuilder().setTitle("Write Song").setOnDate(calendar.getTime()).build());
    }

    private static void createInstance() {
        instance = new MessageManager();
    }

    public void markMessageDone(Message message) {
        if (message == null) {
            Log.d("MessageManager", "message == null");
            return;
        }

        Log.d("MessageManager", "markMessageDone: " + message.getId());

        boolean isNeedRefresh = false;

        ArrayList<Message> allData = m_dbHelper.getAllData();
        for (Message storedMessage : allData) {
            if (message.getId() == storedMessage.getId()) {
                isNeedRefresh = true;
                storedMessage.setMessageState(MessageState.DONE);
                m_dbHelper.updateTask(storedMessage);
            }
        }

        if (isNeedRefresh) {
            Refresh();
        }
    }

    private void Refresh() {
        if (onMessageRefreshListener != null) {
            onMessageRefreshListener.OnRefresh();
        }
    }

    public void updateMessage(Message message) {
//        m_dbHelper.updateTask(message);
    }

    public void addMessage(Message message) {
        final short id = (short) m_dbHelper.addTask(message);

        message.setId(id);

        new SendMessageTask(m_context).execute(message);
    }

    public List<Message> getMessagesForDay(Calendar calendar) {
        List<Message> list = new ArrayList<>();

        Calendar messageCalendar = new GregorianCalendar();

        ArrayList<Message> allData = m_dbHelper.getAllData();

        for (Message message : allData) {
            messageCalendar.setTime(message.getOnDate());

            if(compareForDay(calendar, messageCalendar)){
                list.add(message);
            }
        }

        Collections.sort(list, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                return o1.getOnDate().compareTo(o2.getOnDate());
            }
        });

        return list;
    }

    private boolean compareForDay(Calendar calendar, Calendar messageCalendar) {
        return calendar.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR)
                && calendar.get(Calendar.MONTH) == messageCalendar.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == messageCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public List<Message> GetAll() {
        return m_dbHelper.getAllData();
    }

    public void init(Context context) {
        m_dbHelper = new ParentTaskDbHelper(context);
        m_context = context;
    }

    public void clearDatabase() {
        m_dbHelper.recreateDb();
        Refresh();
    }
}
