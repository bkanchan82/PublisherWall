package com.publisherwall.android.publisherwall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.publisherwall.android.publisherwall.app.PreferenceUtils;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.fcm.PbFirebaseMessagingService;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;
import com.publisherwall.android.publisherwall.utilities.NotificationUtilities;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EventFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        ImageView profilePicImageView = (ImageView) header.findViewById(R.id.profile_pic);
        TextView displayName = (TextView) header.findViewById(R.id.display_name);
        TextView emailTextView = (TextView) header.findViewById(R.id.email_textview);


        Log.v(TAG, "Is Login : " + isLogin());
        if (!isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences sharedPref = this.getSharedPreferences(
                    getString(R.string.preference_file_credential), this.MODE_PRIVATE);

            Picasso.with(this).load(sharedPref.getString(getString(R.string.pref_profile_pic_url_key), "")).into(profilePicImageView);
            displayName.setText(sharedPref.getString(getString(R.string.pref_user_name_key), ""));
            emailTextView.setText(sharedPref.getString(getString(R.string.pref_email_key), ""));
        }
        if (savedInstanceState == null) {

            Fragment fragment = null;
            fragment = new NetworkFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();

        }

        Log.d(TAG, "FCM Token : " + PreferenceUtils.getFcmRegistrationToken(this));
        String currentVersionString = PreferenceUtils.getCurrentVersionOnServer(this);
        int currentVersionOnServer = Integer.parseInt(currentVersionString);
        if (BuildConfig.VERSION_CODE < currentVersionOnServer) {
            boolean isUpdateMustRequire = PreferenceUtils.isUpdateMustRequire(this);
            showUpdateDialog(!isUpdateMustRequire);
        }


    }

    Dialog dialog;

    private void showUpdateDialog(boolean cancelableStatus) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A New Update is Available");
        builder.setCancelable(cancelableStatus);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.publisherwall.android.publisherwall")));
                dialog.dismiss();
            }
        });
        if (cancelableStatus) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        dialog = builder.show();
    }

    private boolean isLogin() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_credential), this.MODE_PRIVATE);
        boolean isSignedIn = sharedPref.getBoolean(getString(R.string.pref_is_signed_in),
                getResources().getBoolean(R.bool.pref_is_signed_default_value));

        return isSignedIn;
    }


    public void setToolbarTitle(int stringResourceId) {
        mToolbar.setTitle(stringResourceId);
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
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
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
        Fragment fragment = null;
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_networks:
                fragment = new NetworkFragment();
                break;
            case R.id.nav_favorite_networks:
                fragment = new FavoriteNetworksFragment();
                break;
            case R.id.nav_events:
                fragment = new EventFragment();
                break;
            case R.id.nav_tools:
                AppUtilities.openWebPage(this, NetworkUtils.PUBLISHER_TOOLS_URL);
                break;
            case R.id.nav_bye_email_data:
                AppUtilities.openWebPage(this, NetworkUtils.BUY_EMAIL_DATA_URL);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
