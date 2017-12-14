package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DirectoryCommand extends ServerCommand{
	
	private static Logger logger=Logger.getLogger(DirectoryCommand.class);

	@Override
	public void run() throws IOException, ServerException {
		// TODO Auto-generated method stub
		logger.debug("Executing Run Method in Directory , Server");

		List<String> list=null;
		try{
			list=FileUtil.directory();
			super.sendOK();
			StreamUtil.writeLine(Integer.toString(list.size()), outputStream);
			for(String s:list){
				StreamUtil.writeLine(s, outputStream);
			}
		
		}catch (ServerException ex) {
			String msg = ex.getMessage();
			logger.error("Exception while processing request, Server Exception " + msg);
			StreamUtil.sendError(msg, outputStream);
			StreamUtil.closeSocket(inputStream);
		}
		catch (Exception ex) {
			logger.error("Exception while processing request " + ex.getMessage());
			StreamUtil.closeSocket(inputStream);
			}
		logger.debug("Exiting Run Method in Directory , Server");

		}
	}
