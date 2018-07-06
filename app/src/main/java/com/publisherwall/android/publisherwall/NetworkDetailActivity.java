package com.publisherwall.android.publisherwall;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.publisherwall.android.publisherwall.adapters.ManagerDetailAdapter;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.data.SearchNetworkViewModel;
import com.publisherwall.android.publisherwall.data.SearchNetworkViewModelFactory;
import com.publisherwall.android.publisherwall.utilities.AppExecutor;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.CompleteNetworkData;
import com.publisherwall.android.publisherwall.utilities.ManagerData;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;
import com.publisherwall.android.publisherwall.utilities.TheNetworkJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NetworkDetailActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = NetworkDetailActivity.class.getSimpleName();
    public static final String EXTRA_SHARE_URL = "extra_share_url";
    public static final String EXTRA_FAVORITE_COUNT = "extra_favorite_count";
    private static final String COMPLETE_NETWORK_DATA = "complete_network_data";
    private static final String MANGER_ARRAY_LIST = "manager_array_list";
    private static final String IS_SHOWING_AFFILIATED_MANAGERS_LIST = "is_showing_affiliated_managers_list";

    private String mSno;
    private String shareUrl;
    private String favoriteCountString;
    private NetworkEntry mNetworkEntry;

    //mDisplayErrorTV will display error message if there is any error in loading the movie list
    private TextView mDisplayErrorTV;

    //loadingNetworkListPb will show progress bar while it will load movie list from network
    private ProgressBar loadingNetworkListPb;
    private View mRootView;
    private CompleteNetworkData mCompleteNetworkData;
    private ArrayList<ManagerData> managerDatas;
    private boolean isManagersRvShowing;

    private FloatingActionButton mMarkAsFavorite;

    private ActionBar actionBar;

    ImageView networkBigImage;
    FloatingActionButton actionShareButton;
    FloatingActionButton actionJoinButton;
    TextView networkContact;
    TextView networkEmail;
    TextView noOfOffers;
    TextView commissionType;
    TextView minimumPayment;
    TextView paymentFrequency;
    TextView paymentMethod;
    TextView referalContact;
    TextView trackingSoftware;
    TextView trackingLink;
    TextView description;
    TextView favoriteCount;
    TextView affiliateManagerTv;

    RecyclerView recyclerView;
    AppDatabase mAppDatabase;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_detail);

        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        networkBigImage = findViewById(R.id.iv_network_big_image);
        actionShareButton = findViewById(R.id.action_share);
        actionJoinButton = findViewById(R.id.action_join_network);
        networkContact = findViewById(R.id.tv_network_contact);
        networkEmail = findViewById(R.id.tv_email);
        noOfOffers = findViewById(R.id.tv_no_of_offers);
        commissionType = findViewById(R.id.tv_commission_type);
        minimumPayment = findViewById(R.id.tv_minimum_payment);
        paymentFrequency = findViewById(R.id.tv_payment_frequency);
        paymentMethod = findViewById(R.id.tv_payment_method);
        referalContact = findViewById(R.id.tv_referal_commission);
        trackingSoftware = findViewById(R.id.tv_tracking_soft);
        trackingLink = findViewById(R.id.tv_tracking_link);
        description = findViewById(R.id.tv_description);
        favoriteCount = findViewById(R.id.favoriteCount);
        affiliateManagerTv = findViewById(R.id.affiliateManagerLable);

        mAppDatabase = AppDatabase.getAppDatabaseInstance(this.getApplicationContext());

        recyclerView = findViewById(R.id.manager_detail_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ManagerDetailAdapter(this);

        recyclerView.setAdapter(adapter);


        mDisplayErrorTV = findViewById(R.id.tv_display_error);
        loadingNetworkListPb = findViewById(R.id.network_loading_pb);
        mRootView = findViewById(R.id.activity_main_done);
        mMarkAsFavorite = findViewById(R.id.action_favorite);
        mMarkAsFavorite.setOnClickListener(this);


        Intent activityInvokingIntent = getIntent();
        if (activityInvokingIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mSno = activityInvokingIntent.getStringExtra(Intent.EXTRA_TEXT);
            shareUrl = activityInvokingIntent.getStringExtra(EXTRA_SHARE_URL);
            favoriteCountString = activityInvokingIntent.getStringExtra(EXTRA_FAVORITE_COUNT);

            SearchNetworkViewModelFactory factory = new SearchNetworkViewModelFactory(mAppDatabase,mSno );

            final SearchNetworkViewModel addTaskViewModel = ViewModelProviders.of(this, factory).get(SearchNetworkViewModel.class);

            addTaskViewModel.getNetworkLiveData().observe(this, new Observer<NetworkEntry>() {
                @Override
                public void onChanged(NetworkEntry networkEntry) {
                    addTaskViewModel.getNetworkLiveData().removeObserver(this);
                    if (networkEntry != null){
                        mNetworkEntry = networkEntry;
                        if (mNetworkEntry.getNetworkIsLike() != null && mNetworkEntry.getNetworkIsLike().equals("1")) {
                            isFavorite = true;
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_96dp);
                        } else {
                            isFavorite = false;
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_border_96dp);
                        }
                    }
                }
            });
        }


        if (savedInstanceState == null) {
            loadNetworks();
        } else {
            showNetwork();
            CompleteNetworkData completeNetworkData = savedInstanceState.getParcelable(COMPLETE_NETWORK_DATA);
            ArrayList<ManagerData> managerDataArrayList = savedInstanceState.getParcelableArrayList(MANGER_ARRAY_LIST);
            isManagersRvShowing = savedInstanceState.getBoolean(IS_SHOWING_AFFILIATED_MANAGERS_LIST);
            mCompleteNetworkData = completeNetworkData;
            managerDatas = managerDataArrayList;
            updateUI(completeNetworkData, managerDataArrayList);
        }

        affiliateManagerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isManagersRvShowing) {
                    expand(recyclerView);
                    affiliateManagerTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less_24dp, 0);
                } else {
                    collapse(recyclerView);
                    affiliateManagerTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_24dp, 0);
                }
                isManagersRvShowing = !isManagersRvShowing;
            }
        });


    }

    public void markAsFavorite() {
        saveFavoriteNetwork(mCompleteNetworkData, isFavorite);
    }


    private void postLikedNetwork(final String networkName, final int isLike) {
        try {

            SharedPreferences sharedPref = NetworkDetailActivity.this.getSharedPreferences(
                    getString(R.string.preference_file_credential), NetworkDetailActivity.this.MODE_PRIVATE);
            final String emailAddress = sharedPref.getString(getString(R.string.pref_email_key), "");

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    NetworkUtils.POST_LIKED_NETWORK_URL,
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
                    params.put("net_name", networkName);
                    params.put("email", emailAddress);
                    params.put("is_like", String.valueOf(isLike));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuId = item.getItemId();

        switch (selectedMenuId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //This is called when we will have a valid data
    private void showNetwork() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mRootView.setVisibility(View.VISIBLE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mRootView.setVisibility(View.INVISIBLE);
    }


    private void loadNetworks() {
//        showNetwork();
        try {
            String movieListString = NetworkUtils.buildNetworkDetailUrl(mSno).toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    movieListString,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, "NETWORK Response : " + response.toString());
                            loadingNetworkListPb.setVisibility(View.INVISIBLE);
                            try {
                                CompleteNetworkData completeNetworkData = TheNetworkJsonUtils.getCompleteNetworkDataFromJsonString(response);
                                ArrayList<ManagerData> managerData = TheNetworkJsonUtils.getManagersDetailFromNetworkDetail(response);
                                if (completeNetworkData != null) {
                                    updateUI(completeNetworkData, managerData);
                                    mCompleteNetworkData = completeNetworkData;
                                    managerDatas = managerData;
                                    showNetwork();
                                } else {
                                    showErrorMessage();
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                                showErrorMessage();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingNetworkListPb.setVisibility(View.INVISIBLE);
                    showErrorMessage();
                    error.printStackTrace();
                    Log.v(TAG, "NETWORK Response error: " + error.getMessage());
                }
            });
            PublisherWallAppController.getInstance().addRequestQueue(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }

    ManagerDetailAdapter adapter;

    private void updateUI(CompleteNetworkData completeNetworkData, ArrayList<ManagerData> managerData) {

        loadingNetworkListPb.setVisibility(View.GONE);

        if (actionBar != null) {
            actionBar.setTitle(completeNetworkData.getNetworkName());
        }

        adapter.swapManagerList(managerData);

        actionShareButton.setOnClickListener(this);
        actionJoinButton.setOnClickListener(this);


        if (completeNetworkData != null) {

            Log.v(TAG, "completeNetworkData.getBigImg() : " + completeNetworkData.getBigImg());

            if (completeNetworkData.getBigImg() != null && completeNetworkData.getBigImg().length() > 0) {
                Picasso.with(this).load(completeNetworkData.getBigImg()).into(networkBigImage);
            }
            networkContact.setText(getString(R.string.network_contact_no, completeNetworkData.getPhone()));
            networkEmail.setText(getString(R.string.network_email, completeNetworkData.getEmail()));
            noOfOffers.setText(completeNetworkData.getOffers());

            commissionType.setText(completeNetworkData.getComm());
            minimumPayment.setText(completeNetworkData.getMinpay());
            paymentFrequency.setText(completeNetworkData.getPayfrq());
            paymentMethod.setText(completeNetworkData.getPaymeth());
            referalContact.setText(completeNetworkData.getRefcomm());
            trackingSoftware.setText(completeNetworkData.getTracksoft());
            trackingLink.setText(completeNetworkData.getTracklink());
            description.setText(completeNetworkData.getDescription());
            favoriteCount.setText(favoriteCountString);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView.getVisibility() == View.VISIBLE) {
            isManagersRvShowing = true;
        } else {
            isManagersRvShowing = false;
        }

    }

    public static void expand(final View v) {
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_share:
//                Log.v(TAG,"Share Url : "+shareUrl);
                if (shareUrl == null) {
                    AppUtilities.shareNetwork(this, mCompleteNetworkData.getAffpayingURL());
                } else {
                    AppUtilities.shareNetwork(this, shareUrl);
                }
                break;
            case R.id.action_favorite:
                int isLike = isFavorite ? 0 : 1;
                postLikedNetwork(mCompleteNetworkData.getNetworkName(), isLike);
                break;
            case R.id.action_join_network:
                if (mCompleteNetworkData != null) {
                    AppUtilities.openWebPage(this, mCompleteNetworkData.getNetworkJoinURL());
                }
                break;
        }
    }

    private boolean saveFavoriteNetwork(CompleteNetworkData completeNetworkData, final boolean isFavorite) {

        AppExecutor appExecutor = AppExecutor.getAppExecutorInstance();

        appExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                int totalFavoriteCount = 0;
                if (mNetworkEntry != null) {
                    String favoriteCountString = mNetworkEntry.getNetworkFavoriteCount();
                    Log.d(TAG, "favoriteCountString. : " + favoriteCountString);
                    try {
                        totalFavoriteCount = Integer.parseInt(favoriteCountString);
                    } catch (NumberFormatException nfe) {
                        totalFavoriteCount = 0;
                    }
                }
                if (isFavorite) {
                    if (totalFavoriteCount != 0) {
                        totalFavoriteCount--;
                    }
                    mNetworkEntry.setNetworkIsLike("0");
//                    mAppDatabase.favoriteNetworkDao().deleteFavoriteNetwork(mFavoriteNetworkEntry);
                } else {
                    totalFavoriteCount++;
                    mNetworkEntry.setNetworkIsLike("1");
//                    mFavoriteNetworkEntry.setNetworkFavoriteCount(favoriteCountString);
//                    mAppDatabase.favoriteNetworkDao().insertFavoriteNetwork(mFavoriteNetworkEntry);
                }
                mNetworkEntry.setNetworkFavoriteCount(String.valueOf(totalFavoriteCount));
                favoriteCountString = mNetworkEntry.getNetworkFavoriteCount();

                mAppDatabase.networkDao().updateTask(mNetworkEntry);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_border_96dp);
                        } else {
                            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_96dp);
                        }

                        favoriteCount.setText(favoriteCountString);
                    }
                });
            }
        });
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(COMPLETE_NETWORK_DATA, mCompleteNetworkData);
        outState.putParcelableArrayList(MANGER_ARRAY_LIST, managerDatas);
        outState.putBoolean(IS_SHOWING_AFFILIATED_MANAGERS_LIST, isManagersRvShowing);
    }
}
