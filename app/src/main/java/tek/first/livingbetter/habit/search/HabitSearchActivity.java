package tek.first.livingbetter.habit.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.CustomGridViewAdapter;
import tek.first.livingbetter.habit.jsonparsing.Yelp;
import tek.first.livingbetter.habit.model.InfoCollectedModel;
import tek.first.livingbetter.todolist.helper.GeneralConstants;
import tek.first.livingbetter.todolist.helper.GeneralHelper;

public class HabitSearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = HabitSearchActivity.class.getSimpleName();

    private ArrayList<InfoCollectedModel> infoCollectedArrayList;

    private GridView gridView;
    private CustomGridViewAdapter customGridViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.habit_gridview_item_display);
        infoCollectedArrayList = new ArrayList<>();
        gridView = (GridView) findViewById(R.id.gridview_user_search_result);

/*        TextView emptyTextView = new TextView(HabitSearchActivity.this);
        emptyTextView.setText(getResources().getString(R.string.empty_text_view));
        gridView.setEmptyView(emptyTextView);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.v(LOG_TAG, "handleIntent(Intent intent) executed.");
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                final String term = intent.getStringExtra(GeneralConstants.IDENTIFIER_KEYWORD);
                final double[] currentAddress = intent.getDoubleArrayExtra(GeneralConstants.IDENTIFIER_CURRENT_ADDRESS);
                final double latitude = currentAddress[0];
                final double longitude = currentAddress[1];
                Log.v(LOG_TAG, "term: " + term);
                if (GeneralHelper.isNetworkAvailable(HabitSearchActivity.this)) {
                    Log.v(LOG_TAG, "latitude: " + latitude + "; longitude: " + longitude);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Yelp yelp = Yelp.getYelp(HabitSearchActivity.this);
                            String jsonString = yelp.search(term, latitude, longitude, Yelp.YELP_SEARCH_LIMIT);
                            Log.v(LOG_TAG, "jsonString: " + jsonString.toString());
                            try {
                                infoCollectedArrayList = yelp.processJson(HabitSearchActivity.this, jsonString);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            customGridViewAdapter = new CustomGridViewAdapter(HabitSearchActivity.this, infoCollectedArrayList);
                            gridView.setAdapter(customGridViewAdapter);
                        }
                    }.execute();

                } else {
                    Crouton.makeText(HabitSearchActivity.this,
                            getResources().getString(R.string.network_not_available), Style.ALERT).show();
                }
            }
        } else {
            Log.e(LOG_TAG, "intent passed is null.");
        }
    }
}
