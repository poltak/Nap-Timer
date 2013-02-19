package com.poltak.NapTimer;

import java.util.Calendar;

import com.example.myfirstapp.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity
{
	public final static String EXTRA_MINUTES = 
			"com.example.myfirstapp.MINUTES";
	private final static int BAD_NUMBER = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/** Called when the user clicks the Set button */
	public void setAlarm(View view)
	{
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String inputStr = editText.getText().toString();
		short minutes = (short) getMinutesFromString(inputStr);
		
		if ((minutes & 0x80) != 0)	// Number is negative (including BAD_NUMBER)
			setHint(editText, R.string.bad_number_message);
		else						// Number is positive
		{
			// Create new Intent for the alarm and start it
			Intent alarmIntent = getAlarmIntent(minutes);
			startActivity(alarmIntent);
			
			// Remove input and reset edit text box to default hint
			setHint(editText, R.string.edit_message);
		}
	}
	
	/**
	 * Creates a new Intent for an alarm clock setting the hour and minute of
	 * the day when it should ring, using values specified in the calculated
	 * offset time.
	 * @param offset The number of minutes to offset the alarm clock.
	 * @return The alarm clock Intent set for the offset time.
	 */
	@SuppressLint("InlinedApi")
	private Intent getAlarmIntent(short offset)
	{
		Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
		
		// Get current time and add to it the minute offset
		Calendar offsetTime = Calendar.getInstance();
		offsetTime.add(Calendar.MINUTE, offset);
		
		// Set the extra fields for the alarm based on new Calendar values
		alarm.putExtra(
				AlarmClock.EXTRA_HOUR, offsetTime.get(Calendar.HOUR_OF_DAY));
		alarm.putExtra(
				AlarmClock.EXTRA_MINUTES, offsetTime.get(Calendar.MINUTE));
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		
		return alarm;
	}
	
	/**
	 * Parses the input string to get it in integer form.
	 * If a NumberFormatException is thrown, return BAD_NUMBER for processing
	 * in calling method.
	 * @param input The input string the user enters.
	 * @return The number of minutes specified as an integer.
	 */
	private int getMinutesFromString(String input)
	{
		int minutes;
		
		try {
			minutes = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return BAD_NUMBER;
		}
		
		return minutes;
	}
	
	/**
	 * Clears the EditText box and sets the hint to @param editMessage.
	 * @param editText EditText object to perform operation on.
	 * @param newHint New hint to set. Need to specify path to XML string.
	 */
	private void setHint(EditText editText, int newHint)
	{
		editText.setText("");
		editText.setHint(newHint);
	}
}
