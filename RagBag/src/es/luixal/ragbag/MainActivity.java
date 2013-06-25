package es.luixal.ragbag;

import java.util.LinkedList;

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
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	
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
	
	private LinkedList<String> items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main_activity);
		setContentView(R.layout.main_list);
		if (this.items == null) this.items = new LinkedList<String>();
		this.setListAdapter(new TagsListAdapter(this, this.items));
		this.getListView().setSaveEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
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
	protected void onPause() {
		super.onPause();
		// disabling foreground dispatch:
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcAdapter.disableForegroundDispatch(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (this.items != null && !this.items.isEmpty()) {
			// save items!
			Toast.makeText(this, "save items here!", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onNewIntent(final Intent intent) {
		if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
			
//			ImageView iv = (ImageView)findViewById(R.id.nfc_logo);
//			iv.setImageBitmap(null);
//			iv.setImageResource(R.drawable.signal_waves);
//			
//			
//			AnimationDrawable animationDrawable = (AnimationDrawable)iv.getDrawable();
//			animationDrawable.setOneShot(true);
//			animationDrawable.start();
//			
//			((TextView)findViewById(R.id.text)).setText(
//					Html.fromHtml("NFC Tag<br />" + 
//					"<b>" + ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)) + "</b>"));
//			
			this.items.add(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
			((ArrayAdapter)this.getListAdapter()).notifyDataSetChanged();
			
		}
	}
	
	private String ByteArrayToHexString(byte [] inarray) {
	    int i, j, in;
	    String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	    String out= "";
	
	    for(j = 0 ; j < inarray.length ; ++j) {
	        in = (int) inarray[j] & 0xff;
	        i = (in >> 4) & 0x0f;
	        out += hex[i];
	        i = in & 0x0f;
	        out += hex[i];
	    }
	    
	    return out;
	}

}
