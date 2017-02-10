package pt.ptinovacao.arqospocket.service.jsonparser.structs;

import android.util.Log;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ptinovacao.arqospocket.service.logs.MyLogger;
import pt.ptinovacao.arqospocket.service.jsonparser.utils.WeekSelectionDaysObject;

public class WeekTestEvent extends TestEventObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LoggerFactory.getLogger(WeekTestEvent.class);
	
	private long interval = -1;
	
	private Calendar hour = null;
	private String hourString = null;
	
	private WeekSelectionDaysObject weekdays = null;
	private int weekdaysInt = -1;
	
	public WeekTestEvent(JSONObject jObject) {
		super();
		
		final String method = "WeekTestEvent";
		
		try {
			
			this.interval = Long.parseLong(jObject.getString("interval"));
			weekdaysInt = Integer.parseInt(jObject.getString("weekdays"));
			this.weekdays = new WeekSelectionDaysObject(weekdaysInt);
			
			
			hourString = jObject.getString("hour");
			String local_hour = hourString.substring(0, 2);
			String local_min = hourString.substring(2, 4);
			String local_sec = hourString.substring(4, 6);
			

			hour = Calendar.getInstance();
			hour.set(Calendar.HOUR, Integer.parseInt(local_hour));
			hour.set(Calendar.MINUTE, Integer.parseInt(local_min));
			hour.set(Calendar.SECOND, Integer.parseInt(local_sec));
			hour.set(Calendar.MILLISECOND, 0);
			Log.d("THURSDAYZ", "HOURZNEW:" + hour);

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public WeekTestEvent(long interval, String hour, int weekdays) {
		super();
		
		final String method = "WeekTestEvent";
		
		try {
			
			this.interval = interval;
			this.weekdays = new WeekSelectionDaysObject(weekdays);
			
			
			String local_hour = hour.substring(0, 2);
			String local_min = hour.substring(2, 4);
			String local_sec = hour.substring(4, 6);

			this.hour = Calendar.getInstance();
			this.hour.set(Calendar.HOUR, Integer.parseInt(local_hour));
			this.hour.set(Calendar.MINUTE, Integer.parseInt(local_min));
			this.hour.set(Calendar.SECOND, Integer.parseInt(local_sec));
			this.hour.set(Calendar.MILLISECOND, 0);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
	}
	
	public long get_interval() {
		return interval;
	}
	
	public Calendar get_hour() {
		return hour;
	}
	
	public WeekSelectionDaysObject get_weekdays() {
		return weekdays;
	}
	
	public Date get_next_execution_date(Date todayOrStartDate) {
		final String method = "get_next_execution_date";
		Log.d("THURSDAYZ", "todayOrStartDate:" + todayOrStartDate);
		Date returnDate = null;
		
		try {
			
			//get the execution date
			
			Calendar dateNow = Calendar.getInstance(); 
			
			// Defini a hora de execução
			//dateNow.set(dateNow.get(Calendar.YEAR), dateNow.get(Calendar.MONDAY), dateNow.get(Calendar.DAY_OF_MONTH), dateNow.get(Calendar.HOUR_OF_DAY), dateNow.get(Calendar.MINUTE), dateNow.get(Calendar.SECOND));

			dateNow.set(Calendar.HOUR, hour.get(Calendar.HOUR));
			dateNow.set(Calendar.MINUTE, hour.get(Calendar.MINUTE));
			dateNow.set(Calendar.SECOND, hour.get(Calendar.SECOND));

			Log.d("THURSDAYZ", "DateNow:" + dateNow);

			switch(dateNow.get(Calendar.DAY_OF_WEEK)) {
				case Calendar.SUNDAY:
					
					if (weekdays.get_Domingo() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);

					}
					
					break;
				case Calendar.MONDAY:
					if (weekdays.get_2feira() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
				case Calendar.TUESDAY:
					if (weekdays.get_3feira() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
				case Calendar.WEDNESDAY:
					if (weekdays.get_4feira() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
				case Calendar.THURSDAY:
					Log.d("THURSDAYZ", "ISTHURDAY: " + weekdays.get_5feira() + "NOW: " + System.currentTimeMillis() + " dateNow: " + dateNow.getTimeInMillis());
					if (weekdays.get_5feira() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						Log.d("THURSDAYZ", "WHERE INZZZZ");
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
				case Calendar.FRIDAY:
					if (weekdays.get_6feira() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_Sabado()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
				case Calendar.SATURDAY:
					if (weekdays.get_Sabado() && (System.currentTimeMillis()<dateNow.getTimeInMillis())) {
						
						// Não faz nada, apenas era necessário definir a hora de execução
						
					} else if (weekdays.get_Domingo()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 1);
						
					} else if (weekdays.get_2feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 2);
						
					} else if (weekdays.get_3feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 3);
						
					} else if (weekdays.get_4feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 4);
						
					} else if (weekdays.get_5feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 5);
						
					} else if (weekdays.get_6feira()) {
						
						dateNow.add(Calendar.DAY_OF_YEAR, 6);
						
					}
					break;
			}

			Log.d("THURSDAYZ", "MiiddleDateNow" + dateNow);
			
			returnDate = new Date(dateNow.getTimeInMillis());

		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}

		MyLogger.debug(logger, method, "Next Execution Date:" + returnDate);
		Log.d("THURSDAYZ", "Next Execution Date:" + returnDate);
		
		return returnDate;
	}
	
	public String toString() {
		final String method = "toString";
		
		StringBuilder sb = new StringBuilder();
		
		try {
			
			sb.append("\ninterval :"+interval);
			sb.append("\nhour :"
                    + hour.get(Calendar.HOUR_OF_DAY) + ":"
                    +  hour.get(Calendar.MINUTE) + ":"
                    +  hour.get(Calendar.SECOND));
			sb.append("\nweekdays :"+weekdays.toString());
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return sb.toString();
	} 
	
	public Object clone() {
		final String method = "IterationsTestEvent";
		
		try {
			
			return new WeekTestEvent(interval, hourString, weekdaysInt);
			
		} catch(Exception ex) {
			MyLogger.error(logger, method, ex);
		}
		
		return null;
	}
}
