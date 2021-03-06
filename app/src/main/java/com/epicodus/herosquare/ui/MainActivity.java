package com.epicodus.herosquare.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epicodus.herosquare.Constants;
import com.epicodus.herosquare.R;
import com.firebase.client.Firebase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.heroSearchEditText) EditText mHeroSearchEditText;
    @Bind(R.id.searchHeroesButton) Button mSearchHeroesButton;
    @Bind(R.id.logInButton) Button mLogInButton;
    @Bind(R.id.seeTeamButton) Button mSeeTeamButton;

    private Firebase mFirebaseRef;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setupUI(findViewById(R.id.parentContainer));
        ButterKnife.bind(this);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        mSearchHeroesButton.setOnClickListener(this);
        mLogInButton.setOnClickListener(this);
        mSeeTeamButton.setOnClickListener(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("message");
        if (name != null) {
            Toast toast = Toast.makeText(MainActivity.this,
                    "Invalid Search, Try Again", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER| Gravity.CENTER, 0, 0);
            toast.show();
        }

        mHeroSearchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        String logStatus =  (mSharedPreferences.getString(Constants.KEY_UID, null));
        if (logStatus == "") {
            mLogInButton.setVisibility(View.VISIBLE);
        } else if (logStatus == null) {
            mLogInButton.setVisibility(View.VISIBLE);

        } else {
            mSeeTeamButton.setVisibility(View.VISIBLE);
        }
        buttonShowStatus();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {

        if (v == mSearchHeroesButton) {
            String name = mHeroSearchEditText.getText().toString();
            Intent intent = new Intent(MainActivity.this, HeroListActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }
        if (v == mLogInButton) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if (v == mSeeTeamButton) {
            Intent intent = new Intent(MainActivity.this, SavedHeroListActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        mFirebaseRef.unauth();
        mEditor.putString(Constants.KEY_UID, "notLogged").apply();
        Log.d("succesfully logged out", "wow");
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void buttonShowStatus() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String uid = prefs.getString(Constants.KEY_UID, null);
        Log.d("Shared Preferences", uid + "");

        if ("notLogged".equals(uid)) {
            mLogInButton.setVisibility(View.VISIBLE);
            mSeeTeamButton.setVisibility(View.INVISIBLE);
        }

    }

}
