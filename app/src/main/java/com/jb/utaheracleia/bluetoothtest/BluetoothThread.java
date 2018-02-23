package com.jb.utaheracleia.bluetoothtest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by junio on 2/13/2018.
 */

public class BluetoothThread extends Thread {
    public final BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice = null;
    public BluetoothSend sender = null;

    BluetoothAdapter bluetooth;

    public BluetoothThread(BluetoothAdapter adapter, String device_name, String uuid) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.

        bluetooth = adapter;
        BluetoothSocket tmp = null;

        UUID Server_UUID = UUID.fromString(uuid);

        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                if(device.getName().equals(device_name))
                {
                    mmDevice = device;
                }
            }
        }

        if(mmDevice != null)
        {
            Log.d("bluetoothmsg", "mmdevice is not null");
        }
        else {
            Log.d("bluetoothmsg", "mmdevice is null");
        }
        try {

            tmp = mmDevice.createRfcommSocketToServiceRecord(Server_UUID);
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run(String message) {
        // Cancel discovery because it otherwise slows down the connection.
        bluetooth.cancelDiscovery();

        if(mmSocket.isConnected())
        {
            sender = new BluetoothSend(mmSocket, message);
            return;

        }

        Log.d("bluetoothmsg", "trying to connect now");
        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("bluetoothmsg", "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.

        sender = new BluetoothSend(mmSocket, message);



        Log.d("bluetoothmsg", "success");
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Could not close the client socket", e);
        }
    }


}
