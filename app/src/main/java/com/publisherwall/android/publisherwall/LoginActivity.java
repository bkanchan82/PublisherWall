package com.publisherwall.android.publisherwall;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1987;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }

        isGooglePlayServicesAvailable();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.google_web_token_id))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleSignInResult(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                firebaseAuthWithGoogle(task.getResult());
            }catch(Exception e){
                Log.e(LoginActivity.class.getSimpleName(),"Play services Exception : "+e.getMessage());
            }

        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
                return false;
            }else {
                Toast.makeText(this,"App won't run without Google Play Services",Toast.LENGTH_SHORT).show();
                finish();
                return false;
            }

        }
        return true;
    }


    private void handleSignInResult(FirebaseUser firebaseUser) {
//        try {
//            GoogleSignInAccount account = firebaseUser.getResult(ApiException.class);
            Log.v(TAG, "Signin successfully with " + firebaseUser.getDisplayName());
            postUserData(firebaseUser);
       /* } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }*/
    }

    private void postUserData(final FirebaseUser account) {
        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    NetworkUtils.POST_USER_DETAIL_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v(TAG, "Post user detail response : " + response);

                            SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences(
                                    getString(R.string.preference_file_credential), LoginActivity.this.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.pref_user_name_key), account.getDisplayName());
                            editor.putString(getString(R.string.pref_profile_pic_url_key), account.getPhotoUrl().toString());
                            editor.putString(getString(R.string.pref_email_key), account.getEmail());
                            editor.putBoolean(getString(R.string.pref_is_signed_in), true);
                            editor.apply();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            LoginActivity.this.finish();
                            startActivity(intent);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.v(TAG, "Post user detail error : " + error.getMessage());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("name", account.getDisplayName());
                    params.put("email", account.getEmail());
                    params.put("image", account.getPhotoUrl().toString());

                    for (Map.Entry<String ,String> entry : params.entrySet()){
                        Log.d(TAG,"PARAMS : "+entry.getKey()+"/"+entry.getValue());
                    }

                    return params;

                }
            };
            PublisherWallAppController.getInstance().addRequestQueue(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }


}
