package utd.persistentDataStore.datastoreServer.commands;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand{
	
	private static Logger logger=Logger.getLogger(ReadCommand.class);
	@Override
	
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		logger.debug("Executing Run Method in Read , Server");

		byte[] data = null;
		try{
			
		String inMessage = StreamUtil.readLine(inputStream); 
		
		data = FileUtil.readData(inMessage);
		String asciiSize = Integer.toString(data.length);
		
		if(asciiSize!=null){
			super.sendOK();
			StreamUtil.writeLine(asciiSize, outputStream);
			StreamUtil.writeData(data, outputStream);
		}else{
			throw new ServerException("File Not Found");
		}
			
			StreamUtil.closeSocket(inputStream);
		}catch (ServerException ex) {
			String msg = ex.getMessage();
			logger.error("Exception while processing request " + msg);
			StreamUtil.sendError(msg, outputStream);
			StreamUtil.closeSocket(inputStream);
		}catch (Exception ex) {
			logger.error("Exception while processing request " + ex.getMessage());
			StreamUtil.closeSocket(inputStream);
		}
		logger.debug("Exiting Run Method in Read , Server");

	}
		
}


