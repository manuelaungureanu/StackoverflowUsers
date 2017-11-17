package com.chefless.ela.stackoverflowusers.userdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.chefless.ela.stackoverflowusers.Injection;
import com.chefless.ela.stackoverflowusers.R;
import com.chefless.ela.stackoverflowusers.data.source.UserLoader;
import com.chefless.ela.stackoverflowusers.utils.ActivityUtils;

public class UserDetailActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int userId = getIntent().getIntExtra(EXTRA_USER_ID, 0);
        UserDetailFragment userDetailFragment = (UserDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (userDetailFragment == null) {
            userDetailFragment = UserDetailFragment.newInstance(userId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    userDetailFragment, R.id.contentFrame);
        }

        // Create the presenter
        new UserDetailPresenter(
                userId,
                Injection.provideUsersRepository(getApplicationContext()),
                userDetailFragment,
                new UserLoader(userId, getApplicationContext()),
                getSupportLoaderManager());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
