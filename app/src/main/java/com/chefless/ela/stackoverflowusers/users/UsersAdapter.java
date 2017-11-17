package com.chefless.ela.stackoverflowusers.users;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.bumptech.glide.Glide;
import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.databinding.UsersListItemBinding;

import java.util.List;

/**
 * Created by ela on 26/09/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserAdapterViewHolder>
{
    private List<User> mData;

    /** An on click handler added for the activity for interact with the recycler viw*/
    private final UsersAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface UsersAdapterOnClickHandler {
        void onClick(User user);
    }

    public UsersAdapter(UsersAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public UserAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        UsersListItemBinding binding = UsersListItemBinding.inflate(inflater, parent, false);
        return new UserAdapterViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(UserAdapterViewHolder holder, int position) {
        User item = mData.get(position);
        holder.binding.setItem(item);
        holder.binding.setPosition(position+1);

        if(item==null)
            return;

        String url = item.getProfile_image();
        if(!URLUtil.isValidUrl(url))
            return;

        Glide.with(holder.itemView.getContext())
                .load(url)
                .thumbnail(0.1f)
                .into(holder.binding.ivThumb);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<User> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class UserAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public UsersListItemBinding binding;

        public UserAdapterViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            User user = mData.get(position);
            mClickHandler.onClick(user);
        }
    }
}
