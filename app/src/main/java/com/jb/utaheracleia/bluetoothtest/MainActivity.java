package com.jb.utaheracleia.bluetoothtest;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    /* CHANGE THE VALUES IN THIS SECTION BELOW TO CONNECT TO A DIFFERENT COMUPUTEr
    Set the name of the bluetooth device that android will connect to
    This device has to be paired with the android device before this program will work*/
    public ArrayList<String> COMPUTER_NAMES = new ArrayList<>();
    public String SERVER_UUID = "7A51FDC2-FDDF-4c9b-AFFC-98BCD91BF93B";

    /*CHANGE THESE VALUES TO EDIT THE SIZE OF THE MENUS*/
    final int EXERCISE_COUNT = 10;
    final int ACTIVITY_TYPE_COUNT = 4;
    int[] exercise_options = new int[EXERCISE_COUNT];
    String[] main_menu_button_text = new String[EXERCISE_COUNT];

    /*Variables for the bluetooth connection, back button, and menu creation*/
    BluetoothAdapter bluetooth;
    public View Current_Menu_Showing;
    public ArrayList<View> Last_Visited = new ArrayList<>();
    Map<String, String> exercise_names = new HashMap<String, String>();
    Map<String, String> foot_options = new HashMap<String, String>();
    ArrayList<String> Intro_Not_Included = new ArrayList<>();
    ArrayList<String> Pratice_Included = new ArrayList<>();


    ArrayList<View> Button_Indicators = new ArrayList<>();

    int success_color = Color.parseColor("#32CD32");
    int fail_color = Color.parseColor("#ffcc0000");

    ColorStateList SUCCESS_COLOR = ColorStateList.valueOf(success_color);
    ColorStateList FAIL_COLOR = ColorStateList.valueOf(fail_color);

    ColorStateList app_bar_color;

    ManageConnections BluetoothConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*code made by Android*/
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

        /*set the exercises that are in each category"
            exercise_options[0] is for the introduction which has no buttons
            exercise_options[9] is for the goodbye button
         */

        exercise_options[1] = 6;
        exercise_options[2] = 3;
        exercise_options[3] = 2;
        exercise_options[4] = 8;
        exercise_options[5] = 2;
        exercise_options[6] = 4;
        exercise_options[7] = 2;
        exercise_options[8] = 12;

        /*set the text for each of the categories*/
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

        /*set the names of the exercises in each category
            index is as follows:
                "categorynumber_exercisenumber"
         */
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

        /*set the ids that will be sent for the buttons that have left and right foot options*/
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

        Intro_Not_Included.add("2_6");
        Intro_Not_Included.add("4_2");
        Intro_Not_Included.add("9_2");
        Intro_Not_Included.add("9_4");
        Intro_Not_Included.add("9_6");
        Intro_Not_Included.add("9_8");
        Intro_Not_Included.add("9_10");
        Intro_Not_Included.add("9_12");

        Pratice_Included.add("5_1");
        Pratice_Included.add("5_3");
        Pratice_Included.add("7_2");
        Pratice_Included.add("7_3");

        //COMPUTER_NAMES.add("DESKTOP-S9K0QPK");
        COMPUTER_NAMES.add("DESKTOP-9DF7A2");

        app_bar_color = findViewById(R.id.toolbar).getBackgroundTintList();

        /* create the user interface*/
        CreateMainMenu(EXERCISE_COUNT, (ConstraintLayout)findViewById(R.id.main_stack));



        HideAllMenus();

        /*show the first menu that allows the user to connect to the computer via bluetooth*/
        SetVisible(findViewById(R.id.main));
        SetVisible(findViewById(R.id.bluetooth_indicator));

        /*check to make sure that the device has a bluetooth adapter*/
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (bluetooth == null) {
            ShowToast("Bluetooth not supported...");
        }

        /*create a prompt that allows the user to enable bluetooth if it is not turned on*/
        int REQUEST_ENABLE_BT = 5;
        if (!bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        /*bluetooth discovery drains battery and isn't necessary for the connection*/
        bluetooth.cancelDiscovery();

        for(int i = 0; i < COMPUTER_NAMES.size(); i++)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.connection_button, null);
            ((LinearLayout)findViewById(R.id.connection_button_list)).addView(view);
            Button_Indicators.add(view.findViewById(R.id.connection_button));
        }

        BluetoothConnector = new ManageConnections(COMPUTER_NAMES, Button_Indicators, bluetooth, SERVER_UUID);

        Thread t = new Thread(BluetoothConnector);
        t.start();


    }

    /*Implements functionality for the back button*/
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

    private boolean IsConnected(BluetoothThread bluetooth_thread)
    {
        if(bluetooth_thread == null) {
            return false;
        }

        if(bluetooth_thread.IsConnected())
        {
            return true;
        }

        return false;
    }

    private boolean IsConnected(ArrayList<BluetoothThread> threads)
    {
        for(BluetoothThread current : threads)
        {
            if(!IsConnected(current))
            {
                return false;
            }
        }
        return true;
    }

    private boolean SendMessage(ArrayList<BluetoothThread> threads, String message)
    {
        for(int i = 0; i < threads.size(); i++)
        {
            if(IsConnected(threads.get(i)))
            {
                threads.get(i).Send(message);
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    private boolean DisconnectConnections(ArrayList<BluetoothThread> threads)
    {
        for(int i = 0; i < threads.size(); i++)
        {
            threads.get(i).cancel();
            threads.set(i, null);
            SetColorTint(BluetoothConnector.STATUS_BUTTONS.get(i), FAIL_COLOR);
        }
        if(!IsConnected(threads))
        {
            ((ImageView)findViewById(R.id.status_bar)).setBackgroundTintList(FAIL_COLOR);
            return true;
        }
        return false;
    }

    /*function that gets called when the connect button is clicked*/
    public void ConnectClick(View window) throws InterruptedException{

        for(int i = 0; i < BluetoothConnector.BLUETOOTH_CONNECTIONS.size(); i++)
        {

            if(IsConnected(BluetoothConnector.BLUETOOTH_CONNECTIONS.get(i)))
            {
                SetColorTint(BluetoothConnector.STATUS_BUTTONS.get(i), SUCCESS_COLOR);
            }
            else
            {
                SetColorTint(BluetoothConnector.STATUS_BUTTONS.get(i), FAIL_COLOR);
            }
        }

        if(IsConnected(BluetoothConnector.BLUETOOTH_CONNECTIONS))
        {
            ((ImageView)findViewById(R.id.status_bar)).setBackgroundTintList(SUCCESS_COLOR);
            HideAllMenus();
            SetVisible(findViewById(R.id.start_activity));
        }
        else
        {
            SetColorTint(findViewById(R.id.status_bar), FAIL_COLOR);
        }
    }

    public void CollapseUIItem(View view)
    {
        view.setVisibility(View.GONE);
    }

    //function that is called when the disconnect button is clicked
    public void DisconnectClick(View window) {

        DisconnectConnections(BluetoothConnector.BLUETOOTH_CONNECTIONS);
    }

    public void ShowToast(String to_show) {
        Context context = getApplicationContext();
        CharSequence text = to_show;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void SenderClick(View window) {

        window.setEnabled(false);

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

        if((Integer.parseInt(seperated_name[3]) - 1) == ActivityType.Stop.ordinal())
        {
            command = AtecCommand.EndActivity;

            ConstraintLayout parent_window = ((ConstraintLayout)window.getParent());

           for(int i = 0; i < parent_window.getChildCount(); i++) {
                if (parent_window.getChildAt(i).getClass().getName().equalsIgnoreCase("android.support.v7.widget.AppCompatButton")) {
                    parent_window.getChildAt(i).setEnabled(true);
                }
            }
        }

        if (seperated_name.length == 4) {

            if(SendMessage(BluetoothConnector.BLUETOOTH_CONNECTIONS, "[" + command.ordinal() + "," + converter.GetJsonString(temp_activity) + "]"))
            {
                ResetBluetoothIndicator();
                for(int i = 0; i < BluetoothConnector.BLUETOOTH_CONNECTIONS.size(); i++)
                {

                    if(IsConnected(BluetoothConnector.BLUETOOTH_CONNECTIONS.get(i)))
                    {
                        SetColorTint(BluetoothConnector.STATUS_BUTTONS.get(i), window.getBackgroundTintList());
                    }
                    else
                    {
                        SetColorTint(BluetoothConnector.STATUS_BUTTONS.get(i), window.getBackgroundTintList());

                    }
                }
                ((ImageView)findViewById(R.id.status_bar)).setBackgroundTintList(window.getBackgroundTintList());

            }
        } else {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }

    private void SendIntroduction()
    {
        if(SendMessage(BluetoothConnector.BLUETOOTH_CONNECTIONS, "[0]"))
        {
            ShowToast("Message Sent...");
        }
        else
        {
            ShowToast("Message failed to send...");
        }
    }

    private void SendGoodbye()
    {
        if(SendMessage(BluetoothConnector.BLUETOOTH_CONNECTIONS, "[3]"))
        {
            ShowToast("Message Sent...");
        }
        else
        {
            ShowToast("Message failed to send...");
        }
    }

    public void StartTest(View view)
    {
        if(!SendMessage(BluetoothConnector.BLUETOOTH_CONNECTIONS, "[" + AtecCommand.StartTest.ordinal() + "]"))
        {
            ShowToast("Message failed to send...");
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

        if(!SendMessage(BluetoothConnector.BLUETOOTH_CONNECTIONS, "[" + AtecCommand.EndTest.ordinal() + "]"))
        {
            ShowToast("Message failed to send...");
        }
    }

    public void GoBack() {
        ChangeAppBarColor(app_bar_color);
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
        /*LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setVerticalGravity(Gravity.CENTER);

        for(int i = 1; i <= num_options; i++) {
            Button new_button = new Button(this);

            new_button.setText(main_menu_button_text[i - 1]);
            new_button.setTag(i + "");


            layout.addView(new_button);


            if(i != 0 || i != num_options) {
                new_button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ExerciseClick(v);
                    }
                });
            }
            else
            {
                if(i == 0)
                {
                    new_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SendIntroduction();
                        }
                    });
                }
                else
                {
                    new_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SendGoodbye();
                        }
                    });
                }
            }

            CreateSubMenu(exercise_options[i - 1], parent_layout, i);
        }
        layout.setTag("first_menu");
        parent_layout.addView(layout);

        HideAllMenus();
        Last_Visited = new ArrayList<>();*/

        View view = LayoutInflater.from(this).inflate(R.layout.exercise_category_menu, null);

        for(int i = 1; i <= num_options; i++) {
            ConstraintLayout new_button_container = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.exercise_category_button, null);

            ((Button)new_button_container.findViewById(R.id.category_button)).setText(main_menu_button_text[i - 1]);
            ((Button)new_button_container.findViewById(R.id.category_button)).setTag(i + "");


            ((LinearLayout)view.findViewById(R.id.category_container)).addView(new_button_container);


            if(i != 0 || i != num_options) {
                ((Button)new_button_container.findViewById(R.id.category_button)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ExerciseClick(v);
                    }
                });
            }
            else
            {
                if(i == 0)
                {
                    ((Button)new_button_container.findViewById(R.id.category_button)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SendIntroduction();
                        }
                    });
                }
                else
                {
                    ((Button)new_button_container.findViewById(R.id.category_button)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            SendGoodbye();
                        }
                    });
                }
            }

            CreateSubMenu(exercise_options[i - 1], parent_layout, i);
        }

        parent_layout.addView(view);
        view.setTag("first_menu");

        ConstraintSet set = new ConstraintSet();
        set.clone(parent_layout);
        set.centerHorizontally(view.getId(), parent_layout.getId());
        set.applyTo(parent_layout);

        HideAllMenus();
        Last_Visited = new ArrayList<>();

    }
    private void CreateSubMenu(int num_options, ConstraintLayout parent_layout, int index)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.exercise_sub_menu, null);

        view.setTag("sub_menu_" + index);

        for(int i = 1; i <= num_options; i++) {


            ConstraintLayout new_button_container = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.exercise_sub_menu_button, null);

            ((Button)new_button_container.findViewById(R.id.option_button)).setTag("middle_" + index + "_" + i);
            ((Button)new_button_container.findViewById(R.id.option_button)).setText(exercise_names.get(index + "_" + i));

            ((LinearLayout)view.findViewById(R.id.container)).addView(new_button_container);


            CreateFinalMenu(ACTIVITY_TYPE_COUNT, parent_layout, index, i);
        }

        parent_layout.addView(view);

        /*ConstraintSet set = new ConstraintSet();
        set.clone(parent_layout);
        //set.centerHorizontally(view.getId(), parent_layout.getId());
        //set.connect(view.getId(), ConstraintSet.TOP, parent_layout.getId(), ConstraintSet.TOP);
        //set.connect(view.getId(), ConstraintSet.BOTTOM, parent_layout.getId(), ConstraintSet.BOTTOM);
       // set.centerVertically(view.getId(), parent_layout.getId());

        set.applyTo(parent_layout);*/


        SetInvisible(view);

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

            ConstraintLayout parent = ((ConstraintLayout) findViewById(R.id.main_stack));

            for (int i = 0; i < parent.getChildCount(); i++) {

                if(parent.getChildAt(i).getClass().getName().equalsIgnoreCase("android.support.constraint.ConstraintLayout"))
                {
                    if(parent.getChildAt(i).getTag() != null) {
                        if ((parent.getChildAt(i)).getTag().toString().equalsIgnoreCase(menu_name)) {
                            HideAllMenus();
                            SetVisible(parent.getChildAt(i));
                        }
                    }
                }
            }
        } else {
            ShowToast("An error occurred when reading the button's tag....");
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void CreateFinalMenu(int num_options, ConstraintLayout parent_layout, int upper_index, int middle_index)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.final_menu_template, null);

        view.setTag("final_menu_" + upper_index + "_" + middle_index);

        ((TextView)view.findViewById(R.id.exercise_label)).setText(exercise_names.get(upper_index + "_" + middle_index));
        view.findViewById(R.id.intro).setTag("final_" + upper_index + "_" + middle_index + "_1");
        view.findViewById(R.id.practice).setTag("final_" + upper_index + "_" + middle_index + "_2");
        view.findViewById(R.id.test).setTag("final_" + upper_index + "_" + middle_index + "_3");
        view.findViewById(R.id.stop).setTag("final_" + upper_index + "_" + middle_index + "_4");


        parent_layout.addView(view);


        if(Intro_Not_Included.contains(upper_index + "_" + middle_index))
        {
            view.findViewById(R.id.intro).setVisibility(View.GONE);
        }
        if(Pratice_Included.contains(upper_index + "_" + middle_index))
        {
            view.findViewById(R.id.practice).setVisibility(View.VISIBLE);
        }

       ConstraintSet set = new ConstraintSet();
       set.clone(parent_layout);
       set.centerHorizontally(view.getId(), parent_layout.getId());
       set.centerVertically(view.getId(), parent_layout.getId());
       set.applyTo(parent_layout);

        SetInvisible(view);
    }

    public void ChangeAppBarColor(ColorStateList color)
    {
        findViewById(R.id.toolbar).setBackgroundTintList(color);

    }

    public void SetVisible(View view) {
        view.setVisibility(View.VISIBLE);
        if (view.getTag() != null) {
            if (!view.getTag().toString().equalsIgnoreCase("omit")) {
                Last_Visited.add(view);
                Current_Menu_Showing = view;
            }
        }
        else
        {
            Last_Visited.add(view);
            Current_Menu_Showing = view;
        }

        ResetBluetoothIndicator();
        if (view.getTag() != null) {
            if (view.getTag().toString().equalsIgnoreCase("first_menu")) {

                CollapseBluetoothIndicator();
                CollapseUIItem(findViewById(R.id.connection_label));
                ((TextView)(findViewById(R.id.connection_label))).setText("Connection Status");
            }
            else if (view.getTag().toString().contains("sub_menu")) {
                CollapseBluetoothIndicator();
                CollapseUIItem(findViewById(R.id.connection_label));
                ((TextView)(findViewById(R.id.connection_label))).setText("Connection Status");
            }
            else if(view.getTag().toString().contains("final_menu"))
            {
                CollapseBluetoothIndicator();
                CollapseUIItem(findViewById(R.id.connection_label));
                (findViewById(R.id.status_bar)).setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                ((TextView)(findViewById(R.id.connection_label))).setText("Activity Status");
            }
        }
    }

    public void ResetBluetoothIndicator() {
        for (View current : Button_Indicators) {
            current.setVisibility(View.VISIBLE);
        }
        (findViewById(R.id.connection_label)).setVisibility(View.VISIBLE);

        if (BluetoothConnector != null) {
            for (int i = 0; i < BluetoothConnector.BLUETOOTH_CONNECTIONS.size(); i++) {
                if (IsConnected(BluetoothConnector.BLUETOOTH_CONNECTIONS.get(i))) {
                    SetColorTint(Button_Indicators.get(i), SUCCESS_COLOR);
                } else {
                    SetColorTint(Button_Indicators.get(i), FAIL_COLOR);
                }
            }
            if (IsConnected(BluetoothConnector.BLUETOOTH_CONNECTIONS)) {
                (findViewById(R.id.status_bar)).setBackgroundTintList(SUCCESS_COLOR);
            } else {
                (findViewById(R.id.status_bar)).setBackgroundTintList(FAIL_COLOR);
            }
        }
    }
    public void CollapseBluetoothIndicator()
    {
        for(View current : Button_Indicators)
        {
            CollapseUIItem(current);
        }
    }

    public void SetInvisible(View view) {


        if (view.getTag() != null) {
            if (!view.getTag().toString().equalsIgnoreCase("omit")) {
                view.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public void SetColorTint(View view, ColorStateList color)
    {
        view.setBackgroundTintList(color);
    }

    public enum ActivityType {
        Introduction,
        Practice,
        Test,
        Stop
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

    public class IncludedCommands
    {
        public boolean IncludeIntro;
        public boolean IncludePractice;
        public boolean IncludeTest;
        public boolean IncludeStop;

        public IncludedCommands()
        {
            IncludeIntro = true;
            IncludePractice = true;
            IncludeTest = true;
            IncludeStop = true;
        }
    }
}