package com.wanyama.posmanager;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.wanyama.configuration.SlaveSetup;




import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Client extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int starId){
		ClientThread thread = new ClientThread();
		thread.execute();
		
		return START_STICKY;
	}
	
	public class ClientThread extends AsyncTask<Void, Void, String> {
    	
    	private String status = "succesfful";
    	private InetAddress serverAddr;
    	private Socket socket;
    	private DataInputStream dis;
    	private DataOutputStream dos;
    	private byte[] b_name;
    	private byte[] b_file;
    	private String file_name;
    	private File f;
    	private FileInputStream fin;
    	private BufferedInputStream bis;
    	
    	public ClientThread(){
    		file_name = "food.csv";
    	}
    	
/*    	protected void onPreExecute(){
    		
    	} */
    	@Override
    	protected String doInBackground (Void...params){
    		
    		try {
    			// create socket connection to Server
				serverAddr = InetAddress.getByName(SlaveSetup.serverIP);
				Log.d("ClientActivity", "C: Connecting...");
				socket = new Socket(serverAddr, SlaveSetup.serverPort);
				
					try {
                        Log.d("ClientActivity", "C: Sending command.");
                        
                        // connect read/write streams
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        
                        // convert filename to bytes
    					b_name = file_name.getBytes();
    					
    					// send name to server
    					dos.writeInt(b_name.length);
    					dos.write(b_name, 0, b_name.length);
                        
    					Log.d("ClientActivity", "C: Sent Name.");
                        ///////////////////////////////////////////
    					
    					// transfer file
    					f = new File(file_name);
    					b_file = new byte[(int) f.length()];
    					fin = new FileInputStream(f);
    					bis = new BufferedInputStream(fin);
    					bis.read(b_file, 0, b_name.length);
    							
    					// close streams
    					dos.writeInt(b_file.length);
    					dos.write(b_file, 0, b_file.length);
                        
    					if (fin != null) fin.close();
                        dos.flush();
                        if (dos != null) dos.close();
                        if (bis!=null) bis.close();

                      Log.d("ClientActivity", "C: Sent File.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                        status = "failed";
                    }
					socket.close();
					Log.d("ClientActivity", "C: Closed.");
					status = "successful";
			} catch (UnknownHostException e) {
				Log.w("ClientActivity", "C: Could not resolve server IP Address");
				e.printStackTrace();
				status = "failed";
			} catch (IOException e) {
				Log.d("ClientActivity", "C: Error creating socket connection to server");
				e.printStackTrace();
				status = "failed";
			}
    		
    		
			return status;
    		
    	}
    	
    	@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(null);
			// alert the user
			Toast.makeText(getApplicationContext(), "File sent", Toast.LENGTH_SHORT)
					.show();
			
			stopSelf();
		}
    }
}
