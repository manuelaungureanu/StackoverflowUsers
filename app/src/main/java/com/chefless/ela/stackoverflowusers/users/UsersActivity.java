package com.chefless.ela.stackoverflowusers.users;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.chefless.ela.stackoverflowusers.Injection;
import com.chefless.ela.stackoverflowusers.R;
import com.chefless.ela.stackoverflowusers.data.source.UsersLoader;
import com.chefless.ela.stackoverflowusers.data.source.UsersRepository;
import com.chefless.ela.stackoverflowusers.databinding.ActivityUsersBinding;
import com.chefless.ela.stackoverflowusers.utils.ActivityUtils;

public class UsersActivity extends AppCompatActivity {

    private UsersPresenter mUsersPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUsersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_users);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        UsersFragment usersFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (usersFragment == null) {
            // Create the fragment
            usersFragment = UsersFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), usersFragment, R.id.contentFrame);
        }


        // Create the presenter
        UsersRepository repository = Injection.provideUsersRepository(getApplicationContext());
        UsersLoader usersLoader = new UsersLoader(getApplicationContext(), repository);

        mUsersPresenter = new UsersPresenter(
                usersLoader,
                getSupportLoaderManager(),
                repository,
                usersFragment
        );
    }
}
