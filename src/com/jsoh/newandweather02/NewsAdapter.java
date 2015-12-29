package com.jsoh.newandweather02;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter {
	private ArrayList<News> items;
	private LayoutInflater inflater;
	
	public NewsAdapter(Context context, int textViewResourceId, ArrayList<News> object) {
		super(context, textViewResourceId, object);
		
		this.items = object;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		// ���� view�� null�̸� ���ο� �並 ����
		if (view == null) {
			view = inflater.inflate(R.layout.news_row, null);
		}
		
		// ǥ���� ������
		News item = items.get(position);
		if (item != null) {
			TextView titleText = (TextView) view.findViewById(R.id.top);
			TextView mediaText = (TextView) view.findViewById(R.id.bottom);
			
			// ����� ��ü ������ ����
			StringTokenizer st = new StringTokenizer(item.getTitle(), "-");
			
			if (titleText != null) {
				titleText.setText(st.nextToken().trim());
			}
			if (mediaText != null) {
				mediaText.setText(st.nextToken().trim());
			}
		}
			
		return view;
	}

	
	
}
