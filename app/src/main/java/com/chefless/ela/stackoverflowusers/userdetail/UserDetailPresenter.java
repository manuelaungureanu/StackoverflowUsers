package com.chefless.ela.stackoverflowusers.userdetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chefless.ela.stackoverflowusers.BasePresenter;
import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.data.source.UserLoader;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link UserDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class UserDetailPresenter implements BasePresenter,
        LoaderManager.LoaderCallbacks<User> {

    private static final int USER_QUERY = 3;

    private UsersRepository mUsersRepository;

    private UserDetailContract.View mDetailView;

    private UserLoader mUserLoader;

    private LoaderManager mLoaderManager;

    @Nullable
    private int mUserId;

    public UserDetailPresenter(@Nullable int userId,
                               @NonNull UsersRepository usersRepository,
                               @NonNull UserDetailContract.View userDetailView,
                               @NonNull UserLoader userLoader,
                               @NonNull LoaderManager loaderManager) {
        mUserId = userId;
        mUsersRepository = checkNotNull(usersRepository, "usersRepository cannot be null!");
        mDetailView = checkNotNull(userDetailView, "usersDetailView cannot be null!");
        mUserLoader = checkNotNull(userLoader, "usersLoader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loaderManager cannot be null!");

        mDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(USER_QUERY, null, this);
    }


    private void showUser(@NonNull User user) {
        mDetailView.setModel(user);
        mDetailView.showDetails();
        mDetailView.setLoadingIndicator(false);
    }

    @Override
    public Loader<User> onCreateLoader(int id, Bundle args) {
        if (mUserId == 0) {
            return null;
        }
        mDetailView.setLoadingIndicator(true);
        return mUserLoader;
    }

    @Override
    public void onLoadFinished(Loader<User> loader, User data) {
        if (data != null) {
            showUser(data);
        } else {
            mDetailView.showMissingUser();
        }
    }

    @Override
    public void onLoaderReset(Loader<User> loader) {
        // no-op
    }

}
