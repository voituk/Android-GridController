package com.deadline.autoloadinglist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;

public class GridAdapter extends BaseAdapter {
	
	@SuppressWarnings("unused")
    private static final String TAG = GridAdapter.class.getName();
	
	private static final LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1.0f);
	
	private Adapter mWrapedAdapter;
	
	volatile private int mColumns;

	private ListView mListView;

	public GridAdapter(ListView list, int columns, Adapter wrapedAdapter) {
		this.mListView      = list;
		this.mColumns       = columns;
	    this.mWrapedAdapter = wrapedAdapter;
	    
    }
	
	public void setColumnsCount(int columns) {
		this.mColumns = columns;
		notifyDataSetChanged();
	}

	@Override
    public int getCount() {
		final int count = mWrapedAdapter.getCount();
	    return count / mColumns + ( count % mColumns > 0 ? 1 : 0 );
    }

	
	@Override
    public Object getItem(int position) {
	    return null;
    }

	
	@Override
    public long getItemId(int position) {
		return 0;
    }
	
	@Override
	public boolean areAllItemsEnabled() {
	    return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}

	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		final ViewGroup line;
		if (convertView == null) {
			line = line();
		} else {
			line = (ViewGroup) convertView;
			for(int j=0; j<line.getChildCount(); j++)
				line.getChildAt(j).setVisibility(View.GONE);
		}
		
		int i = 0;
		int count = mWrapedAdapter.getCount();
		while ( i<mColumns ) {
			
			//TODO: Rewrite these 2x2 options to 3x1
			View child;
			if ( position*mColumns + i < count ) {
				View old = line.getChildAt(i);
				if (old == null) {
					child = mWrapedAdapter.getView(position*mColumns +i, null /*put old view here*/, line);
					line.addView( child, childLayoutParams );
				} else {
					child = mWrapedAdapter.getView(position*mColumns +i, old /*put old view here*/, line);
					old.setVisibility(View.VISIBLE);
				}
				
			} else {
				View old = line.getChildAt(i);
				if (old == null) {
					child = mWrapedAdapter.getView(0, null , line);
					child.setVisibility(View.INVISIBLE);
					line.addView( child, childLayoutParams );
				} else {
					old.setVisibility(View.INVISIBLE);
				}
				
			}
			
			i++;
		}
		
		if (position == getCount()-1) { // We're showing last line
			if (parent instanceof ListView) {
				Toast.makeText(parent.getContext(), "ListView", Toast.LENGTH_SHORT).show();
				/*ProgressBar progress = new ProgressBar(parent.getContext());
				progress.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));				
				((ListView)parent).addFooterView(progress, null, false);*/
			}
			
		}
		
		return line;
    }
	
		
	private ViewGroup line() {
		final LinearLayout bucket = new LinearLayout(mListView.getContext());
		bucket.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		bucket.setOrientation(LinearLayout.HORIZONTAL);
		return bucket;
	}
	

}
