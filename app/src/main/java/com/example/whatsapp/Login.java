package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtLoginEmail,edtLoginPassword;
    private Button btnLogin,btnSignUpPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        edtLoginEmail=findViewById(R.id.edtEmailLogin);
        edtLoginPassword=findViewById(R.id.edtPasswordLogin);
        btnLogin=findViewById(R.id.btnLogin);
        btnSignUpPage=findViewById(R.id.btnSignUpPage);

        btnSignUpPage.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        edtLoginPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    onClick(btnLogin);
                }

                return false;
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnLogin:

                if(edtLoginPassword.getText().toString().equals("") || edtLoginPassword.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    Log.i("App Credential Login ", "\nLogin\nEmail : " + edtLoginEmail.getText().toString() + "\n Password :" + edtLoginPassword.getText().toString());

                    ParseUser.logInInBackground(edtLoginEmail.getText().toString(), edtLoginPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            if(e==null)
                            {
                                Toast.makeText(Login.this,user.getUsername()+" Logged In",Toast.LENGTH_SHORT).show();

                                TransitionToHomepage();

                            }
                            else
                            {
                                Toast.makeText(Login.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }


                break;

            case R.id.btnSignUpPage:

                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();



                break;
        }
    }

    public void ConstraintLayoutClicked(View view)
    {

        try {
            InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void TransitionToHomepage()
    {
        Intent intent=new Intent(Login.this,Home.class);
        startActivity(intent);
        finish();
    }

}
