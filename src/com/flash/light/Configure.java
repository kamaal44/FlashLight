package com.flash.light;

import java.util.List;
import android.util.Log;
import android.os.Bundle;
import android.app.Activity;

import android.widget.Toast;
import android.widget.EditText;
import android.widget.RelativeLayout;

import android.content.Intent;
import android.content.ComponentName;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.MenuInflater;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.view.ActionMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatCallback;

public class Configure extends Activity implements AppCompatCallback {

  private static EditText editPhoneNumber;
  private static EditText editEmailAddress;

  private static String sPhoneNumber;
  private static String sEmailAddress;

  private static String sPhoneNumberDb;
  private static String sEmailAddressDb;

  private static DatabaseHandler db;
  private static AppCompatDelegate delegate;
  private static ComponentName componentName;

  private float x1, x2;
  static final int MIN_DISTANCE = 150;
  private static String action  = "create";

  public void toast(String text) {
    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private AppCompatDelegate getDelegate() {
    if (delegate == null) {
      delegate = AppCompatDelegate.create(this, this);
    }

  return delegate;
  }

  public boolean supportRequestWindowFeature(int featureId) {
    return getDelegate().requestWindowFeature(featureId);
  }

  public void invalidateOptionsMenu() {
    getDelegate().invalidateOptionsMenu();
  }

  @Override
  public void onSupportActionModeStarted(ActionMode mode) { }

  @Override
  public void onSupportActionModeFinished(ActionMode mode) { }

  public ActionMode startSupportActionMode(ActionMode.Callback callback) {
    return getDelegate().startSupportActionMode(callback);
  }

  @Nullable
  @Override
  public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
    return null;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }

  }

  public static boolean isEqual(String string1, String string2) {
    if(string1.equals(string2) && string2 != null) {
      return true;
    }
    else {
      return false;
    }
  }

  public boolean isEmpty(String string) {
    if(string.length() == 0 || string == null || string == "" || string.isEmpty()) {
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    sPhoneNumber  = editPhoneNumber.getText().toString();
    sEmailAddress = editEmailAddress.getText().toString();
    if(!isEmpty(sPhoneNumber) && !isEmpty(sEmailAddress) && action == "update") {
      if(!isEqual(sEmailAddress,sEmailAddressDb) || !isEqual(sPhoneNumber,sPhoneNumberDb)) {
        toast("Updating DB now!");
        db.updateFlashLightDatabase(new FlashLightDatabase(1, "no", sEmailAddress, sPhoneNumber));
      }
    }
    else if(!isEmpty(sPhoneNumber) && !isEmpty(sEmailAddress) && action == "create") {
      toast("Creating DB now!");
      db.addFlashLightDatabase(new FlashLightDatabase(1, "no", sEmailAddress, sPhoneNumber));
    }
    finish();
  }

  public void getDatabaseInfo()  {

    List<FlashLightDatabase> flashLightDatabase = db.getAllFlashLightDatabase();

    if(flashLightDatabase == null) {
      return;
    }

    for(FlashLightDatabase fldb : flashLightDatabase) {
      sPhoneNumberDb  = fldb.getPhoneNumber();
      sEmailAddressDb = fldb.getEmailAddress();
    }

    if(sEmailAddressDb != null) {
      editPhoneNumber.setText(sPhoneNumber);
      editEmailAddress.setText(sEmailAddress);
      action = "update";
    }
    else {
      action = "create";
    }

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.configure);

    delegate = AppCompatDelegate.create(this, this);
    delegate.onCreate(savedInstanceState);
    delegate.setContentView(R.layout.configure);

    Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar_configure);

    delegate.setSupportActionBar(toolbar);
    delegate.getSupportActionBar().setDisplayShowTitleEnabled(false);

    editPhoneNumber  = (EditText)findViewById(R.id.edit_phone_number);
    editEmailAddress = (EditText)findViewById(R.id.edit_email_address);

    db = new DatabaseHandler(Configure.this); 
    
    getDatabaseInfo();

    //hideAppIcon(getApplicationContext());

  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch(event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        x1 = event.getX();
      break;
      case MotionEvent.ACTION_UP:
        x2 = event.getX();
        float deltaX = x2 - x1;

        if(Math.abs(deltaX) > MIN_DISTANCE && x2 > x1) {
          Intent intent = new Intent(Configure.this, FlashLight.class);
          startActivity(intent);
        }
      break;
    }
    return super.onTouchEvent(event);
  }

}
