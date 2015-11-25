package com.logic.util;

import org.json.JSONException;

/**
 * Created by zoson on 3/21/15.
 */
public interface HttpListener {
    public void succToRequire(String msg,String data);
    public void failToRequire(String msg,String data);
    public void netWorkError(String msg,String data);
}
