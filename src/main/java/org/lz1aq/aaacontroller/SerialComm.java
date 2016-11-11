/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lz1aq.aaacontroller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import jssc.SerialPortList;
import jssc.SerialPort;
import jssc.SerialPortException;

/**
 *
 * @author Pottry
 */
public class SerialComm
{

    private SerialPort serialPort = null;
    private String serialPortName = null;
    private int baudRate;

    /**
     * @return Returns a new DefaultComboBoxModel containing all available COM
     * ports
     */
    public static DefaultComboBoxModel getComportsComboboxModel()
    {
        String[] portNames = SerialPortList.getPortNames();
        return new DefaultComboBoxModel(portNames);
    }

    /**
     * Sends command to the DLine Controller device
     *
     * @param portName
     * @param baudRate
     * @param deviceId
     * @param command
     * @throws Exception
     */
    public void sendCommand(String portName,
                            int baudRate,
                            byte deviceId,
                            byte command) throws Exception
    {

        // If no comm port has ben yet opened
        if(serialPort == null)
        {
            System.out.println("First time opening commPort");
            this.open(portName, baudRate);
        }
        // If another port needs to be used
        else if(portName.compareTo(this.serialPortName) != 0)
        {
            System.out.println("Opening another commPort");
            this.close();
            this.open(portName, baudRate);
        }

        if(this.baudRate != baudRate)
        {
            this.setBaudRate(baudRate);
        }

        serialPort.writeByte((byte) (128 + deviceId)); // Each command for the DLine controller should be preceeded by 0x80
        serialPort.writeByte(command);      // Send the actual command
        serialPort.purgePort(SerialPort.PURGE_TXCLEAR);
    }

    public void close()
    {
        if(serialPort != null)
        {
            try
            {
                serialPort.closePort();
            }
            catch(SerialPortException ex)
            {
                Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void open(String portName, int baudRate)
    {
        serialPortName = portName;

        serialPort = new SerialPort(serialPortName);
        try
        {
            serialPort.openPort();
        }
        catch(SerialPortException ex)
        {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setBaudRate(int baudRate) throws SerialPortException
    {
        serialPort.setParams(baudRate,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
    }

}
