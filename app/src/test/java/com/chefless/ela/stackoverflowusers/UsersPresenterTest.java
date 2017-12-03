package com.chefless.ela.stackoverflowusers;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chefless.ela.stackoverflowusers.users.UsersContract;
import com.chefless.ela.stackoverflowusers.users.UsersPresenter;
import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.data.source.UsersLoader;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link UsersPresenter}
 */
public class UsersPresenterTest {

    private static List<User> UserS = Lists.newArrayList(
            new User(123, "Spiderman", 143241, "United Kingdom, London"),
            new User(345, "Superman", 546780, "France, Paris"),
            new User(678, "Aquaman", 799435, "US, NY")
    );

    private static List<User> EMPTY_UserS = new ArrayList<>(0);

    @Mock
    private UsersRepository mRepository;

    @Mock
    private UsersContract.View mView;

    @Captor
    private ArgumentCaptor<List> mShowUsersArgumentCaptor;

    @Mock
    private UsersLoader mLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private UsersPresenter mPresenter;
    private List<UsersRepository.UsersRepositoryObserver> mObservers = new ArrayList<>();

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        // Get a reference to the class under test
        mPresenter = new UsersPresenter(mLoader, mLoaderManager, mRepository, mView);
    }

    @Test
    public void loadAllUsersFromRepositoryAndLoadIntoView() {
        // When the loader finishes with tasks and filter is set to all
        mPresenter.onLoadFinished(mock(Loader.class), UserS);

        // Then progress indicator is hidden and all Users are shown in UI
        verify(mView).setLoadingIndicator(false);
        verify(mView).showUsers(mShowUsersArgumentCaptor.capture());
        assertThat(mShowUsersArgumentCaptor.getValue().size(), is(3));
    }

    @Test
    public void clickOnUser_ShowsDetailUi() {
        // Given a stubbed User
        User requestedUser = new User(123, "Spiderman", 143241, "United Kingdom, London");

        // When opening details is requested
        mPresenter.openUserDetails(requestedUser);

        // Then User detail UI is shown
        verify(mView).showUserDetailsUi(any(int.class));
    }

    @Test
    public void unavailableUsers_ShowsError() {
        // When the loader finishes with error
        mPresenter.onLoadFinished(mock(Loader.class), null);

        // Then an error message is shown
        verify(mView).showLoadingUsersError();
    }

    @Test
    public void clickOnFollowUser(){
        User userToFollow = new User(678, "Aquaman", 799435, "US, NY");

        // When the loader finishes with tasks and filter is set to all
        mPresenter.onLoadFinished(mock(Loader.class), UserS);
        mPresenter.followUser(userToFollow, 2);
        verify(mView).showUserFollowed(2);
    }

    @Test
    public void clickOnBlockUser(){
        User userToBlock = new User(345, "Superman", 546780, "France, Paris");

        // When the loader finishes with tasks and filter is set to all
        mPresenter.onLoadFinished(mock(Loader.class), UserS);
        mPresenter.blockUser(userToBlock, 1);
        verify(mView).showUserBlocked(1);
    }
}
