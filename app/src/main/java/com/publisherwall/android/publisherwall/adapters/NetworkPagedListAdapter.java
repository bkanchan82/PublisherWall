package com.publisherwall.android.publisherwall.adapters;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.data.NetworkEntry;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.NetworkData;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NetworkPagedListAdapter extends PagedListAdapter<NetworkEntry, NetworkPagedListAdapter.NetworksViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    // Class variables for the List that holds task data and the Context
//    private Context mContext;

    /**
     * Constructor for the TaskAdapter that initializes the Context.
     *
     * @param context  the current Context
     * @param listener the ItemClickListener
     */
    public NetworkPagedListAdapter(Context context, ItemClickListener listener) {
        super(NetworkEntry.DIFF_CALLBACK);
//        mContext = context;
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
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
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

        NetworkEntry networkEntry = getItem(position);

        if (networkEntry != null) {
            holder.onBindData(position);
        } else {
            holder.clear();
        }
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

        void clear() {

            networkLogoImageView.invalidate();
            networkNameTextView.invalidate();
            joinTextView.invalidate();
            commissionTextView.invalidate();
            minPayTextView.invalidate();
            payFrequencyTextView.invalidate();
            offerTextView.invalidate();
            likeTextView.invalidate();
            containerView.invalidate();
            itemView.invalidate();
        }

        public void onBindData(int position) {
            NetworkEntry networkEntry = getItem(position);
            Context context = itemView.getContext();
            Picasso.with(context).load(networkEntry.getNetworkImageUrl()).into(networkLogoImageView);
            networkNameTextView.setText(networkEntry.getNetworkName());
            commissionTextView.setText(networkEntry.getNetworkCommission());
            minPayTextView.setText(context.getString(R.string.tv_min_pay, networkEntry.getNetworkMinPay()));
            payFrequencyTextView.setText(networkEntry.getNetworkPayFrequency());
            offerTextView.setText(networkEntry.getNetworkOffers());
            if(networkEntry.getNetworkFavoriteCount().trim().equals("0")){
                likeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_24dp, 0, 0, 0);
            }else{
                likeTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red_24dp, 0, 0, 0);
            }
            likeTextView.setText(networkEntry.getNetworkFavoriteCount());

        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Context context = view.getContext();
            NetworkEntry networkEntry = getItem(adapterPosition);
            if (view.getId() == R.id.bt_join) {
                AppUtilities.openWebPage(context, networkEntry.getNetworkJoinUrl());
            } else {
                Log.v(NetworkPagedListAdapter.class.getSimpleName(), "Network SNO : " + networkEntry.getNetworkSno());
                mItemClickListener.onItemClickListener(networkEntry);
            }
        }

    }

}
