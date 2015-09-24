package derekhsieh.derekhsiehapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

import Serializer.Serializer;


public class MainPageActivity extends ActionBarActivity {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Bundle extras = getIntent().getExtras();
        String user = extras.getString("user");
        List<String> response = extras.getStringArrayList("serverResponse");
        int noFriendRequests = Integer.valueOf(response.get(0));
        List<String> friendList = (List<String>) Serializer.toObject(response.get(1));
        ((Button) findViewById(R.id.NumFriendRequests)).setText(noFriendRequests);
        //show number of notifications
        // show friends
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    public void goToFriendRequests() {
        Intent goTofFriendRequestActivity = new Intent(this, FriendRequestActivity.class);
        startActivity(goTofFriendRequestActivity);
    }

    public void goToFriendLists(){
        Intent goToFriendListActivity = new Intent(this, FriendListActivity.class );
        startActivity(goToFriendListActivity);
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
