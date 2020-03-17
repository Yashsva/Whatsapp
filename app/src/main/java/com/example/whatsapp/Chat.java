package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    private EditText edtInputMsg;
    private Button btnSendMsg;
    private ListView listViewChats;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> chatList;
    private String recieverName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        recieverName=getIntent().getStringExtra("userReciever");

        edtInputMsg=findViewById(R.id.edtInputMsg);
        btnSendMsg=findViewById(R.id.btnSendMsg);
        listViewChats=findViewById(R.id.listViewChats);


        btnSendMsg.setOnClickListener(this);


        setTitle(recieverName);

        //To swipe down the keyboard after sending message
        swipeDownKeyboard();


        chatList=new ArrayList();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,chatList);



        try {

            ParseQuery<ParseUser> queryConversations = new ParseQuery<ParseUser>("chats");

            //2 users who are communicating,used while querying
             ArrayList<String> users;
            users= new ArrayList();
            users.add(ParseUser.getCurrentUser().getUsername());
            users.add(recieverName);


//            ParseQuery<ParseUser> queryMsgFromSender=new ParseQuery<ParseUser>("chats");
//            ParseQuery<ParseUser> queryMsgFromReciever=new ParseQuery<ParseUser>("chats");


            queryConversations.whereContainedIn("sender", users);
            queryConversations.whereContainedIn("reciever",users);


            queryConversations.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null)
                    {

                        if(objects.size()>0)
                        {
                            for (ParseObject chat: objects)
                            {
                                chatList.add(chat.get("sender")+" : "+chat.get("chatText"));
                            }
                        }

                        listViewChats.setAdapter(arrayAdapter);


                    }
                    else
                    {
                        Toast.makeText(Chat.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSendMsg)
        {
            if(edtInputMsg.getText().toString().equals(""))
            {
                Toast.makeText(this,"Please write some text",Toast.LENGTH_SHORT).show();

            }
            else
            {
                final String message=edtInputMsg.getText().toString();

                ParseObject objectSendMessage=new ParseObject("chats");
                objectSendMessage.put("sender",ParseUser.getCurrentUser().getUsername());
                objectSendMessage.put("reciever",recieverName);
                objectSendMessage.put("chatText",message);

                objectSendMessage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {

                            chatList.add(ParseUser.getCurrentUser().getUsername() +" : "+message);
                            arrayAdapter.notifyDataSetChanged();
                            edtInputMsg.setText("");
//                            Toast.makeText(Chat.this,"Message Sent Successfully",Toast.LENGTH_SHORT).show();

                            //To swipe down the keyboard after sending message
                            swipeDownKeyboard();

                        }
                        else
                        {
                            Toast.makeText(Chat.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
    }


    public void swipeDownKeyboard()
    {
        try {
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
