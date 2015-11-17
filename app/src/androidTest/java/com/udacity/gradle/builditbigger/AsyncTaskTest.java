package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AsyncTaskTest extends AndroidTestCase {

    private static final String LOG_TAG = "AsyncTaskTest";

    @SuppressWarnings("unchecked")
    public void runTest() {
        String result = null;
        EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(getContext());
        endpointsAsyncTask.execute();
        try {
            result = endpointsAsyncTask.get();
            Log.d(LOG_TAG, "Retrieved: " + result);
        } catch (Exception e) {
            Log.d(LOG_TAG, "Retrieved null string");
            e.printStackTrace();
        }
        assertNotNull(result);
    }
}