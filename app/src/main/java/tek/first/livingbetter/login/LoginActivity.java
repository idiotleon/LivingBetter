package tek.first.livingbetter.login;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.HomeActivity;
import tek.first.livingbetter.R;
import tek.first.livingbetter.helper.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText username_editText;
    private EditText password_editText;

    private DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username_editText = (EditText) findViewById(R.id.username_editText_main);
        password_editText = (EditText) findViewById(R.id.password_editText_main);

        helper = new DatabaseHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void login(View view) {

        if (username_editText.getText().toString().trim().equals("")) {
            Crouton.makeText(LoginActivity.this, "Username is empty.", Style.ALERT).show();
            return;
        }

        if (password_editText.getText().toString().trim().equals("")) {
            Crouton.makeText(LoginActivity.this, "Password is empty.", Style.ALERT).show();
            return;
        }

        int loginRes = helper.validateLogin(username_editText.getText().toString().trim(),
                password_editText.getText().toString().trim());


        // validate Internet

        if (!isOnline()) {
            Crouton.makeText(LoginActivity.this, "Network unavaliable", Style.ALERT).show();

        }

        if (!GPSavailiable()) {
            Crouton.makeText(LoginActivity.this, "GPS unavailiable", Style.ALERT).show();
        }

        if (loginRes == -1) {
            Crouton.makeText(LoginActivity.this, "There is no such user named " + username_editText.getText().toString().trim(),
                    Style.ALERT).show();
            return;
        }

        if (loginRes == 0) {
            Crouton.makeText(LoginActivity.this, "Wrong username/password.",
                    Style.ALERT).show();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public boolean isOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public boolean GPSavailiable() {
        LocationManager locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    public final void openGPS() {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(LoginActivity.this, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
