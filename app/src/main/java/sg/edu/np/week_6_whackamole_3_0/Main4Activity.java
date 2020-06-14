package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    int highScore;
    final int[] advancedCount = {0};
    int levelSelected;
    String username;
    TextView advancedScore;
    UserData userData;
    MyDBHandler handler;
    Button backPress;
    CustomScoreAdaptor adapter;

    private void readyTimer(){
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, "Ready Countdown! " + millisUntilFinished/1000);
                String string = "Get Ready in " + millisUntilFinished/1000 + " seconds!";
                Toast.makeText(Main4Activity.this, string, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG ,"Ready Countdown Complete!");
                Toast.makeText(Main4Activity.this, "GO!", Toast.LENGTH_SHORT).show();
                placeMoleTimer();
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }
    private void placeMoleTimer(){
        int a = 0;
        switch(levelSelected){
            case 1:
                a = 10000;
                break;
            case 2:
                a = 9000;
                break;
            case 3:
                a = 8000;
                break;
            case 4:
                a = 7000;
                break;
            case 5:
                a = 6000;
                break;
            case 6:
                a = 5000;
                break;
            case 7:
                a = 4000;
                break;
            case 8:
                a = 3000;
                break;
            case 9:
                a = 2000;
                break;
            case 10:
                a = 1000;
        }
        newMolePlaceTimer = new CountDownTimer(a,a) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (levelSelected >= 1 && levelSelected <= 5){
                    setNewMole();
                }
                else{
                    setTwoNewMoles();
                }
                Log.v(TAG,"New Mole Location!");

            }

            @Override
            public void onFinish() {
                newMolePlaceTimer.start();
            }
        };
        newMolePlaceTimer.start();
    }
    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.left1, R.id.center1, R.id.right1,
            R.id.left2, R.id.center2, R.id.right2,
            R.id.left3, R.id.center3, R.id.right3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */
        advancedScore = (TextView)findViewById(R.id.advancedScore);
        Intent receivingEnd = getIntent();
        levelSelected = Integer.parseInt(receivingEnd.getStringExtra("Level"));
        username = receivingEnd.getStringExtra("Username");
        highScore = Integer.valueOf(receivingEnd.getStringExtra("Score"));
        handler = new MyDBHandler(this, "WhackAMoleDB.db", null,1);
        userData = handler.findUser(username);
        backPress = (Button) findViewById(R.id.backPress);
        adapter = new CustomScoreAdaptor(userData, this);

        for(final int id : BUTTON_IDS){
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button b = (Button) findViewById(id);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doCheck(b);
                }
            });
        }
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                onBackPressed();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        readyTimer();
    }
    private void doCheck(Button checkButton)
    {
        TextView advancedScore = (TextView)findViewById(R.id.advancedScore);
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, "Hit, score added!");
            Log.v(TAG, "Missed, point deducted!");
            belongs here.
        */
        if(checkButton.getText() == "*"){
            Log.v(TAG,"Hit, Score added!");
            ++advancedCount[0];
        }
        else{
            Log.v(TAG,"Missed, point deducted!");
            --advancedCount[0];
        }
        advancedScore.setText(String.valueOf(advancedCount[0]));
        if (levelSelected >= 1 && levelSelected <= 5){
            setNewMole();
        }
        else{
            setTwoNewMoles();
        }


    }

    public void setNewMole()
    {
        /* Hint:
            Clears the previous mole location and gets a new random location of the next mole location.
            Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
         */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        for (int i: BUTTON_IDS){
            Button b = (Button) findViewById(i);
            b.setText("O");
        }
        Button mole = (Button) findViewById(BUTTON_IDS[randomLocation]);
        mole.setText("*");
    }

    public void setTwoNewMoles(){
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int randomLocation2 = ran.nextInt(9);
        for (int i :BUTTON_IDS){
            Button b = (Button) findViewById(i);
            b.setText("O");
        }
        if(randomLocation != randomLocation2){
            Button mole1 = (Button) findViewById(BUTTON_IDS[randomLocation]);
            Button mole2 = (Button) findViewById(BUTTON_IDS[randomLocation2]);
            mole1.setText("*");
            mole2.setText("*");
        }
        else{
            setTwoNewMoles();
        }
    }

    private void updateUserScore()
    {
        readyTimer.cancel();
        newMolePlaceTimer.cancel();
        int score = Integer.parseInt(advancedScore.getText().toString());
        if(score > highScore){
            ArrayList<Integer> scoreList = userData.getScores();
            scoreList.set(levelSelected - 1, score);
            userData.setScores(scoreList);
        }
        saveScore(userData);
        Log.v(TAG, FILENAME + ": Update User Score...");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateUserScore();
        Intent intent = new Intent(Main4Activity.this, Main3Activity.class);
        startActivity(intent);
    }

    public void saveScore(UserData user){
        handler.addUser(user);
    }
}
