package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<Message> msgElements = new ArrayList<>();
    private MsgListAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        Button sendBtn = findViewById(R.id.send_btn);
        Button receiveBtn = findViewById(R.id.receive_btn);
        ListView msgList = findViewById(R.id.msg_list);
        EditText msgEditText = findViewById(R.id.msg_editText);

        msgList.setAdapter(msgAdapter = new MsgListAdapter());

        sendBtn.setOnClickListener(click -> {
            String text = msgEditText.getText().toString();
            if (text != null && text.length() > 0) {
                Message msg = new Message(text, true);
                msgElements.add(msg);
                msgAdapter.notifyDataSetChanged();
                msgEditText.setText("");
            }
        });

        receiveBtn.setOnClickListener(click -> {
            String text = msgEditText.getText().toString();
            if (text != null && text.length() > 0) {
                Message msg = new Message(text, false);
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
                        msgElements.remove(pos);
                        msgAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> {
                    })
                    .create().show();
        });


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
            return (long) position;
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