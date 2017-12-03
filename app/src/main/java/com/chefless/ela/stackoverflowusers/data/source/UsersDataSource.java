package com.chefless.ela.stackoverflowusers.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import com.chefless.ela.stackoverflowusers.data.models.User;

/**
 * Main entry point for accessing users com.chefless.ela.stackoverflowusers.data.
 */
public interface UsersDataSource {

    interface GetUserCallback {

        void onUserLoaded(User user);

        void onDataNotAvailable();
    }

    @Nullable
    List<User> getUsers();

    @Nullable
    User getUser(@NonNull int userId);

    void refreshUsers();

    void followUser(@NonNull User user);

    void unfollowUser(@NonNull User user);

    void blockUser(@NonNull User user);

    void unblockUser(@NonNull User user);
}
