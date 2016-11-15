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
    
    static final int ANTENNA_COUNT = 4; // The number of antennas
    
    // List of properties that are going to be saved in a file
    static final String PROP_DEVICE_ID          = "deviceId";
    static final String PROP_COMPORT            = "comPort";
    static final String PROP_BAUDE_RATE         = "baudRate";
    static final String PROP_MAIN_WINDOW_X      = "x";
    static final String PROP_MAIN_WINDOW_Y      = "y";
    static final String PROP_MAIN_WINDOW_WIDTH  = "w";
    static final String PROP_MAIN_WINDOW_HEIGHT = "h";
    
    static final String PROP_ANTENNA_NAME       = "antenna_name";
    static final String PROP_ANTENNA_SWITCHING_PERIOD  = "antenna_period";
    static final String PROP_ANTENNA_IS_CYCLED  = "atenna_is_cycled";
    static final String PROP_LAST_USED_ANTENNA  = "last_used_antenna";
    
    
    private String    comPort;
    private String    baudRate;
    private String[]  antennaName;            
    private String[]  antennaSwitchingPeriod; // String keeping integer
    private String[]  isAntennaCycled;        // String keeping boolean
    private int       lastUsedAntenna;  // A number from 1 to 4
   
    private Rectangle mainWindowDimensions; // JFrame position and size
 
    
    private final Properties prop;
    
    
    
    /**
     * Constructor 
     * 
     * - tries to read the settings from the disk. If it fails default values are used.
     */
    public AppSettings()
    { 
      antennaName            = new String[ANTENNA_COUNT];
      isAntennaCycled        = new String[ANTENNA_COUNT];
      antennaSwitchingPeriod = new String[ANTENNA_COUNT];
      
      this.prop            = new Properties();
      mainWindowDimensions = new Rectangle();
      
      this.LoadSettingsFromDisk();
    }
 
    
    public Rectangle getJFrameDimensions()
    {
        return mainWindowDimensions;
    }

    public void setJFrameDimensions(Rectangle jFrameDimensions)
    {
        this.mainWindowDimensions = jFrameDimensions;
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
    
    public void setAntennaLabel(int antennaIndex, String antLabel)
    {
      this.antennaName[antennaIndex] = antLabel;
    }
    
    public String getAntennaLabel(int antennaIndex)
    {
      return antennaName[antennaIndex];
    }
    
    public void setLastUsedAntenna(int antennaNumber)
    {
      lastUsedAntenna = antennaNumber;
    }
    
    public int getLastUsedAntenna()
    {
      return lastUsedAntenna;
    }
     
    public void setAntennaSwitchingPeriod(int antennaIndex, int periodInMs)
    {
      antennaSwitchingPeriod[antennaIndex] = Integer.toString(periodInMs);
    }
    
    public int getAntennaSwitchingPeriod(int antennaIndex)
    {
      return Integer.parseInt(antennaSwitchingPeriod[antennaIndex]);
    }
    
    public void setIsAntennaCycled(int antennaIndex, boolean isCycled)
    {
      isAntennaCycled[antennaIndex] = Boolean.toString(isCycled);
    }   
    
    public boolean getIsAntennaCycled(int antennaIndex)
    {
      return Boolean.parseBoolean(isAntennaCycled[antennaIndex]);
    }
    
    
    /**
     * Stores the array of values into properties which are named using
     * key+index of the value
     * 
     * @param key 
     * @param values 
     */
    private void setAntProperties(String key, String[] values)
    {
      for(int i=0; i<values.length; i++)
      {
        prop.setProperty(key+i, values[i]);
      }
    }
    
    
    private void getAntProperties(String key, String[] values)
    {
      for(int i=0; i<values.length; i++)
      {
        values[i] = prop.getProperty(key+i);
      }
    }
    
    /**
     * Saves the settings into a file called "DLineSettings.properties"
     */
    public void SaveSettingsToDisk()
    {
        // Store last used antenna and direction:
        prop.setProperty(PROP_COMPORT, comPort);
        prop.setProperty(PROP_BAUDE_RATE, baudRate);
     
        // Now save the JFrame dimensions:
        prop.setProperty(PROP_MAIN_WINDOW_X, Integer.toString(mainWindowDimensions.x));
        prop.setProperty(PROP_MAIN_WINDOW_Y, Integer.toString(mainWindowDimensions.y));
        prop.setProperty(PROP_MAIN_WINDOW_WIDTH, Integer.toString(mainWindowDimensions.width));
        prop.setProperty(PROP_MAIN_WINDOW_HEIGHT, Integer.toString(mainWindowDimensions.height));
        
        // Antenna names
        setAntProperties(PROP_ANTENNA_NAME, antennaName);
        // Switching periods for the antennas
        setAntProperties(PROP_ANTENNA_SWITCHING_PERIOD, antennaSwitchingPeriod);
        // isCycled for each antenna
        setAntProperties(PROP_ANTENNA_IS_CYCLED, isAntennaCycled);
        // Last used antenna
        prop.setProperty(PROP_LAST_USED_ANTENNA, Integer.toString(lastUsedAntenna));
        
        
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
            
            // CommPort
            comPort  = prop.getProperty(PROP_COMPORT);
            if(comPort == null)
                throwMissingPropertyException(PROP_COMPORT);
            baudRate = prop.getProperty(PROP_BAUDE_RATE);
            if(baudRate == null)
                throwMissingPropertyException(PROP_BAUDE_RATE);    
            
            // JFrame dimensions:
            int x = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_X));
            int y = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_Y));
            int w = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_WIDTH));
            int h = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_HEIGHT));   
            this.mainWindowDimensions = new Rectangle(x,y,w,h);
    
            // Antenna names
            getAntProperties(PROP_ANTENNA_NAME, antennaName);
            // Switching periods for the antennas
            getAntProperties(PROP_ANTENNA_SWITCHING_PERIOD, antennaSwitchingPeriod);
            // isCycled for each antenna
            getAntProperties(PROP_ANTENNA_IS_CYCLED, isAntennaCycled);
            // Last used antenna
            lastUsedAntenna = Integer.parseInt(prop.getProperty(PROP_LAST_USED_ANTENNA));
            
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
        // CommPort
        comPort = "";
        baudRate = "2400";         
        
        // We have minimum size so we don't have to worry about the values:
        mainWindowDimensions.height = 0;
        mainWindowDimensions.width = 0;
        mainWindowDimensions.x = 0;
        mainWindowDimensions.y = 0;
        
        // Antenna names
        antennaName[0] = "A Loop";
        antennaName[1] = "B Loop";
        antennaName[2] = "Dipole";
        antennaName[3] = "X Loop";
                
        // Switching periods
        for(int i=0; i<antennaSwitchingPeriod.length; i++)
        {
          antennaSwitchingPeriod[i] = Integer.toString(App.DEFAULT_SWITCHING_PERIOD_IN_MS);
        }
        
        // isAntennaCycled
        for(int i=0; i<isAntennaCycled.length; i++)
        {
          isAntennaCycled[i] = Boolean.toString(true);
        }
         
        // Last active antenna
        lastUsedAntenna = 1;
    }
    
    void throwMissingPropertyException(String propertyName) throws Exception
    {
      throw new Exception("Error when trying to read element " + propertyName + " from file " + SETTINGS_FILE_NAME);
    }
}
