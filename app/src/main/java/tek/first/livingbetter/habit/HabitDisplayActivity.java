package tek.first.livingbetter.habit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.support.v7.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.jsonparsing.Yelp;
import tek.first.livingbetter.habit.model.InfoCollectedModel;
import tek.first.livingbetter.habit.search.HabitSearchActivity;
import tek.first.livingbetter.todolist.helper.GeneralConstants;
import tek.first.livingbetter.todolist.helper.GeneralHelper;

public class HabitDisplayActivity extends AppCompatActivity {

    private static final String LOG_TAG = HabitDisplayActivity.class.getSimpleName();

    public static final String CUSTOM_INTENT_FILTER = "tek.first.livingbetter.HabitDisplayActivity.CustomIntentFilter";

    private GridView gridViewFood, gridViewEntertainment, gridViewShopping;
    private ArrayList<InfoCollectedModel> foodArrayList = new ArrayList<>();
    private ArrayList<InfoCollectedModel> entertainmentArrayList = new ArrayList<>();
    private ArrayList<InfoCollectedModel> shoppingArrayList = new ArrayList<>();
    //    private double[] currentAddress = new double[2];
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude;
    private double longitude;
    private boolean refersh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_display_activity);

        gridViewFood = (GridView) findViewById(R.id.gridview_food_result);
        gridViewEntertainment = (GridView) findViewById(R.id.gridview_entertainment_result);
        gridViewShopping = (GridView) findViewById(R.id.gridview_shopping_result);

        final TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabWidget tabWidget = tabHost.getTabWidget();

        tabHost.addTab(tabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("Food", null));
        tabHost.addTab(tabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("Entertainment", null));
        tabHost.addTab(tabHost.newTabSpec("tab3").setContent(R.id.tab3).setIndicator("Shopping", null));

        TextView tv1 = (TextView) tabWidget.getChildAt(0).findViewById(android.R.id.title);
        tv1.setTextSize(10);
        TextView tv2 = (TextView) tabWidget.getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextSize(10);
        TextView tv3 = (TextView) tabWidget.getChildAt(2).findViewById(android.R.id.title);
        tv3.setTextSize(10);
        tabHost.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View v) {
            }

            @Override
            public void onViewAttachedToWindow(View v) {
                tabHost.getViewTreeObserver().removeOnTouchModeChangeListener(tabHost);
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                latitude = location.getLatitude();
                Log.v(LOG_TAG, "latitude: " + latitude);
                preferenceEditor.putLong(getString(R.string.preference_latitude), Double.doubleToRawLongBits(latitude));
//                Log.v(LOG_TAG, "latitude, from Preference: " + Double.longBitsToDouble(sharedPreferences.getLong(getResources().getString(R.string.preference_latitude), 0)));
                Log.v(LOG_TAG, "latitude, from Preference: " + Double.longBitsToDouble(sharedPreferences.getLong(getResources().getString(R.string.preference_latitude), 0)));
                longitude = location.getLongitude();
                Log.v(LOG_TAG, "longitude: " + longitude);
//                preferenceEditor.putLong(getResources().getString(R.string.preference_longitude), Double.doubleToRawLongBits(longitude));
                preferenceEditor.putLong(getString(R.string.preference_longitude), Double.doubleToLongBits(longitude));
                preferenceEditor.commit();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        initInfo();
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.habit_display_activity, menu);

        // Associate habit_searchable configuration with the SearchView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search_habit_display_activity).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.v(LOG_TAG, "onNewIntent(Intent intent) executed.");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.v(LOG_TAG, "handleIntent(Intent intent) executed.");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String keyword = intent.getStringExtra(SearchManager.QUERY);
            Log.v(LOG_TAG, "keyword, handleIntent(Intent intent): " + keyword);
            Intent searchActivityIntent = new Intent(HabitDisplayActivity.this, HabitSearchActivity.class);
            searchActivityIntent.putExtra(GeneralConstants.IDENTIFIER_KEYWORD, keyword);
            searchActivityIntent.setAction(CUSTOM_INTENT_FILTER);
            startActivity(searchActivityIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initInfo();
                return true;
            case R.id.action_search_habit_display_activity:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    private void initGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            refersh = true;
            AlertDialog.Builder dialog = new AlertDialog.Builder(HabitDisplayActivity.this);
            dialog.setMessage("Please open GPS to locate your position");
            dialog.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0);
                }
            });
            dialog.setNeutralButton("cancel", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            updateLocation();
            refersh = false;
        }
    }

    private void updateLocation() {
        try {
            if (ContextCompat.checkSelfPermission(HabitDisplayActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(HabitDisplayActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                longitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
/*                SharedPreferences sharedPreferences = HabitDisplayActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                preferenceEditor.putLong(getResources().getString(R.string.preference_latitude), Double.doubleToRawLongBits(latitude));
                preferenceEditor.putLong(getResources().getString(R.string.preference_longitude), Double.doubleToRawLongBits(longitude));*/
            } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                longitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
/*                SharedPreferences sharedPreferences = HabitDisplayActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                preferenceEditor.putLong(getResources().getString(R.string.preference_latitude), Double.doubleToRawLongBits(latitude));
                preferenceEditor.putLong(getResources().getString(R.string.preference_longitude), Double.doubleToRawLongBits(longitude));*/
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initInfo() {
        Log.v(LOG_TAG, "initInfo() executed.");
        if (!GeneralHelper.isNetworkAvailable(HabitDisplayActivity.this)) {
            Crouton.makeText(HabitDisplayActivity.this,
                    getResources().getString(R.string.network_not_available),
                    Style.ALERT).show();
        } else {
            initGPS();
            updateLocation();
            initData();
        }
    }

    public void initData() {
        Log.v(LOG_TAG, "initData() executed.");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Yelp yelp = Yelp.getYelp(HabitDisplayActivity.this);
                try {
                    SharedPreferences sharedPreferences = HabitDisplayActivity.this.getPreferences(Context.MODE_PRIVATE);
                    Log.v(LOG_TAG, sharedPreferences.getString("shop", "shopping"));

                    String foodJson = yelp.search(sharedPreferences.getString("food", "food"), latitude, longitude, Yelp.YELP_SEARCH_LIMIT);
                    String entertainmentJson = yelp.search(sharedPreferences.getString("entertainment", "entertainment"), latitude, longitude, Yelp.YELP_SEARCH_LIMIT);
                    String shoppingJson = yelp.search(sharedPreferences.getString("shop", "shopping"), latitude, longitude, Yelp.YELP_SEARCH_LIMIT);

                    Log.v(LOG_TAG, "json res:" + shoppingJson);
                    // todo: 1. Get familiar with Yelp Web API; 2. More interactions with "category" (such as: preferences for input from users); 3. how to display info on
                    foodArrayList = yelp.processJson(HabitDisplayActivity.this, foodJson);
                    entertainmentArrayList = yelp.processJson(HabitDisplayActivity.this, entertainmentJson);
                    shoppingArrayList = yelp.processJson(HabitDisplayActivity.this, shoppingJson);

//                        Log.v(LOG_TAG, "res size: " + String.valueOf(shoppingArrayList.size()));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                gridViewFood.setAdapter(new CustomGridViewAdapter(HabitDisplayActivity.this, foodArrayList));
                gridViewEntertainment.setAdapter(new CustomGridViewAdapter(HabitDisplayActivity.this, entertainmentArrayList));
                gridViewShopping.setAdapter(new CustomGridViewAdapter(HabitDisplayActivity.this, shoppingArrayList));
            }
        }.execute();
    }
}