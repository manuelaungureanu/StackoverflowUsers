package com.chefless.ela.stackoverflowusers.data.source;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chefless.ela.stackoverflowusers.Injection;
import com.chefless.ela.stackoverflowusers.data.models.User;

/**
 * Custom {@link android.content.Loader} for a {@link com.chefless.ela.stackoverflowusers.data.models.User}, using the
 * {@link UsersRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class UserLoader extends AsyncTaskLoader<User>
        implements UsersRepository.UsersRepositoryObserver{

    private final int mUserId;
    private UsersRepository mRepository;

    public UserLoader(int userId, Context context) {
        super(context);
        this.mUserId = userId;
        mRepository = Injection.provideUsersRepository(context);
    }

    @Override
    public User loadInBackground() {
        return mRepository.getUser(mUserId);
    }

    @Override
    public void deliverResult(User data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }

    }

    @Override
    protected void onStartLoading() {

        // Deliver any previously loaded data immediately if available.
        if (mRepository.cachedUsersAvailable()) {
            deliverResult(mRepository.getCachedUser(mUserId));
        }

        // Begin monitoring the underlying data source.
        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedUsersAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onUsersChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
