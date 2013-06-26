package es.luixal.ragbag;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TagsListAdapter extends ArrayAdapter<String> {

	public TagsListAdapter(Context context, List<String> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) convertView = View.inflate(this.getContext(), R.layout.tag_list_item, null);
		((TextView)convertView.findViewById(R.id.id)).setText(position + 1 + "");
		((TextView)convertView.findViewById(R.id.uid)).setText(this.getItem(position));
		return convertView;
	}

}
