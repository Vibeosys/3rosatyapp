package com.threerosaty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.threerosaty.activities.BaseActivity;
import com.threerosaty.activities.ContactUsActivity;
import com.threerosaty.activities.CustomerLoginActivity;
import com.threerosaty.activities.PortfolioListActivity;
import com.threerosaty.activities.SubscriberLoginActivity;
import com.threerosaty.activities.SubscriberProfileActivity;
import com.threerosaty.activities.SubscriptionActivity;
import com.threerosaty.activities.UserProfileActivity;
import com.threerosaty.fragments.PortfolioListFragment;
import com.threerosaty.fragments.SubscriberMainView;
import com.threerosaty.utils.UserAuth;
import com.threerosaty.utils.UserType;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int FILTER_RESULT = 2;
    private static final String PORFOLIO_LIST_FRAGMENT = "portfolioList";
    private static final String SUBSCRIBER_MAIN_FRAGMENT = "subMainView";
    private FrameLayout fragmentFrameLay;
    private int userType = UserType.USER_OTHER;
    private OnFilterClickListener onFilterClickListener;
    private TextView mNavigationUserEmailId, mNavigationUserName;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        userType = mSessionManager.getUserType();
        fragmentFrameLay = (FrameLayout) findViewById(R.id.fragment_frame_lay);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mNavigationUserEmailId = (TextView) headerView.findViewById(R.id.userEmailId);
        mNavigationUserName = (TextView) headerView.findViewById(R.id.userName);
        if (userType == UserType.USER_SUBSCRIBER) {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_main_subscriber);
            mNavigationUserEmailId.setText("" + mSessionManager.getEmail());
            mNavigationUserName.setText("" + mSessionManager.getName());
            SubscriberMainView subscriberMainView = new SubscriberMainView();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_frame_lay, subscriberMainView, SUBSCRIBER_MAIN_FRAGMENT).commit();
        } else if (userType == UserType.USER_CUSTOMER) {
            mNavigationUserEmailId.setText("" + mSessionManager.getUserEmail());
            mNavigationUserName.setText("" + mSessionManager.getUserFName() + " " + mSessionManager.getUserLName());
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_main_user);
            PortfolioListFragment userListFragment = new PortfolioListFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_frame_lay, userListFragment, PORFOLIO_LIST_FRAGMENT).commit();
        } else if (userType == UserType.USER_OTHER) {
            mNavigationUserEmailId.setText("info@3rosaty.com");
            mNavigationUserName.setText("www.3rosaty.com");
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_main_drawer);
            PortfolioListFragment userListFragment = new PortfolioListFragment();
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_frame_lay, userListFragment, PORFOLIO_LIST_FRAGMENT).commit();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userType != UserType.USER_SUBSCRIBER) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/
        if (id == R.id.filter) {
            onFilterClickListener.onFilterClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sub_profile) {
            Intent iLogin = new Intent(getApplicationContext(), SubscriberProfileActivity.class);
            startActivity(iLogin);
        } else if (id == R.id.nav_user_profile) {
            Intent iLogin = new Intent(getApplicationContext(), UserProfileActivity.class);
            startActivity(iLogin);
        } else if (id == R.id.nav_business_login) {
            Intent iLogin = new Intent(getApplicationContext(), SubscriberLoginActivity.class);
            startActivity(iLogin);
            finish();
        } else if (id == R.id.nav_contact_us) {
            Intent intent = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            callToLogOut();
        } else if (id == R.id.nav_portfolio) {
            if (mSessionManager.getIsSubscribed()) {
                Intent intent = new Intent(getApplicationContext(), PortfolioListActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), SubscriptionActivity.class);
                intent.putExtra(SubscriptionActivity.STYPE, mSessionManager.getSType());
                startActivity(intent);
            }

        } else if (id == R.id.nav_customer_login) {
            Intent intent = new Intent(getApplicationContext(), CustomerLoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callToLogOut() {
        UserAuth.CleanAuthenticationInfo();
        finish();
        recreate();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://3rosaty.com/"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.threerosaty/http/3rosaty.com/")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://3rosaty.com/"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.threerosaty/http/3rosaty.com/")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public interface OnFilterClickListener {
        void onFilterClick();
    }

    public void setFilterClickListener(OnFilterClickListener listener) {
        this.onFilterClickListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
