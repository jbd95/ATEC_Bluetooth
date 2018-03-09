package com.jb.utaheracleia.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by James Brady on 3/9/2018.
 */

public class ManageConnections implements Runnable {

    public ArrayList<View> STATUS_BUTTONS;
    public ArrayList<String> DEVICE_NAMES;

    public BluetoothAdapter bluetooth;
    public String SERVER_UUID;

    public ArrayList<BluetoothThread> BLUETOOTH_CONNECTIONS;
    public boolean IsRunning = true;
    public ColorStateList CONNECTED_COLOR = ColorStateList.valueOf(Color.RED);
    public ColorStateList DISCONNECTED_COLOR = ColorStateList.valueOf(Color.GREEN);


    public ManageConnections(ArrayList<String> device_names, ArrayList<View> status_buttons, BluetoothAdapter adapter, String uuid)
    {
        DEVICE_NAMES = device_names;
        STATUS_BUTTONS = status_buttons;
        bluetooth = adapter;
        SERVER_UUID = uuid;
        BLUETOOTH_CONNECTIONS = new ArrayList<>();

    }

    @Override
    public void run()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        SetupConnections();

        ConnectIfNecessary(BLUETOOTH_CONNECTIONS);

        SystemClock.sleep(3000);

        while(IsRunning)
        {
            ConnectIfNecessary(BLUETOOTH_CONNECTIONS);

            SystemClock.sleep(1000);

            UpdateButtonIndicators();

            SystemClock.sleep(1000);
        }

    }

    private void UpdateButtonIndicators()
    {
        for(int i = 0; i < BLUETOOTH_CONNECTIONS.size(); i++)
        {
            if(IsConnected(BLUETOOTH_CONNECTIONS.get(i)))
            {
                //ui_thread.SetColorTint(STATUS_BUTTONS.get(i), CONNECTED_COLOR);
            }
            else
            {
                //ui_thread.SetColorTint(STATUS_BUTTONS.get(i), DISCONNECTED_COLOR);
            }
        }
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

    private boolean ConnectIfNecessary(ArrayList<BluetoothThread> threads)
    {
        for(int i = 0; i < threads.size(); i++)
        {
            if (!IsConnected(threads.get(i))) {
                threads.set(i, new BluetoothThread(bluetooth, DEVICE_NAMES.get(i), SERVER_UUID));
                threads.get(i).Send("");
            }
        }
        if(IsConnected(threads))
        {
            return true;
        }
        return false;
    }

    private void SetupConnections()
    {
        for(int i = 0; i < DEVICE_NAMES.size(); i++){
            BluetoothThread bluetooth_thread = new BluetoothThread(bluetooth, DEVICE_NAMES.get(i), SERVER_UUID);
            BLUETOOTH_CONNECTIONS.add(bluetooth_thread);
        }
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
        }
        if(!IsConnected(threads))
        {
            return true;
        }
        return false;
    }


}
