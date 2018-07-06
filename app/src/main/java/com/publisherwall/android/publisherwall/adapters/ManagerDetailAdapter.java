package com.publisherwall.android.publisherwall.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.publisherwall.android.publisherwall.R;
import com.publisherwall.android.publisherwall.utilities.ManagerData;

import java.util.ArrayList;

public class ManagerDetailAdapter extends RecyclerView.Adapter<ManagerDetailAdapter.ManagerViewHolder> {

    private Context mContext;
    private ArrayList<ManagerData> managerDataArrayList;

    public ManagerDetailAdapter(Context context) {
        mContext = context;
    }

    public void swapManagerList(ArrayList<ManagerData> managerData) {
        if (managerData == null) return;
        this.managerDataArrayList = managerData;
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.aff_manager_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutId,
                parent,
                false);

        ManagerDetailAdapter.ManagerViewHolder managerListHolder = new ManagerDetailAdapter.ManagerViewHolder(view);
        return managerListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerViewHolder holder, int position) {
        holder.onBindData(position);
    }

    @Override
    public int getItemCount() {
        if (managerDataArrayList == null) {
            return 0;
        }

        return managerDataArrayList.size();
    }

    public class ManagerViewHolder extends RecyclerView.ViewHolder {

        TextView managerNameTv;
        TextView emailTv;
        TextView phoneTv;
        TextView aimTv;
        TextView skypeTv;

        public ManagerViewHolder(View itemView) {
            super(itemView);

            managerNameTv = itemView.findViewById(R.id.tv_manager_name);
            emailTv = itemView.findViewById(R.id.tv_manager_email);
            phoneTv = itemView.findViewById(R.id.tv_manager_phone_no);
            aimTv = itemView.findViewById(R.id.aim_id);
            skypeTv = itemView.findViewById(R.id.skype_id);
        }

        public void onBindData(int position) {
            ManagerData managerData = managerDataArrayList.get(position);

            managerNameTv.setText(managerData.getManagerName());
            emailTv.setText(managerData.getEmail());
            phoneTv.setText(managerData.getPhoneNo());
            aimTv.setText(mContext.getString(R.string.aff_manager_aim,managerData.getAimId()));
            skypeTv.setText(mContext.getString(R.string.aff_manager_skype_id,managerData.getPhoneNo()));
        }
    }

}
