package es.luixal.ragbag.fragments;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import es.luixal.ragbag.MainActivity;
import es.luixal.ragbag.R;
import es.luixal.ragbag.Tag;
import es.luixal.ragbag.TagsListAdapter;
import es.luixal.ragbag.Utils;
import es.luixal.ragbag.listeners.OnNewTagListener;

public class TagListFragment extends ListFragment implements OnNewTagListener {
	
	private final String KEY_ITEMS = "ITEMS";
	
	private ArrayList<Tag> items;
	private int counterInit = 0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		return inflater.inflate(R.layout.tag_list_fragment, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ITEMS)) {
			this.items = savedInstanceState.getParcelableArrayList(KEY_ITEMS);
		} else {
			this.items = (ArrayList<Tag>) Tag.getAll();
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		this.getMainActivity().registerOnNewTagListener(this);
		this.setListAdapter(new TagsListAdapter(this.getActivity(), this.items));
	}
	
	@Override
	public void onStop() {
		super.onStop();
		this.getMainActivity().unregisterOnNewTagListener(this);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(KEY_ITEMS, this.items);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main_activity, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.export:
			if (this.items != null && !this.items.isEmpty()) {
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "saved.xls");
				try {
					
					if (this.counterInit == 0) {
						Utils.exportToExcel(this.items, file.getAbsolutePath());
					} else {
						Utils.exportToExcel(this.items, this.counterInit, file.getAbsolutePath());
					}
					Toast.makeText(this.getActivity(), "Excel file saved!\n" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("application/vnd.ms-excel");
					sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					
					try {
						startActivity(sharingIntent);
					} catch (ActivityNotFoundException ex) {
						Toast.makeText(this.getActivity(), "Excel file saved!\n", Toast.LENGTH_SHORT).show();
					}
					
				} catch (IOException e) {
					Toast.makeText(this.getActivity(), "ERROR saving Excel file", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this.getActivity(), "No data to export", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.remove:
			if (this.items != null && !this.items.isEmpty()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
				builder.setMessage("Are you sure you wanto to remove ALL LIST ITEMS?");
				builder.setPositiveButton("YES", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						items.clear();
						Tag.removeAll();
						((ArrayAdapter<?>)getListAdapter()).notifyDataSetChanged();
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("No", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
			break;
			
		case R.id.count_init:
			final EditText et = new EditText(this.getActivity());
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
			builder.setTitle("Counter init");
			builder.setView(et);
			builder.setPositiveButton("Ok", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					counterInit = Integer.parseInt(et.getText().toString());
				}
			});
			builder.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
			break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public MainActivity getMainActivity() {
		return ((MainActivity)getActivity());
	}
	
	@Override
	public void onListItemClick(ListView l, View v, final int position, long id) {
		super.onListItemClick(l, v, position, id);
		final EditText et = new EditText(this.getActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
		builder.setTitle("Tag Name");
		builder.setView(et);
		builder.setPositiveButton("Ok", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				items.get(position).setName(et.getText().toString());
				items.get(position).save();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	@Override
	public void onNewTag(Tag tag) {
		if (this.items.contains(tag.getUuid())) {
			Toast.makeText(this.getActivity(), "Tag repetido en la posición " + ( this.items.indexOf(tag.getUuid()) + 1) + "!", Toast.LENGTH_SHORT).show();
		} else {
			tag.setName(tag.getName() + " " + (this.counterInit + this.items.size()));
			this.items.add(tag);
			tag.save();
			((ArrayAdapter<?>)this.getListAdapter()).notifyDataSetChanged();
		}
	}
}
