package com.aniXification.copypaste;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

public class AppInfo extends SherlockActivity {

    private final static String APP_TITLE = "Copy-Paste";
    private final static String APP_PNAME = "com.aniXification.copypaste";
	
	private ShareActionProvider mShareActionProvider;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        getSupportMenuInflater().inflate(R.menu.items, menu);
 
        mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
        Intent intent = getDefaultShareIntent();
 
        /** Setting a share intent */
        if(intent!=null)
            mShareActionProvider.setShareIntent(intent);
        return super.onCreateOptionsMenu(menu);
	}
	
	  /** Returns a share intent */
    private Intent getDefaultShareIntent(){
 
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
        return intent;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case R.id.rate:
			showRateDialog(AppInfo.this);
			break;

		default:
			break;
		}

		return true;
	}
	
    public static void showRateDialog(final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        dialog.setContentView(ll);        
        dialog.show();        
    }
    
}
