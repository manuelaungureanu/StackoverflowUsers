package com.chefless.ela.stackoverflowusers.utils;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.chefless.ela.stackoverflowusers.data.models.User;

/**
 * Created by ela on 26/09/2017.
 */

public class NetworkUtils {
    public static final String STATIC_USERS_BASE_URL = "http://api.stackexchange.com/2.2/users";

    public static final String USERS_LIMIT_PARAM = "?pagesize=20";
    public static final String STATIC_USERS_OTHER_PARAMS = "&order=desc&sort=reputation&site=stackoverflow";

    public static final String STATIC_USER_OTHER_PARAMS = "?site=stackoverflow";

    public static final String STATIC_USERS_RESPONSE_RESULTS_NODE = "items";
    public static final String JSON_EXTENSION = ".json";

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static User[] parseJSONToUsers(String response) {
        if(response==null || TextUtils.isEmpty(response))
            return null;

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, User[].class);
    }

    public static User parseJSONToUser(String response) {
        if(response==null || TextUtils.isEmpty(response))
            return null;

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, User.class);
    }
}
