package com.fangyi.businessthrough.bluetoothprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/**
 * Created by FANGYI on 2016/10/23.
 */

public class PrintDataService {
    private String deviceAddress = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isConnection = false;

    public PrintDataService() {
        super();
        Set<BluetoothDevice> bondedDevices= bluetoothAdapter.getBondedDevices();
        if(bondedDevices!=null && bondedDevices.size()>0)
        {
            for(BluetoothDevice dev:bondedDevices)
            {

                int majorClass=dev.getBluetoothClass().getMajorDeviceClass();
                if(majorClass==7936 || majorClass==1536)
                {
                    this.deviceAddress = dev.getAddress();
                    break;
                }
            }
        }
        if(this.deviceAddress!=null)
            this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);


    }



    /**
     * 获取设备名称
     *
     * @return String
     */
    public String getDeviceName() {
        return this.device.getName();
    }

    /**
     * 蓝牙设备是否打开
     * @return
     */
    public boolean isBluetoothEnable()
    {
        return bluetoothAdapter.isEnabled();
    }
    /**
     * 是否打印机配对
     * @return
     */
    public boolean isPairedPrinter()
    {
        if(this.device==null)
            return false;
        else
            return true;
    }
    /**
     * 连接蓝牙设备
     */
    public String connect() {
        if (!this.isConnection) {
            try {
                if(!bluetoothAdapter.isEnabled())
                    return "蓝牙未开启,请打开蓝牙！";
                if(this.device==null)
                    return "蓝牙打印机未进行配对！";
                bluetoothSocket = this.device
                        .createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                this.isConnection = true;
                if (this.bluetoothAdapter.isDiscovering()) {
                    System.out.println("关闭适配器！");
                    this.bluetoothAdapter.isDiscovering();
                }
            } catch (Exception e) {
                return "打印机连接失败！";
            }

            return null;
        } else {
            return null;
        }
    }

    /**
     * 断开蓝牙设备连接
     */
    public  void disconnect() {

        try {
            bluetoothSocket.close();
            outputStream.close();
            this.isConnection=false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 发送打印命令
     * @param command
     * @return
     */
    public  String setCommand(byte[] command)
    {
        if (isConnection) {
            try {
                outputStream.write(command);
                outputStream.flush();

            } catch (IOException e) {
                return "打印失败";
            }
            return null;
        } else {
            return "设备未连接，请重新连接！";
        }
    }
    /**
     * 发送数据
     */
    public String send(String sendData) {
        if (this.isConnection) {
            try {
                byte[] data = sendData.getBytes("gbk");
                outputStream.write(data, 0, data.length);
                outputStream.flush();
            } catch (IOException e) {
                return "打印失败！";

            }
            return null;
        } else {
            return "设备未连接，请重新连接！";

        }
    }
}
