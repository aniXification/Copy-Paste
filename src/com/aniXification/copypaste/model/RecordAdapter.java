package com.aniXification.copypaste.model;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aniXification.copypaste.R;

public class RecordAdapter extends ArrayAdapter<Record>{
	
	private Context context;
	private List<Record> recordsList;
	Format formatter;
	String timeStamp;
	
	public RecordAdapter(Context context, int textViewResourceId, List<Record> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.recordsList = items;
	}

	public int getCount() {
		return recordsList.size();
	}

	public Record getItem(int position) {
		return recordsList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {
		
		Record record = recordsList.get(position);
		
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
        }
        
        
        if (record != null) {
			TextView tt = (TextView) convertView.findViewById(R.id.toptext);
			TextView bt = (TextView) convertView.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText(record.getRecord());
			}
			if (bt != null) {
				formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
				timeStamp = formatter.format(record.getTimestamp());
				
				bt.setText(timeStamp );
			}
		}
		return convertView;
       
	}

	public void remove(Record record) {
		recordsList.remove(record);
	}
	
	public void removeAll(Record record) {
		recordsList.remove(record);
	}

}
