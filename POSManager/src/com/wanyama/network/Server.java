package com.wanyama.network;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.wanyama.configuration.SlaveSetup;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Server extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int starId){
		// start server Thread
		ServerThread thread = new ServerThread();
		thread.execute();
		
		return START_STICKY;
	}
	
public class ServerThread extends AsyncTask<Void, String, Void>{
		
		// status of transfer operation
		private Socket client;
		private DataInputStream dis;   // socket input stream
		private DataOutputStream dos;  // socket output stream
		private int length;			   // length of byte array
		private byte[] b_name;		   // filename byte array
		private byte[] b_file;		   // file byte array
		private String file_name;	   // file name
		private FileOutputStream fos;  // file output stream
		private BufferedOutputStream bos;
		private ServerSocket serverSocket;
		
		// set display before execution
		public void onPreExecute(){
			// verify working ip address
			if (SlaveSetup.serverIP == null){
				Toast.makeText(getApplicationContext(), "Error encountered with IP address", Toast.LENGTH_LONG).show();
				Log.w("Server", "ServerIP error: "+SlaveSetup.serverIP);
			}
			
		}
		
		@Override
		protected Void doInBackground(Void...params){
			
			// try connect to client
			try {
				// open port for listening
				serverSocket = new ServerSocket(SlaveSetup.serverPort);
			} catch (IOException e) {
				Log.d("ServerActivity", "S: Error creating ServerSocket ");
				e.printStackTrace();
			}
			
			// listen
			while (true) {
				try {
					client = serverSocket.accept();
					publishProgress("Connected");
					
					dis = new DataInputStream(client.getInputStream());
					dos = new DataOutputStream(client.getOutputStream());
					
					// get length of name array
					length = dis.readInt();
					b_name = new byte[length];
					// read name from stream
					dis.readFully(b_name);
					// convert name back to string
					file_name = new String(b_name);
					
					// receive file
					length = dis.readInt();
					b_file = new byte[length];
					dis.readFully(b_file);
					
					// write out file
					fos = new FileOutputStream(file_name);
					bos = new BufferedOutputStream(fos);
					bos.write(b_file, 0, b_name.length);
					// close streams
					bos.flush();
					bos.close();
					fos.flush();
					fos.close();
					
				} catch (IOException e) {
					publishProgress("Error accepting clients");
					e.printStackTrace();
				} catch (Exception e){
					publishProgress("Oops. Connection interrupted. Please reconnect your phones.");
					e.printStackTrace();
				}
			}
			
			// publish file name
			// launch import service
			
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			// stop service
			stopSelf();
		}
		
	}

}
