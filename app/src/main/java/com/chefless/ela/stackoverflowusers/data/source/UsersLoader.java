
package com.chefless.ela.stackoverflowusers.data.source;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import java.util.List;

import com.chefless.ela.stackoverflowusers.data.models.User;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Custom {@link android.content.Loader} for a list of {@link com.chefless.ela.stackoverflowusers..models.User}, using the
 * {@link UsersRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the com.chefless.ela.stackoverflowusers.data asynchronously.
 */
public class UsersLoader extends AsyncTaskLoader<List<User>>
        implements UsersRepository.UsersRepositoryObserver{

    private UsersRepository mRepository;

    public UsersLoader(Context context, @NonNull UsersRepository repository) {
        super(context);
        checkNotNull(repository);
        mRepository = repository;
    }

    @Override
    public List<User> loadInBackground() {
        return mRepository.getUsers();
    }

    @Override
    public void deliverResult(List<User> data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // Deliver any previously loaded com.chefless.ela.stackoverflowusers.data immediately if available.
        if (mRepository.cachedUsersAvailable()) {
            deliverResult(mRepository.getCachedUsers());
        }

        // Begin monitoring the underlying com.chefless.ela.stackoverflowusers.data source.
        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedUsersAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
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
