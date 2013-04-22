package com.deadline.autoloadinglist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class GridListAdapter extends BaseAdapter {
	
	@SuppressWarnings("unused")
    private static final String TAG = GridListAdapter.class.getName();
	
	private static final LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1.0f);
	
	private Adapter      mWrapedAdapter;
	private ListView     mListView;
	volatile private int mColumns;

	
	private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mListView == null)
				return;
			final OnItemClickListener listener = mListView.getOnItemClickListener();
			if (listener != null)
				listener.onItemClick(mListView, v, -1, -1); //FIXME: Pass real position & id values there 
			
		}
	};
	

	public GridListAdapter(ListView list, int columns, Adapter wrapedAdapter) {
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
			//TODO: Maybe to add FrameLayout around each item
			View child;
			if ( position*mColumns + i < count ) {
				View old = line.getChildAt(i);
				if (old == null) {
					child = mWrapedAdapter.getView(position*mColumns +i, null /*put old view here*/, line);
					line.addView( child, childLayoutParams );
				} else {
					child = mWrapedAdapter.getView(position*mColumns +i, old /*put old view here*/, line);
					child.setVisibility(View.VISIBLE);
				}
				child.setOnClickListener(this.mOnClickListener);
				
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
		
		return line;
    }
	
		
	private ViewGroup line() {
		final LinearLayout bucket = new LinearLayout(mListView.getContext());
		bucket.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		bucket.setOrientation(LinearLayout.HORIZONTAL);
		return bucket;
	}
	

}
