package com.chefless.ela.stackoverflowusers.userdetail;

import android.support.annotation.NonNull;
import com.chefless.ela.stackoverflowusers.BasePresenter;
import com.chefless.ela.stackoverflowusers.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface UserDetailContract {

    interface View extends BaseView<BasePresenter> {

        void setLoadingIndicator(boolean active);

        void showDetails();

        void showMissingUser();

        void setModel(@NonNull Object model);
    }
}
