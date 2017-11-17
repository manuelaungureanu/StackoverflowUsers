package com.chefless.ela.stackoverflowusers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chefless.ela.stackoverflowusers.data.source.UsersDataSource;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;
import com.chefless.ela.stackoverflowusers.data.source.remote.UsersRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link UsersDataSource} at compile time.
 */
public class Injection {

    public static UsersRepository provideUsersRepository(@NonNull Context context) {
        checkNotNull(context);
        return UsersRepository.getInstance(UsersRemoteDataSource.getInstance());
    }
}
