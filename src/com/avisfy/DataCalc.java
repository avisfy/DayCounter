package com.avisfy;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.DAYS;

public class DataCalc {
    private final LocalDate initDate;
    private final int initType;
    public boolean errorOccurred;

    private static final int UNKNOWN = 0;
    private static final int IN_DAY = 1;
    private static final int IN_NIGHT = 2;
    private static final int FIRST_FREE = 3;
    private static final int SECOND_FREE = 4;
    private static final Locale loc = new Locale("ru","RU");
    private static final DateTimeFormatter outForm = DateTimeFormatter.ofPattern("d MMM uuuu EEE", loc);
    private static final  DateTimeFormatter pointedFormat = DateTimeFormatter.ofPattern("d.M.yy", loc);
    private static final  DateTimeFormatter textFormL = DateTimeFormatter.ofPattern("d MMMM yyyy", loc);
    private static final  DateTimeFormatter textFormS  = DateTimeFormatter.ofPattern("d MMMM yy", loc);



    private static Logger log = Logger.getLogger(DataCalc.class.getName());


    //input string format: date date_type
    public DataCalc(String inputInfo) {
        errorOccurred = false;
        String[] inpArr = inputInfo.split(" ");
        String[] inpDate;

        switch (inpArr.length) {
            //case day_of_month.month.year type
            case 2:
                inpDate = new String[1];
                inpDate[0] = inpArr[0];
                initDate = parseDate(inpDate);
                initType = parseType(inpArr[1]);
                break;
            //case day_of_month month type
            case 3:
                inpDate = new String[2];
                inpDate[0] = inpArr[0];
                inpDate[1] = inpArr[1];
                initDate = parseDate(inpDate);
                initType = parseType(inpArr[2]);
                break;
            //case day_of_month month year type
            case 4:
                inpDate = new String[3];
                inpDate[0] = inpArr[0];
                inpDate[1] = inpArr[1];
                inpDate[2] = inpArr[2];
                initDate = parseDate(inpDate);
                initType = parseType(inpArr[3]);
                break;
            default:
                initDate = LocalDate.now();
                initType = UNKNOWN;
                errorOccurred =  true;
        }
        if (errorOccurred) {
            log.info("Failed creating DateCalc");
        }
    }


    public String typeOfDay(String strDate) {
        if (errorOccurred) {
            return "";
        }
        String[] dateArr = strDate.split(" ");
        LocalDate targetDate = parseDate(dateArr);
        if (errorOccurred) {
            errorOccurred =  false;
            return "";
        }
        long daysDiff = DAYS.between(initDate, targetDate);
        int calcType = initType;
        /*for (long i = 0; i < daysDiff; i++) {
            calcType = (calcType == SECOND_FREE) ? IN_DAY : calcType + 1;
        }*/
        int part  = (int)daysDiff % 4;
        for (long i = 0; i < part; i++) {
            calcType = (calcType == SECOND_FREE) ? IN_DAY : calcType + 1;
        }
        return targetDate.format(outForm) + " " + getStringType(calcType);
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
                log.info("Can't parse type from string");
                errorOccurred = true;
                return  UNKNOWN;
        }
    }

    private LocalDate parseDate(String[] arrDate) {
        int year;
        String strDate;
        try {
            switch (arrDate.length) {
                //day.month.year
                case 1:
                    return LocalDate.parse(arrDate[0].subSequence(0, arrDate[0].length()), pointedFormat);
                //day month(text)
                case 2:
                    strDate  = arrDate[0] + " " + arrDate[1] + " " + LocalDate.now().getYear();
                    return LocalDate.parse(strDate, textFormL);
                //day month(text) year
                case 3:
                    strDate  = arrDate[0] + " " + arrDate[1] + " " + arrDate[2];
                    DateTimeFormatter textFormat2;
                    if (arrDate[2].length() > 2) {
                        return LocalDate.parse(strDate, textFormL);
                    } else {
                        return LocalDate.parse(strDate, textFormS);
                    }
                default:
                    log.info("Can't parse date in this format");
                    errorOccurred = true;
                    return LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            errorOccurred = true;
            log.log(Level.SEVERE, "Failed to parse pointed date: ", e);
            return LocalDate.now();
        } catch (DateTimeException e) {
            errorOccurred = true;
            log.log(Level.SEVERE, "Failed to create LocalDate from input: ", e);
            return LocalDate.now();
        } catch (IllegalArgumentException e) {
            log.log(Level.INFO, "Error date parse pattern : ", e);
            return LocalDate.now();
        }
    }

    private static String getStringType(int type) {
        switch (type) {
            case IN_DAY:
                return  "день";
            case IN_NIGHT:
                return  "ночь";
            case FIRST_FREE:
                return  "отсыпной";
            case SECOND_FREE:
                return "выходной";
            default :
                log.fine("Can't get string type");
                return "";
        }
    }

}