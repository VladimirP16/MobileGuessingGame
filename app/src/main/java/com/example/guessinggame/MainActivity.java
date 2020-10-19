package com.example.guessinggame;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private EditText txtGuess;
    private Button btnGuess;
    private TextView lblOutput;
    private int theNumber;
    private Button btnAgain;
    private TextView txtCom;
    int range = 100;
    private int numberOfTries = 7;
    private TextView lblRange;



    public void checkGuess(){
        String guessText = txtGuess.getText().toString();
        String message = "";
        try{
            numberOfTries--;
            int guess = Integer.parseInt(guessText);
            if(guess < theNumber)
                message = guess + " is too low. Try again!";
            else if(guess > theNumber)
                message = guess + " is too high. Try again!";
            else {
                int numberOfGuess = (int)(Math.log(range)/Math.log(2)+4) - numberOfTries--;
                message = guess + " is correct! You win after " + numberOfGuess + " tries! Good work! Let's play again!";
                Toast.makeText(MainActivity.this, "Congratulations!", Toast.LENGTH_LONG).show();
                //message = guess + " is correct. You win! Let's play again!";
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("gamesWon", gamesWon);
                editor.apply();
                btnGuess.setVisibility(View.INVISIBLE);
                btnAgain.setVisibility(View.VISIBLE);
                txtCom.setVisibility(View.INVISIBLE);
            }
            if(numberOfTries <= 0){
                message = "You are a LOSER! Right number is " + theNumber + ".";
                btnGuess.setVisibility(View.INVISIBLE);
                btnAgain.setVisibility(View.VISIBLE);
                txtCom.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Don't worry you'll be lucky next time!", Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e){
            message = "Please, enter a whole number between 1 and " + range + "!";
        }
        finally{
            lblOutput.setText(message);
            txtGuess.requestFocus();
            txtGuess.selectAll();
            txtCom.setText("You have " + numberOfTries + " tries left!");
        }
    }

    public void newGame(){
        theNumber = (int) (Math.random() * range + 1);
        lblRange.setText("Enter a number between 1 and " + range + ":");
        numberOfTries = (int)(Math.log(range)/Math.log(2)+4);
        btnAgain.setVisibility(View.INVISIBLE);
        btnGuess.setVisibility(View.VISIBLE);
        lblOutput.setText("Enter a number above, then click \"Guess!\"");
        txtCom.setText("You have " + numberOfTries + " tries to guess the number");
        txtCom.setVisibility(View.VISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int gamesAll = preferences.getInt("gamesAll", 0) + 1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("gamesAll", gamesAll);
        editor.apply();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGuess = (EditText) findViewById(R.id.txtGuess);
        btnGuess = (Button) findViewById(R.id.btnGuess);
        lblOutput = (TextView) findViewById(R.id.lblOutput);
        lblRange = (TextView) findViewById(R.id.textView2);
        txtCom = (TextView) findViewById(R.id.txtCom);
        btnAgain = (Button) findViewById(R.id.btnAgain);
        btnAgain.setVisibility(View.INVISIBLE);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
        txtGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                checkGuess();
                return true;
            }
        });
        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGuess();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        range = preferences.getInt("range", 100);
        numberOfTries = (int)(Math.log(range)/Math.log(2)+4);
        newGame();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.action_settings:
                final CharSequence[] items = {"Easy", "Normal", "Hard"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select difficulty level:");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                range = 10;
                                storeRange(10);
                                newGame();
                                break;
                            case 1:
                                range = 100;
                                storeRange(100);
                                newGame();
                                break;
                            case 2:
                                range = 1000;
                                storeRange(1000);
                                newGame();
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            //noinspection SimplifiableIfStatement
            //if (id == R.id.action_settings) {
            case R.id.action_newgame:
                newGame();
                return true;
            case R.id.action_about:
                AlertDialog aboutDialog = new AlertDialog.Builder(MainActivity.this).create();
                aboutDialog.setTitle("About Guessing Game");
                aboutDialog.setMessage("(c)2018 Vovan");
                aboutDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                aboutDialog.show();
                return true;
            case  R.id.action_gamestats:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                int gamesWon = preferences.getInt("gamesWon", 0);
                int gamesAll = preferences.getInt("gamesAll", 0);
                int percentWins = (int) (gamesWon / gamesAll) * 100;
                AlertDialog statDialog = new AlertDialog.Builder(MainActivity.this).create();
                statDialog.setTitle("Guessing Game Stats");
                statDialog.setMessage("You have won " + gamesWon + " out of " + gamesAll + " games. You have " + percentWins  +"% of wins. Way to go!");
                statDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                statDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }
    public void storeRange(int newRange){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("range", newRange);
        editor.apply();
    }

}