package simonov.pk.jokesdisplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by petr on 17-Nov-15.
 */
public class JokeActivity extends ActionBarActivity {
    private static final String JOKE_KEY = "joke_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(JOKE_KEY)!= null){
            TextView textView = (TextView)findViewById(R.id.text);
            textView.setText(intent.getStringExtra(JOKE_KEY));

        }


    }
}
