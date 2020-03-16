package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> usersList;
    private ArrayAdapter arrayAdapter;
    private TextView txtLoadingData;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView=findViewById(R.id.listView);
        listView.setVisibility(View.GONE);

        usersList=new ArrayList();
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,usersList);

        txtLoadingData=findViewById(R.id.txtLoadingData);

        swipeRefreshLayout=findViewById(R.id.swipeContainer);

        try {

            ParseQuery<ParseUser> queryAllUsers=ParseUser.getQuery();

            queryAllUsers.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            queryAllUsers.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null)
                    {
                        if(objects.size()>0)
                        {
                            for(ParseUser user: objects)
                            {
                                usersList.add(user.getUsername());
                            }

                            listView.setAdapter(arrayAdapter);
                            txtLoadingData.animate().alpha(0).setDuration(2000);
                            listView.setVisibility(View.VISIBLE);

                        }
                        else
                        {
//                            Toast.makeText(Home.this,"No Users Available ",Toast.LENGTH_SHORT).show();

                            txtLoadingData.setText(" No Users Available ");
                        }
                    }

                    else
                    {
                        Toast.makeText(Home.this,"Error : "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username",usersList);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(e==null)
                            {
                                if(objects.size()>0)
                                {
                                    for(ParseUser user : objects)
                                    {
                                        usersList.add(user.getUsername());
                                    }

                                    arrayAdapter.notifyDataSetChanged();

                                    Toast.makeText(Home.this,objects.size()+" new users added ",Toast.LENGTH_SHORT).show();

                                    if(swipeRefreshLayout.isRefreshing())
                                    {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }


                                }
                                else
                                {
                                    if(swipeRefreshLayout.isRefreshing())
                                    {
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.logoutUserItem)
        {

            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        Toast.makeText(Home.this,"Successfully Logged Out",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(Home.this,SignUp.class);
                        startActivity(intent);
                        finish();


                    }
                    else
                    {
                        Toast.makeText(Home.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });



        }


        return super.onOptionsItemSelected(item);
    }
}
