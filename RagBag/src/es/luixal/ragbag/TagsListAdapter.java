package es.luixal.ragbag;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TagsListAdapter extends ArrayAdapter<Tag> {

	public TagsListAdapter(Context context, List<Tag> objects) {
		super(context, 0, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tag tag = this.getItem(position);
		if (convertView == null) convertView = View.inflate(this.getContext(), R.layout.tag_list_item, null);
		((TextView)convertView.findViewById(R.id.id)).setText(position + 1 + "");
		((TextView)convertView.findViewById(R.id.name)).setText(tag.getName());
		((TextView)convertView.findViewById(R.id.uid)).setText(tag.getUuid());
		int visibility = tag.isLocated() ? View.VISIBLE : View.INVISIBLE;
		((ImageView)convertView.findViewById(R.id.geo_icon)).setVisibility(visibility);
		return convertView;
	}

}
