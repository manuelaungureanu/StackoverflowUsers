package com.chefless.ela.stackoverflowusers;

import android.content.Context;

import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.data.source.UsersDataSource;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;
import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class UsersRepositoryTest {
    
    private static List<User> UserS = Lists.newArrayList(
            new User(123, "Spiderman", 143241, "United Kingdom, London"),
            new User(345, "Superman", 546780, "France, Paris"),
            new User(678, "Aquaman", 799435, "US, NY")
    );

    private UsersRepository mRepository;

    @Mock
    private UsersDataSource mRemoteDataSource;

    @Mock
    private Context mContext;

    @Mock
    private UsersDataSource.GetUserCallback mGetCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<UsersDataSource.GetUserCallback> mCallbackCaptor;

    @Before
    public void setupUsersRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mRepository = UsersRepository.getInstance(mRemoteDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        UsersRepository.destroyInstance();
    }

    @Test
    public void getUsers_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the Users repository
        twoUsersLoadCallsToRepository();

        // Then Users were only requested once from Service API
        verify(mRemoteDataSource).getUsers();
    }

    @Test
    public void getUsersWithDataSourceUnavailable_returnsNull() {
        /// Given unavailable Users in both data sources
        setUsersNotAvailable(mRemoteDataSource);

        // When calling getUsers in the repository
        List<User> returnedUsers = mRepository.getUsers();

        // Then the returned Users are null
        assertNull(returnedUsers);
    }

    @Test
    public void getUserWithDataSourceUnavailable_firesOnDataUnavailable() {
        // Given a User id
        final int UserId = 123;

        // And the remote data source has no data available
        setUserNotAvailable(mRemoteDataSource, UserId);

        // When calling getUser in the repository
        User User = mRepository.getUser(UserId);

        // Verify no data is returned
        assertThat(User, is(nullValue()));
    }

    @Test
    public void getUsers_refreshesLocalDataSource() {
        // Mark cache as dirty to force a reload of data from remote data source.
        mRepository.refreshUsers();

        // Make the remote data source return data
        setUsersAvailable(mRemoteDataSource, UserS);

        // When calling getUsers in the repository
        mRepository.getUsers();
    }

    /**
    * Convenience method that issues two calls to the Users repository
    */
    private void twoUsersLoadCallsToRepository() {

        // and a remote data source with no data
        when(mRemoteDataSource.getUsers()).thenReturn(UserS);

        // When Users are requested from repository
        mRepository.getUsers(); // First call to API

        // Then the remote data source is called
        verify(mRemoteDataSource).getUsers();

        mRepository.getUsers(); // Second call to API
    }

    private void setUsersNotAvailable(UsersDataSource dataSource) {
        when(dataSource.getUsers()).thenReturn(null);
    }

    private void setUsersAvailable(UsersDataSource dataSource, List<User> Users) {
        when(dataSource.getUsers()).thenReturn(Users);
    }

    private void setUserNotAvailable(UsersDataSource dataSource, int UserId) {
        when(dataSource.getUser(UserId)).thenReturn(null);
    }

    private void setUserAvailable(UsersDataSource dataSource, User User) {
        when(dataSource.getUser(User.getUser_id())).thenReturn(User);
    }


}
