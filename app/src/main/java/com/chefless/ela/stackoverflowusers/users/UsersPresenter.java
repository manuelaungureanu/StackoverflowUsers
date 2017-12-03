package com.chefless.ela.stackoverflowusers.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.data.source.UsersDataSource;
import com.chefless.ela.stackoverflowusers.data.source.UsersLoader;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required. It is implemented as a non UI to make use of the
 * {@link LoaderManager} mechanism for managing loading and updating data asynchronously.
 */
public class UsersPresenter implements UsersContract.Presenter,
        LoaderManager.LoaderCallbacks<List<User>> {

    private final static int USERS_QUERY = 1;

    private final UsersRepository mUsersRepository;

    private final UsersContract.View mUsersView;

    private final UsersLoader mLoader;

    private final LoaderManager mLoaderManager;

    private List<User> mCurrentUsers;

    private boolean mFirstLoad;

    public UsersPresenter(@NonNull UsersLoader loader, @NonNull LoaderManager loaderManager,
                          @NonNull UsersRepository usersRepository, @NonNull UsersContract.View usersView) {
        mLoader = checkNotNull(loader, "loader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loader manager cannot be null");
        mUsersRepository = checkNotNull(usersRepository, "UsersRepository cannot be null");
        mUsersView = checkNotNull(usersView, "usersView cannot be null!");

        mUsersView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(USERS_QUERY, null, this);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        mUsersView.setLoadingIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        mUsersView.setLoadingIndicator(false);

        mCurrentUsers = data;
        if (mCurrentUsers == null) {
            mUsersView.showLoadingUsersError();
        } else {
            processUsers(mCurrentUsers);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> loader) {
        // no-op
    }

    /**
     * @param forceUpdate Pass in true to refresh the data in the {@link UsersDataSource}
     */
    public void loadUsers(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mUsersRepository.refreshUsers();
        } else {
            processUsers(mCurrentUsers);
        }
    }

    public void processUsers(List<User> users) {
        if (users.isEmpty()) {
            // Show a message indicating there are no users for that filter type.
            processEmptyUsers();
        } else {
            // Show the list of users
            mUsersView.showUsers(users);
        }
    }

    private void processEmptyUsers() {
        mUsersView.showNoUsers();
    }

    @Override
    public void openUserDetails(@NonNull User requestedUser) {
        checkNotNull(requestedUser, "requestedUser cannot be null!");
        mUsersView.showUserDetailsUi(requestedUser.getUser_id());
    }

    @Override
    public void followUser(@NonNull User user, int position) {
        checkNotNull(user, "user cannot be null!");
        mUsersRepository.followUser(user);
        mUsersView.showUserFollowed(position);
        //loadUsers(false);
    }

    @Override
    public void unfollowUser(@NonNull User user, int position) {
        checkNotNull(user, "user cannot be null!");
        mUsersRepository.unfollowUser(user);
        mUsersView.showUserUnfollowed(position);
    }

    @Override
    public void blockUser(@NonNull User user, int position) {
        checkNotNull(user, "user cannot be null!");
        mUsersRepository.blockUser(user);
        mUsersView.showUserBlocked(position);
    }

    @Override
    public void unblockUser(@NonNull User user, int position) {
        checkNotNull(user, "user cannot be null!");
        mUsersRepository.unblockUser(user);
        mUsersView.showUserUnBlocked(position);
    }
}
