package com.mny.share.testrx.model;

import android.util.Log;

/**
 * Crate by E470PD on 2018/8/16
 */
public class Translation {
    public int status;
    public content content;
    public class content{
        public String from;
        public String to;
        public String vendor;
        public String out;
        public int errNo;
    }
    public String show(){
        return content.out;
    }
}
