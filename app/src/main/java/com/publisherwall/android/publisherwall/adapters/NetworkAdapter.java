package com.publisherwall.android.publisherwall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.NetworkData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.NetworksViewHolder> implements Filterable {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
    private List<NetworkEntry> mNetworkEntries;
    private List<NetworkEntry> mFilteredNetworkEntries = new ArrayList<>();
    private Context mContext;

    /**
     * Constructor for the TaskAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public NetworkAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public NetworksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.network_list_item, parent, false);

        return new NetworksViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(NetworksViewHolder holder, int position) {
        // Determine the values of the wanted data
        holder.onBindData(position);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mFilteredNetworkEntries == null) {
            return 0;
        }
        return mFilteredNetworkEntries.size();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setNetworks(List<NetworkEntry> networkEntries) {
        mNetworkEntries = networkEntries;
        mFilteredNetworkEntries = networkEntries;
//        mFilteredNetworkEntries.addAll(mNetworkEntries);
        notifyDataSetChanged();
    }


    public List<NetworkEntry> getNetworks() {
        return mNetworkEntries;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredNetworkEntries = mNetworkEntries;
                } else {
                    List<NetworkEntry> filteredList = new ArrayList<>();
                    for (NetworkEntry row : mNetworkEntries) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNetworkName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mFilteredNetworkEntries = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredNetworkEntries;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredNetworkEntries = (ArrayList<NetworkEntry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {
        void onItemClickListener(NetworkEntry networkEntry);
    }


    public class NetworksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView networkLogoImageView;
        TextView networkNameTextView;
        TextView joinTextView;
        TextView commissionTextView;
        TextView minPayTextView;
        TextView payFrequencyTextView;
        TextView offerTextView;
        TextView likeTextView;
        View containerView;

        public NetworksViewHolder(View itemView) {
            super(itemView);
            networkLogoImageView = (ImageView) itemView.findViewById(R.id.iv_logo);
            networkNameTextView = (TextView) itemView.findViewById(R.id.tv_network_name);
            joinTextView = (TextView) itemView.findViewById(R.id.bt_join);
            commissionTextView = (TextView) itemView.findViewById(R.id.iv_commission);
            minPayTextView = (TextView) itemView.findViewById(R.id.tv_min_pay);
            payFrequencyTextView = (TextView) itemView.findViewById(R.id.tv_pay_frequency);
            offerTextView = (TextView) itemView.findViewById(R.id.iv_offers);
            likeTextView = (TextView) itemView.findViewById(R.id.iv_likes);
            containerView = itemView.findViewById(R.id.layoutContainer);

            joinTextView.setOnClickListener(this);
            containerView.setOnClickListener(this);
        }

        public void onBindData(int position) {
            if (position < mFilteredNetworkEntries.size()) {
                NetworkEntry networkEntry = mFilteredNetworkEntries.get(position);
                Picasso.with(mContext).load(networkEntry.getNetworkImageUrl()).into(networkLogoImageView);
                networkNameTextView.setText(networkEntry.getNetworkName());
                commissionTextView.setText(networkEntry.getNetworkCommission());
                minPayTextView.setText(mContext.getString(R.string.tv_min_pay, networkEntry.getNetworkMinPay()));
                payFrequencyTextView.setText(networkEntry.getNetworkPayFrequency());
                offerTextView.setText(networkEntry.getNetworkOffers());
                if(networkEntry.getNetworkFavoriteCount().trim().equals("0")){
                    likeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_24dp, 0, 0, 0);
                }else{
                    likeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red_24dp, 0, 0, 0);
                }
                likeTextView.setText(networkEntry.getNetworkFavoriteCount());
            }

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            NetworkEntry networkEntry = mFilteredNetworkEntries.get(adapterPosition);
            if (view.getId() == R.id.bt_join) {
                AppUtilities.openWebPage(mContext, networkEntry.getNetworkJoinUrl());
            } else {
                Log.v(NetworkAdapter.class.getSimpleName(), "Network SNO : " + networkEntry.getNetworkSno());
                mItemClickListener.onItemClickListener(networkEntry);
            }
        }

    }

}
