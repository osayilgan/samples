package okan.apps.samples;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		
		// Acquire the lock
		wl.acquire();
		
		generateNotification(context, intent.getExtras().getString("name"));
		
		wl.release();
	}
	
	/**
	 * Sets alarm with Android Alarm Manager
	 * 
	 * @param context		Context
	 * @param calendar		Calendar object will be used to determine exact date for alarm
	 * @param alarmId		ID
	 * @param reminderName	Name
	 */
	public void setAlarm(Context context, Calendar calendar, int alarmId, String reminderName) {
		
		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.putExtra("name", reminderName);
		intent.putExtra("id", alarmId);
		
		PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
		
		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	
		// Notify User
		notifyUser(context, "Alarm has been add with alarm ID : " + alarmId);
	}
	
	/**
	 * Cancels Alarm
	 * 
	 * @param context		Context
	 * @param alarmId		ID to cancel
	 */
	public void cancelAlarm(Context context, int alarmId) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
	
	/**
	 * Generates Push Notification
	 * 
	 * @param context		Context
	 * @param message		Message to show to User.
	 */
	public void generateNotification(Context context, String message) {
		Intent notificationIntent = new Intent(context, SampleList.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
				PendingIntent.FLAG_ONE_SHOT);
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Resources res = context.getResources();
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		
		builder.setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher)).setTicker("")
				.setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentTitle(context.getString(R.string.app_name))
				.setContentText(message);
		builder.setDefaults(Notification.DEFAULT_ALL);
		
		Notification n = builder.build();
		
		nm.notify(0, n);
	}
	
	/*
	 * Private Methods 
	 */
	
	/**
	 * Notifies user with Android Toast Message
	 * 
	 * @param mContext			Context
	 * @param notificationText	Message will be shown to user
	 */
	private void notifyUser(Context mContext, String notificationText) {
		Toast.makeText(mContext, notificationText, Toast.LENGTH_SHORT).show();
	}
}
