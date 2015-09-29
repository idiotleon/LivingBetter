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
import tek.first.livingbetter.helper.DatabaseHelper;
import tek.first.livingbetter.helper.UserContract;

public class SignUpActivity extends AppCompatActivity {

    private EditText username_editText;
    private EditText password_editText;
    private EditText password_com_editText;
    private EditText age_editText;
    private EditText street_editText;
    private EditText city_editText;
    private EditText country_editText;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        username_editText = (EditText) findViewById(R.id.username_editText_signUp);
        password_editText = (EditText) findViewById(R.id.password_editText_signUp);
        password_com_editText = (EditText) findViewById(R.id.password_com_editText_signUp);
        age_editText = (EditText) findViewById(R.id.age_editText_signUp);
        street_editText = (EditText) findViewById(R.id.street_editText_signUp);
        city_editText = (EditText) findViewById(R.id.city_editText_signUp);
        country_editText = (EditText) findViewById(R.id.country_editText_signUp);

        helper = new DatabaseHelper( this );
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

        if( username_editText.getText().toString().trim().equals("")) {
            Crouton.makeText(SignUpActivity.this, "Please input your username.", Style.ALERT).show();
            return;
        }

        if( password_editText.getText().toString().trim().equals("") ){
            Crouton.makeText(SignUpActivity.this, "Please input your password.", Style.ALERT).show();
            return;
        }

        if( !password_editText.getText().toString().trim().equals(password_com_editText.getText().toString().trim())){
            Crouton.makeText(SignUpActivity.this, "Please input your password in the same way.", Style.ALERT).show();
            return;
        }

        Crouton.makeText(this, "You have created your account successfully.", Style.CONFIRM).show();

        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_ID, UUID.randomUUID().hashCode());
        values.put(UserContract.UserEntry.COLUMN_USER_USERNAME, username_editText.getText().toString().trim());
        values.put(UserContract.UserEntry.COLUMN_USER_AGE, age_editText.getText().toString().trim());
        values.put(UserContract.UserEntry.COLUMN_USER_COUNTRY, country_editText.getText().toString().trim());
        values.put(UserContract.UserEntry.COLUMN_USER_STREET, street_editText.getText().toString().trim());
        values.put(UserContract.UserEntry.COLUMN_USER_CITY, city_editText.getText().toString().trim());
        values.put(UserContract.UserEntry.COLUMN_USER_PASSWORD, password_editText.getText().toString().trim());

        helper.insertUser( values );

        finish();
    }
}
