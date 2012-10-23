package com.aniXification.copypaste;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.aniXification.copypaste.Util.AppRater;
import com.aniXification.copypaste.model.Record;
import com.aniXification.copypaste.model.RecordAdapter;
import com.aniXification.copypaste.model.RecordDataSource;


@SuppressLint("NewApi")
public class StarterActivity extends SherlockListActivity {

	int currentapiVersion = android.os.Build.VERSION.SDK_INT;

	private RecordDataSource datasource;
	Long timeStamp_milli;

	private ProgressDialog mProgressDialog = null;
	private ArrayList<Record> mRecords = null;
	private RecordAdapter mAdapter = null;
	private Runnable viewRecords;

	//private ShareActionProvider mShareActionProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starter);

		mRecords = new ArrayList<Record>();
		this.mAdapter = new RecordAdapter(this, R.layout.row, mRecords);
		setListAdapter(this.mAdapter);

		viewRecords = new Runnable() {
			@Override
			public void run() {
				getRecords();
			}
		};

		Thread thread = new Thread(null, viewRecords, "MagentoBackground");
		thread.start();
		mProgressDialog = ProgressDialog.show(StarterActivity.this,"Please wait...", "Retreiving data.", true);

		/* onitemlongclick listener */
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int selectedIndex, long id) {

				final Record r = (Record) getListAdapter().getItem(selectedIndex);
				final CharSequence[] items = { "Share", "Delete" };

				AlertDialog.Builder builder = new AlertDialog.Builder(StarterActivity.this);
				builder.setTitle("For Selected record");
				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							Intent sendIntent = new Intent();
							sendIntent.setAction(Intent.ACTION_SEND);
							sendIntent.putExtra(Intent.EXTRA_TEXT, r.getRecord());
							sendIntent.setType("text/plain");
							startActivity(sendIntent);
							break;

						case 1:
							AlertDialog.Builder builder = new AlertDialog.Builder(
									StarterActivity.this);
							builder.setMessage(
									"Are you sure you want to delete?")
									.setCancelable(false)
									// Prevents user to use "back button"
									.setPositiveButton(
											"Delete",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													System.out.println("OK clikecd!! id: " + id);
													/* delete */
													deleteRecord(r);
												}
											})
									.setNegativeButton(
											"Cancel",
											new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													dialog.cancel();
												}
											});
							builder.show();
							break;
						default:
							break;
						}

					}
				});

				AlertDialog alert = builder.create();
				alert.show();
				return false;

			}
		});

	}

	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {	
			
			//Run AppRater
			AppRater.app_launched(StarterActivity.this);
			
			if (mRecords != null && mRecords.size() > 0) {
				mAdapter.notifyDataSetChanged();

				for (Iterator<Record> it = mRecords.iterator(); it.hasNext();) {
					mAdapter.add(it.next());
				}

				mProgressDialog.dismiss();
				mAdapter.notifyDataSetChanged();
			} else {
				mProgressDialog.dismiss();
			}
		}
	};

	private void deleteRecord(Record r) {
		try {

			System.out.println("the delete record in starterActivity");

			try {
				datasource = new RecordDataSource(this);
				datasource.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			datasource.deletRecord(r); // delete from database
			datasource.close();
			mAdapter.remove(r); // remove from adapter
			mAdapter.notifyDataSetChanged(); // notify to UI

			Toast.makeText(this, "record deleted!!", Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getRecords() {
		try {

			datasource = new RecordDataSource(this);
			datasource.open();

			mRecords = (ArrayList<Record>) datasource.getAllRecord();

			System.out.println("the records size:: " + mRecords.size());
			setListAdapter(mAdapter);

			System.out.println("the records size:: " + mRecords.size());

		} catch (Exception e) {
			e.printStackTrace();
		} 

		runOnUiThread(returnRes);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.paste:

			CharSequence pasteData = "";
			try {
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
					android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

					ClipData.Item cd = clipboard.getPrimaryClip().getItemAt(0);
					pasteData = cd.getText();
				} else {
					android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					pasteData = clipboard.getText().toString();

				}

				//Toast.makeText(getApplicationContext(), pasteData, 	Toast.LENGTH_SHORT).show();

			} catch (Exception ex) {
				System.out.println("Nothing in the clipboard to paste.");
				ex.printStackTrace();
			}

			try {
				datasource = new RecordDataSource(getApplicationContext());
				datasource.open();
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			Record record = null;
			timeStamp_milli = new Date().getTime();

			System.out.println("THe pasted item:: " + pasteData);
			System.out.println("THe pasted item:: " + pasteData.toString());

			// Check if there is anything to paste
			if (!pasteData.toString().equals("")) {
				record = datasource.createRecord(pasteData.toString(),timeStamp_milli, "fav static!!");
				Toast.makeText(this, "Pasted in the List.", Toast.LENGTH_SHORT).show();

				record.setRecord(pasteData.toString());
				record.setTimestamp(timeStamp_milli);

				mRecords.add(record);
				mAdapter.add(record);

				mAdapter.notifyDataSetChanged();

			} else {
				Toast.makeText(this, "Nothing to paste.", Toast.LENGTH_SHORT).show();
			}

			datasource.close();
			
			break;

		case R.id.appinfo:
			Intent appInfoIntent = new Intent();
			appInfoIntent.setClass(StarterActivity.this, AppInfo.class);
			startActivity(appInfoIntent);

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	protected void onListItemClick(ListView parent, View v, int selectedIndex,long id) {
		
		Toast.makeText(this, "Copied from the List.", Toast.LENGTH_LONG).show();

		Record r = (Record) getListAdapter().getItem(selectedIndex);

		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("simple text", r.getRecord());
			clipboard.setPrimaryClip(clip);
		} else {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(r.getRecord());
		}

		Toast.makeText(getApplicationContext(), r.getRecord(),Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.starter, menu);

/*		mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
		Intent intent = getDefaultShareIntent();

		if (intent != null)
			mShareActionProvider.setShareIntent(intent);*/
		return super.onCreateOptionsMenu(menu);

	}

	/** Returns a share intent */
/*	private Intent getDefaultShareIntent() {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
		return intent;
	}*/

	@Override
	protected void onResume() {
		try {
			datasource.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}

}
