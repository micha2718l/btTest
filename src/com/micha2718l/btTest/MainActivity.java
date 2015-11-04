package com.micha2718l.btTest;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonOn = (Button) findViewById(R.id.buttonOn);
        Button buttonOff = (Button) findViewById(R.id.buttonOff);

        buttonOn.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
        	  TextView text1 = (TextView) findViewById(R.id.textView1);
        	  text1.setText("Button!");
        	  
          }
        });
        
        
        buttonOff.setOnClickListener(new View.OnClickListener()
        {
          public void onClick(View v)
          {
        	  TextView text1 = (TextView) findViewById(R.id.textView1);
        	  text1.setText("");
        	  
          }
        });
        
    }
}
