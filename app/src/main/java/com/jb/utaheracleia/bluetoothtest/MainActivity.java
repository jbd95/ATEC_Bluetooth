package com.jb.utaheracleia.bluetoothtest;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /* CHANGE THE VALUES IN THIS SECTION BELOW TO CONNECT TO A DIFFERENT COMUPUTEr
    Set the name of the bluetooth device that android will connect to
    This device has to be paired with the android device before this program will work*/
    public String DEVICE_NAME = "DESKTOP-OM1D1UH";
    public String SERVER_UUID = "7A51FDC2-FDDF-4c9b-AFFC-98BCD91BF93B";

    /*CHANGE THESE VALUES TO EDIT THE SIZE OF THE MENUS*/
    final int EXERCISE_COUNT = 10;
    final int ACTIVITY_TYPE_COUNT = 3;
    int[] exercise_options = new int[EXERCISE_COUNT];
    String[] main_menu_button_text = new String[EXERCISE_COUNT];


    BluetoothAdapter bluetooth;
    public View Current_Menu_Showing;
    public ArrayList<View> Last_Visited = new ArrayList<>();
    Map<String, String> exercise_names = new HashMap<String, String>();
    Map<String, String> foot_options = new HashMap<String, String>();


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



        exercise_options[1] = 6;
        exercise_options[2] = 3;
        exercise_options[3] = 2;
        exercise_options[4] = 8;
        exercise_options[5] = 2;
        exercise_options[6] = 4;
        exercise_options[7] = 2;
        exercise_options[8] = 12;

        main_menu_button_text[0] = "Introduction";
        main_menu_button_text[1] = "Gross Motor Gait and Balance";
        main_menu_button_text[2] = "Synchronous Movements";
        main_menu_button_text[3] = "Coordination and Inhibition - Beat";
        main_menu_button_text[4] = "LightsCoordination and Inhibition - ";
        main_menu_button_text[5] = "Visual Response Inhibition";
        main_menu_button_text[6] = "Cross Body Game";
        main_menu_button_text[7] = "Finger-Nose Coordination";
        main_menu_button_text[8] = "Rapid Sequential Movements";
        main_menu_button_text[9] = "Goodbye";

        exercise_names.put("2_1", "Walk Naturally");
        exercise_names.put("2_2", "Sneaky Toes");
        exercise_names.put("2_3", "Heat to Toe Walk");
        exercise_names.put("2_4", "Spell Caster");
        exercise_names.put("2_5", "Open Right Foot Wonder");
        exercise_names.put("2_6", "Open Left Foot Wonder");

        exercise_names.put("3_1", "March to the Intro");
        exercise_names.put("3_2", "March to the Beat Slow");
        exercise_names.put("3_3", "March to the Beat Fast");

        exercise_names.put("4_1", "Bag Pass Slow");
        exercise_names.put("4_2", "Bag Pass Fast");

        exercise_names.put("5_1", "Red/Green Light Slow");
        exercise_names.put("5_2", "Red/Green Light Fast");
        exercise_names.put("5_3", "Red/Green/Yellow Light Slow");
        exercise_names.put("5_4", "Red/Green/Yellow Light Fast");
        exercise_names.put("5_5", "Traffic Light Slow");
        exercise_names.put("5_6", "Traffic Light Fast");
        exercise_names.put("5_7", "Traffic Light Yellow Slow");
        exercise_names.put("5_8", "Traffic Light Yellow Fast");

        exercise_names.put("6_1", "Sailor Step Slow");
        exercise_names.put("6_2", "Sailor Step Fast");

        exercise_names.put("7_1", "Cross Your Body");
        exercise_names.put("7_2", "Opposite - Ears and Knees");
        exercise_names.put("7_3", "Opposite - Hips and Shoulders");
        exercise_names.put("7_4", "Opposite - Cross Your Body");

        exercise_names.put("8_1", "Stretch and Touch Game");
        exercise_names.put("8_2", "Strecth and Touch - Both Hands");

        exercise_names.put("9_1", "Foot Tap Right");
        exercise_names.put("9_2", "Foot Tap Left");
        exercise_names.put("9_3", "Heel Toe Right");
        exercise_names.put("9_4", "Heel Toe Left");
        exercise_names.put("9_5", "Hand Pat Right");
        exercise_names.put("9_6", "Hand Pat Left");
        exercise_names.put("9_7", "Hand Flip-Flop Right");
        exercise_names.put("9_8", "Hand Flip-Flop Left");
        exercise_names.put("9_9", "Finger Tap Right");
        exercise_names.put("9_10", "Finger Tap Left");
        exercise_names.put("9_11", "Fingers In Order Right");
        exercise_names.put("9_12", "Fingers in Order Left");


        foot_options.put("2_5", "141");
        foot_options.put("2_6", "142");

        foot_options.put("9_1", "801");
        foot_options.put("9_2", "802");
        foot_options.put("9_3", "811");
        foot_options.put("9_4", "812");
        foot_options.put("9_5", "821");
        foot_options.put("9_6", "822");
        foot_options.put("9_7", "831");
        foot_options.put("9_8", "832");
        foot_options.put("9_9", "841");
        foot_options.put("9_10", "842");
        foot_options.put("9_11", "851");
        foot_options.put("9_12", "852");

        CreateMainMenu(EXERCISE_COUNT, (ConstraintLayout)findViewById(R.id.main_stack));


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

    enum Foot
    {
        None,
        Right,
        Left
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (findViewById(R.id.settings).getVisibility() == View.VISIBLE) {
                HideAllMenus();
                SetVisible(findViewById(R.id.main));
            } else if (Current_Menu_Showing.getId() != R.id.main) {
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

        } else if (id == R.id.nav_manage) {
            HideAllMenus();
            SetVisible(findViewById(R.id.settings));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    BluetoothThread bluetoothConnection = null;

    public void ConnectClick(View window) {

        if (bluetoothConnection == null) {
            ShowToast("Connecting....");

            bluetoothConnection = new BluetoothThread(bluetooth, DEVICE_NAME, SERVER_UUID);
            bluetoothConnection.run("");

            if (bluetoothConnection.sender != null) {
                ShowToast("Connected....");

                HideAllMenus();
                SetVisible(findViewById(R.id.start_activity));
            } else {
                ShowToast("Not connected....");
            }
        } else {
            if (bluetoothConnection.sender != null) {
                ShowToast("Connected....");

                HideAllMenus();
                SetVisible(findViewById(R.id.start_activity));
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
        } else {
            ShowToast("Not connnected....");
        }
    }

    public void DisconnectClick(View window) {
        if (!(bluetoothConnection == null)) {
            bluetoothConnection.cancel();
        }
        bluetoothConnection = null;
        ShowToast("Disconnected....");
    }

    public void ShowToast(String to_show) {
        Context context = getApplicationContext();
        CharSequence text = to_show;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void SenderClick(View window) {
        String clicked = ((Button) window).getTag().toString();

        String[] seperated_name = clicked.split("_");

        JsonConverter converter = new JsonConverter();

        AtecActivity temp_activity = new AtecActivity();
        //temp_activity.TaskId = (Integer.parseInt(seperated_name[1]) * 10) + Integer.parseInt(seperated_name[2]);
        temp_activity.TaskId = (((Integer.parseInt(seperated_name[1]) - 1) * 100) + ((Integer.parseInt(seperated_name[2]) - 1) * 10));

        if((foot_options.get(seperated_name[1] + "_" + seperated_name[2]) != null))
        {
                temp_activity.TaskId = Integer.parseInt(foot_options.get(seperated_name[1] + "_" + seperated_name[2]));
        }


        for(ActivityType current_type : ActivityType.values())
        {
            if(current_type.ordinal() == (Integer.parseInt(seperated_name[3]) - 1))
            {
                temp_activity.ActivityType = current_type;
            }
        }

        converter.SetDate(temp_activity);

        AtecCommand command = AtecCommand.StartActivity;

        if (seperated_name.length == 4) {

            if (bluetoothConnection.sender != null) {
                ShowToast("Sending....");
                bluetoothConnection.run("[" + command.ordinal() + "," + converter.GetJsonString(temp_activity) + "]");
            } else {
                ShowToast("Not connnected....");
            }

        } else {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }

    public void StartTest(View view)
    {
        if (bluetoothConnection.sender != null) {
            bluetoothConnection.run("[" + AtecCommand.StartTest.ordinal() + "]");
        } else {
            ShowToast("Not connnected....");
        }

        HideAllMenus();

        ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));
        for (int i = 0; i < parent.getChildCount(); i++) {
            if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.widget.linearlayout"))
            {
                if(((LinearLayout)parent.getChildAt(i)).getTag().toString().equalsIgnoreCase("first_menu"))
                {
                    HideAllMenus();
                    SetVisible(parent.getChildAt(i));
                }
            }
        }


        findViewById(R.id.start_test).setVisibility(View.INVISIBLE);
        findViewById(R.id.continue_test).setVisibility(View.VISIBLE);
        findViewById(R.id.end_test).setVisibility(View.VISIBLE);
    }

    public void ContinueTest(View view)
    {
        HideAllMenus();

        ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));
        for (int i = 0; i < parent.getChildCount(); i++) {
            if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.widget.linearlayout"))
            {
                if(((LinearLayout)parent.getChildAt(i)).getTag().toString().equalsIgnoreCase("first_menu"))
                {
                    HideAllMenus();
                    SetVisible(parent.getChildAt(i));
                }
            }
        }
    }

    public void EndTest(View view)
    {
        findViewById(R.id.start_test).setVisibility(View.VISIBLE);
        findViewById(R.id.continue_test).setVisibility(View.INVISIBLE);
        findViewById(R.id.end_test).setVisibility(View.INVISIBLE);

        if (bluetoothConnection.sender != null) {

            bluetoothConnection.run("[" + AtecCommand.EndTest.ordinal() + "]");
        } else {
            ShowToast("Not connnected....");
        }
    }

    public void GoBack() {
        View before_changed = Current_Menu_Showing;
        HideAllMenus();
        if (Last_Visited.size() > 0) {
            SetVisible(Last_Visited.get(Last_Visited.size() - 2));
            Last_Visited.remove(Last_Visited.size() - 2);
            Last_Visited.remove(Last_Visited.size() - 1);
        }


    }

    public void HideAllMenus() {
        ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));

        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.support.constraint.ConstraintLayout")) {
                SetInvisible(parent.getChildAt(i));
            }
            else if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.widget.linearlayout"))
            {
                SetInvisible(parent.getChildAt(i));
            }
        }
    }

    private void CreateMainMenu(int num_options, ConstraintLayout parent_layout)
    {
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setVerticalGravity(Gravity.CENTER);

        for(int i = 1; i <= num_options; i++) {
            Button new_button = new Button(this);

            new_button.setText(main_menu_button_text[i - 1]);
            new_button.setTag(i + "");


            layout.addView(new_button);



            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   ExerciseClick(v);
                }
            });


            CreateSubMenu(exercise_options[i - 1], parent_layout, i);
        }
        layout.setTag("first_menu");
        parent_layout.addView(layout);

        HideAllMenus();
        Last_Visited = new ArrayList<>();

    }
    private void CreateSubMenu(int num_options, ConstraintLayout parent_layout, int index)
    {
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setVerticalGravity(Gravity.CENTER);


        for(int i = 1; i <= num_options; i++) {


            Button new_button = new Button(this);
            new_button.setText(exercise_names.get(index + "_" + i));
            new_button.setTag("middle_" + index + "_" + i);






            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    OptionClick(v);
                }
            });


            layout.addView(new_button);

            CreateFinalMenu(ACTIVITY_TYPE_COUNT, parent_layout, index, i);
        }

        layout.setTag("sub_menu_" + index);
        parent_layout.addView(layout);

        SetInvisible(layout);
    }

    public void ExerciseClick(View window) {
        String clicked = ((Button) window).getTag().toString();

        String menu_name = "sub_menu_" + clicked;

        ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));

        for (int i = 0; i < parent.getChildCount(); i++) {
            if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.widget.linearlayout"))
            {
                if(((LinearLayout)parent.getChildAt(i)).getTag().toString().equalsIgnoreCase(menu_name))
                {
                    HideAllMenus();
                    SetVisible(parent.getChildAt(i));
                }
            }
        }
    }

    public void OptionClick(View window) {
        String clicked = ((Button) window).getTag().toString();

        String[] seperated_name = clicked.split("_");

        if (seperated_name.length == 3) {


            String menu_name = "final_menu_" + seperated_name[1] + "_" + seperated_name[2];

            //ShowToast(menu_name);

            ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));

            for (int i = 0; i < parent.getChildCount(); i++) {
                if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.widget.linearlayout"))
                {
                    if(((LinearLayout)parent.getChildAt(i)).getTag().toString().equalsIgnoreCase(menu_name))
                    {
                        HideAllMenus();
                        SetVisible(parent.getChildAt(i));
                    }
                }
            }
        } else {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }



    private void CreateFinalMenu(int num_options, ConstraintLayout parent_layout, int upper_index, int middle_index)
    {
        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setVerticalGravity(Gravity.CENTER);


        for(int i = 1; i <= num_options; i++) {


            Button new_button = new Button(this);

            for(ActivityType current_type : ActivityType.values())
            {
                if(current_type.ordinal() == (i-1))
                {
                    new_button.setText(current_type.toString());
                }
            }


            new_button.setTag("final_" + upper_index + "_" + middle_index + "_" + i);


            new_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SenderClick(v);
                }
            });


            layout.addView(new_button);
        }

        layout.setTag("final_menu_" + upper_index + "_" + middle_index);
        parent_layout.addView(layout);

        SetInvisible(layout);
    }

    public void SetVisible(View view) {
        view.setVisibility(View.VISIBLE);
        Last_Visited.add(view);
        Current_Menu_Showing = view;
    }

    public void SetInvisible(View view) {
        view.setVisibility(View.INVISIBLE);
    }


    public enum ActivityType {
        Introduction,
        Practice,
        Test
    }

    public class AtecActivity {
        public int TaskId;
        public String TimeStarted;
        public ActivityType ActivityType;
    }

    public enum AtecCommand {
        StartTest,
        EndTest,
        StartActivity,
        EndActivity
    }
}