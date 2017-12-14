package utd.persistentDataStore.datastoreServer.commands;


import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class WriteCommand extends ServerCommand {

	private static Logger logger=Logger.getLogger(WriteCommand.class);

	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		logger.debug("Executing Run Method in Write , Server");

		try{
			String name = StreamUtil.readLine(inputStream);
			String asciiSize = StreamUtil.readLine(inputStream);
			
			int length = Integer.valueOf(asciiSize);
			byte[] data = StreamUtil.readData(length,inputStream);
						
			FileUtil.writeData(name, data);
			
			super.sendOK();
			
			StreamUtil.closeSocket(inputStream);
			
		}catch (Exception ex) {
			logger.error("Exception while processing request " + ex.getMessage());
		}
			
		logger.debug("Exiting Run Method in Write , Server");	
	}

}
