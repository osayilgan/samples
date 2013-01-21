package okan.apps.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okan.apps.samples.DateTimePicker;
import okan.apps.samples.DateTimePicker.ICustomDateTimeListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseAndAlarmManager extends Activity implements OnClickListener, ICustomDateTimeListener {
	
	DateTimePicker eventDatePicker;
	Calendar eventDate;
	
	Button pickUpDate;
	Button addReminder;
	EditText eventName;
	
	ListView mAlarmListView;
	
	EventsDataSource dataSource;
	
	AlarmReceiver alarmReceiver;
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.database_layout);
		
		/*
		 * Initializing Layout Elements
		 */
		pickUpDate = (Button) findViewById(R.id.pick_up_date);
		addReminder = (Button) findViewById(R.id.add_event_and_reminder);
		eventName = (EditText) findViewById(R.id.event_name);
		
		/*
		 * Defining click listeners
		 */
		pickUpDate.setOnClickListener(this);
		addReminder.setOnClickListener(this);
		
		/*
		 * Initializing events data source.
		 */
		dataSource = new EventsDataSource(this);
		dataSource.open();
		
		/*
		 * Initializing AlarmReceiver Broadcast
		 */
		alarmReceiver = new AlarmReceiver();
		
		/*
		 * Initializing DateTimePicker
		 */
		eventDatePicker = new DateTimePicker(this, this);
		
		/*
		 * Initializing List View
		 */
		mAlarmListView = (ListView) findViewById(R.id.alarmList);
		initializeListView();
	}
	
	/*
	 * Retrieves all entries in database and sets to a List View
	 */
	private void initializeListView() {
		mAlarmListView.setAdapter(new ReminderListAdapter(this, dataSource.getAllAlarms()));
		((BaseAdapter)mAlarmListView.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();

		switch (id) {
		case R.id.pick_up_date:

			eventDatePicker.showDialog();

			break;
			
		case R.id.add_event_and_reminder:
			
			addReminder();
			
			break;

		default:
			break;
		}

	}

	@Override
	public void onSet(Calendar calendarSelected, Date dateSelected, int year, String monthFullName,
			String monthShortName, int monthNumber, int date, String weekDayFullName, String weekDayShortName,
			int hour24, int hour12, int min, int sec, String AM_PM) {

		// this will set the year, month and the day only.
		eventDate = calendarSelected;

		// this will set the hour of the day, minute and second
		eventDate.set(Calendar.HOUR_OF_DAY, hour24);
		eventDate.set(Calendar.MINUTE, min);
		eventDate.set(Calendar.SECOND, sec);

		pickUpDate.setText(dateFormatter.format(eventDate.getTime()));

	}

	@Override
	public void onCancel() {
		eventDate = null;
	}

	/*
	 * Private Methods
	 */
	
	/**
	 * Inserts Event to the events.db
	 * Adds alarm to the selected date.
	 */
	private void addReminder() {
		
		String name = eventName.getText().toString();
		
		if (eventDate != null && !name.equals("") ) {
			
			long eventId = dataSource.createEvent(name, dateFormatter.format(eventDate.getTime()).toString());
			
			// Create alarm in Broadcast Receiver
			setAlarm((int) eventId, name);
			
			// Notify Data Set changed in List View
			initializeListView();
			
		} else {
			// Notify user for empty spaces.
			notifyUser("Please Enter a Date and Name !");
		}
	}
	
	/**
	 * Notifies User with Toast Message
	 * 
	 * @param notificationText		String to insert into Notification Message
	 */
	private void notifyUser(String notificationText) {
		Toast.makeText(this, notificationText, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Creates an alarm in Broadcast Receiver
	 * 
	 * @param alarmId		this ID is not necessary to create an alarm but necessary to cancel the alarm.
	 * @param alarmName		name to indicate user when the alarm time is up.
	 */
	private void setAlarm(int alarmId, String alarmName) {
		alarmReceiver.setAlarm(this, eventDate, alarmId, alarmName);
	}
	
	@Override
	protected void onPause() {
		dataSource.close();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		dataSource.open();
		super.onResume();
	}
	
	/*
	 * 
	 * Inner Adapter Class
	 * 
	 */
	
	private class ReminderListAdapter extends BaseAdapter {
		
		private List<Event> mList;
		private Context mContext;
		
		public ReminderListAdapter(Context context, List<Event> events) {
			this.mList = events;
			this.mContext = context;	
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		
		@Override
		public Event getItem(int position) {
			return mList.get(position);
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				// inflate a new View every time a new row requires one.
				convertView = LayoutInflater.from(mContext).inflate(R.layout.database_layout_listview_item, parent, false);
			}
			
			Button deleteButton = (Button) convertView.findViewById(R.id.delete_button);
			TextView reminderName = (TextView) convertView.findViewById(R.id.reminderNameTextView);
			
			final Event event = mList.get(position);
			
			reminderName.setText(event.getName());
			
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onClickDeleteButton(event);
				}
			});
			
			return convertView;
		}
		
		private void onClickDeleteButton(Event event) {
			dataSource.deleteEvent(event);
			initializeListView();
		}
		
	}
}