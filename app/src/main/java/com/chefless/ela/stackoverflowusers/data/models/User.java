package com.chefless.ela.stackoverflowusers.data.models;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ela on 12/11/2017.
 */

public class User implements Serializable {

    private int user_id;
    private String profile_image;
    private String display_name;
    private long reputation;
    private String location;
    private long creation_date;

    private boolean followed = false;
    private boolean blocked = false;

    public User(int id, String name, long reputation, String location) {
        this.user_id = id;
        this.display_name = name;
        this.reputation = reputation;
        this.location = location;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getReputation() {
        return reputation;
    }

    public String getReputationAsString(){
        return NumberFormat.getNumberInstance(Locale.US).format(reputation);
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(long creation_date) {
        this.creation_date = creation_date;
    }

    public String getTimeSinceCreation(){
        long now = Calendar.getInstance().getTimeInMillis();
        //long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(creation_date*1000, now, DateUtils.YEAR_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE).toString();
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
