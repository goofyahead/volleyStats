package com.mgl.volleystats.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mgl.volleystats.R;
import com.mgl.volleystats.api.VolleyStatsApi;
import com.mgl.volleystats.base.VolleyPrefs;
import com.mgl.volleystats.base.VolleyStatApplication;
import com.mgl.volleystats.dialogFragments.PasswordDialogFragment;
import com.mgl.volleystats.fragments.CurrentMatch;
import com.mgl.volleystats.fragments.DefinePositions;
import com.mgl.volleystats.fragments.NewMatch;
import com.mgl.volleystats.fragments.SelectTeam;
import com.mgl.volleystats.fragments.TeamView;
import com.mgl.volleystats.interfaces.OnPasswordGiven;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PasswordDialogFragment.PasswordSetInterface, SelectTeam.TeamSelected {

    private static final String TAG = MainActivity.class.getName();
    @Inject
    VolleyPrefs prefs;
    private OnPasswordGiven mPasswordGiven;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        ((VolleyStatApplication) getApplication()).inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (prefs.getTeamId().equalsIgnoreCase("none")) {
            // show team selection
            Log.d(TAG, "show team selector");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            SelectTeam dlView = SelectTeam.newInstance();
            mPasswordGiven = dlView;
            ft.replace(R.id.fragment_holder, dlView);
            ft.commit();

        } else {
            // show normal screen resume of team
            Log.d(TAG, "team is selected");
            Log.d(TAG, "show home for team");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            TeamView dlView = TeamView.newInstance(prefs.getTeamId());
            ft.replace(R.id.fragment_holder, dlView);
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        CreatePlayer dlView = CreatePlayer.newInstance();
//        ft.replace(R.id.fragment_holder, dlView);
//        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.create_player) {
            Intent createPlayer = new Intent(MainActivity.this, CreatePlayerActivity.class);
            startActivity(createPlayer);
            // Handle the camera action
        } else if (id == R.id.new_match) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            NewMatch dlView = NewMatch.newInstance();
            ft.replace(R.id.fragment_holder, dlView);
            ft.commit();
        } else if (id == R.id.positions) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            DefinePositions dlView = DefinePositions.newInstance(prefs.getTeamId());
            ft.replace(R.id.fragment_holder, dlView);
            ft.commit();
        } else if (id == R.id.current_match){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            CurrentMatch dlView = CurrentMatch.newInstance(prefs.getTeamId());
            ft.replace(R.id.fragment_holder, dlView);
            ft.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPasswordGiven(String string) {
        mPasswordGiven.passwordGiven(string);
    }

    @Override
    public void onTeamSelected(String id) {
        Log.d(TAG, "show home for team");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        TeamView dlView = TeamView.newInstance(id);
        ft.replace(R.id.fragment_holder, dlView);
        ft.commit();
    }
}
