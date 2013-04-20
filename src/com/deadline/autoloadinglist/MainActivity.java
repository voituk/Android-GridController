package com.deadline.autoloadinglist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private GridAdapter mGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
        ArrayList<Integer> items = new ArrayList<Integer>();
        for(int i=1; i < 147; i++){
        	items.add(i);
        }

		
		ArrayAdapter<Integer> srcAdapter = new ArrayAdapter<Integer>(this, R.layout.grid_item, android.R.id.text1 , items) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			    System.out.println("convertView["+position+"]=" + convertView);
			    return super.getView(position, convertView, parent);
			}
		};
		
		ListView list = (ListView) findViewById(android.R.id.list);
		
		mGridAdapter = new GridAdapter(list, 2, srcAdapter);
		
		list.setAdapter(mGridAdapter);
		
		// Fire config changes
		onConfigurationChanged(getResources().getConfiguration());
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		System.out.println("MainActivity.onConfigurationChanged()");
	    super.onConfigurationChanged(newConfig);
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
	    	mGridAdapter.setColumnsCount(3);
	    else 
	    	mGridAdapter.setColumnsCount(2);
	}


}
