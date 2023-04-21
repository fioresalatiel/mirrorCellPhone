package org.example;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        conectarAparelho();
    }


    public static void conectarAparelho() throws Exception {
        AndroidDebugBridge.init(false);

        AndroidDebugBridge debugBridge = AndroidDebugBridge.createBridge("src/main/resources/platform-tools/adb.exe", true);
        if (debugBridge == null) {
            System.err.println("Invalid ADB location.");
            System.exit(1);
        }

        AndroidDebugBridge.addDeviceChangeListener(new AndroidDebugBridge.IDeviceChangeListener() {

            @Override
            public void deviceChanged(IDevice device, int arg1) {
                // not implement
            }

            @Override
            public void deviceConnected(IDevice device) {
                System.out.println(String.format("%s connected", device.getSerialNumber()));
                try {
                    espelhar(device.getSerialNumber());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void deviceDisconnected(IDevice device) {
                System.out.println(String.format("%s disconnected", device.getSerialNumber()));

            }
        });

        System.out.println("Press enter to exit.");
        System.in.read();
    }

    public static void espelhar(String serialNumber) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("scrcpy", "-s", serialNumber);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


}