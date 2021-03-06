package com.mobicom.pinballframework;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

import com.android.pinball.NetworkResponse;
import com.android.pinball.Request;
import com.android.pinball.Response;
import com.android.pinball.PinballError;
import com.android.pinball.toolbox.HttpHeaderParser;

public class LObjectRequest extends Request<byte[]> implements Parcelable {

    final String TAG = "LObjectRequest";

    String url = null;


    Response.Listener<byte[]> sListener = null;

    Response.ProgressListener pListener = null;


    /**
     * Creates a new request with the given method (one of the values from {@link Method}),
     * URL, and error listener.  Note that the normal response listener is not provided here as
     * delivery of responses is provided by subclasses, who have a better idea of how to deliver
     * an already-parsed response.
     *
     * @param url
     * @param listener
     */
    public LObjectRequest(String url, Response.Listener<byte[]> listener) {
        super(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(PinballError error) {
                Log.e("LObjectRequest", error.getLocalizedMessage());
            }
        });
        sListener = listener;
    }


    /**
     * 构造函数
     *
     * @param url
     * @param delay
     * @param tag
     */
    public LObjectRequest(String url, int delay, String tag) {
        super(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(PinballError error) {
                Log.e("LStringRequest", error.getMessage());
            }
        });
        this.delay = delay;
        this.url = url;
        this.tag = tag;
        setArrTime(SystemClock.elapsedRealtime());
        setEndTime(getArrTime() + delay*1000);
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
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //一般来说，在这个方法里面，会从response里获取data，然后做相应的处理，最后再创建一个T类型的Response，这个Response里面的内容最终会被回调给Developer
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
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
    protected void deliverResponse(byte[] response) {
        sListener.onResponse(response);
    }


    public Response.ProgressListener getProgressListener() {
        return pListener;
    }

    public void setProgressListener(Response.ProgressListener pListener) {
        this.pListener = pListener;
    }

    public Response.Listener<byte[]> getListener() {
        return sListener;
    }

    public void setListener(Response.Listener<byte[]> sListener) {
        this.sListener = sListener;
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
