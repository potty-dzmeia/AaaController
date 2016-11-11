package org.lz1aq.aaacontroller;


import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Chavdar
 */
public final class AppSettings 
{
    static final String SETTINGS_FILE_NAME          = "Settings.properties";
    static final String PROPERTY_DEVICE_ID          = "deviceId";
    static final String PROPERTY_COMPORT            = "comPort";
    static final String PROPERTY_BAUDE_RATE         = "baudRate";
    static final String PROPERTY_MAIN_WINDOW_X      = "x";
    static final String PROPERTY_MAIN_WINDOW_Y      = "y";
    static final String PROPERTY_MAIN_WINDOW_WIDTH  = "w";
    static final String PROPERTY_MAIN_WINDOW_HEIGHT = "h";
    static final String PROPERTY_LABEL_ANT1         = "ant1";
    static final String PROPERTY_LABEL_ANT2         = "ant2";
    static final String PROPERTY_LABEL_ANT3         = "ant3";
    static final String PROPERTY_LABEL_ANT4         = "ant4"; 

    private String  deviceId;   // This is used when composing the command send thru the serial interface
    private String  comPort;
    private String  baudRate;
    private String  labelAnt1;
    private String  labelAnt2;
    private String  labelAnt3;
    private String  labelAnt4;
    private Rectangle jFrameDimensions; // JFrame settings: position and size
    
    private final Properties prop;
    
    
    
    /**
     * Tries to read the settings from the disk. If it fails default values are used.
     */
    public AppSettings()
    {     
        this.prop        = new Properties();
        jFrameDimensions = new Rectangle();
        this.LoadSettingsFromDisk();
    }

    
    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
    
    public Rectangle getJFrameDimensions()
    {
        return jFrameDimensions;
    }

    public void setJFrameDimensions(Rectangle jFrameDimensions)
    {
        this.jFrameDimensions = jFrameDimensions;
    }

    public String getComPort()
    {
        return comPort;
    }

    public void setComPort(String comPort)
    {
        this.comPort = comPort;
    }

    public String getBaudRate()
    {
        return baudRate;
    }

    public void setBaudRate(String baudRate)
    {
        this.baudRate = baudRate;
    }   
    
    public void setLabelAnt1(String labelAnt1)
    {
      this.labelAnt1 = labelAnt1;
    }
    public String getLabelAnt1()
    {
      return labelAnt1;
    }
    
    public void setLabelAnt2(String labelAnt2)
    {
      this.labelAnt2 = labelAnt2;
    }
    public String getLabelAnt2()
    {
      return labelAnt2;
    }
    
    public void setLabelAnt3(String labelAnt3)
    {
      this.labelAnt3 = labelAnt3;
    }
    public String getLabelAnt3()
    {
      return labelAnt3;
    }
    
    public void setLabelAnt4(String labelAnt4)
    {
      this.labelAnt4 = labelAnt4;
    }
    public String getLabelAnt4()
    {
      return labelAnt4;
    }
    
    /**
     * Saves the settings into a file called "DLineSettings.properties"
     */
    public void SaveSettingsToDisk()
    {
        // Store last used antenna and direction:
        prop.setProperty(PROPERTY_DEVICE_ID, deviceId);
        prop.setProperty(PROPERTY_COMPORT, comPort);
        prop.setProperty(PROPERTY_BAUDE_RATE, baudRate);
     
        // Now save the JFrame dimensions:
        prop.setProperty(PROPERTY_MAIN_WINDOW_X, Integer.toString(jFrameDimensions.x));
        prop.setProperty(PROPERTY_MAIN_WINDOW_Y, Integer.toString(jFrameDimensions.y));
        prop.setProperty(PROPERTY_MAIN_WINDOW_WIDTH, Integer.toString(jFrameDimensions.width));
        prop.setProperty(PROPERTY_MAIN_WINDOW_HEIGHT, Integer.toString(jFrameDimensions.height));
        
        // Texts for antena types
        prop.setProperty(PROPERTY_LABEL_ANT1, labelAnt1);
        prop.setProperty(PROPERTY_LABEL_ANT2, labelAnt2);
        prop.setProperty(PROPERTY_LABEL_ANT3, labelAnt3);
        prop.setProperty(PROPERTY_LABEL_ANT4, labelAnt4);
        
        try
        {
            prop.store(new FileOutputStream(SETTINGS_FILE_NAME), null);
        } catch (IOException ex)
        {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    
    /**
     * Loads the settings from a file called DLineSettings.properties
     */
    public void LoadSettingsFromDisk()
    {
        try
        {
            
            prop.load(new FileInputStream(SETTINGS_FILE_NAME));
            
            deviceId = prop.getProperty(PROPERTY_DEVICE_ID);
            if(deviceId == null)
                throwMissingPropertyException(PROPERTY_DEVICE_ID);
            
            comPort  = prop.getProperty(PROPERTY_COMPORT);
            if(comPort == null)
                throwMissingPropertyException(PROPERTY_COMPORT);
                
            baudRate = prop.getProperty(PROPERTY_BAUDE_RATE);
            if(baudRate == null)
                throwMissingPropertyException(PROPERTY_BAUDE_RATE);    
            
            
            // Read the JFrame dimensions:
            int x = Integer.parseInt(prop.getProperty(PROPERTY_MAIN_WINDOW_X));
            int y = Integer.parseInt(prop.getProperty(PROPERTY_MAIN_WINDOW_Y));
            int w = Integer.parseInt(prop.getProperty(PROPERTY_MAIN_WINDOW_WIDTH));
            int h = Integer.parseInt(prop.getProperty(PROPERTY_MAIN_WINDOW_HEIGHT));
            
            this.jFrameDimensions = new Rectangle(x,y,w,h);
    
            
            //Read the antenna type texts
            labelAnt1  = prop.getProperty(PROPERTY_LABEL_ANT1);
            if(labelAnt1 == null)
                throwMissingPropertyException(PROPERTY_LABEL_ANT1);
            labelAnt2 = prop.getProperty(PROPERTY_LABEL_ANT2);
            if(labelAnt2 == null)
                throwMissingPropertyException(PROPERTY_LABEL_ANT2);
            labelAnt3 = prop.getProperty(PROPERTY_LABEL_ANT3);
            if(labelAnt3 == null)
                throwMissingPropertyException(PROPERTY_LABEL_ANT3);
            labelAnt4 = prop.getProperty(PROPERTY_LABEL_ANT4);
            if(labelAnt4 == null)
                throwMissingPropertyException(PROPERTY_LABEL_ANT4);
                    
        } catch (IOException ex)
        {
            // If some error we will set to default values
            this.SetSettingsToDefault();
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NumberFormatException ex)
        {
            // If some error we will set to default values
            this.SetSettingsToDefault();
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex)
        {
            this.SetSettingsToDefault();
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);    
        }
                    
    }
    
    /**
     * Set all settings to default
     */
    private void SetSettingsToDefault()
    {
        deviceId = "0";
        comPort = "";
        baudRate = "2400";         
        
        // We have minimum size so we don't have to worry about the values:
        jFrameDimensions.height = 0;
        jFrameDimensions.width = 0;
        jFrameDimensions.x = 0;
        jFrameDimensions.y = 0;
        
        // Set texts for the antenna types
        labelAnt1 = "A Loop";
        labelAnt2 = "B Loop";
        labelAnt3 = "Dipole";
        labelAnt4 = "X Loop";
                
    }
    
    void throwMissingPropertyException(String propertyName) throws Exception
    {
      throw new Exception("Error when trying to read element " + propertyName + " from file " + SETTINGS_FILE_NAME);
    }
}
