package dev.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class devProperties {
	
	Properties m_Proper = new Properties();
	
	public devProperties(){
		
	}
	
	private int nServerPort = 0;
	private int nMaxThreadCnt = 5;
	
	/*
	 * Load ProPerties Value
	 */
	public int getPropertiesValue(){
		
		try {
			m_Proper.load(new FileInputStream("devPolygon.properties"));
			
			nServerPort = Integer.valueOf(m_Proper.getProperty("Server Port", "19999"));
			nMaxThreadCnt = Integer.valueOf(m_Proper.getProperty("MaxThreadCnt", "5"));
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 100;
	}

}
