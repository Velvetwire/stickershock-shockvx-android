package com.ice.stickershock_shockvx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.SecureRandom;

import static android.content.ContentValues.TAG;


public class WelcomeScreen extends Activity {

    private static EditText username;
    private static EditText password;
    private static EditText email;
    private static Button login_btn;
    int attempt_counter = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginButton();
    }

    public  void LoginButton() {
        username = (EditText)findViewById(R.id.textName);
        password = (EditText)findViewById(R.id.textPassword);
        email    = (EditText)findViewById(R.id.textEmail);
        login_btn = (Button)findViewById(R.id.registerButton);

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
           //             if(username.getText().toString().equals("user") &&
             //                   password.getText().toString().equals("pass")  ) {
                 //           Toast.makeText(WelcomeScreen.this,"User and Password is correct",
                  //                  Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(WelcomeScreen.this, ReadStickerNfc.class);
                            intent.putExtra("id", generateID());
                            startActivity(intent);


                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
  //      getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String generateID() {
        SecureRandom random = new SecureRandom();
        byte[] r = new byte[16]; //Means 128 bit
        random.nextBytes(r);
        String s = new String(r);
        Log.d(TAG, s);
        return s;
    }
}
