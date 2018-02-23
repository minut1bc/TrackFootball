package com.example.android.trackfootball;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int goalsTeamA = 0;
    int goalsTeamB = 0;
    int foulsTeamA = 0;
    int foulsTeamB = 0;
    int yellowsTeamA = 0;
    int yellowsTeamB = 0;
    int redsTeamA = 0;
    int redsTeamB = 0;
    int minA = 0;
    int minB = 0;

    Button goalButtonA;
    Button offsideButtonA;
    Button foulButtonA;
    Button yellowButtonA;
    Button redButtonA;
    Button goalButtonB;
    Button offsideButtonB;
    Button foulButtonB;
    Button yellowButtonB;
    Button redButtonB;
    EditText teamA;
    EditText teamB;

    CountDownTimer countDownTimer;
    boolean timerRunning;
    long timeLeftInMilliseconds = 91000;
    TextView countDownText;
    Button countDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countDownText = findViewById(R.id.countDownText);
        countDownButton = findViewById(R.id.countDownButton);
        goalButtonA = findViewById(R.id.goalButtonA);
        offsideButtonA = findViewById(R.id.offisdeButtonA);
        foulButtonA = findViewById(R.id.foulButtonA);
        yellowButtonA = findViewById(R.id.yellowButtonA);
        redButtonA = findViewById(R.id.redButtonA);
        goalButtonB = findViewById(R.id.goalButtonB);
        offsideButtonB = findViewById(R.id.offisdeButtonB);
        foulButtonB = findViewById(R.id.foulButtonB);
        yellowButtonB = findViewById(R.id.yellowButtonB);
        redButtonB = findViewById(R.id.redButtonB);
        teamA = findViewById(R.id.teamA);
        teamB = findViewById(R.id.teamB);
        toggleEnable(false);
        countDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });
    }

    public void toggleEnable(boolean valid) {       // used to switch the buttons between Enabled and Disabled, dependent on the countdown's state
        goalButtonA.setEnabled(valid);
        offsideButtonA.setEnabled(valid);
        foulButtonA.setEnabled(valid);
        yellowButtonA.setEnabled(valid);
        redButtonA.setEnabled(valid);
        goalButtonB.setEnabled(valid);
        offsideButtonB.setEnabled(valid);
        foulButtonB.setEnabled(valid);
        yellowButtonB.setEnabled(valid);
        redButtonB.setEnabled(valid);
    }

    public void setDefault() {      // used by the Reset Match and New Match Dialog buttons to set everything in the initial state
        goalsTeamA = 0;
        goalsTeamB = 0;
        foulsTeamA = 0;
        foulsTeamB = 0;
        yellowsTeamA = 0;
        yellowsTeamB = 0;
        redsTeamA = 0;
        redsTeamB = 0;
        minA = 0;
        minB = 0;
        displayGoalTeamA(goalsTeamA);
        displayGoalTeamB(goalsTeamB);
        displayFoulTeamA(foulsTeamA);
        displayFoulTeamB(foulsTeamB);
        displayYellowTeamA(yellowsTeamA);
        displayYellowTeamB(yellowsTeamB);
        displayRedTeamA(redsTeamA);
        displayRedTeamB(redsTeamB);
        timerRunning = false;
        if (timeLeftInMilliseconds != 91000)        // makes sure the app doesn't crash when pressing pressing the Reset Match Button before Start Match Button
            countDownTimer.cancel();
        timeLeftInMilliseconds = 91000;
        countDownText.setText("0");
        countDownButton.setText(R.string.start_match);
        countDownButton.setEnabled(true);
        teamA.setText("");
        teamB.setText("");
        toggleEnable(false);
        teamA.setEnabled(true);
        teamB.setEnabled(true);
    }

    public void startStop() {       // toggles the Countdown
        if (timerRunning)
            stopTimer(false);
        else
            startTimer();
    }

    public void startTimer() {      // code to run while the Countdown is running
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) { // happens every "second" passes
                timeLeftInMilliseconds = l;
                displayTimer();
            }

            @Override
            public void onFinish() {    // when the timer reaches 90 (the countdown reaches 0), displays Alert Dialog with winner
                countDownButton.setText(R.string.match_over);
                int difference = goalsTeamA - goalsTeamB;
                String winningMessage = "";
                String nameTeamA;
                String nameTeamB;
                if (difference < 0)
                    difference = 0 - difference;
                if (teamA.getText().toString().equals(""))       // creates the value to show for Team A
                    nameTeamA = "" + getString(R.string.team_a);
                else
                    nameTeamA = "" + teamA.getText();
                if (teamB.getText().toString().equals(""))       // creates the value to show for Team B
                    nameTeamB = "" + getString(R.string.team_b);
                else
                    nameTeamB = "" + teamB.getText();
                if (goalsTeamA != goalsTeamB) {
                    if (goalsTeamA > goalsTeamB)
                        winningMessage = "" + nameTeamA;
                    else if (goalsTeamB > goalsTeamA)
                        winningMessage = "" + nameTeamB;
                    winningMessage += " " + getString(R.string.won_diff) + " " + difference + " ";
                    if (difference == 1)
                        winningMessage += getString(R.string.goal_sing);
                    else
                        winningMessage += getString(R.string.goal_plur);
                } else {
                    winningMessage = "" + nameTeamA + " " + getString(R.string.and) + " " + nameTeamB + " " + getString(R.string.tie) + " " + goalsTeamA + " ";
                    if (goalsTeamA == 1)
                        winningMessage += getString(R.string.goal_sing);
                    else
                        winningMessage += getString(R.string.goal_plur);
                }
                winningMessage += ".";
                AlertDialog.Builder builderMatchWon = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.match_over)
                        .setMessage(winningMessage)
                        .setCancelable(false)
                        .setPositiveButton(R.string.new_match, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setDefault();
                            }
                        });
                AlertDialog alert = builderMatchWon.create();
                alert.show();
            }
        }.start();
        timerRunning = true;
        countDownButton.setText(R.string.pause_match);
        toggleEnable(true);
        teamA.setEnabled(false);
        teamB.setEnabled(false);

    }

    public void displayTimer() {    // updates the Timer TextView
        int seconds = (int) timeLeftInMilliseconds / 1000;
        String timeLeft;
        timeLeft = "" + (91 - seconds);     // transforms the countdown from 91 to 0 into a timer from 0 to 90
        countDownText.setText(timeLeft);
        if (seconds == 46)      // half time (45 seconds in TextView)
            stopTimer(true);
    }

    public void stopTimer(boolean half) { // happens when the Pause Match button is pressed and when the Timer reaches half time ( boolean is true)
        countDownTimer.cancel();
        timerRunning = false;
        if (!half)
            countDownButton.setText(R.string.resume_match);
        else {
            countDownButton.setText(R.string.next_half);
            timeLeftInMilliseconds -= 1000;     // ensures the CountDown can resume
            TextView goalsA = findViewById(R.id.goal_team_a);
            TextView goalsB = findViewById(R.id.goal_team_b);
            CharSequence minTeamA = goalsA.getText();
            CharSequence minTeamB = goalsB.getText();
            minA = Integer.parseInt(minTeamA.toString());       // creates the minimum goals value for team A, used when subtracting goals by the Offside Button
            minB = Integer.parseInt(minTeamB.toString());       // creates the minimum goals value for team B, used when subtracting goals by the Offside Button
        }
        toggleEnable(false);        // makes the buttons unavailable while the Timer isn't running
    }

    public void displayGoalTeamA(int goal) {        // updates the TextView for Team A's goal number
        TextView goalsView = findViewById(R.id.goal_team_a);
        goalsView.setText(String.valueOf(goal));
    }

    public void goalTeamA(View v) {     // adds a goal to Team A
        goalsTeamA++;
        displayGoalTeamA(goalsTeamA);
    }

    public void offsideTeamA(View v) {      // substracts a goal from Team A, with a lower limit of 0, or the number of goals from the first half
        if (goalsTeamA > minA)      // ensures the minimum value is either 0 or the number from the first half
            goalsTeamA--;
        displayGoalTeamA(goalsTeamA);
    }

    public void displayFoulTeamA(int foul) {        // updates the TextView for Team A's foul number
        TextView foulsView = findViewById(R.id.foul_team_a);
        foulsView.setText(String.valueOf(foul));
    }

    public void foulTeamA(View v) {     // adds a foul to Team A
        foulsTeamA++;
        displayFoulTeamA(foulsTeamA);
    }

    public void displayYellowTeamA(int yellow) {        // updates the TextView for Team A's yellow cards number
        TextView yellowsView = findViewById(R.id.yellow_team_a);
        yellowsView.setText(String.valueOf(yellow));
    }

    public void yellowTeamA(View v) {       // adds a yellow card to Team A, up to 2, in which case the yellow cards become a single red card
        yellowsTeamA++;
        if (yellowsTeamA == 2) {
            yellowsTeamA = 0;
            redButtonA.performClick();      // transfers the yellow cards to red cards
        }
        displayYellowTeamA(yellowsTeamA);
    }

    public void displayRedTeamA(int red) {      // updates the TextView for Team A's red cards number
        TextView redsView = findViewById(R.id.red_team_a);
        redsView.setText(String.valueOf(red));
    }

    public void redTeamA(View v) {      // adds a red card to team A, up to 4, in which case displays the Match Over (minimum 8 players on field)
        redsTeamA++;
        displayRedTeamA(redsTeamA);
        String lostRedTeamA;       // creates the Losing Mesage
        if (!(teamA.getText().toString()).equals(""))       // integrates the Team's name into the losing mesage
            lostRedTeamA = "" + teamA.getText();
        else
            lostRedTeamA = "" + getString(R.string.team_a);
        lostRedTeamA += " " + getString(R.string.losing_message);
        AlertDialog.Builder builderMatchLost = new AlertDialog.Builder(this)
                .setTitle(R.string.match_over)
                .setMessage(lostRedTeamA)
                .setCancelable(false)       // makes sure the Dialog disappears only when the Button is pressed
                .setPositiveButton(R.string.new_match, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setDefault();
                    }
                });
        AlertDialog alert = builderMatchLost.create();
        if (redsTeamA < 4)
            alert.dismiss();
        else {
            alert.show();
            countDownTimer.cancel();
        }
    }

    public void displayGoalTeamB(int goal) {        // updates the TextView for Team B's goal number
        TextView goalsView = findViewById(R.id.goal_team_b);
        goalsView.setText(String.valueOf(goal));
    }

    public void goalTeamB(View v) {     // adds a goal to Team B
        goalsTeamB++;
        displayGoalTeamB(goalsTeamB);
    }

    public void offsideTeamB(View v) {      // substracts a goal from Team B, with a lower limit of 0, or the number of goals from the first half
        if (goalsTeamB > minB)      // ensures the minimum value is either 0 or the number from the first half
            goalsTeamB--;
        displayGoalTeamB(goalsTeamB);
    }

    public void displayFoulTeamB(int foul) {        // updates the TextView for Team B's foul number
        TextView foulsView = findViewById(R.id.foul_team_b);
        foulsView.setText(String.valueOf(foul));
    }

    public void foulTeamB(View v) {     // adds a foul to Team B
        foulsTeamB++;
        displayFoulTeamB(foulsTeamB);
    }

    public void displayYellowTeamB(int yellow) {        // updates the TextView for Team B's yellow cards number
        TextView yellowsView = findViewById(R.id.yellow_team_b);
        yellowsView.setText(String.valueOf(yellow));
    }

    public void yellowTeamB(View v) {       // adds a yellow card to Team B, up to 2, in which case the yellow cards become a single red card
        yellowsTeamB++;
        if (yellowsTeamB == 2) {
            yellowsTeamB = 0;
            redButtonB.performClick();      // transfers the yellow cards to red cards
        }
        displayYellowTeamB(yellowsTeamB);
    }

    public void displayRedTeamB(int red) {      // updates the TextView for Team B's red cards number
        TextView redsView = findViewById(R.id.red_team_b);
        redsView.setText(String.valueOf(red));
    }

    public void redTeamB(View v) {      // adds a red card to team B, up to 4, in which case displays the Match Over (minimum 8 players on field)
        redsTeamB++;
        displayRedTeamB(redsTeamB);
        String lostRedTeamB;       // creates the Losing Mesage
        if (!(teamB.getText().toString()).equals(""))       // integrates the Team's name into the losing mesage
            lostRedTeamB = "" + teamB.getText();
        else
            lostRedTeamB = "" + getString(R.string.team_b);
        lostRedTeamB += " " + getString(R.string.losing_message);
        AlertDialog.Builder builderMatchLost = new AlertDialog.Builder(this)
                .setTitle(R.string.match_over)
                .setMessage(lostRedTeamB)
                .setCancelable(false)       // makes sure the Dialog disappears only when the Button is pressed
                .setPositiveButton(R.string.new_match, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setDefault();
                    }
                });
        AlertDialog alert = builderMatchLost.create();
        if (redsTeamB < 4)
            alert.dismiss();
        else {
            countDownTimer.cancel();
            alert.show();
            displayRedTeamB(4);
        }
    }

    public void Reset(View v) {     // actions when the Reset Match button is pressed
        setDefault();
    }

}
