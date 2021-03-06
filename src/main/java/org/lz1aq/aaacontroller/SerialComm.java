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
    private String portName = null;

    
    /**
     * @return Returns a new DefaultComboBoxModel containing all available COM
     * ports
     */
    public static DefaultComboBoxModel getComportsComboboxModel()
    {
        String[] portNames = SerialPortList.getPortNames();
        return new DefaultComboBoxModel(portNames);
    }
    

    public void open(String portName)
    {
        // First try to close currently open CommPort
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
         
        this.portName = portName;

        serialPort = new SerialPort(this.portName);
        try
        {
            serialPort.openPort();
        }
        catch(SerialPortException ex)
        {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }

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
      
    
    /**
     * Sends command to the serial port to activate certain antenna.
     * 
     * @param ant Antenna that we would like to switch (Value of ANT_1 to ANT_4
     * @return String describing the command being sent to the Comm port or any
     * eventual errors
     */
    public String setAntenna(int ant)
    {

        if(serialPort == null)
        {
            return "ComPort not open!";
        }
        /* The following commands are sent to the CommPort depending on the ant
         DTR | RTS
         -----|-----
         ANT_1(Dipole)  0  |  0
         ANT_2(A Loop)  1  |  0
         ANT_3(B Loop)  0  |  1
         ANT_4(X Loop)  1  |  1
         */
        try
        {
            switch (ant)
            {
                case App.ANT_1:
                    serialPort.setDTR(false);
                    serialPort.setRTS(false);
                    return "DTR=0, RTS=0";

                case App.ANT_2:
                    serialPort.setDTR(true);
                    serialPort.setRTS(false);
                    return "DTR=1, RTS=0";

                case App.ANT_3:
                    serialPort.setDTR(false);
                    serialPort.setRTS(true);
                    return "DTR=0, RTS=1";

                case App.ANT_4:
                    serialPort.setDTR(true);
                    serialPort.setRTS(true);
                    return "DTR=1, RTS=1";
                default:
                    return "Unknown antenna number!";
            }
        }
        catch (SerialPortException ex)
        {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
            return ex.toString();
        }
    }
        
    
    public String getName()
    {
        return portName;
    }
}
