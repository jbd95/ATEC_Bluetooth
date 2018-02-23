package com.jb.utaheracleia.bluetoothtest;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothSend extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream

    public BluetoothSend(BluetoothSocket socket, String message) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        write(message);
    }

    // Call this from the main activity to send data to the remote device.
    public void write(String message) {

        try {
            mmOutStream.write(message.getBytes());
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Error occurred when sending data", e);

        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("bluetoothmsg", "Could not close the connect socket", e);
        }
    }
}
