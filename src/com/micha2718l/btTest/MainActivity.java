package com.micha2718l.btTest;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;  
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.graphics.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.widget.SeekBar.*;


public class MainActivity extends Activity
{
	TextView myLabel;
    EditText myTextbox;
	ImageView imageColor;
	Color myColor;
	float hsvC[] = {180,1,1};
	int colorArduino = 25;
	int progFinal = 25;
	SeekBar colorPick;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonOn = (Button) findViewById(R.id.buttonOn);
        Button buttonOff = (Button) findViewById(R.id.buttonOff);
        Button buttonSend = (Button) findViewById(R.id.buttonSend);
        
		colorPick = (SeekBar)findViewById(R.id.seekBar1);
        imageColor = (ImageView)findViewById(R.id.imageView1);
        myLabel = (TextView)findViewById(R.id.textView2);
        //myColor = Color.HSVToColor(hsvC);
		imageColor.setBackgroundColor(Color.HSVToColor(hsvC));
		
		
		colorPick.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar s, int prog, boolean mysteryBool)
			{
				colorArduino = prog/2-1;
				if(colorArduino<0){colorArduino=0;}
				myLabel.setText(Integer.toString(colorArduino));
				progFinal = prog;
				hsvC[0] = prog*3.6f;
				imageColor.setBackgroundColor(Color.HSVToColor(hsvC));
				
			}
			
			public void onStartTrackingTouch(SeekBar s)
			{

			}
			
			public void onStopTrackingTouch(SeekBar s)
			{
				
			}

				
				
		});
		
        buttonOn.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
        	  TextView text1 = (TextView) findViewById(R.id.textView1);
        	 
        	  try 
              {
                  findBT();
                  openBT();
                  text1.setText("Connected");
              }
              catch (IOException ex) {
            	  text1.setText("Error");
              }
        	  
          }
        });
        
        
        buttonOff.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
        	  try 
              {
				  myLabel.setText("c"+String.format("%02d",colorArduino));
                  sendData("c"+String.format("%02d",colorArduino));
              }
              catch (IOException ex) { }
        	  
          }
        });
        
        buttonSend.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
        	  try 
              {
                  sendData("s03");
              }
              catch (IOException ex) { }
        	  
          }
        });
        
        
        
    }
    
    void sendData(String msg) throws IOException
    {
        //String msg = "s03";
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
    }
    
    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
    }
    
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
				//myLabel.setText(device.getName());
                if(device.getName().equals("HC-06")) 
                {
                    mmDevice = device;
					myLabel.setText("Found: " + device.getName());
                    break;
                }
            }
        }
        //myLabel.setText("Bluetooth Device Found");
    }
    
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        
        
        myLabel.setText("Bluetooth Opened");
    }
    
}
