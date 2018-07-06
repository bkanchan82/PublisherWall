package com.publisherwall.android.publisherwall;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.publisherwall.android.publisherwall.adapters.EventListAdapter;
import com.publisherwall.android.publisherwall.app.PublisherWallAppController;
import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.data.EventViewModel;
import com.publisherwall.android.publisherwall.sync.PublisherWallSyncUtils;
import com.publisherwall.android.publisherwall.utilities.EventData;
import com.publisherwall.android.publisherwall.utilities.NetworkUtils;
import com.publisherwall.android.publisherwall.utilities.TheNetworkJsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EventFragment extends Fragment implements
        SearchView.OnQueryTextListener,
        MenuItem.OnActionExpandListener, EventListAdapter.EventAdapterListener {

    private OnFragmentInteractionListener mListener;

    public EventFragment() {
        // Required empty public constructor
    }


    private static final String TAG = EventFragment.class.getSimpleName();

    private TextView mDisplayErrorTV;
    private RecyclerView mDisplayEventRV;
    private ProgressBar loadingEventListPb;

    private EventListAdapter adapter;
    private static ArrayList<EventData> mEventList;


    private Context mContext;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setToolbarTitle(R.string.event_list);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event,
                container,
                false);

        mDisplayErrorTV = view.findViewById(R.id.tv_display_error);
        mDisplayEventRV = view.findViewById(R.id.rv_display_event);
        loadingEventListPb = view.findViewById(R.id.pb_loading_event);

       /* final int columns = getResources().getInteger(R.integer.gallery_column);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL);*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mDisplayEventRV.setLayoutManager(linearLayoutManager);
        mDisplayEventRV.setHasFixedSize(true);

        adapter = new EventListAdapter(mContext, this);

        PublisherWallSyncUtils.initializeEvent(mContext);
        mDisplayEventRV.setAdapter(adapter);
        loadEventViewModel();
        return view;
    }

    private void loadEventViewModel() {
        loadingEventListPb.setVisibility(View.VISIBLE);
        EventViewModel networkViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        networkViewModel.getListLiveData().observe(this, new Observer<List<EventEntry>>() {
            @Override
            public void onChanged(List<EventEntry> eventEntries) {
                if (eventEntries != null && eventEntries.size() > 0) {
                    adapter.swapEventList(eventEntries);
                    showNetworks();
                    loadingEventListPb.setVisibility(View.INVISIBLE);
                }else {
                    Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //This is called when we will have a valid data
    private void showNetworks() {
        mDisplayErrorTV.setVisibility(View.INVISIBLE);
        mDisplayEventRV.setVisibility(View.VISIBLE);
    }


    //This is called when an error occurred in loading movie list
    private void showErrorMessage() {
        mDisplayErrorTV.setVisibility(View.VISIBLE);
        mDisplayEventRV.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem mActionSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView mActionSearchView = (SearchView) mActionSearchMenuItem.getActionView();
        mActionSearchView.setOnQueryTextListener(this);
        mActionSearchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }


    private void openEventDetail(EventData eventData) {
        Intent intent = new Intent(mContext, EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_DATA, eventData);
        startActivity(intent);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEventSelected(EventEntry eventEntry) {
        Intent intent = new Intent(mContext, EventDetailActivity.class);
        EventData eventData = new EventData(eventEntry);
        intent.putExtra(EventDetailActivity.EXTRA_EVENT_DATA, eventData);
        mContext.startActivity(intent);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
