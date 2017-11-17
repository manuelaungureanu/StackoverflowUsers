package com.chefless.ela.stackoverflowusers.data.source.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chefless.ela.stackoverflowusers.data.models.User;
import com.chefless.ela.stackoverflowusers.data.source.UsersDataSource;
import com.chefless.ela.stackoverflowusers.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class UsersRemoteDataSource implements UsersDataSource {

    private static UsersRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private final static Map<String, User> USERS_SERVICE_DATA;

    static {
        USERS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private UsersRemoteDataSource() {}

    @Override
    public List<User> getUsers() {

        try {
            URL uUsersUrl = new URL(NetworkUtils.STATIC_USERS_BASE_URL + NetworkUtils.USERS_LIMIT_PARAM + NetworkUtils.STATIC_USERS_OTHER_PARAMS);
            String response = NetworkUtils.getResponseFromHttpUrl(uUsersUrl);
            JSONObject jsonRootObject = new JSONObject(response);
            String uUsersJsonAsString = jsonRootObject.getString(NetworkUtils.STATIC_USERS_RESPONSE_RESULTS_NODE);

            User[] arrayResult =  NetworkUtils.parseJSONToUsers(uUsersJsonAsString);

            if(arrayResult==null || arrayResult.length==0)
                return new ArrayList<>();

            return new ArrayList<>(Arrays.asList(arrayResult));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public User getUser(@NonNull int uUserId) {
        if(uUserId==0)
            return null;
        try {
            String url = String.format("%s/%s/%s", NetworkUtils.STATIC_USERS_BASE_URL, uUserId, NetworkUtils.STATIC_USERS_OTHER_PARAMS);
            URL uUserUrl = new URL(url);
            String response = NetworkUtils.getResponseFromHttpUrl(uUserUrl);
            JSONObject jsonRootObject = new JSONObject(response);
            String uUsersJsonAsString = jsonRootObject.getString(NetworkUtils.STATIC_USERS_RESPONSE_RESULTS_NODE);

            User[] arrayResult =  NetworkUtils.parseJSONToUsers(uUsersJsonAsString);

            if(arrayResult==null || arrayResult.length==0)
                return null;

            return arrayResult[0];

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void refreshUsers() {
        // Not required because the {@link UsersRepository} handles the logic of refreshing the
        // Users from all the available data sources.
    }
}
