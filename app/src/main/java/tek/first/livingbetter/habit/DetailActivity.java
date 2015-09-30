package tek.first.livingbetter.habit;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import tek.first.livingbetter.R;
import tek.first.livingbetter.habit.model.InfoCollectedModel;
import tek.first.livingbetter.helper.DatabaseHelper;

public class DetailActivity extends AppCompatActivity {
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private TextView detailName, detailReviews, detailDistance, detailDescription,
            detailAddress, detailPhone, detailWebsite, detailComments;
    private RatingBar detailRating;
    private Button btnSelect;
    private String webUrl, phone, mapAddress, jsonInfoCollection;
    private LocationListener locationListener;
    private double latitudeDetail, longitudeDetail;
    private InfoCollectedModel infoCollected;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.habit_detail_activity);

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

        helper = new DatabaseHelper(DetailActivity.this);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

                if (infoCollected != null) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.TODOLIST_ITEM_COLUMN_TITLE, infoCollected.getName());
                    values.put(DatabaseHelper.TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED, sdf.format(new Date()));

                    helper.insertData(values);
                }

                finish();
            }
        });

        MapFragment mapFragment =
                ((MapFragment) getFragmentManager().findFragmentById(R.id.detail_map));

        if (mapFragment.getMap() == null)
            Log.e("getMap: ", "null");
        else {
            googleMap = mapFragment.getMap();
            //googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
        }

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            LatLng latLng = new LatLng(latitudeDetail, longitudeDetail);
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            jsonInfoCollection = extras.getString("resultObject");
        }

        infoCollected = new Gson().fromJson(jsonInfoCollection, InfoCollectedModel.class);

        detailName.setText(infoCollected.getName());
        detailRating.setRating(infoCollected.getRating());
        detailReviews.setText(infoCollected.getNumberComment() + "reviews");
        detailDistance.setText(infoCollected.getDistance() + "miles");
        detailDescription.setText(infoCollected.getCategory());
        detailAddress.setText(infoCollected.getAddress());
        detailPhone.setText(infoCollected.getPhoneNumber());
        detailWebsite.setText(infoCollected.getMobileUrl());
        detailComments.setText(infoCollected.getSnippet_text());
        mapAddress = infoCollected.getAddress();
        webUrl = infoCollected.getMobileUrl();
        phone = infoCollected.getPhoneNumber();
        latitudeDetail = infoCollected.getLatitude();
        longitudeDetail = infoCollected.getLongtitude();

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
}
