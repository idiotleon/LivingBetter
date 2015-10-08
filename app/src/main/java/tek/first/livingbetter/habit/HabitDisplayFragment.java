package tek.first.livingbetter.habit;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class HabitDisplayFragment extends Fragment {

    private static final String LOG_TAG = HabitDisplayFragment.class.getSimpleName();

    private GridView gridViewFood, gridViewEntertainment, gridViewShopping;
    private ArrayList<InfoCollectedModel> foodArrayList = new ArrayList<>();
    private ArrayList<InfoCollectedModel> entertainmentArrayList = new ArrayList<>();
    private ArrayList<InfoCollectedModel> shoppingArrayList = new ArrayList<>();
    private double[] currentAddress = new double[2];
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean refersh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreate(Bundle savedInstanceState) executed");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.habit_menu_display_fragment, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initInfo();
                break;
            case R.id.action_search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.habit_display_fragment, container, false);
        gridViewFood = (GridView) view.findViewById(R.id.gridView_food_result);
        gridViewEntertainment = (GridView) view.findViewById(R.id.gridview_entertainment_result);
        gridViewShopping = (GridView) view.findViewById(R.id.gridview_shopping_result);
        final TabHost tabHost = (TabHost) view.findViewById(R.id.tabHost);
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
                currentAddress[0] = location.getLatitude();
                currentAddress[1] = location.getLongitude();
                // initData();
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

        return view;
    }


    private void initGPS() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            refersh = true;
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("Please open GPS to locate your position");
            dialog.setPositiveButton("OK",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
            if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
/*                public void requestPermissions (@NonNull String[]permissions,int requestCode){

                }*/
                // here to request the missing permissions, and then overriding
/*                @Override
                public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                int[] grantResults){

                }*/
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
                    currentAddress[0] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    currentAddress[1] = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                    currentAddress[0] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    currentAddress[1] = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initInfo() {
        Log.v(LOG_TAG, "initInfo() executed.");
        if (!isOnline()) {
            Crouton.makeText(getActivity(), "Network Error", Style.ALERT).show();
        } else {
            initGPS();
            updateLocation();
            initData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initInfo();
    }

    public void initData() {
        Log.v(LOG_TAG, "initData() executed.");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (currentAddress[0] == 0) {
                    Log.e(LOG_TAG, "currentAddress[0] == 0");
                } else {
                    Yelp yelp = Yelp.getYelp(getActivity());
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        Log.v(LOG_TAG, sharedPreferences.getString("shop", "shopping"));

                        String foodJson = yelp.search(sharedPreferences.getString("food", "food"), currentAddress[0], currentAddress[1], "20");
                        String entertainmentJson = yelp.search(sharedPreferences.getString("entertainment", "entertainment"), currentAddress[0], currentAddress[1], "20");
                        String shoppingJson = yelp.search(sharedPreferences.getString("shop", "shopping"), currentAddress[0], currentAddress[1], "20");

                        Log.v(LOG_TAG, "json res:" + shoppingJson);
                        // todo: 1. Get familiar with Yelp Web API; 2. More interactions with "category" (such as: preferences for input from users); 3. how to display info on
                        foodArrayList = yelp.processJson(getActivity(), foodJson);
                        entertainmentArrayList = yelp.processJson(getActivity(), entertainmentJson);
                        shoppingArrayList = yelp.processJson(getActivity(), shoppingJson);

                        Log.v(LOG_TAG, "res size: " + String.valueOf(shoppingArrayList.size()));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                gridViewFood.setAdapter(new CustomGridViewAdapter(getActivity(), foodArrayList));
                gridViewEntertainment.setAdapter(new CustomGridViewAdapter(getActivity(), entertainmentArrayList));
                gridViewShopping.setAdapter(new CustomGridViewAdapter(getActivity(), shoppingArrayList));
            }
        }.execute();
    }

    public boolean isOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public static Fragment newInstance(int position) {
        Fragment fragment = new HabitDisplayFragment();
        Bundle args = new Bundle();
        args.putInt("selection", position);
        fragment.setArguments(args);
        return fragment;
    }
}