package com.via.letmein.ui.register;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class IPListenAsyncTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "IPListen async";
    public static final int PORT = 8085;
    public static final String PAYLOAD = "REAC";
    private final RegisterActivity registerActivity;

    public IPListenAsyncTask(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        DatagramSocket socket;

        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            socket = new DatagramSocket(PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            Log.i(TAG, "starting");

            while (true) {
                Log.i(TAG, "listening");
                //Receive a packet
                byte[] receiveBuffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(packet);
                //Packet received
                String payload = new String(packet.getData()).trim();

                if (payload.equals(PAYLOAD))
                    return packet.getAddress().toString();
            }
        } catch (IOException ex) {
            Log.i(TAG, "Oops" + ex.getMessage());
            return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        registerActivity.onIpReceived(s);
    }
}
