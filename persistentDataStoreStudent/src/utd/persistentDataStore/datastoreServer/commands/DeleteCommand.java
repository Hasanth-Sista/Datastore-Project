package utd.persistentDataStore.datastoreServer.commands;


import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DeleteCommand extends ServerCommand{

	private static Logger logger=Logger.getLogger(DeleteCommand.class);

	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		logger.debug("Executing Run Method in Delete , Server");
		try{
			String name = StreamUtil.readLine(inputStream);
			FileUtil.deleteData(name);
			super.sendOK();
			
		}catch (ServerException ex) {
			String msg = ex.getMessage();
			logger.error("Exception while processing request, Server Exception " + msg);
			StreamUtil.sendError(msg, outputStream);
			StreamUtil.closeSocket(inputStream);
		}
		catch (Exception ex) {
			logger.error("Exception while processing request. " + ex.getMessage());
			StreamUtil.closeSocket(inputStream);
		}
		logger.debug("Exiting Run Method in Delete , Server");
		
	}
		
	

}
