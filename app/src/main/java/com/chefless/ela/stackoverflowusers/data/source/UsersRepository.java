package com.chefless.ela.stackoverflowusers.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chefless.ela.stackoverflowusers.data.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Concrete implementation to load users from the com.chefless.ela.stackoverflowusers.data sources into a cache.
 * <p>
 * For simplicity, this implements a dumb synchronisation between locally persisted com.chefless.ela.stackoverflowusers.data and com.chefless.ela.stackoverflowusers.data
 * obtained from the server, by using the remote com.chefless.ela.stackoverflowusers.data source only if the local database doesn't
 * exist or is empty.
 */
public class UsersRepository implements UsersDataSource {

    private static UsersRepository INSTANCE = null;

    private final UsersDataSource mUsersRemoteDataSource;

    private List<UsersRepositoryObserver> mObservers = new ArrayList<UsersRepositoryObserver>();

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    private Map<String, User> mCachedUsers;

    /**
     * Marks the cache as invalid, to force an update the next time com.chefless.ela.stackoverflowusers.data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty;

    public static UsersRepository getInstance(UsersDataSource usersRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(usersRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Prevent direct instantiation.
    private UsersRepository(@NonNull UsersDataSource usersRemoteDataSource) {
        mUsersRemoteDataSource = checkNotNull(usersRemoteDataSource);
    }

    public void addContentObserver(UsersRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(UsersRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (UsersRepositoryObserver observer : mObservers) {
            observer.onUsersChanged();
        }
    }

    /**
     *
     * Gets Users from cache, local com.chefless.ela.stackoverflowusers.data source (SQLite) or remote com.chefless.ela.stackoverflowusers.data source, whichever is
     * available first. This is done synchronously because it's used by the {@link UsersLoader},
     * which implements the async mechanism.
     */
    @Nullable
    @Override
    public List<User> getUsers() {
        List<User> users;

        if (!mCacheIsDirty) {
            // Respond immediately with cache if available and not dirty
            if (mCachedUsers != null) {
                users = getCachedUsers();
                return users;
            }
        }

        // Grab remote com.chefless.ela.stackoverflowusers.data if cache is dirty or local com.chefless.ela.stackoverflowusers.data not available.
        users = mUsersRemoteDataSource.getUsers();

        processLoadedUsers(users);
        return getCachedUsers();
    }

    public boolean cachedUsersAvailable() {
        return mCachedUsers != null && !mCacheIsDirty;
    }

    public List<User> getCachedUsers() {
        return mCachedUsers == null ? null : new ArrayList<>(mCachedUsers.values());
    }

    public User getCachedUser(int userId) {
        return mCachedUsers.get(String.valueOf(userId));
    }

    private void processLoadedUsers(List<User> users) {
        if (users == null) {
            mCachedUsers = null;
            mCacheIsDirty = false;
            return;
        }
        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }
        mCachedUsers.clear();
        for (User user : users) {
            mCachedUsers.put(String.valueOf(user.getUser_id()), user);
        }
        mCacheIsDirty = false;
    }

    @Override
    public User getUser(@NonNull final int userId) {
        checkNotNull(userId);

        User cachedUser = getUserWithId(userId);

        // Respond immediately with cache if we have one
        if (cachedUser != null) {
            return cachedUser;
        }

        // Query the network.
        return mUsersRemoteDataSource.getUser(userId);
    }

    @Nullable
    private User getUserWithId(@NonNull int id) {
        checkNotNull(id);
        if (mCachedUsers == null || mCachedUsers.isEmpty()) {
            return null;
        } else {
            return mCachedUsers.get(id);
        }
    }

    @Override
    public void refreshUsers() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    @Override
    public void followUser(@NonNull User user) {

        checkNotNull(user);
        mUsersRemoteDataSource.followUser(user);

        user.setFollowed(true);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }
        mCachedUsers.put(String.valueOf(user.getUser_id()), user);
        //notifyContentObserver();
    }

    @Override
    public void unfollowUser(@NonNull User user) {

        checkNotNull(user);
        mUsersRemoteDataSource.unfollowUser(user);

        user.setFollowed(false);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }
        mCachedUsers.put(String.valueOf(user.getUser_id()), user);
        //notifyContentObserver();
    }

    @Override
    public void blockUser(@NonNull User user) {
        checkNotNull(user);
        mUsersRemoteDataSource.blockUser(user);

        user.setBlocked(true);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }
        mCachedUsers.put(String.valueOf(user.getUser_id()), user);
        //notifyContentObserver();
    }

    @Override
    public void unblockUser(@NonNull User user) {
        checkNotNull(user);
        mUsersRemoteDataSource.unblockUser(user);

        user.setBlocked(false);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedUsers == null) {
            mCachedUsers = new LinkedHashMap<>();
        }
        mCachedUsers.put(String.valueOf(user.getUser_id()), user);
        //notifyContentObserver();
    }

    public interface UsersRepositoryObserver {

        void onUsersChanged();

    }
}
