package es.luixal.ragbag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "saved.xls");
			try {
				Utils.exportToExcel(this.items, file.getAbsolutePath());
				Toast.makeText(this, "Excel file saved!\n" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(this, "ERROR saving Excel file", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			break;

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
			this.items.add(Utils.byteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
			((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		this.items.remove(position);
		((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
	}

}
