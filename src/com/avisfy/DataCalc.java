package com.avisfy;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DataCalc {
    private Calendar initDate;
    private int initType;
    public boolean errorOccurred;

    private static final int UNKNOWN = 0;
    private static final int IN_DAY = 1;
    private static final int IN_NIGHT = 2;
    private static final int FIRST_FREE = 3;
    private static final int SECOND_FREE = 4;


    //input string format: date date_type
    public DataCalc(String inputInfo) {
        errorOccurred = false;
        String[] inpArr = inputInfo.split(" ");

        //case input format day_of_month.month.year type
        if (inpArr.length == 2) {
            initType = parseType(inpArr[1]);

        } //case input format day_of_month month type
        else if (inpArr.length == 3) {
            initType = parseType(inpArr[2]);
            initDate = parseDate(inpArr[0], inpArr[1]);
        }

        if (errorOccurred) {
            System.out.println("Incorrect DataCalc");
        }
    }

    private DataCalc(Calendar day, int type) {
        errorOccurred = false;
        initType = type;
        initDate = (Calendar) day.clone();
    }


    private Calendar getInitDate() {
        return initDate;
    }


    public String typeOfDay(String strDate) {
        String[] dateArr = strDate.split(" ");
        Calendar targetDate = parseDate(dateArr[0], dateArr[1]);
        DataCalc dateCount = new DataCalc (initDate, initType);
        if (dateCount.errorOccurred)
            return "Error in typeOfDay";

        while (targetDate.after(dateCount.getInitDate())) {
            dateCount.nextDay();
            //System.out.println(dateCount.getStringDate() + " " + dateCount.getStringType());
        }
        return dateCount.getStringDate() + " " + dateCount.getStringType();
    }

    private void nextDay() {
        initDate.add(Calendar.DATE, 1);
        initType = (initType == SECOND_FREE) ? IN_DAY : initType + 1;
    }


    private int parseType(String type) {
        switch (type) {
            case "день":
                return  IN_DAY;
            case "ночь":
                return  IN_NIGHT;
            case "отсыпной":
                return  FIRST_FREE;
            case "выходной":
                return  SECOND_FREE;
            default :
                System.out.println("Error in parseType, inp type");
                errorOccurred = true;
                return  UNKNOWN;
        }
    }

    //date format day_of_month month
    private Calendar parseDate(String strDay, String strMonth) {
        int day = -1;
        int month = -1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        switch (strMonth) {
            case "января":
                month = Calendar.JANUARY;
            break;
            case "февраля":
                month = Calendar.FEBRUARY;
                break;
            case "марта":
                month = Calendar.MARCH;
                break;
            case "апреля":
                month = Calendar.APRIL;
                break;
            case "мая":
                month = Calendar.MAY;
                break;
            case "июня":
                month = Calendar.JUNE;
                break;
            case "июля":
                month = Calendar.JULY;
                break;
            case "августа":
                month = Calendar.AUGUST;
                break;
            case "сентября":
                month = Calendar.SEPTEMBER;
                break;
            case "октября":
                month = Calendar.OCTOBER;
                break;
            case "ноября":
                month = Calendar.NOVEMBER;
                break;
            case "декабря":
                month = Calendar.DECEMBER;
                break;
            default:
                errorOccurred = true;
                System.out.println("Error in setDate: month");
        }
        try {
            day = Integer.parseInt(strDay);
        } catch (NumberFormatException e) {
            day = -1;
            errorOccurred = true;
            System.out.println("Error in setDate: day");
        }
        if ((day != -1) && (month != -1)) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            return calendar;
        } else {
            errorOccurred = true;
            return Calendar.getInstance();
        }
    }

    private String getStringType() {
        switch (initType) {
            case IN_DAY:
                return  "день";
            case IN_NIGHT:
                return  "ночь";
            case FIRST_FREE:
                return  "отсыпной";
            case SECOND_FREE:
                return "выходной";
            default :
                System.out.println("Error in printStringType, inp type");
                return "";
        }
    }

    private String getStringDate() {
        String answer = initDate.get(Calendar.DATE) + " " + initDate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ROOT) + " " + initDate.get(Calendar.YEAR) + ",  " + initDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar. LONG, Locale.ROOT );
        return answer;
    }
}
