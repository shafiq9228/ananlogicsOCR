package com.analogics.utils;
/**
 * @author ANIL REDDY GANTLA
 * 
 * 
 * */

import android.annotation.SuppressLint;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	SimpleDateFormat format;

	public String getVoucherDate() {
		String pattern = "yyMMdd";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getTodaysDate() {
		String pattern = "yyyyMMdd";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getDate() {
		String pattern = "dd-MM-yyyy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getTime() {
		String pattern = "HH:mm:ss";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getGeneratedDatetime() {
		String pattern = "ddMMyyHHmm";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getMonth() {
		String pattern = "MM";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getHours() {
		String pattern = "HH";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getday() {
		String pattern = "dd";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getyear() {
		String pattern = "yy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getPrintdatetime() {
		String pattern = "dd/MM/yyyy   HH:mm";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}
	
	public String getCurrentdate() {
		String pattern = "ddMMyyyy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}
	
	public String getDatetime() {
		String pattern = "dd/MM/yyyy HH:mm:ss";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}
	public String getDatetimeNew() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}
	public String getDatetimeStamp() {
		String pattern = "yyyMMddHHmmss";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}


	public String getBillDate() {
		String pattern = "dd/MM/yyyy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(new Date());

		return formattedDate;
	}

	public String getDueDate(){

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();

		// convert date to calendar
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);

		// manipulate date
		c.add(Calendar.YEAR, 0);
		c.add(Calendar.MONTH, 0);
		c.add(Calendar.DATE, 15); //same with c.add(Calendar.DAY_OF_MONTH, 1);

		// convert calendar to date
		Date currentDatePlusOne = c.getTime();

		System.out.println(dateFormat.format(currentDatePlusOne));
		return dateFormat.format(currentDatePlusOne);
	}


	//*******************************************************************************8

	public String getAPSPDCL_DueDate() throws ParseException {


		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		// add 4 years as an example
		//Date fromDate = formatter.parse("05/08/2020"),toDate = formatter.parse("11/29/2020");// DateUtils.addDays(fromDate,365 * 4);
		Date fromDate = formatter.parse(getBillDate());
		int numberOfDaysCount=0;
		// int daysBetween  = daysBetween(fromDate,toDate);
		int daysBetween  = 60;
		Date caurDate = fromDate;
		String duedate="";
		for(int i=0;i<=daysBetween ; i++ ) {
			// if(isWeekDay(caurDate) && !isFederalHoliday(caurDate) )
			try {
				if(!isFederalHoliday(caurDate) )
					numberOfDaysCount++;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			caurDate = DateUtils.addDays(caurDate,1); // add one day
			if(numberOfDaysCount==15){
				duedate =caurDate.toString();

				String pattern = "dd/MM/yyyy";
				format = new SimpleDateFormat(pattern);
				String formattedDate = format.format(caurDate);
				//duedate =caurDate.toString();
				duedate =formattedDate;
				break;
			}

		}
		//System.out.println("number of business days between "+fromDate+" and "+toDate+" is: "+numberOfDaysCount+ "   duedate >>> "+duedate);
	/*	String pattern = "dd/MM/yyyy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(duedate);*/
return duedate+"";
	}

	public String getAPSPDCL_DisconnectionDate() throws ParseException {


		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		//Date fromDate = formatter.parse("05/08/2020"),toDate = formatter.parse("11/29/2020");// DateUtils.addDays(fromDate,365 * 4);
		Date fromDate = formatter.parse(getAPSPDCL_DueDate());
		int numberOfDaysCount=0;
		// int daysBetween  = daysBetween(fromDate,toDate);
		int daysBetween  = 60;
		Date caurDate = fromDate;
		String duedate="";
		for(int i=0;i<=daysBetween ; i++ ) {

					numberOfDaysCount++;

			caurDate = DateUtils.addDays(caurDate,1); // add one day
			if(numberOfDaysCount==16){
				String pattern = "dd/MM/yyyy";
				format = new SimpleDateFormat(pattern);
				String formattedDate = format.format(caurDate);
				//duedate =caurDate.toString();
				duedate =formattedDate;
				break;
			}

		}
		//System.out.println("number of business days between "+fromDate+" and "+toDate+" is: "+numberOfDaysCount+ "   duedate >>> "+duedate);
		/*String pattern = "dd/MM/yyyy";
		format = new SimpleDateFormat(pattern);
		String formattedDate = format.format(duedate);*/
		return duedate+"";
	}
	private static boolean isWeekDay(Date caurDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(caurDate);
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		// return dayOfWeek!= Calendar.SATURDAY && dayOfWeek!= Calendar.SUNDAY ;
		return dayOfWeek!= Calendar.SUNDAY ;
	}


	private static boolean isFederalHoliday(Date caurDate) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yy");    //list will come from dao.getFederalHoliday();
		ArrayList<Date> federalHolidays =  new ArrayList<Date>();
		federalHolidays.add(formatter.parse("07/01/2020"));
		federalHolidays.add(formatter.parse("14/01/2020"));
		federalHolidays.add(formatter.parse("15/01/2020"));
		federalHolidays.add(formatter.parse("16/01/2020"));
		federalHolidays.add(formatter.parse("21/01/2020"));
		federalHolidays.add(formatter.parse("26/01/2020"));
		federalHolidays.add(formatter.parse("28/01/2020"));

		federalHolidays.add(formatter.parse("04/02/2020"));
		federalHolidays.add(formatter.parse("11/02/2020"));
		federalHolidays.add(formatter.parse("13/02/2020"));
		federalHolidays.add(formatter.parse("18/02/2020"));
		federalHolidays.add(formatter.parse("25/02/2020"));

		federalHolidays.add(formatter.parse("02/03/2020"));
		federalHolidays.add(formatter.parse("04/03/2020"));
		federalHolidays.add(formatter.parse("11/03/2020"));
		federalHolidays.add(formatter.parse("18/03/2020"));
		federalHolidays.add(formatter.parse("25/03/2020"));
		federalHolidays.add(formatter.parse("30/03/2020"));

		federalHolidays.add(formatter.parse("01/04/2020"));
		federalHolidays.add(formatter.parse("05/04/2020"));
		federalHolidays.add(formatter.parse("08/04/2020"));
		federalHolidays.add(formatter.parse("14/04/2020"));
		federalHolidays.add(formatter.parse("15/04/2020"));
		federalHolidays.add(formatter.parse("22/04/2020"));
		federalHolidays.add(formatter.parse("29/04/2020"));

		federalHolidays.add(formatter.parse("06/05/2020"));
		federalHolidays.add(formatter.parse("13/05/2020"));
		federalHolidays.add(formatter.parse("20/05/2020"));
		federalHolidays.add(formatter.parse("27/05/2020"));

		federalHolidays.add(formatter.parse("03/06/2020"));
		federalHolidays.add(formatter.parse("10/06/2020"));
		federalHolidays.add(formatter.parse("16/06/2020"));
		federalHolidays.add(formatter.parse("17/06/2020"));
		federalHolidays.add(formatter.parse("24/06/2020"));

		federalHolidays.add(formatter.parse("01/07/2020"));
		federalHolidays.add(formatter.parse("08/07/2020"));
		federalHolidays.add(formatter.parse("14/07/2020"));
		federalHolidays.add(formatter.parse("15/07/2020"));
		federalHolidays.add(formatter.parse("22/07/2020"));
		federalHolidays.add(formatter.parse("29/07/2020"));

		federalHolidays.add(formatter.parse("05/08/2020"));
		federalHolidays.add(formatter.parse("12/08/2020"));
		federalHolidays.add(formatter.parse("15/08/2020"));
		federalHolidays.add(formatter.parse("19/08/2020"));
		federalHolidays.add(formatter.parse("22/08/2020"));
		federalHolidays.add(formatter.parse("26/08/2020"));

		federalHolidays.add(formatter.parse("02/09/2020"));
		federalHolidays.add(formatter.parse("03/09/2020"));
		federalHolidays.add(formatter.parse("09/09/2020"));
		federalHolidays.add(formatter.parse("13/09/2020"));
		federalHolidays.add(formatter.parse("16/09/2020"));
		federalHolidays.add(formatter.parse("21/09/2020"));
		federalHolidays.add(formatter.parse("23/09/2020"));
		federalHolidays.add(formatter.parse("30/09/2020"));

		federalHolidays.add(formatter.parse("02/10/2020"));
		federalHolidays.add(formatter.parse("07/10/2020"));
		federalHolidays.add(formatter.parse("14/10/2020"));
		federalHolidays.add(formatter.parse("17/10/2020"));
		federalHolidays.add(formatter.parse("18/10/2020"));
		federalHolidays.add(formatter.parse("21/10/2020"));
		federalHolidays.add(formatter.parse("28/10/2020"));

		federalHolidays.add(formatter.parse("04/11/2020"));
		federalHolidays.add(formatter.parse("07/11/2020"));
		federalHolidays.add(formatter.parse("11/11/2020"));
		federalHolidays.add(formatter.parse("18/11/2020"));
		federalHolidays.add(formatter.parse("21/11/2020"));
		federalHolidays.add(formatter.parse("25/11/2020"));

		federalHolidays.add(formatter.parse("02/12/2020"));
		federalHolidays.add(formatter.parse("09/12/2020"));
		federalHolidays.add(formatter.parse("16/12/2020"));
		federalHolidays.add(formatter.parse("23/12/2020"));
		federalHolidays.add(formatter.parse("25/12/2020"));
		federalHolidays.add(formatter.parse("30/12/2020"));

		for (Date holiday : federalHolidays) {
			if(DateUtils.isSameDay(caurDate,holiday)) //using Apache commons-lang
				return true;
		}
		return false;
	}

	public static int daysBetween(Date d1, Date d2){
		return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}


	public static int totalMonths(String start, String end) throws ParseException {

		DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Date a=sdf.parse(start);
		Date b=sdf.parse(end);


		Calendar cal = Calendar.getInstance();
		if (a.before(b)) {
			cal.setTime(a);
		} else {
			cal.setTime(b);
			b = a;
		}
		int c = 0;
		while (cal.getTime().before(b)) {
			cal.add(Calendar.MONTH, 1);
			c++;
		}

		c=c - 1;
		if(c==0){
			c=1;
		}

		return c  ;
	}
	public static int monthsBetweenDates(Date startDate, Date endDate){



		Calendar start = Calendar.getInstance();
		start.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		int monthsBetween = 0;
		int dateDiff = end.get(Calendar.DAY_OF_MONTH)-start.get(Calendar.DAY_OF_MONTH);

		if(dateDiff<0) {
			int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
			dateDiff = (end.get(Calendar.DAY_OF_MONTH)+borrrow)-start.get(Calendar.DAY_OF_MONTH);
			monthsBetween--;

			if(dateDiff>0) {
				monthsBetween++;
			}
		}
		else {
			monthsBetween++;
		}
		monthsBetween += end.get(Calendar.MONTH)-start.get(Calendar.MONTH);
		monthsBetween  += (end.get(Calendar.YEAR)-start.get(Calendar.YEAR))*12;
		return monthsBetween;
	}

}
