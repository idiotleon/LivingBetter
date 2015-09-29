package tek.first.livingbetter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity {

    private EditText food_edit;
    private EditText shop_edit;
    private EditText entertainment_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);

        food_edit = (EditText)findViewById( R.id.food_setting);
        food_edit.setText(sharedPreferences.getString("food","food"));
        shop_edit = (EditText)findViewById(R.id.shopping_setting);
        shop_edit.setText(sharedPreferences.getString("shop","shopping"));
        entertainment_edit = (EditText) findViewById(R.id.entertainment_setting);
        entertainment_edit.setText( sharedPreferences.getString("entertainment","entertainment"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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

    public void save( View view ){
        SharedPreferences perserence = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        SharedPreferences.Editor editor = perserence.edit();
        if( !food_edit.getText().toString().trim().equals("")){
            editor.putString("food", food_edit.getText().toString().trim());
        }else
            editor.putString("food","food");

        if( shop_edit.getText().toString().trim().equals("") )
            editor.putString("shop", "shopping");
        else
            editor.putString("shop", shop_edit.getText().toString().trim());

        if( entertainment_edit.getText().toString().trim().equals(""))
            editor.putString("entertainment", "entertainment");
        else
            editor.putString("entertainment", entertainment_edit.getText().toString().trim());

        editor.commit();
        finish();



    }
}
