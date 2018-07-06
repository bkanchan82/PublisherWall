package com.publisherwall.android.publisherwall;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.utilities.AppExecutor;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.EventData;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String TAG = EventDetailActivity.class.getSimpleName();
    public static final String EXTRA_EVENT_DATA = "extra_event_data";
    public static final String SAVED_EVENT_DATA = "saved_event_data";

    private ActionBar actionBar;

    private int eventId;
    private EventData eventData;

    private FloatingActionButton mMarkAsFavorite;
    ImageView mPosterImageView;
    TextView mDateTextView;
    TextView mEventLocationTextView;
    TextView mEventDescriptionTextView;
    TextView mEventTitle;
    TextView mFavoriteCount;
    Button visitPageButton;

    private boolean isFavorite;
    private AppDatabase mAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_one);

        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mAppDatabase = AppDatabase.getAppDatabaseInstance(this.getApplicationContext());

        mMarkAsFavorite = findViewById(R.id.mark_as_favorite);
        mMarkAsFavorite.setOnClickListener(this);

        mPosterImageView = findViewById(R.id.iv_event_poster);
        mDateTextView = findViewById(R.id.event_date_text_view);
        mEventLocationTextView = findViewById(R.id.event_location_text_view);
        mEventDescriptionTextView = findViewById(R.id.event_description);
        mEventTitle = findViewById(R.id.event_name_text_view);
        mFavoriteCount = findViewById(R.id.favoriteCount);
        visitPageButton = findViewById(R.id.visit_web);



        if (savedInstanceState == null) {
            Intent intentThatInvokeTheActivity = getIntent();
            if (intentThatInvokeTheActivity.hasExtra(EXTRA_EVENT_DATA)) {
                eventData = intentThatInvokeTheActivity.getParcelableExtra(EXTRA_EVENT_DATA);

                LiveData<EventEntry> liveData = mAppDatabase.eventDao().getEventEntryByEid(eventData.getEdi());
                liveData.observe(this, new Observer<EventEntry>() {
                    @Override
                    public void onChanged(@Nullable EventEntry eventEntry) {
                        liveData.removeObserver(this);
                        if(eventEntry != null){
                            eventId = eventEntry.getId();
                            if( eventEntry.getIsFavorite().trim().equals("1")) {
                                isFavorite = true;
                                mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_96dp);
                            }else{
                                isFavorite = false;
                                if(eventData.getIsFavorite().trim().equals("1")){
                                    saveFavoriteEvent(eventEntry,isFavorite);
                                }
                                mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_border_96dp);
                            }
                        }
                    }
                });

            }
        } else {
            eventData = savedInstanceState.getParcelable(SAVED_EVENT_DATA);
        }


        updateUi(eventData);

    }


    private void postLikedEvent(final String eventEid, final String isLike, final String eventName) {
        try {

            SharedPreferences sharedPref = EventDetailActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_credential), EventDetailActivity.this.MODE_PRIVATE);
            final String emailAddress = sharedPref.getString(getString(R.string.pref_email_key), "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    NetworkUtils.POST_LIKED_EVENT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.v(TAG, "Post liked network detail response : " + response);
                            if (response.contains("success")) {
                                markAsFavorite();
                            }
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
                    params.put("event_id", eventEid);
                    params.put("event_name", eventName);
                    params.put("email", emailAddress);
                    params.put("is_fav", isLike);

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        Log.v(TAG, "Param key : " + entry.getKey() + " and value : " + entry.getValue());
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

    public void markAsFavorite() {
        EventEntry eventEntry = new EventEntry(eventData.getEdi(),
                eventData.getTitle(),
                eventData.getUrl(),
                eventData.getDate(),
                eventData.getLocation(),
                eventData.getDescription(),
                eventData.getPosterUrl(),
                eventData.getIsFavorite(),
                eventData.getFavoriteCount());
        eventEntry.setId(eventId);
        saveFavoriteEvent(eventEntry,isFavorite);
    }

    private boolean saveFavoriteEvent(final EventEntry eventEntry, final boolean isFavorite){

        AppExecutor appExecutor = AppExecutor.getAppExecutorInstance();

        appExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                String favoritCounts = eventEntry.getTotalFavorite();
                int fc = 0;
                try {
                    fc = Integer.parseInt(favoritCounts);
                }catch (NumberFormatException ne){
                    fc = 0;
                }
                if(isFavorite) {
                    eventEntry.setIsFavorite("0");
                    if(fc != 0){
                        fc--;
                    }
                }else {
                    eventEntry.setIsFavorite("1");
                    fc++;
                }
                eventEntry.setTotalFavorite(String.valueOf(fc));
                mAppDatabase.eventDao().updateTask(eventEntry);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isFavorite) {
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_border_24dp);
                        }else {
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_96dp);
                        }
                        mFavoriteCount.setText(eventEntry.getTotalFavorite());
                    }
                });
            }
        });
        return false;
    }


    private void updateUi(final EventData eventData) {

        if (eventData != null) {
            mEventTitle.setText(eventData.getTitle());
            if (actionBar != null) {
                actionBar.setTitle(eventData.getTitle());
            }
            Picasso.with(this).load(eventData.getPosterUrl()).into(mPosterImageView);
//                collapsingToolbar.setTitle(eventData.getTitle());
            mDateTextView.setText(eventData.getDate());
            mEventLocationTextView.setText(eventData.getLocation());
            mEventDescriptionTextView.setText(eventData.getDescription());
            mFavoriteCount.setText(eventData.getFavoriteCount());
            visitPageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtilities.openWebPage(EventDetailActivity.this, eventData.getUrl());
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_EVENT_DATA, eventData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mark_as_favorite:
                int isLike = isFavorite ? 0 : 1;
                postLikedEvent(eventData.getEdi(), String.valueOf(isLike),eventData.getTitle());
                break;

        }
    }
}
