package com.android;

import java.util.Date;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlusplusActivity extends Activity {
	
	//private static final int ACTIVITY_CREATE = 0;
	//private static final int ACTIVITY_EDIT = 1;
	
	private int quantity = 0;
	

	
	
	private PDbAdapter mDbHelper;
	//private Cursor mNotesCursor;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mDbHelper = new PDbAdapter(this);
        mDbHelper.open();
        
        Button plusButton = (Button) findViewById(R.id.trigger);
        plusButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mDbHelper.plusPlus();
				// TODO Auto-generated method stub
				
			}
		});
        
        

        dumpDay();
        
    }
    
    @SuppressWarnings("null")
	public void dumpDay()
    {
    	long current = System.currentTimeMillis();
    	Date today = null;
    	today.setTime(current);
    	Date dbDate = null;
    	dbDate.setTime(mDbHelper.getLastDate());
    	
    	if( today.getDate()== 1 && today.compareTo(dbDate) > 0)
    	{
    		if(today.getDate() != dbDate.getDate())
    			mDbHelper.insertQuantity(current, quantity);
    		
    	}
    	else if( today.getDate() > dbDate.getDate())
    	{
    		mDbHelper.insertQuantity(current, quantity);
    	}
    }
    
    
    
    
}