package sg.edu.np.week_6_whackamole_3_0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    EditText usernameEditText;
    EditText passwordEditText;
    Button login;
    TextView registerTextView;
    MyDBHandler handler;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        login = (Button) findViewById(R.id.login);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        handler = new MyDBHandler(this, "WhackAMoleDB.db", null,1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME + ": Logging in with: " + usernameEditText.getText().toString() + ": " + passwordEditText.getText().toString());
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(isValidUser(username,password) == true){
                    Log.v(TAG, FILENAME + ": Valid User! Logging in");
                    Intent in = new Intent(MainActivity.this, Main3Activity.class);
                    in.putExtra("Username", username);
                    startActivity(in);
                }
                else{
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Invalid user!");
                }
            }
        });
        String string = "New User? Click here";
        SpannableString ss = new SpannableString(string);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Log.v(TAG, FILENAME + ": Create new user!");
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        };
        ss.setSpan(clickableSpan1, 10,20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerTextView.setText(ss);
        registerTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        UserData dbData = handler.findUser(userName);
        if (dbData != null && dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password)){
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            return true;
        }
        else{
            return false;
        }

    }

}
