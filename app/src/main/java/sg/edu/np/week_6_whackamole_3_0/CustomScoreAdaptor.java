package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    ArrayList<Integer> levelList;
    ArrayList<Integer> scoreList;
    String username;
    Context context;
    UserData userData;

    public CustomScoreAdaptor(UserData userdata, Context context){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.levelList = userdata.getLevels();
        this.scoreList = userdata.getScores();
        this.username = userdata.getMyUserName();
        this.context = context;
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select, parent, false);
        return new CustomScoreViewHolder(v);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        final int information1 = levelList.get(position);
        holder.level.setText("Level " + String.valueOf(information1));

        final int information2 = scoreList.get(position);
        holder.score.setText("Highest Score: " + String.valueOf(information2));

        Log.v(TAG, FILENAME + " Showing level " + levelList.get(position) + " with highest score: " + scoreList.get(position));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, FILENAME+ ": Load level " + levelList.get(position) +" for: " + username);
                Intent intent = new Intent(context, Main4Activity.class);
                intent.putExtra("Level", String.valueOf(information1));
                intent.putExtra("Username", username);
                intent.putExtra("Score", String.valueOf(information2));
                context.startActivity(intent);
            }
        });
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return levelList.size();
    }
}