package com.chefless.ela.stackoverflowusers.users;

import android.support.annotation.NonNull;

import com.chefless.ela.stackoverflowusers.BasePresenter;
import com.chefless.ela.stackoverflowusers.BaseView;
import com.chefless.ela.stackoverflowusers.data.models.User;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UsersContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showUsers(List<User> users);

        void showUserDetailsUi(int userId);

        void showLoadingUsersError();

        void showNoUsers();

        void showUserFollowed(int position);

        void showUserUnfollowed(int position);

        void showUserBlocked(int position);

        void showUserUnBlocked(int position);
    }

    interface Presenter extends BasePresenter {

        void loadUsers(boolean forceUpdate);

        void openUserDetails(@NonNull User requestedUser);

        void followUser(@NonNull User user, int position);

        void unfollowUser(@NonNull User user, int position);

        void blockUser(@NonNull User user, int position);

        void unblockUser(@NonNull User user, int position);
    }
}
