package com.xbongbong.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class DefaultConfig {
	public DefaultConfig(){}
	private static Properties props = new Properties(); 
	private static final Logger LOG = LogManager.getLogger(DefaultConfig.class);
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("properties/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			LOG.error("DefaultConfig FileNotFoundException" + e);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("DefaultConfig IOException" + e);
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void updateProperties(String key,String value) {    
            props.setProperty(key, value); 
    } 
}
