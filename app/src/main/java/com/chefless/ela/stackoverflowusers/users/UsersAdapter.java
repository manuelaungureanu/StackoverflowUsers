package com.chefless.ela.stackoverflowusers.users;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.chefless.ela.stackoverflowusers.R;
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
    public void onBindViewHolder(final UserAdapterViewHolder holder, final int position) {
        final User item = mData.get(position);
        final Context context = holder.itemView.getContext();
        holder.binding.setItem(item);
        holder.binding.setContext(context);

        if(item==null)
            return;

        String url = item.getProfile_image();
        if(!URLUtil.isValidUrl(url))
            return;

        Glide.with(holder.itemView.getContext())
                .load(url)
                .fitCenter()
                .thumbnail(0.1f)
                .into(holder.binding.ivThumb);

        holder.binding.btnFollowUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setFollowed(!item.isFollowed());
                notifyItemChanged(position);
            }
        });

        //change name in "Unfollow" when touching the button in following state
        holder.binding.btnFollowUnfollow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(item.isFollowed())
                        holder.binding.btnFollowUnfollow.setText(R.string.unfollow);
                }
                return false;
            }
        });

        holder.binding.btnBlockUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setBlocked(!item.isBlocked());
                notifyItemChanged(position);
            }
        });

        //change name in "Unblock" when touching the button in blocked state
        holder.binding.btnBlockUnblock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(item.isBlocked())
                        holder.binding.btnBlockUnblock.setText(R.string.unblock);
                }
                return false;
            }
        });

        //entire view becomes unclickable for blocked users
        holder.itemView.setClickable(!item.isBlocked());
        holder.itemView.setEnabled(!item.isBlocked());

        //cannot follow someone blocked
        holder.binding.btnFollowUnfollow.setClickable(!item.isBlocked());
        holder.binding.btnFollowUnfollow.setEnabled(!item.isBlocked());
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
