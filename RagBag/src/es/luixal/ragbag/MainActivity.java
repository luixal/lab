package es.luixal.ragbag;

import java.util.LinkedList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import es.luixal.ragbag.fragments.TagListFragment;
import es.luixal.ragbag.listeners.OnNewTagListener;

public class MainActivity extends FragmentActivity {
	
//	private final String KEY_ITEMS = "ITEMS";
	
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
	
	private List<OnNewTagListener> onNewTagListeners;
	
//	private ArrayList<Tag> items;
//	private int counterInit = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main_activity);
		setContentView(R.layout.main_list);
//		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_ITEMS)) {
////			this.items = savedInstanceState.getStringArrayList(KEY_ITEMS);
//			this.items = savedInstanceState.getParcelableArrayList(KEY_ITEMS);
//		} else {
//			this.items = (ArrayList<Tag>) Tag.getAll();
//			for (Tag item:items) Log.e("TAG", item.toString());
//			//this.items = new ArrayList<Tag>();
//		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		return true;
//	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		
//		case R.id.export:
//			if (this.items != null && !this.items.isEmpty()) {
//				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "saved.xls");
//				try {
//					
//					if (this.counterInit == 0) {
//						Utils.exportToExcel(this.items, file.getAbsolutePath());
//					} else {
//						Utils.exportToExcel(this.items, this.counterInit, file.getAbsolutePath());
//					}
//					Toast.makeText(this, "Excel file saved!\n" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//					
//					Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//					sharingIntent.setType("application/vnd.ms-excel");
//					sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//					
//					try {
//						startActivity(sharingIntent);
//					} catch (ActivityNotFoundException ex) {
//						Toast.makeText(this, "Excel file saved!\n", Toast.LENGTH_SHORT).show();
//					}
//					
//				} catch (IOException e) {
//					Toast.makeText(this, "ERROR saving Excel file", Toast.LENGTH_SHORT).show();
//					e.printStackTrace();
//				}
//			} else {
//				Toast.makeText(this, "No data to export", Toast.LENGTH_SHORT).show();
//			}
//			break;
//			
//		case R.id.remove:
//			if (this.items != null && !this.items.isEmpty()) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setMessage("Are you sure you wanto to remove ALL LIST ITEMS?");
//				builder.setPositiveButton("YES", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						items.clear();
//						Tag.removeAll();
//						((ArrayAdapter<?>)getListAdapter()).notifyDataSetChanged();
//						dialog.dismiss();
//					}
//				});
//				builder.setNegativeButton("No", new OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//				builder.create().show();
//			}
//			break;
//			
//		case R.id.count_init:
//			final EditText et = new EditText(this);
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle("Counter init");
//			builder.setView(et);
//			builder.setPositiveButton("Ok", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					counterInit = Integer.parseInt(et.getText().toString());
//				}
//			});
//			builder.setNegativeButton("Cancel", new OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.create().show();
//			
//		default:
//			break;
//		}
//		return super.onOptionsItemSelected(item);
//	}
	
	@Override
	protected void onStart() {
		super.onStart();
		TagListFragment fragment = new TagListFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.fragment_container, fragment);
		ft.commit();
//		this.setListAdapter(new TagsListAdapter(this, this.items));
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
//		outState.putStringArrayList(KEY_ITEMS, this.items);
//		outState.putParcelableArrayList(KEY_ITEMS, this.items);
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
//			String uid = Utils.byteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
			Tag tag = new Tag(
					"Tag",
					Utils.byteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)),
					this.getCurrentLocation());
			// notifying listeners:
			if (this.onNewTagListeners != null) for (OnNewTagListener listener:this.onNewTagListeners) listener.onNewTag(tag);
		}
	}
	
//	@Override
//	protected void onListItemClick(ListView l, View v, final int position, long id) {
//		super.onListItemClick(l, v, position, id);
//		final EditText et = new EditText(this);
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("Tag Name");
//		builder.setView(et);
//		builder.setPositiveButton("Ok", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				items.get(position).setName(et.getText().toString());
//				items.get(position).save();
//			}
//		});
//		builder.setNegativeButton("Cancel", new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
	
	public Location getCurrentLocation() {
		// setting criteria for getting providers:
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// getting activated providers and keeping the most recent location from them:
		LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
	    List<String> providers = locationManager.getProviders(criteria, true);
	    if (providers.size() > 0) {
	        Location newestLocation = null;
	        for (String provider : providers) {
	            Location location = locationManager.getLastKnownLocation(provider);
	            if (location != null) {
	                if (newestLocation == null) {
	                    newestLocation = location;
	                } else {
	                    if (location.getTime() > newestLocation.getTime()) {
	                        newestLocation = location;
	                    }
	                }
	            }
	        }
	        // if not location provided, request it:
//	        locationManager.requestLocationUpdates(providers.get(0), 0, 0, this);
	        return newestLocation;
	    } else {
//	    	Log.e("TAG", providers.size() + "providers found!");
//	        LocationDialogFragment dialog = new LocationDialogFragment();
//	        dialog.show(getSupportFragmentManager(), LocationDialogFragment.class.getName());
	        return null;
	    }
	}
	
	public void registerOnNewTagListener(OnNewTagListener listener) {
		if (this.onNewTagListeners == null) this.onNewTagListeners = new LinkedList<OnNewTagListener>();
		this.onNewTagListeners.add(listener);
	}
	
	public boolean unregisterOnNewTagListener(OnNewTagListener listener) {
		if (this.onNewTagListeners != null) return this.onNewTagListeners.remove(listener);
		return false;
	}

}
