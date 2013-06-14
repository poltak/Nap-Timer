package com.poltak.NapTimer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class MainActivity extends Activity
{
    private final static int BAD_NUMBER = -1;

    private EditText userInput;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        userInput = (EditText) findViewById(R.id.user_input);
        userInput.setRawInputType(Configuration.KEYBOARD_12KEY);    // Make numpad popup instead of QWERTY keyboard.

        Button setButton = (Button) findViewById(R.id.set_button);
        setButton.setOnClickListener(new View.OnClickListener()     // Call setAlarm() on Button click.
        {
            @Override
            public void onClick(View v) { setAlarm(); }
        });
	}

    /** Starts alarm activity with a calculated time from the EditText's user specified minute offset. */
	public void setAlarm()
	{
		String   inputStr  = userInput.getText().toString();
		int      minutes   = getMinutesFromString(inputStr);

        userInput.setText("");                                      // Clear whatever text user has entered.

		if (minutes < 0)    // Catches BAD_NUMBER and negatives.
        {
            userInput.setHint(R.string.bad_number_message);
        } else
		{
			// Create new Intent for the alarm and start it.
			Intent alarmIntent = getAlarmIntent(minutes);
			startActivity(alarmIntent);

			// Reset edit text box to default hint.
            userInput.setHint(R.string.edit_message);
		}
	}

	/**
	 * Parses the input string to get it in integer form.
	 * If a NumberFormatException is thrown, return BAD_NUMBER for processing in calling method.
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
	 * Creates a new Intent for an alarm clock setting the hour and minute of
	 * the day when it should ring, using values specified in the calculated
	 * offset time.
	 * @param offset The number of minutes to offset the alarm clock.
	 * @return The alarm clock Intent set for the offset time.
	 */
	private Intent getAlarmIntent(int offset)
	{
		Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);

		// Get current time and add to it the minute offset.
		Calendar offsetTime = Calendar.getInstance();
		offsetTime.add(Calendar.MINUTE, offset);

		// Set the extra fields for the alarm based on new Calendar values.
		alarm.putExtra(AlarmClock.EXTRA_HOUR,    offsetTime.get(Calendar.HOUR_OF_DAY));
		alarm.putExtra(AlarmClock.EXTRA_MINUTES, offsetTime.get(Calendar.MINUTE));

        // Specify to skip alarm app UI.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);

		return alarm;
	}
}
