package com.deadline.autoloadinglist;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private GridListAdapter mGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
        ArrayList<Integer> items = new ArrayList<Integer>();
        for(int i=1; i < 147; i++){
        	items.add(i);
        }

		
		ArrayAdapter<Integer> srcAdapter = new ArrayAdapter<Integer>(this, R.layout.grid_item, android.R.id.text1 , items);
		
		
		TextView xtt = new TextView(this);
		xtt.setText("Hello footer!");
		xtt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		ListView list = (ListView) findViewById(android.R.id.list);
		list.addFooterView(xtt);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				Toast.makeText(list.getContext(), view.toString(), Toast.LENGTH_SHORT).show();
            }
		});
		
		mGridAdapter = new GridListAdapter(list, 2, srcAdapter);
		
		list.setAdapter(mGridAdapter);
		
		// Fire config changes
		onConfigurationChanged(getResources().getConfiguration());
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		System.out.println("MainActivity.onConfigurationChanged()");
	    super.onConfigurationChanged(newConfig);
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	mGridAdapter.setColumnsCount(3);
	    } else { 
	    	mGridAdapter.setColumnsCount(2);
	    	
	    }
	    
	    hideActionBarOnRotate();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void hideActionBarOnRotate() {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB /* TODO: && Screen in SMALL */) {
	    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    		getActionBar().hide();
	    	} else {
	    		getActionBar().show();
	    	}
	    }
	}


}
