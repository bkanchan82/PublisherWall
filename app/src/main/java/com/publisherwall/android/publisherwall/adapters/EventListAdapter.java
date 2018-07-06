package com.publisherwall.android.publisherwall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.data.EventDao;
import com.publisherwall.android.publisherwall.data.EventEntry;
import com.publisherwall.android.publisherwall.utilities.AppUtilities;
import com.publisherwall.android.publisherwall.utilities.EventData;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListHolder>
implements Filterable{

    private Context mContext;
    private List<EventEntry> eventEntryArrayList;
    private List<EventEntry> filteredEventDataArrayList;

    private EventAdapterListener mListener;

    public EventListAdapter(Context context,EventAdapterListener eventAdapterListener) {
        mContext = context;
        mListener = eventAdapterListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredEventDataArrayList = eventEntryArrayList;
                } else {
                    List<EventEntry> filteredList = new ArrayList<>();
                    for (EventEntry row : eventEntryArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredEventDataArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredEventDataArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEventDataArrayList = (ArrayList<EventEntry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface EventAdapterListener {
        void onEventSelected(EventEntry eventEntry);
    }

    public void swapEventList(List<EventEntry> eventEntryArrayList) {
        if (eventEntryArrayList == null) return;
        this.eventEntryArrayList = eventEntryArrayList;
        this.filteredEventDataArrayList = eventEntryArrayList;
        this.notifyDataSetChanged();
    }

    public ArrayList<EventEntry> getFilteredEventDataArrayList(){
        return (ArrayList<EventEntry>) filteredEventDataArrayList;
    }

    @NonNull
    @Override
    public EventListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.event_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutId,
                parent,
                false);

        EventListHolder eventListHolder = new EventListHolder(view);
        return eventListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventListHolder holder, int position) {
        holder.onBindData(position);
    }

    @Override
    public int getItemCount() {
        if (filteredEventDataArrayList == null) {
            return 0;
        }

        return filteredEventDataArrayList.size();
    }


    public class EventListHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        TextView eventTitleTv;
        TextView eventLocationTv;
        TextView eventDateTv;
        TextView eventUrlTv;
        TextView favoriteCount;
        View container;

        public EventListHolder(View itemView) {
            super(itemView);

            eventTitleTv = itemView.findViewById(R.id.tv_event_name);
            eventLocationTv = itemView.findViewById(R.id.tv_event_location);
            eventDateTv = itemView.findViewById(R.id.tv_event_date);
            eventUrlTv = itemView.findViewById(R.id.visit_url);
            container = itemView.findViewById(R.id.event_list_item_container);
            favoriteCount = itemView.findViewById(R.id.favoriteCount);

            eventUrlTv.setOnClickListener(this);
            container.setOnClickListener(this);
        }

        public void onBindData(int position) {

            EventEntry eventEntry = filteredEventDataArrayList.get(position);
            eventTitleTv.setText(eventEntry.getTitle());
            eventLocationTv.setText(eventEntry.getLocation());
            eventDateTv.setText(eventEntry.getDate());
            if(eventEntry.getTotalFavorite().trim().equals("0")){
                favoriteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_24dp, 0, 0, 0);
            }else{
                favoriteCount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_red_24dp, 0, 0, 0);
            }
            favoriteCount.setText(eventEntry.getTotalFavorite());

        }

        @Override
        public void onClick(View view) {
            int adapterPosition  = getAdapterPosition();
            EventEntry eventEntry = filteredEventDataArrayList.get(adapterPosition);
            if(view.getId() == R.id.visit_url){
                AppUtilities.openWebPage(mContext,eventEntry.getUrl());
            }else{
                mListener.onEventSelected(eventEntry);
            }

        }
    }

}
