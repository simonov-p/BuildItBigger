package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.example.petr.myapplication.backend.myApi.MyApi;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.R;

import java.io.IOException;

import simonov.pk.jokesdisplay.JokeActivity;

/**
 * Created by petr on 17-Nov-15.
 */
public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static final String JOKE_KEY = "joke_key";

    private static MyApi myApiService = null;
    private Context context;

    private InterstitialAd mInterstitialAd;
    private String mJokeText;

    public EndpointsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mInterstitialAd = new InterstitialAd(context);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID))
                .build();

        mInterstitialAd.setAdUnitId(context.getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                jokeActivityDisplay();
            }
        });
    }

    private void jokeActivityDisplay() {
        // Create the text message with a string
        Intent sendIntent = new Intent(context, JokeActivity.class);
        sendIntent.putExtra(JOKE_KEY, mJokeText);
        context.startActivity(sendIntent);
    }

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;


        try {
            Log.e("mytag", myApiService.pullJoke().execute().getJoke());
            return myApiService.pullJoke().execute().getJoke();
//            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mJokeText = result;
        if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
        } else {
            jokeActivityDisplay();
        }
    }
}