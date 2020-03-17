package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSignUpEmail,edtSignUpUsername,edtSignUpPassword;
    private Button btnSignUp,btnLoginPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        setTitle("Sign Up");

        edtSignUpEmail=findViewById(R.id.edtEmailSignUp);
        edtSignUpPassword=findViewById(R.id.edtPasswordSignUp);
        edtSignUpUsername=findViewById(R.id.edtUsernameSignUp);

        btnSignUp=findViewById(R.id.btnSignUp);
        btnLoginPage=findViewById(R.id.btnLoginPage);

        btnSignUp.setOnClickListener(this);
        btnLoginPage.setOnClickListener(this);

        edtSignUpPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN)
                {
                    onClick(btnSignUp);

                }

                return false;
            }
        });


        if(ParseUser.getCurrentUser()!=null)
        {
//            ParseUser.logOut();

            Toast.makeText(this,ParseUser.getCurrentUser().getUsername()+" Logged In",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(SignUp.this,Home.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSignUp:

                if(edtSignUpEmail.getText().toString().equals("")
                || edtSignUpUsername.getText().toString().equals("")
                || edtSignUpPassword.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    final ParseUser newUser=new ParseUser();
                    Log.i("App Credential SignUp ", "\nSign Up\nEmail : " + edtSignUpEmail.getText().toString() + "\nName : " + edtSignUpUsername.getText().toString() + "\n Password :" + edtSignUpPassword.getText().toString());

                    newUser.setEmail(edtSignUpEmail.getText().toString());
                    newUser.setUsername(edtSignUpUsername.getText().toString());
                    newUser.setPassword(edtSignUpPassword.getText().toString());


                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if(e==null)
                            {
                                Toast.makeText(SignUp.this, newUser.getUsername() + " Sign Up Successful", Toast.LENGTH_SHORT).show();

                                TransitionToHomepage();
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();



                            }
                        }
                    });



                }



                break;

            case R.id.btnLoginPage:

                Intent intent=new Intent(SignUp.this,Login.class);
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
        Intent intent=new Intent(SignUp.this,Home.class);
        startActivity(intent);
        finish();
    }
}
