package com.publisherwall.android.publisherwall;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.adapters.NetworkPagedListAdapter;
import com.publisherwall.android.publisherwall.data.AppDatabase;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.data.NetworkPagedViewModel;
import com.publisherwall.android.publisherwall.data.NetworkViewModelFactory;
import com.publisherwall.android.publisherwall.data.NetworkViewModelTest;
import com.publisherwall.android.publisherwall.sync.PublisherWallSyncUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkFragment extends Fragment
        implements
        SearchView.OnQueryTextListener,
        NetworkPagedListAdapter.ItemClickListener{

    private static final String TAG = NetworkFragment.class.getSimpleName();

    private TextView mDisplayErrorTV;
    private RecyclerView mDisplayNetworksRV;
    private ProgressBar loadingNetworksListPb;

        private NetworkPagedListAdapter adapter;
//    private NetworkAdapter adapter;

    private Context mContext;
    private AppDatabase mDb;
    NetworkViewModelFactory factory;
    NetworkViewModelTest viewModelTest;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_networks,
                container,
                false);
        final View view1 = view.findViewById(R.id.fragmentNetworks);
        mDisplayErrorTV = view.findViewById(R.id.tv_display_error);
        mDisplayNetworksRV = view.findViewById(R.id.rv_display_networks);
        loadingNetworksListPb = view.findViewById(R.id.pb_loading_networks);

       /* final int columns = getResources().getInteger(R.integer.gallery_column);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mDisplayNetworksRV.setLayoutManager(linearLayoutManager);

        adapter = new NetworkPagedListAdapter(mContext,this);



        PublisherWallSyncUtils.initialize(mContext);
        mDisplayNetworksRV.setAdapter(adapter);

        mHandler = new Handler();
        if(savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG);
        }
        mDb = AppDatabase.getAppDatabaseInstance(mContext);

        factory = new NetworkViewModelFactory(mDb,null );
        viewModelTest = ViewModelProviders.of(this, factory).get(NetworkViewModelTest.class);
        startListening();

        if(!isOnline()){
            Snackbar.make(view1,mContext.getString(R.string.network_error_message),Snackbar.LENGTH_LONG).show();
        }
//        loadNetworkPagedViewModel();
        return view;
    }



    private void loadNetworkPagedViewModel() {
        NetworkPagedViewModel viewModel = ViewModelProviders.of(this).get(NetworkPagedViewModel.class);
        LiveData<PagedList<NetworkEntry>> pagedListLiveData = viewModel.getListLiveData();
        pagedListLiveData.observe(this, pagedList -> adapter.submitList(pagedList));
    }

    private void startListening() {
        showLoading();
        viewModelTest.getListLiveData().observe(this, pagedList -> {
            showNetworks();
            //noinspection Convert2MethodRef
            adapter.submitList(pagedList); // used to be `setList`
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle(R.string.network_list);

        }

    }

    //This is called when we will have a valid data
    private void showNetworks() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mDisplayNetworksRV.setVisibility(View.VISIBLE);
        loadingNetworksListPb.setVisibility(View.GONE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mDisplayNetworksRV.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mDisplayNetworksRV.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        loadingNetworksListPb.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SEARCH_QUERY_TAG, mSearchQuery);
        super.onSaveInstanceState(outState);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        mActionSearchMenuItem = menu.findItem(R.id.action_search);
        mActionSearchView = (SearchView) mActionSearchMenuItem.getActionView();
        mActionSearchView.setOnQueryTextListener(this);
        mActionSearchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    public static final String SEARCH_QUERY_TAG = "SEARCH_QUERY_TAG";
    private MenuItem mActionSearchMenuItem;
    private SearchView mActionSearchView;
    private String mSearchQuery;

    private Handler mHandler;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchQuery = newText;
        replaceSubscription(newText);
        return true ;
    }

    private void replaceSubscription(String networkName) {
        viewModelTest.replaceSubscription(mDb,this, networkName);
        startListening();
    }

    @Override
    public void onItemClickListener(NetworkEntry networkEntry) {
        Intent intent = new Intent(mContext, NetworkDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, networkEntry.getNetworkSno());
        intent.putExtra(NetworkDetailActivity.EXTRA_SHARE_URL, networkEntry.getNetworkShareUrl());
        intent.putExtra(NetworkDetailActivity.EXTRA_FAVORITE_COUNT, networkEntry.getNetworkFavoriteCount());
        startActivity(intent);
    }
}
