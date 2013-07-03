package es.luixal.ragbag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
	private final String KEY_ITEMS = "ITEMS";
	
	// list of NFC technologies detected:
	private final String[][] techList = new String[][] {
			new String[] {
				NfcA.class.getName(),
	            NfcB.class.getName(),
	            NfcF.class.getName(),
	            NfcV.class.getName(),
	            IsoDep.class.getName(),
	            MifareClassic.class.getName(),
	            MifareUltralight.class.getName(), Ndef.class.getName()
			}
	};
	
	private ArrayList<String> items;
	private int counterInit = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main_activity);
		setContentView(R.layout.main_list);
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ITEMS)) {
			this.items = savedInstanceState.getStringArrayList(KEY_ITEMS);
		} else {
			this.items = new ArrayList<String>();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
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
					Toast.makeText(this, "Excel file saved!\n" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					
					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					sharingIntent.setType("application/vnd.ms-excel");
					sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					
					try {
						startActivity(sharingIntent);
					} catch (ActivityNotFoundException ex) {
						Toast.makeText(this, "Excel file saved!\n", Toast.LENGTH_SHORT).show();
					}
					
				} catch (IOException e) {
					Toast.makeText(this, "ERROR saving Excel file", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.remove:
			if (this.items != null && !this.items.isEmpty()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Are you sure you wanto to remove ALL LIST ITEMS?");
				builder.setPositiveButton("YES", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						items.clear();
						((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
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
			final EditText et = new EditText(this);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		this.setListAdapter(new TagsListAdapter(this, this.items));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// creating pending intent:
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// creating intent receiver for NFC events:
		IntentFilter filter = new IntentFilter();
		filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
		// enabling foreground dispatch for getting intent from NFC event:
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList(KEY_ITEMS, this.items);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// disabling foreground dispatch:
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	protected void onNewIntent(final Intent intent) {
		if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			String uid = Utils.byteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
			if (this.items.contains(uid)) {
				Toast.makeText(this, "ERROR! Tag repetido en la posición " + this.items.indexOf(uid) + "!", Toast.LENGTH_SHORT).show();
			} else {
				this.items.add(uid);
				((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
			}
		}
	}
	
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
//		this.items.remove(position);
//		((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
//	}

}
