package tek.first.livingbetter.habit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.model.InfoCollectedModel;
import tek.first.livingbetter.provider.DatabaseHelper;
import tek.first.livingbetter.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment;
import tek.first.livingbetter.todolist.fragment.dialog.DetailedNewToDoItemDialogFragment.OnNewItemAddedListener;
import tek.first.livingbetter.todolist.helper.GeneralConstants;
import tek.first.livingbetter.todolist.helper.GeneralHelper;
import tek.first.livingbetter.todolist.model.DetailedToDoItem;

public class HabitDetailActivity extends AppCompatActivity implements OnNewItemAddedListener {

    private static final String LOG_TAG = HabitDetailActivity.class.getSimpleName();

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private TextView detailName, detailReviews, detailDistance, detailDescription,
            detailAddress, detailPhone, detailWebsite, detailComments;
    private RatingBar detailRating;
    private Button btnSelect;
    private String webUrl, phone, mapAddress;
    private InfoCollectedModel infoCollected;
    private LocationListener locationListener;
    private double latitudeDetail, longitudeDetail;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_detail_activity);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            infoCollected = extras.getParcelable("resultObject");
        }

        detailName = (TextView) findViewById(R.id.detail_name);
        detailRating = (RatingBar) findViewById(R.id.ratingBar);
        detailReviews = (TextView) findViewById(R.id.detail_comment);
        detailDistance = (TextView) findViewById(R.id.detail_distance);
        detailDescription = (TextView) findViewById(R.id.detail_description);
        detailAddress = (TextView) findViewById(R.id.textViewAddress);
        detailAddress.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        detailPhone = (TextView) findViewById(R.id.textViewPhone);
        detailPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        detailWebsite = (TextView) findViewById(R.id.textViewWebsite);
        detailWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        detailComments = (TextView) findViewById(R.id.textViewCommentDetail);
        btnSelect = (Button) findViewById(R.id.btnselect);

        dbHelper = new DatabaseHelper(HabitDetailActivity.this);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToToDoList(infoCollected);
            }
        });

        detailName.setText(infoCollected.getName());
        detailRating.setRating(infoCollected.getRating());
        detailReviews.setText(infoCollected.getNumberComment() + "reviews");
        detailDistance.setText(infoCollected.getDistance() + "miles");
        detailDescription.setText(infoCollected.getCategory());
        detailAddress.setText(infoCollected.getAddress());
//        Log.v(LOG_TAG, "detailAddress: " + infoCollected.getAddress());

        ConvertAddressToLocation convertAddressToLocation = new ConvertAddressToLocation();
        convertAddressToLocation.execute(infoCollected.getAddress());

        detailPhone.setText(infoCollected.getPhoneNumber());
        detailWebsite.setText(infoCollected.getMobileUrl());
        detailComments.setText(infoCollected.getSnippetText());
        mapAddress = infoCollected.getAddress();
        webUrl = infoCollected.getMobileUrl();
        phone = infoCollected.getPhoneNumber();
        latitudeDetail = infoCollected.getLatitude();
        longitudeDetail = infoCollected.getLongitude();

        detailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri geoLocation = Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q", mapAddress).build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(geoLocation);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        detailWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(webUrl);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        detailPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });
    }

    private void addToToDoList(InfoCollectedModel infoCollected) {
        Log.v(LOG_TAG, "addToToDoList() executed.");

        String description = infoCollected.getAddress() + "\n" +
                infoCollected.getMobileUrl() + "\n" +
                infoCollected.getSnippetText();

        DetailedNewToDoItemDialogFragment detailedNewToDoItemDialogFragment = new DetailedNewToDoItemDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(GeneralConstants.TO_DO_ITEM_TITLE_IDENTIFIER, infoCollected.getName());
        arguments.putString(GeneralConstants.TO_DO_ITEM_DESCRIPTION_IDENTIFIER, description);
        detailedNewToDoItemDialogFragment.setArguments(arguments);
        detailedNewToDoItemDialogFragment.show(getFragmentManager(), "DetailedNewToDoItemDialogFragmentFromHabit");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewItemAdded(DetailedToDoItem detailedToDoItem) {
        Toast.makeText(HabitDetailActivity.this, "A new DetailedToDoItem added", Toast.LENGTH_SHORT).show();
        GeneralHelper.insertToDoListItem(HabitDetailActivity.this, detailedToDoItem);
    }

    class ConvertAddressToLocation extends AsyncTask<String, Void, double[]> {

        static final String GOOGLE_MAP_GEOCODING_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        static final String KEY = "&key=";
        double[] location = new double[]{0.0, 0.0};

        @Override
        protected double[] doInBackground(String... params) {
            String address = params[0];
            String API_KEY = getResources().getString(R.string.google_map_geocoding_api_key);

            String addressUrl = parseStringAddressToUrl(address, API_KEY);

            String urlString = GOOGLE_MAP_GEOCODING_BASE_URL + addressUrl;

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            StringBuffer stringBuffer = null;
            String locationInfoJsonStr = null;

            try {
                URL url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null)
                    return null;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                if (stringBuffer.length() == 0)
                    return null;

                locationInfoJsonStr = stringBuffer.toString();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } finally {
                if (httpURLConnection != null) httpURLConnection.disconnect();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            try {
                location = parseJSONStringToLatitudeAndLongitude(locationInfoJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return location;
        }

        private double[] parseJSONStringToLatitudeAndLongitude(String locationInfoJsonStr) throws JSONException {

            String LB_RESULT = "results";
            String LB_GEOMETRY = "geometry";
            String LB_LOCATION = "location";
            String LB_LAT = "lat";
            String LB_LNG = "lng";

            JSONObject jsonObjectResults = new JSONObject(locationInfoJsonStr);
            JSONArray jsonArray = jsonObjectResults.getJSONArray(LB_RESULT);
            JSONObject firstJsonObjectResult = jsonArray.getJSONObject(0);
            JSONObject jsonObjectGeometry = firstJsonObjectResult.getJSONObject(LB_GEOMETRY);
            JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject(LB_LOCATION);
            double latitude = jsonObjectLocation.getDouble(LB_LAT);
            Log.v(LOG_TAG, "latitude, parseJSONStringToLatitudeAndLongitude(String locationInfoJsonStr): " + latitude);
            double longitude = jsonObjectLocation.getDouble(LB_LNG);
            Log.v(LOG_TAG, "longitude, parseJSONStringToLatitudeAndLongitude(String locationInfoJsonStr): " + longitude);
            double[] location = new double[]{latitude, longitude};

            return location;
        }

        private String parseStringAddressToUrl(String address, String API_KEY) {

            String parsedAddress = "";

            String[] addressSplitByEnter = address.split(System.getProperty("line.separator"));

            String[] addressPart1 = addressSplitByEnter[0].split("\\s+");
            for (int i = 0; i < addressPart1.length - 1; i++) {
                parsedAddress += addressPart1[i] + "+";
            }
            parsedAddress += addressPart1[addressPart1.length - 1] + ",+";
            if (addressSplitByEnter.length > 1) {

                String[] addressPart2 = addressSplitByEnter[1].split("\\s+");
                for (int i = 0; i < addressPart2.length - 1; i++) {
                    parsedAddress += addressPart2[i] + "+";
                }
                parsedAddress += addressPart2[addressPart2.length - 1];
            }

/*            String[] addressSplitByComma = address.split(",");
            String[] addressPart3 = addressSplitByComma[1].split("\\s+");
            parsedAddress += addressPart3[0];*/

            parsedAddress += KEY + API_KEY;
            Log.v(LOG_TAG, "parsedAddress: " + parsedAddress);

            return parsedAddress;
        }

        @Override
        protected void onPostExecute(double[] doubles) {
            super.onPostExecute(doubles);

            MapFragment mapFragment =
                    ((MapFragment) getFragmentManager().findFragmentById(R.id.detail_map));

            if (mapFragment.getMap() == null)
                Log.e(LOG_TAG, "mapFragment is null");
            else {
                googleMap = mapFragment.getMap();
                //googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomGesturesEnabled(true);
            }

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
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

            try {
                if (ContextCompat.checkSelfPermission(HabitDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(HabitDetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                LatLng latLng = new LatLng(location[0], location[1]);
                Marker placeOfInterests = googleMap.addMarker(new MarkerOptions().position(latLng).title(infoCollected.getName()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
