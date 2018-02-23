package com.jb.utaheracleia.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Set the name of the bluetooth device that android will connect to
    //This device has to be paired with the android device before this program will work
    public String DEVICE_NAME = "DESKTOP-9DF7A2";
    public String SERVER_UUID = "7A51FDC2-FDDF-4c9b-AFFC-98BCD91BF93B";



    BluetoothAdapter bluetooth;
    public View Current_Menu_Showing;
    public ArrayList<View> Last_Visited = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        HideAllMenus();
        SetVisible(findViewById(R.id.main));



        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth == null) {
            ShowToast("Bluetooth not supported...");
        }

        int REQUEST_ENABLE_BT = 5;

        if (!bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        bluetooth.cancelDiscovery();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(findViewById(R.id.settings).getVisibility() == View.VISIBLE) {
                HideAllMenus();
                SetVisible(findViewById(R.id.main));
            } else if (Current_Menu_Showing.getId() != R.id.main)
            {
                GoBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            HideAllMenus();
            SetVisible(findViewById(R.id.main));

        }
        else if (id == R.id.nav_manage) {
            HideAllMenus();
            SetVisible(findViewById(R.id.settings));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    BluetoothThread bluetoothConnection = null;
    public void ConnectClick(View window)
    {

        if(bluetoothConnection == null) {
            ShowToast("Connecting....");

            bluetoothConnection = new BluetoothThread(bluetooth, DEVICE_NAME, SERVER_UUID);
            bluetoothConnection.run("");

            if (bluetoothConnection.sender != null) {
                ShowToast("Connected....");

                HideAllMenus();
                SetVisible(findViewById(R.id.first_menu));
            } else {
                ShowToast("Not connected....");
            }
        }
        else
        {
            if (bluetoothConnection.sender != null) {
                ShowToast("Connected....");

                HideAllMenus();
                SetVisible(findViewById(R.id.first_menu));
            } else {
                bluetoothConnection = null;
                ShowToast("Not connected....");
            }
        }
    }

    public void SendClick(View window) {
        if (bluetoothConnection.sender != null) {
            ShowToast("Sending....");
            bluetoothConnection.run("test message");
        }
        else
        {
            ShowToast("Not connnected....");
        }
    }

   public void DisconnectClick(View window)
    {
        if(!(bluetoothConnection == null))
        {
            bluetoothConnection.cancel();
        }
        bluetoothConnection = null;
        ShowToast("Disconnected....");
    }

    public void ShowToast(String to_show)
    {
        Context context = getApplicationContext();
        CharSequence text = to_show;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void ExerciseClick(View window)
    {
        String clicked = ((Button)window).getText().toString();

        String button_num = Character.toString(clicked.charAt(clicked.length() - 1));

        Resources resources = getResources();

        String menu_name = "sub_menu_" + button_num;
        int menu_id = resources.getIdentifier(menu_name, "id", getApplicationContext().getPackageName());

        HideAllMenus();
        SetVisible(findViewById(menu_id));


    }

    public void OptionClick(View window)
    {
        String clicked = ((Button)window).getTag().toString();

        String[] seperated_name = clicked.split("_");

        if(seperated_name.length == 3)
        {
            Resources resources = getResources();

            String menu_name = "final_menu_" + seperated_name[1] + "_" + seperated_name[2];
            int menu_id = resources.getIdentifier(menu_name, "id", getApplicationContext().getPackageName());

            HideAllMenus();
            SetVisible(findViewById(menu_id));
        }
        else
        {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }

    public void SenderClick(View window)
    {
        String clicked = ((Button)window).getTag().toString();

        String[] seperated_name = clicked.split("_");

        if(seperated_name.length == 4)
        {
            String message = "message from send_" + seperated_name[1] + "_" + seperated_name[2] + "_" + seperated_name[3];

            if (bluetoothConnection.sender != null) {
                ShowToast("Sending....");
                bluetoothConnection.run(message);
            }
            else
            {
                ShowToast("Not connnected....");
            }

        }
        else
        {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }

    public void GoBack()
    {
        View before_changed = Current_Menu_Showing;
        HideAllMenus();
        if (Last_Visited.size() > 0) {
            SetVisible(Last_Visited.get(Last_Visited.size() - 2));
            Last_Visited.remove(Last_Visited.size() - 2);
            Last_Visited.remove(Last_Visited.size() - 1);
        }


    }
    public void HideAllMenus()
    {
        ConstraintLayout parent = ((ConstraintLayout)findViewById(R.id.main_stack));

        for(int i = 0; i < parent.getChildCount(); i++)
        {
            if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.support.constraint.ConstraintLayout"))
            {
                SetInvisible(parent.getChildAt(i));
            }
        }
    }

    public void SetVisible(View view)
    {
        view.setVisibility(View.VISIBLE);
        Last_Visited.add(view);
        Current_Menu_Showing = view;
    }
    public void SetInvisible(View view)
    {
        view.setVisibility(View.INVISIBLE);
    }
}
