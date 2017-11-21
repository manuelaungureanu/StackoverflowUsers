package com.chefless.ela.stackoverflowusers.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chefless.ela.stackoverflowusers.R;
import com.chefless.ela.stackoverflowusers.databinding.FragmentUsersBinding;
import com.chefless.ela.stackoverflowusers.userdetail.UserDetailActivity;
import com.chefless.ela.stackoverflowusers.data.models.User;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment implements UsersContract.View, UsersAdapter.UsersAdapterOnClickHandler {

    private UsersContract.Presenter mPresenter;

    private UsersAdapter mAdapter;

    private View mNoUsersView;

    private TextView mNoUsersMainView;

    private RelativeLayout mUsersView;

    private TextView mFilteringLabelView;

    private RecyclerView mRVUsers;

    private ProgressBar mLoadingIndicator;

    private TextView mTotalPagesNoView;

    private EditText mBudgetView;

    public UsersFragment() {
        // Required empty public constructor
    }

    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new UsersAdapter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentUsersBinding binding = FragmentUsersBinding.inflate(inflater, container, false);
        initUI(binding);

        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void initUI(FragmentUsersBinding binding) {

        mRVUsers = binding.rvUsers;
        mUsersView = binding.usersLL;

        mNoUsersView = binding.noUsers;
        mNoUsersMainView = binding.noUsersMain;
        mLoadingIndicator = binding.pbLoadingData;

        GridLayoutManager manager = new GridLayoutManager(binding.getRoot().getContext(), 2);
        mRVUsers.setLayoutManager(manager);
        mRVUsers.setHasFixedSize(true);
        mRVUsers.setAdapter(mAdapter);

        RecyclerView.ItemAnimator animator = mRVUsers.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mAdapter.setData(null);
                mPresenter.loadUsers(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.users_fragment_menu, menu);
    }

    @Override
    public void setPresenter(UsersContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadingIndicator.setVisibility(active?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showUsers(List<User> users) {
        mAdapter.setData(users);

        mUsersView.setVisibility(View.VISIBLE);
        mNoUsersView.setVisibility(View.GONE);
    }

    @Override
    public void showUserDetailsUi(int userId) {
        Intent intent = new Intent(getContext(), UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void showLoadingUsersError() {
        setLoadingIndicator(false);
        showMessage(getString(R.string.loading_users_error));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoUsers() {
        showNoUsersViews(getResources().getString(R.string.no_users_all));
    }

    private void showNoUsersViews(String mainText) {
        mUsersView.setVisibility(View.GONE);
        mNoUsersView.setVisibility(View.VISIBLE);
        mNoUsersMainView.setText(mainText);
    }

    //region UsersAdapter.UsersAdapterOnClickHandler implementation
    @Override
    public void onClick(User user) {
        showUserDetailsUi(user.getUser_id());
    }
    //endregion
}
