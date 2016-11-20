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
    static final String SETTINGS_FILE_NAME  = "Settings.properties";
    
    static final int ANTENNA_COUNT = 4; // The number of antennas
    
    // List of properties that are going to be saved in a file
    private static final String PROP_COMPORT            = "comPort";
    private static final String PROP_MAIN_WINDOW_X      = "x";
    private static final String PROP_MAIN_WINDOW_Y      = "y";
    private static final String PROP_MAIN_WINDOW_WIDTH  = "w";
    private static final String PROP_MAIN_WINDOW_HEIGHT = "h";
    
    private static final String PROP_ANTENNA_NAME             = "antenna_name";
    private static final String PROP_ANTENNA_SWITCHING_PERIOD = "antenna_period";
    private static final String PROP_ANTENNA_IS_CHECKMARKED   = "atenna_is_checkmarked";
    private static final String PROP_LAST_USED_ANTENNA        = "last_used_antenna";
    
    private String    comPort;
    private String    baudRate;
    private String[]  arrayAntennaName;            
    private String[]  arrayAntennaSwitchingPeriod;  // String keeping integer
    private String[]  arrayIsAntennaCheckmarked;    // String keeping boolean
    private int       lastUsedAntenna;              // A number from 1 to 4
   
    private Rectangle mainWindowDimensions; // JFrame position and size

    private final Properties prop;
    
    
    
    /**
     * Constructor 
     * 
     * - tries to read the settings from the disk. If it fails default values are used.
     */
    public AppSettings()
    { 
      arrayAntennaName            = new String[ANTENNA_COUNT];
      arrayIsAntennaCheckmarked   = new String[ANTENNA_COUNT];
      arrayAntennaSwitchingPeriod = new String[ANTENNA_COUNT];
      
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
      this.arrayAntennaName[antennaIndex] = antLabel;
    }
    
    public String getAntennaLabel(int antennaIndex)
    {
      return arrayAntennaName[antennaIndex];
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
      arrayAntennaSwitchingPeriod[antennaIndex] = Integer.toString(periodInMs);
    }
    
    public int getAntennaSwitchingPeriod(int antennaIndex)
    {
      return Integer.parseInt(arrayAntennaSwitchingPeriod[antennaIndex]);
    }
    
    public void setIsAntennaCheckmarked(int antennaIndex, boolean isChecked)
    {
      arrayIsAntennaCheckmarked[antennaIndex] = Boolean.toString(isChecked);
    }   
    
    public boolean getIsAntennaCheckmarked(int antennaIndex)
    {
      return Boolean.parseBoolean(arrayIsAntennaCheckmarked[antennaIndex]);
    }
    
    
    /**
     * Stores the array of values into properties which are named using
     * key+index of the value
     * 
     * @param key - property key
     * @param values - array of values that is going to be stored
     */
    private void setProperties(String key, String[] values)
    {
      for(int i=0; i<values.length; i++)
      {
        prop.setProperty(key+i, values[i]);
      }
    }
    
    
    /**
     * Reads multiple properties that were written using the function setProperties()
     * 
     * @param key - Property key
     * @param values - array where the properties will be written
     */
    private void getProperties(String key, String[] values)
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
     
        // Now save the JFrame dimensions:
        prop.setProperty(PROP_MAIN_WINDOW_X, Integer.toString(mainWindowDimensions.x));
        prop.setProperty(PROP_MAIN_WINDOW_Y, Integer.toString(mainWindowDimensions.y));
        prop.setProperty(PROP_MAIN_WINDOW_WIDTH, Integer.toString(mainWindowDimensions.width));
        prop.setProperty(PROP_MAIN_WINDOW_HEIGHT, Integer.toString(mainWindowDimensions.height));
        
        // Antenna names
        setProperties(PROP_ANTENNA_NAME, arrayAntennaName);
        // Switching periods for the antennas
        setProperties(PROP_ANTENNA_SWITCHING_PERIOD, arrayAntennaSwitchingPeriod);
        // isCycled for each antenna
        setProperties(PROP_ANTENNA_IS_CHECKMARKED, arrayIsAntennaCheckmarked);
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
            
            // JFrame dimensions:
            int x = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_X));
            int y = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_Y));
            int w = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_WIDTH));
            int h = Integer.parseInt(prop.getProperty(PROP_MAIN_WINDOW_HEIGHT));   
            this.mainWindowDimensions = new Rectangle(x,y,w,h);
    
            // Antenna names
            getProperties(PROP_ANTENNA_NAME, arrayAntennaName);
            // Switching periods for the antennas
            getProperties(PROP_ANTENNA_SWITCHING_PERIOD, arrayAntennaSwitchingPeriod);
            // isCycled for each antenna
            getProperties(PROP_ANTENNA_IS_CHECKMARKED, arrayIsAntennaCheckmarked);
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
        comPort  = "";
        baudRate = "2400";         
        
        // We have minimum size so we don't have to worry about the values:
        mainWindowDimensions.height = 0;
        mainWindowDimensions.width  = 0;
        mainWindowDimensions.x = 0;
        mainWindowDimensions.y = 0;
        
        // Antenna names
        arrayAntennaName[0] = "Dipole";
        arrayAntennaName[1] = "A Loop";
        arrayAntennaName[2] = "B Loop";
        arrayAntennaName[3] = "X Loop";
                
        // Switching periods
        for(int i=0; i<arrayAntennaSwitchingPeriod.length; i++)
        {
          arrayAntennaSwitchingPeriod[i] = Integer.toString(App.DEFAULT_SWITCHING_PERIOD_IN_MS);
        }
        
        // isAntennaCheckmarked
        for(int i=0; i<arrayIsAntennaCheckmarked.length; i++)
        {
          arrayIsAntennaCheckmarked[i] = Boolean.toString(true);
        }
         
        // Last active antenna
        lastUsedAntenna = 0;
    }
    
    void throwMissingPropertyException(String propertyName) throws Exception
    {
      throw new Exception("Error when trying to read element " + propertyName + " from file " + SETTINGS_FILE_NAME);
    }
}
