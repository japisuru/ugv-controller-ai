package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLog 
{
	private Logger logger;
	private FileHandler fh;
	String preText;
	
	public MyLog(String fileName, String preText)
	{
		this.preText = preText;
		try {
			fh = new FileHandler("/home/isuru/MSC-AI/Project/Log/" + fileName + ".log");
			logger = Logger.getAnonymousLogger();
			logger.addHandler(fh);
			logger.setUseParentHandlers(false);
			
			MyFormatter formatter = new MyFormatter();  
	        fh.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
          
	}
	
	public void log(String message)
	{
		//logger.info(this.preText + " : " + message);
	}
	
	public void testLog(String message)
	{
		logger.info(this.preText + " : " + message);
	}
}
