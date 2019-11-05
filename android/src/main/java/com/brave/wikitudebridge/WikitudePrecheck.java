package com.brave.wikitudebridge;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;

import java.util.Arrays;

public class WikitudePrecheck extends Activity {

	private PermissionManager mPermissionManager;

	protected static final String TAG = "WikitudePrecheck";

	private String architectWorldURL = "";
	private String sdkKey = "";
	private boolean hasGeolocation = false;
	private boolean hasImageRecognition = false;
	private boolean hasInstantTracking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wikitude_precheck);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

		mPermissionManager = ArchitectView.getPermissionManager();

		Bundle extras = getIntent().getExtras();

		this.architectWorldURL = extras.getString(WikitudeActivity.EXTRAS_KEY_AR_URL);
		this.hasGeolocation = extras.getBoolean(WikitudeActivity.EXTRAS_KEY_HAS_GEO, false);
		this.hasImageRecognition = getIntent().getExtras().getBoolean(WikitudeActivity.EXTRAS_KEY_HAS_IR, false);
		this.hasInstantTracking = getIntent().getExtras().getBoolean(WikitudeActivity.EXTRAS_KEY_HAS_INSTANT, false);
		this.sdkKey = getIntent().getExtras().getString(WikitudeActivity.EXTRAS_KEY_SDK_KEY);


		String[] permissions = this.hasGeolocation ?
				new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION} :
				new String[]{Manifest.permission.CAMERA};


		mPermissionManager.checkPermissions(WikitudePrecheck.this, permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
			@Override
			public void permissionsGranted(int requestCode) {
				Log.i(TAG, "Permission Granted, proceeding to start wikitude intent.");
				startWikitudeIntent();
			}

			@Override
			public void permissionsDenied(String[] deniedPermissions) {
				Toast.makeText(WikitudePrecheck.this, "The following permissions are required to enable an AR experience: " + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void showPermissionRationale(final int requestCode, final String[] permissions) {
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(WikitudePrecheck.this);
				alertBuilder.setCancelable(true);
				alertBuilder.setTitle("AR Experience Permissions");
				alertBuilder.setMessage("The following permissions are required to enable an AR experience: " + Arrays.toString(permissions));
				alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPermissionManager.positiveRationaleResult(requestCode, permissions);
					}
				});

				AlertDialog alert = alertBuilder.create();
				alert.show();
			}
		});

	}

  @Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}


	private void startWikitudeIntent()
	{
		final Intent intent = new Intent(this, WikitudeActivity.class);

		intent.putExtra(WikitudeActivity.EXTRAS_KEY_AR_URL, this.architectWorldURL);
		intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_GEO, this.hasGeolocation);
		intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_IR, this.hasImageRecognition);
		intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_INSTANT, this.hasInstantTracking);
		intent.putExtra(WikitudeActivity.EXTRAS_KEY_SDK_KEY, this.sdkKey);

		//launch activity
		this.startActivityForResult(intent, 0xe110);
	}

	@Override
	protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData)
	{
		super.onActivityResult(aRequestCode, aResultCode, aData);

		Log.i(TAG, "Got activity result code "+ aData.getStringExtra(WikitudeActivity.WIKITUDE_RESULT) + "");

		if (aRequestCode == 0xe110)
		{
			Log.i(TAG, "Exiting.");
			Intent output = new Intent();
			output.putExtra(WikitudeActivity.WIKITUDE_RESULT, aData.getStringExtra(WikitudeActivity.WIKITUDE_RESULT));
			setResult(Activity.RESULT_OK, output);
			this.finish();
		}

	}

}
