package com.mobicom.pinballframework;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

import com.android.pinball.NetworkResponse;
import com.android.pinball.Request;
import com.android.pinball.Response;
import com.android.pinball.Response.ErrorListener;
import com.android.pinball.Response.Listener;
import com.android.pinball.PinballError;
import com.android.pinball.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

public class LStringRequest extends Request<String> implements Parcelable {

    final String TAG = "LStringRequest";

    String url = null;


    Listener<String> sListener = null;


    Response.ProgressListener pListener = null;


    /**
     * 重载之后的构造函数，使用者只需申明需要获取的URL地址，以及获取成功之后的事情即可
     *
     * @param url
     * @param ml
     */
    public LStringRequest(String url, Listener<String> ml) {
        super(Request.Method.GET, url, new ErrorListener() {
            @Override
            public void onErrorResponse(PinballError error) {
                Log.e("LStringRequest", error.getMessage());
            }
        });
        sListener = ml;
        this.url = url;
    }

    /**
     * 另外，构造函数还可以申明是否需要使用进度条
     *
     * @param url
     * @param ml
     * @param pl
     */
    public LStringRequest(String url, Listener<String> ml, Response.ProgressListener pl) {
        this(url, ml);
        pListener = pl;
    }

    /**
     * 重新按照新的API接口定义构造函数
     */
    public LStringRequest(String url, int delay, String tag) {
        super(Request.Method.GET, url, new ErrorListener() {
            @Override
            public void onErrorResponse(PinballError error) {
                Log.e("LStringRequest", error.getMessage());
            }
        });
        this.delay = delay;
        this.url = url;
        this.tag = tag;
        setArrTime(SystemClock.elapsedRealtime());
        setEndTime(getArrTime() + delay * 1000);
    }

    /**
     * Subclasses must implement this to parse the raw network response
     * and return an appropriate response type. This method will be
     * called from a worker thread.  The response will not be delivered
     * if you return null.
     *
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * Subclasses must implement this to perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     *
     * @param response The parsed response returned by
     *                 {@link #parseNetworkResponse(NetworkResponse)}
     */
    @Override
    protected void deliverResponse(String response) {
        sListener.onResponse(response);
    }

    public Listener<String> getListener() {
        return sListener;
    }

    public void setListener(Listener<String> sListener) {
        this.sListener = sListener;
    }

    public Response.ProgressListener getProgressListener() {
        return pListener;
    }

    public void setProgressListener(Response.ProgressListener pListener) {
        this.pListener = pListener;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
