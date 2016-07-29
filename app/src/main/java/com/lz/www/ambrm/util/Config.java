package com.lz.www.ambrm.util;

import android.net.Uri;

/**
 * Created by Administrator on 2016-05-31.
 */
public class Config {
    public static final String AMB_API="http://122.114.95.32/api";
    public static final String UserAPI=AMB_API + "/user";
    public static final String NewsAPI=AMB_API + "/news";
    public static final String PhotoAPI=AMB_API + "/photo";


    public static final Uri AMBLOG_URI= Uri.parse("content://com.lz.www.ambrm.ContentProvider.LogProvider/AmbLog");

}
