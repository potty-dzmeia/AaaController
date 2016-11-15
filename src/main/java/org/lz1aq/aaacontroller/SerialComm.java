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
     * @param ant Value of ANT_1 to ANT_4
     */
    public void setAntenna(int ant)
    {
        if(serialPort == null)
        {
            System.err.println("Open come port first!");
        }
        /* The following commands are sent to the CommPort depending on the ant
                      DTR | RTS
                     -----|-----
        ANT_1(A Loop)  0  |  0
        ANT_2(B Loop)  1  |  0
        ANT_3(Dipole)  0  |  1
        ANT_4(X Loop)  1  |  1
        */
        try
        {
            switch(ant)
            {
                case App.ANT_1:
                    serialPort.setDTR(false);
                    serialPort.setRTS(false);
                    break;

                case App.ANT_2:
                    serialPort.setDTR(true);
                    serialPort.setRTS(false);
                    break;

                case App.ANT_3:
                    serialPort.setDTR(false);
                    serialPort.setRTS(true);
                    break;

                case App.ANT_4:
                    serialPort.setDTR(true);
                    serialPort.setRTS(true);
                    break;
            }
        } 
        catch(SerialPortException ex)
        {
            Logger.getLogger(SerialComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    
    public String getName()
    {
        return serialPortName;
    }
}
