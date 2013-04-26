package com.deadline.autoloadinglist;

import java.util.ArrayList;

import android.app.Service;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

abstract public class AutoLoadingGridAdapter<T> extends BaseAdapter {
	
	@SuppressWarnings("unused")
    private static final String TAG = AutoLoadingGridAdapter.class.getName();
	
	private static final LinearLayout.LayoutParams childLayoutParams = new LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT, 1.0f);
	
	
	private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View v) {
			if (mListView == null || v == null)
				return;
			final OnItemClickListener listener = mListView.getOnItemClickListener();
			if (listener != null)
				listener.onItemClick(mListView, v, -1, -1);
		}
	};
	
	private enum LoadingState {
		IDLE,		// Do not show loading, start loadData() on request 
		LOADING,    // Show loading, do not start loadData() on request
		ERROR, 		// (Same as loading, but with message. Shuld switch to IDLE on click) 
		LOADED 		// Nothing more to load. Do now show loading, do not start loadData 
	}

	
	private final ListView	       mListView;
	private final ArrayList<T>     list	= new ArrayList<T>();
	private final View             progress;
	protected final LayoutInflater mInflater;
	
	volatile private LoadingState  state;
	volatile private int	       mColumns;

	public AutoLoadingGridAdapter(ListView list, View progress, int columns) {
		this.mListView      = list;
		this.mInflater      = (LayoutInflater) mListView.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.mColumns       = columns;
		this.progress       = progress;
		
		this.state = LoadingState.IDLE;
		
		// TODO:
		/*for (int i=1; i<=146; i++)
			this.list.add((T)(new Integer(i)));*/
		
		this.mListView.addFooterView(progress, null, false);
		this.mListView.setAdapter(this);
		
		progress.setVisibility(View.GONE);
    }
	
	
	public void setColumnsCount(int columns) {
		this.mColumns = columns;
		notifyDataSetChanged();
	}
	
	abstract protected View bindView(final T item, final View convertView, final ViewGroup parent);
	
	abstract protected void loadData();

	
	protected synchronized boolean isState(LoadingState state) {
		return this.state == state;
	}
	
	
	synchronized public void add(T it) {
		list.add(it);
		notifyDataSetChanged();
	}
	
	
	synchronized public void add(T...its) {
		for(T it: its)
			list.add(it);
		notifyDataSetChanged();
	}
	
	
	@Override
    synchronized public int getCount() {
		final int count = list.size();
		if (count == 0)
			return 1;
	    return count / mColumns + ( count % mColumns > 0 ? 1 : 0 );
    }

	
	@Override
    public Object getItem(int position) {
	    return list.get(position);
    }

	
	@Override
    public long getItemId(int position) {
		return position;
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
	synchronized public View getView(int position, View convertView, ViewGroup parent) {
		final ViewGroup line;
		final int count = list.size();
		
		if (convertView == null) {
			line = createLine();
		} else {
			line = (ViewGroup) convertView;
			for(int j=0; j<line.getChildCount(); j++)
				line.getChildAt(j).setVisibility(View.GONE);
		}
		
		int i = 0;
		while ( i < mColumns ) {
			
			final int wrapedPosition = position*mColumns + i;
			
			//TODO: Rewrite these 2x2 options to 2x1
			View child;
			if ( wrapedPosition < count ) {
				View reuse = line.getChildAt(i);
				if (reuse == null) {
					child = bindView(list.get(wrapedPosition),  null/*create new view here*/, line); //TODO: line VS mListView???
					line.addView( child, childLayoutParams );
				} else {
					child = bindView(list.get(wrapedPosition), reuse /*put old view here*/, line);  //TODO: line VS mListView???
					child.setVisibility(View.VISIBLE);
				}
				child.setOnClickListener(mOnClickListener);
				
			} else {
				View reuse = line.getChildAt(i);
				if (reuse == null) {
					child = bindView(null, null , line);  //TODO: line VS mListView???
					child.setVisibility(count>0 ? View.INVISIBLE : View.GONE);
					line.addView( child, childLayoutParams );
				} else {
					reuse.setVisibility(count>0 ? View.INVISIBLE : View.GONE);
				}
			}
			
			i++;
		}
		
		// If last item
		if (position == getCount()-1)
			startLoadData();
		
		return line;
    }
	
	private void startLoadData() {
		if ( isState(LoadingState.LOADED) || isState(LoadingState.LOADING) ) // Do not start loading if data loaded or loading already
			return;
		
		state = LoadingState.LOADING;
		progress.setVisibility(View.VISIBLE);
		notifyDataSetChanged();
		loadData();
	}
	
	protected void finishLoadData(boolean fullyLoaded) {
		progress.setVisibility(View.GONE);
		state = fullyLoaded ? LoadingState.LOADED : LoadingState.IDLE;
	}
	
	
		
	private ViewGroup createLine() {
		final LinearLayout bucket = new LinearLayout(mListView.getContext());
		bucket.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
		bucket.setOrientation(LinearLayout.HORIZONTAL);
		return bucket;
	}
	

}
