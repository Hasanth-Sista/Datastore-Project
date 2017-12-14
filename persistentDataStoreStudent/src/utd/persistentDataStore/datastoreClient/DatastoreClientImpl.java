package utd.persistentDataStore.datastoreClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.ServerException;
import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
	public void write(String name, byte data[]) throws ClientException, ConnectionException
	{
		int length = data.length;
		InputStream inputStream =null;
		String asciiSize = Integer.toString(length);
		logger.debug("Executing Write Operation, Client");
		try {
			SocketAddress saddr = new InetSocketAddress(address, port);
			Socket socket = new Socket();
			socket.connect(saddr); // Connects to server
			inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(asciiSize, outputStream);
			StreamUtil.writeData(data, outputStream);

			String response;
			response= StreamUtil.readLine(inputStream);

			if(!response.equals("OK")){
				throw new ClientException("Socket closed before reading N bytes");
			}
			System.out.println(response);

		}
		catch (ClientException e) {
			logger.error("Exception while processing request " + e.getMessage());
			throw e;
		}catch (Exception e) {
			logger.error("Exception while processing request " + e.getMessage());
		}
		finally {
			StreamUtil.closeSocket(inputStream);
			logger.debug("Socket Closed at Write, Client");
		}

	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
	public byte[] read(String name) throws ClientException, ConnectionException
	{
		logger.debug("Executing Read Operation, Client");
		byte[] data=null;
		InputStream inputStream=null;
		try {
			SocketAddress saddr = new InetSocketAddress(address, 10023);
			Socket socket = new Socket();
			socket.connect(saddr);

			inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			StreamUtil.writeLine("read", outputStream);
			StreamUtil.writeLine(name, outputStream);


			String response = StreamUtil.readLine(inputStream);

			if(!response.equals("OK")){
				throw new ClientException("File Not Found");
			}


			String asciiSize = StreamUtil.readLine(inputStream);
			int length = Integer.valueOf(asciiSize);
			if(length==0){
				throw new ClientException("No data exists for the file");
			}

			System.out.println(response);

			data=StreamUtil.readData(length,inputStream);



		}
		catch (ClientException e) {
			logger.error("Exception while processing request " + e.getMessage());
			throw e;
		}catch (Exception e) {
			System.out.println("125");
			logger.error("Exception while processing request " + e.getMessage());	
		}finally {
			StreamUtil.closeSocket(inputStream);
			logger.debug("Socket Closed at Read, Client");
		}

		return data;
	}
	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
	public void delete(String name) throws ClientException, ConnectionException
	{
		logger.debug("Executing Delete Operation, Client");
		InputStream inputStream=null;
		Socket socket;
		try {
			SocketAddress saddr = new InetSocketAddress(address, 10023);
			socket = new Socket();

			socket.connect(saddr); // Connects to server
			
			inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			StreamUtil.writeLine("delete", outputStream);
			StreamUtil.writeLine(name, outputStream);

			String response = StreamUtil.readLine(inputStream);

			if(!response.equals("OK")){
				throw new ClientException("File not found");
			}

			System.out.println(response);


		}catch (ClientException e) {
			logger.error("Exception while processing request " + e.getMessage());
			throw e;
		}catch (Exception e) {
			logger.error("Exception while processing request " + e.getMessage());
		}finally {
			StreamUtil.closeSocket(inputStream);
			logger.debug("Socket Closed at Delete, Client");
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
	public List<String> directory() throws ClientException, ConnectionException
	{
		logger.debug("Executing Directory Operation, Client");
		List<String> list=new ArrayList<String>();
		InputStream inputStream = null;
		try {

			SocketAddress saddr = new InetSocketAddress(address, 10023);
			Socket socket = new Socket();
			socket.connect(saddr); // Connects to server

			inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			StreamUtil.writeLine("directory", outputStream);

			String response=StreamUtil.readLine(inputStream);
			System.out.println(response);

			int length=Integer.valueOf(StreamUtil.readLine(inputStream));
			System.out.println(length);

			for(int i=0;i<length;i++){
				list.add(StreamUtil.readLine(inputStream));
			}

		}catch (Exception e) {
			logger.error("Exception while processing request " + e.getMessage());
			
		}finally{
			StreamUtil.closeSocket(inputStream);
			logger.debug("Socket Closed at Directory, Client");
		}
		return list;
	}

}
