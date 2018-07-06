package com.publisherwall.android.publisherwall;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.adapters.NetworkAdapter;
import com.publisherwall.android.publisherwall.data.FavoriteNetworkVIewModel;
import com.publisherwall.android.publisherwall.data.NetworkEntry;

import java.util.List;

public class FavoriteNetworksFragment extends Fragment
        implements SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener,
        NetworkAdapter.ItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    //mDisplayErrorTV will display error message if there is any error in loading the movie list
    private TextView mDisplayErrorTV;

    //mDisplayNetworksRV is used to display the movie list on the entire screen
    private RecyclerView mDisplayNetworksRV;

    //loadingNetworksListPb will show progress bar while it will load movie list from network
    private ProgressBar loadingNetworksListPb;

    private NetworkAdapter adapter;


    private Context mContext;

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

        mDisplayErrorTV = view.findViewById(R.id.tv_display_error);
        mDisplayNetworksRV = view.findViewById(R.id.rv_display_networks);
        loadingNetworksListPb = view.findViewById(R.id.pb_loading_networks);
        loadingNetworksListPb.setVisibility(View.GONE);

        final int columns = getResources().getInteger(R.integer.gallery_column);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);

        mDisplayNetworksRV.setLayoutManager(gridLayoutManager);
        mDisplayNetworksRV.setHasFixedSize(true);

        adapter = new NetworkAdapter(mContext, this);

        mDisplayNetworksRV.setAdapter(adapter);

        mHandler = new Handler();
        if(savedInstanceState != null) {
            mSearchQuery = savedInstanceState.getString(SEARCH_QUERY_TAG);
        }

        loadNetworkViewModel();
        return view;
    }

    private void loadNetworkViewModel() {
        showLoading();
        FavoriteNetworkVIewModel networkViewModel = ViewModelProviders.of(this).get(FavoriteNetworkVIewModel.class);
        networkViewModel.getListLiveData().observe(this, new Observer<List<NetworkEntry>>() {
            @Override
            public void onChanged(List<NetworkEntry> networkEntries) {
                adapter.setNetworks(networkEntries);
                showNetworks();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle(R.string.favorite_network_list);

        }

    }
    //This is called when we will have a valid data
    private void showNetworks() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mDisplayNetworksRV.setVisibility(View.VISIBLE);
//        loadingNetworksListPb.setVisibility(View.GONE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mDisplayErrorTV.setText(getString(R.string.no_favorite_network_message));
        mDisplayNetworksRV.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        /* Then, hide the weather data */
        mDisplayNetworksRV.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
//        loadingNetworksListPb.setVisibility(View.VISIBLE);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SEARCH_QUERY_TAG, mSearchQuery);
        super.onSaveInstanceState(outState);
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
        if(mSearchQuery != null && !mSearchQuery.isEmpty()){
            final String query = mSearchQuery;

            if(mHandler != null
                    && mActionSearchView != null
                    && mActionSearchMenuItem != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mActionSearchMenuItem.expandActionView();
                        mActionSearchView.setQuery(query, true);
                        mActionSearchView.clearFocus();
                    }
                });
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchQuery = query;
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mSearchQuery = newText;
        adapter.getFilter().filter(newText);
        return true ;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
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
