package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidlabs.entity.Message;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> msgElements = new ArrayList<>();
    private MsgListAdapter msgAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        Button sendBtn = findViewById(R.id.send_btn);
        Button receiveBtn = findViewById(R.id.receive_btn);
        ListView msgList = findViewById(R.id.msg_list);
        EditText msgEditText = findViewById(R.id.msg_editText);

        loadDataFromDatabase();
        msgList.setAdapter(msgAdapter = new MsgListAdapter());

        sendBtn.setOnClickListener(click -> {
            String text = msgEditText.getText().toString();
            if (text != null && text.length() > 0) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();

                //Now provide a value for every database column defined in MyOpener.java:
                //put string name in the NAME column:
                newRowValues.put(MsgOpener.COL_MSG, text);
                newRowValues.put(MsgOpener.COL_IS_SEND, 0);
                //Now insert in the database:
                long newId = db.insert(MsgOpener.TABLE_NAME, null, newRowValues);

                //now you have the newId, you can create the Contact object
                Message msg = new Message(newId, text, true);
                msgElements.add(msg);
                msgAdapter.notifyDataSetChanged();
                msgEditText.setText("");
            }
        });

        receiveBtn.setOnClickListener(click -> {
            String text = msgEditText.getText().toString();
            if (text != null && text.length() > 0) {
                ContentValues newRowValues = new ContentValues();

                //Now provide a value for every database column defined in MyOpener.java:
                //put string name in the NAME column:
                newRowValues.put(MsgOpener.COL_MSG, text);
                newRowValues.put(MsgOpener.COL_IS_SEND, 1);
                //Now insert in the database:
                long newId = db.insert(MsgOpener.TABLE_NAME, null, newRowValues);

                //now you have the newId, you can create the Contact object
                Message msg = new Message(newId, text, false);
                msgElements.add(msg);
                msgAdapter.notifyDataSetChanged();
                msgEditText.setText("");
            }
        });

        msgList.setOnItemClickListener((parent, view, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is: " + pos + ", The database id is: " + id)
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteMsg(id);
                        msgElements.remove(pos);
                        msgAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .create().show();
        });
        String[] columns = {MsgOpener.COL_ID, MsgOpener.COL_MSG, MsgOpener.COL_IS_SEND};
        Cursor results = db.query(false, MsgOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        printCursor(results, db.getVersion());
    }

    protected void loadDataFromDatabase() {
        //get a database connection:
        Log.e("debug", "load data");
        MsgOpener dbOpener = new MsgOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
// We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MsgOpener.COL_ID, MsgOpener.COL_MSG, MsgOpener.COL_IS_SEND};
        //query all the results from the database:
        Cursor results = db.query(false, MsgOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int msgColumnIndex = results.getColumnIndex(MsgOpener.COL_MSG);
        int isSendColIndex = results.getColumnIndex(MsgOpener.COL_IS_SEND);
        int idColIndex = results.getColumnIndex(MsgOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String msg = results.getString(msgColumnIndex);
            long isSendLong = results.getLong(isSendColIndex);
            boolean isSend;
            if (isSendLong == 0) {
                isSend = true;
            } else {
                isSend = false;
            }

            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            msgElements.add(new Message(id, msg, isSend));
        }


    }


    protected void printCursor(Cursor c, int version) {
        Log.e("Cursor_Info", "database version: " + version);
        Log.e("Cursor_Info", "number of columns: " + c.getColumnCount());
        Log.e("Cursor_Info", "name of columns: " + Arrays.toString(c.getColumnNames()));
        Log.e("Cursor_Info", "name of rows: " + c.getCount());
        if (c != null && c.moveToFirst()) {

            do {
                Log.e("Cursor_Info", "row: " + c.getPosition() + "{ id: " + c.getInt(c.getColumnIndex(MsgOpener.COL_ID)) + " msg: " + c.getString(c.getColumnIndex(MsgOpener.COL_MSG)) + " isSend: " + c.getInt(c.getColumnIndex(MsgOpener.COL_IS_SEND)) + " }");

            } while (c.moveToNext());

        }
    }

    protected void deleteMsg(long id) {
        db.delete(MsgOpener.TABLE_NAME, MsgOpener.COL_ID + "= ?", new String[]{Long.toString(id)});
    }

    private class MsgListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return msgElements.size();
        }

        @Override
        public Object getItem(int position) {
            return msgElements.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) msgElements.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Message msg = (Message) getItem(position);
            LayoutInflater inflater = getLayoutInflater();

            //make a new row:
            View newView = inflater.inflate(
                    (msg.isSent() ? R.layout.row_receive_layout : R.layout.row_send_layout),
                    parent, false);

            //set what the text should be for this row:
            TextView tView = newView.findViewById(R.id.textGoesHere);
            tView.setText(msg.getMsg());

            //return it to be put in the table
            return newView;
        }
    }
}