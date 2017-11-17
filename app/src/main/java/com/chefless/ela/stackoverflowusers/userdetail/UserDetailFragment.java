package com.chefless.ela.stackoverflowusers.userdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chefless.ela.stackoverflowusers.BasePresenter;
import com.chefless.ela.stackoverflowusers.R;
import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.databinding.FragmentUserDetailBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserDetailFragment extends Fragment implements UserDetailContract.View {

    @NonNull
    private static final String ARG_PARAM_USER_ID = "userId";
    private int mUserId;
    private BasePresenter mPresenter;
    private FragmentUserDetailBinding mBinding;

    private RelativeLayout mDetailContainer;
    private TextView mNoData;
    private ProgressBar mLoadingIndicator;

    public static UserDetailFragment newInstance(int userId) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mUserId = getArguments().getInt(ARG_PARAM_USER_ID, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public UserDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentUserDetailBinding.inflate(inflater, container, false);
        initUI(mBinding);
        return mBinding.getRoot();
    }

    private void initUI(FragmentUserDetailBinding mBinding) {
        mLoadingIndicator = mBinding.pbLoadingData;
        mDetailContainer = mBinding.detailContainer;
        mNoData = mBinding.noData;
    }


    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadingIndicator.setVisibility(active?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showMissingUser() {
        mNoData.setText(getString(R.string.no_data));
        mNoData.setVisibility(View.VISIBLE);
        mDetailContainer.setVisibility(View.INVISIBLE);
        setLoadingIndicator(false);
    }

    @Override
    public void showDetails() {
        mNoData.setVisibility(View.INVISIBLE);
        mDetailContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setModel(@NonNull Object model) {
        Object obj = checkNotNull(model);
        User user = (User) obj;
        mBinding.setUser(user);

        String mainImageUrl = user.getProfile_image();
       if(!TextUtils.isEmpty(mainImageUrl))

        Glide.with(getActivity())
                .load(mainImageUrl)
                .thumbnail(0.1f)
                .into(mBinding.ivMainImage);
    }
}
