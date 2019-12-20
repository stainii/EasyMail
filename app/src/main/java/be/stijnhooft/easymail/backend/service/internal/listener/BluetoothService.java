package be.stijnhooft.easymail.backend.service.internal.listener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import be.stijnhooft.easymail.EasyMailApplication;
import be.stijnhooft.easymail.backend.model.Mail;
import be.stijnhooft.easymail.backend.model.Person;
import be.stijnhooft.easymail.backend.repository.SettingRepository;

public class BluetoothService implements MailListener {

    private final boolean enabled;
    private final String deviceName;
    private final String turnOffSignal;
    private final Map<String, String> personNameAndBluetoothOnSignal;

    public BluetoothService() {
        this(new SettingRepository(EasyMailApplication.getInstance()));
    }

    public BluetoothService(SettingRepository settingRepository) {
        try {
            JSONObject settings = settingRepository.getSettings();
            JSONObject bluetoothSettings = settings
                    .getJSONObject("notifications")
                    .getJSONObject("bluetooth");
            this.enabled = bluetoothSettings.getBoolean("enabled");
            this.deviceName = bluetoothSettings.getString("deviceName");
            this.turnOffSignal = bluetoothSettings.getString("turnOffSignal");

            // create map of person name and corresponding bluetooth on signal
            personNameAndBluetoothOnSignal = new HashMap<>();
            JSONArray contacts = settings.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                final JSONObject person = contacts.getJSONObject(i);
                personNameAndBluetoothOnSignal.put(person.getString("name"), person.getString("bluetoothTurnOnSignal"));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Could not read bluetooth settings.", e);
        }
    }

    @Override
    public void onNewMail(Mail mail, Person sender) {
        if (enabled) {
            String bluetoothOnSignal = personNameAndBluetoothOnSignal.get(sender.getName());
            sendBluetoothSignal(bluetoothOnSignal);
        }
    }

    public void onEverythingRead() {
        sendBluetoothSignal(turnOffSignal);
    }

    private void sendBluetoothSignal(String signal) {
        try {
            BluetoothSocket bluetoothSocket = connect();
            bluetoothSocket.getOutputStream().write(signal.getBytes());
            disconnect(bluetoothSocket);
        } catch (Exception e) {
            Log.e("BluetoothService", "Could not send bluetooth signal", e);
        }
    }

    private void disconnect(BluetoothSocket bluetoothSocket) throws IOException {
        bluetoothSocket.getInputStream().close();
        bluetoothSocket.getOutputStream().close();
        bluetoothSocket.close();
    }

    private BluetoothSocket connect() throws Exception {
        enableBluetooth();
        BluetoothDevice bluetoothDevice = findBluetoothDevice();


        BluetoothSocket bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass()
                .getMethod("createRfcommSocket", int.class)
                .invoke(bluetoothDevice, 1);
        if (!bluetoothSocket.isConnected()) {
            bluetoothSocket.connect();
        }
        return bluetoothSocket;
    }

    private BluetoothDevice findBluetoothDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Optional<String> address = bluetoothAdapter.getBondedDevices()
                .stream()
                .filter(device -> device.getName().contains(deviceName))
                .map(BluetoothDevice::getAddress)
                .findAny();

        if (address.isPresent()) {
            bluetoothAdapter.cancelDiscovery();
            return bluetoothAdapter.getRemoteDevice(address.get());
        } else {
            throw new RuntimeException("No bonded bluetooth device called " + deviceName + " found!");
        }
    }

    private void enableBluetooth() throws InterruptedException {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            Thread.sleep(1000L);
        }
    }


}
