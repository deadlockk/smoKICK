package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.NewSensorEvent;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Subscribe;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RemoteSensorManager remoteSensorManager;

    Toolbar mToolbar;

    private ViewPager pager;
    private View emptyState;
    private NavigationView mNavigationView;
    private Menu mNavigationViewMenu;
    private List<Node> mNodes;

    PDetails pDetails;
    Money money;
    Motivation motivation;
    Days days;
    FragmentManager fragmentManager;
    SharedPreferences sharedPreferences;

    ImageButton profile;

    void init() {

        sharedPreferences = getSharedPreferences("smokingInformation", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Boolean isFirst=sharedPreferences.getBoolean("first",true);

        if(isFirst==null||isFirst) {
            editor.putInt("smokingInformation", 1);
            editor.putBoolean("first",false);
            editor.commit();
        }
        pDetails = new PDetails();
        money = new Money();
        motivation = new Motivation();
        days = new Days();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.days, days).commit();
        fragmentManager.beginTransaction().add(R.id.money, money).commit();
        fragmentManager.beginTransaction().add(R.id.motivation, motivation).commit();
        fragmentManager.beginTransaction().add(R.id.details, pDetails).commit();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getStringExtra("result").equals("1")) {
                profile.setImageResource(R.drawable.splash);
            } else if (data.getStringExtra("result").equals("2")) {
                profile.setImageResource(R.drawable.daily);
            } else if (data.getStringExtra("result").equals("3")) {
                profile.setImageResource(R.drawable.money);
            }
/*            switch(requestCode) {
                case 3000:
                    profile.setImageResource(R.drawable.splash);
                    break;
            }*/
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        FirebaseUser cur_user = FirebaseAuth.getInstance().getCurrentUser();

        if (cur_user != null) {
            String userEmail = cur_user.getEmail();
            email.setText(userEmail);
        }

        sharedPreferences = getSharedPreferences("smoKICK", Context.MODE_PRIVATE);
        TextView myName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_name);
        myName.setText(sharedPreferences.getString("Name", "Name"));


        profile = (ImageButton) navigationView.getHeaderView(0).findViewById(R.id.drawer_imageView);

        if (cur_user != null) {
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Emblem.class);
                    startActivityForResult(intent, 3000);
                }
            });

        }


        IntentFilter filter = new IntentFilter();
        filter.addAction("smoKICK_Info");
    }


    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
        remoteSensorManager= RemoteSensorManager.getInstance(this);
      //  pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager(), sensors));

//        if (sensors.size() > 0) {
//            emptyState.setVisibility(View.GONE);
//        } else {
//            emptyState.setVisibility(View.VISIBLE);
//        }

      remoteSensorManager.startMeasurement();

//        mNavigationViewMenu.clear();
//        remoteSensorManager.getNodes(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
//            @Override
//            public void onResult(final NodeApi.GetConnectedNodesResult pGetConnectedNodesResult) {
//                mNodes = pGetConnectedNodesResult.getNodes();
//                for (Node node : mNodes) {
//                    SubMenu menu = mNavigationViewMenu.addSubMenu(node.getDisplayName());
//
//                    MenuItem item = menu.add("15 sensors");
//                    if (node.getDisplayName().startsWith("G")) {
//                        item.setChecked(true);
//                        item.setCheckable(true);
//                    } else {
//                        item.setChecked(false);
//                        item.setCheckable(false);
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);

        //측정 정지시키는 함수
        // remoteSensorManager.stopMeasurement();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = null;
        if (id == R.id.nav_home) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_calendar) {
            intent = new Intent(this, Calendar.class);
        } else if (id == R.id.nav_community) {
            intent = new Intent(this, Community.class);
        } else if (id == R.id.nav_vs) {
            intent=new Intent(this,VSmode.class);
        } else if (id == R.id.nav_about) {
//            new SweetAlertDialog(this,SweetAlertDialog.CUSTOM_IMAGE_TYPE).setCustomImage(R.drawable.about)
//                    .setTitleText("Developer").setContentText("Dishant Mahajan\nD3CSEA1\nRoll:1507567\nPhone:9023074222").show();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("알림");
            builder.setMessage("로그아웃 하시겠습니까?");
            builder.setNegativeButton("취소", null);
            builder.setPositiveButton("로그아웃",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(MainActivity.this, Details.class);
                            startActivity(i);
                            finish();
                        }
                    });
            builder.show();
        }
        if (intent != null) {
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(receiver);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<Sensor> sensors;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Sensor> symbols) {
            super(fm);
            this.sensors = symbols;
        }


        public void addNewSensor(Sensor sensor) {
            this.sensors.add(sensor);
        }


        private Sensor getItemObject(int position) {
            return sensors.get(position);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return SensorFragment.newInstance(sensors.get(position).getId());
        }

        @Override
        public int getCount() {
            return sensors.size();
        }

    }


    private void notifyUSerForNewSensor(Sensor sensor) {
        Toast.makeText(this, "New Sensor!\n" + sensor.getName(), Toast.LENGTH_SHORT).show();
    }


    @Subscribe
    public void onNewSensorEvent(final NewSensorEvent event) {
     //   ((ScreenSlidePagerAdapter) pager.getAdapter()).addNewSensor(event.getSensor());
      //  pager.getAdapter().notifyDataSetChanged();
      //  emptyState.setVisibility(View.GONE);
      //  notifyUSerForNewSensor(event.getSensor());
    }
}
