package tek.first.livingbetter.login;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.UUID;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.R;
import tek.first.livingbetter.provider.DatabaseHelper;
import tek.first.livingbetter.provider.LivingBetterContract;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordComEditText;
    private EditText ageEditText;
    private EditText streetEditText;
    private EditText cityEditText;
    private EditText countryEditText;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = (EditText) findViewById(R.id.username_editText_signUp);
        passwordEditText = (EditText) findViewById(R.id.password_editText_signUp);
        passwordComEditText = (EditText) findViewById(R.id.password_com_editText_signUp);
        ageEditText = (EditText) findViewById(R.id.age_editText_signUp);
        streetEditText = (EditText) findViewById(R.id.street_editText_signUp);
        cityEditText = (EditText) findViewById(R.id.city_editText_signUp);
        countryEditText = (EditText) findViewById(R.id.country_editText_signUp);

        helper = new DatabaseHelper(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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

    public void signUp(View view) {

        if (usernameEditText.getText().toString().trim().equals("")) {
            Crouton.makeText(SignUpActivity.this, "Please input your username.", Style.ALERT).show();
            return;
        }

        if (passwordEditText.getText().toString().trim().equals("")) {
            Crouton.makeText(SignUpActivity.this, "Please input your password.", Style.ALERT).show();
            return;
        }

        if (!passwordEditText.getText().toString().trim().equals(passwordComEditText.getText().toString().trim())) {
            Crouton.makeText(SignUpActivity.this, "Please input your password in the same way.", Style.ALERT).show();
            return;
        }

        Crouton.makeText(this, "You have created your account successfully.", Style.CONFIRM).show();

        ContentValues values = new ContentValues();
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_ID, UUID.randomUUID().hashCode());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_USERNAME, usernameEditText.getText().toString().trim());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_AGE, ageEditText.getText().toString().trim());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_COUNTRY, countryEditText.getText().toString().trim());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_STREET, streetEditText.getText().toString().trim());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_CITY, cityEditText.getText().toString().trim());
        values.put(LivingBetterContract.UserEntry.COLUMN_USER_PASSWORD, passwordEditText.getText().toString().trim());

        getContentResolver().insert(LivingBetterContract.UserEntry.CONTENT_URI, values);

        finish();
    }
}
