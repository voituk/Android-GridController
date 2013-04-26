package com.deadline.autoloadinglist;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private AutoLoadingGridAdapter<Integer> mGridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
        /*ArrayList<Integer> items = new ArrayList<Integer>();
        for(int i=1; i < 147; i++){
        	items.add(i);
        }

		
		ArrayAdapter<Integer> srcAdapter = new ArrayAdapter<Integer>(this, R.layout.grid_item, android.R.id.text1 , items);
		
		
		TextView xtt = new TextView(this);
		xtt.setText("Hello footer!");
		xtt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		
		list.addFooterView(xtt);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				Toast.makeText(list.getContext(), view.toString(), Toast.LENGTH_SHORT).show();
            }
		});
		
		mGridAdapter = new GridListAdapter(list, 2, srcAdapter);
		
		list.setAdapter(mGridAdapter);*/
		
		
		View progress = getLayoutInflater().inflate(R.layout.list_footer, null);
		
		final Handler handler = new Handler();
		
		
		ListView list = (ListView) findViewById(android.R.id.list);
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override public void onItemClick(AdapterView<?> list, View view, int position, long id) {
				Integer it = (Integer)view.getTag();
				if (it == null)
					return;
				
				Toast.makeText(list.getContext(), "Click " + it.intValue(), Toast.LENGTH_SHORT).show();
            }
		});
		
		mGridAdapter = new AutoLoadingGridAdapter<Integer>(list, progress, 2) {
			@Override
            protected View bindView(Integer item, View convertView, ViewGroup parent) {
				View v = convertView == null ? mInflater.inflate(R.layout.grid_item, null, false) : convertView;
				if (item == null)
					return v;
				
				((TextView)v.findViewById(android.R.id.text1)).setText(String.format("%04d",  item.intValue()*10 ));
				v.setTag(item);
				
				return v;
            }

			@Override
            protected void loadData() {
				Toast.makeText(MainActivity.this, "loadData", Toast.LENGTH_SHORT).show();
				
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						finishLoadData(false);
						
						Integer[] arr = new Integer[10];
						for (int i=0; i<arr.length; i++)
							arr[i] = i+1;
						
						add(arr);
						
					}
				}, 3000);
            }
		};
		
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
