package com.example.belegen;

import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    ActionBar actionBar;
    public static boolean internet = true;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_menu);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.db);
            actionBar.setTitle("Dashboard");
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        actionBar.setTitle(savedInstanceState.getCharSequence("toolbar_title"));
        navigationView.setCheckedItem(savedInstanceState.getInt("item_selected"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putCharSequence("toolbar_title", actionBar.getTitle());
        outState.putInt("item_selected", navigationView.getCheckedItem().getItemId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(internet && !menuItem.isChecked()){
            menuItem.setChecked(true);

            switch (menuItem.getItemId()){
                case R.id.db:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new DashboardFragment()).commit();
                    actionBar.setTitle("Dashboard");
                    break;
                case R.id.view_emp:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new fragment_display_Employee_list()).commit();
                    actionBar.setTitle("List of Employees");
                    break;
                case R.id.add_emp:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AddEmployeeFragment()).commit();
                    actionBar.setTitle("Add an employee");
                    break;
                case R.id.view_cont:
                    actionBar.setTitle("List of Contractors");
                    break;
                case R.id.add_cont:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AddContractorFragment()).commit();
                    actionBar.setTitle("Add a contractor");
                    break;
                case R.id.add_sale:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AddSaleFragment()).commit();
                    actionBar.setTitle("Add sale for a day");
                    break;
                case R.id.upd_prices:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FuelListFragment()).commit();
                    actionBar.setTitle("Update Product Prices");
                    break;
                case R.id.settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ChangeCredentialsFragment()).commit();
                    actionBar.setTitle("Change Credentials");
                    break;
            }
        }
        else if(!internet){

//            if(menuItem.getItemId() == R.id.nav_settings) {
//                launchActivity(SettingsActivity.class);
//                mDrawerLayout.closeDrawers();
//                return true;
//            }
//            else if (menuItem.getItemId() == R.id.nav_logout){
//                FirebaseAuth.getInstance().signOut();
//                launchActivity(LoginActivity.class);
//                mDrawerLayout.closeDrawers();
//                return true;
//            }

            showDialog("Please connect to a wifi or cellular data network");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, 100);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    protected void showDialog(String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        // Set the Alert Dialog Message
        builder.setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                alert.dismiss();
                            }
                        }
                )
                .setIcon(android.R.drawable.ic_dialog_alert);

        alert = builder.create();
        alert.show();
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        mDrawerLayout.setDrawerLockMode(lockMode);
        mToggle.setDrawerIndicatorEnabled(enabled);
    }
}
