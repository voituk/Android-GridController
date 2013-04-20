package com.deadline.autoloadinglist;

import java.util.WeakHashMap;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class GridAdapter extends BaseAdapter {
	
	private static final String TAG = GridAdapter.class.getName();
	
	private static final LinearLayout.LayoutParams childLayoutParams =  new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1.0f);
	
	private Adapter mWrapedAdapter;
	
	volatile private int mColumns;

	private ListView mListView;

	private WeakHashMap<Long, View> viewCache = new WeakHashMap<Long, View>();

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
			Log.d(TAG, "line=null");
		} else {
			line = (ViewGroup) convertView;
			for(int j=0; j<line.getChildCount(); j++)
				line.getChildAt(j).setVisibility(View.GONE);
		}
		
		int i = 0;
		int count = mWrapedAdapter.getCount();
		while ( i<mColumns ) {
			
			View child;
			if ( position*mColumns + i < count ) {
				// TODO: Cache items instead of recreating it every time
				View  old = line.getChildAt(i);
				if (old == null) {
					child = mWrapedAdapter.getView(position*mColumns +i, null /*put old view here*/, line);
					line.addView( child, childLayoutParams );
				} else {
					child = mWrapedAdapter.getView(position*mColumns +i, old /*put old view here*/, line);
					old.setVisibility(View.VISIBLE);
				}
				
			} else {
				View  old = line.getChildAt(i);
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
		
		//line.postInvalidate();
		
		return line;
    }
	
	
	private View getChildView(int position) {
		return mWrapedAdapter.getView( position, null, null);
	}
	
	private ViewGroup line() {
		final LinearLayout bucket = new LinearLayout(mListView.getContext());
		bucket.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		bucket.setOrientation(LinearLayout.HORIZONTAL);
		return bucket;
	}
	

}