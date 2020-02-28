package com.avisfy;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.abs;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;

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
    private static final DateTimeFormatter outForm = DateTimeFormatter.ofPattern("EEE d MMM uu", loc);
    private static final  DateTimeFormatter pointedForm = DateTimeFormatter.ofPattern("d.M.yy", loc);
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
        return targetDate.format(outForm) + " " + getStrType(calcType(targetDate));
    }

    public String daysNextWeek() {
        if (errorOccurred) {
            return "";
        }
        LocalDate targetDate = LocalDate.now();
        //find first day of next week
        while (targetDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            targetDate =  targetDate.plusDays(1);
        }
        return getWeek(targetDate);
    }

    public String daysWeek(String strDate) {
        if (errorOccurred) {
            return "";
        }
        String[] dateArr = strDate.split(" ");
        LocalDate targetDate = parseDate(dateArr);
        if (errorOccurred) {
            errorOccurred =  false;
            return "";
        }
        //find first day of this week
        while (targetDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            targetDate =  targetDate.minusDays(1);
        }
        return getWeek(targetDate);
    }


    public String month(String strMonth) {
        if (errorOccurred) {
            return "";
        }
        try {
            LocalDate monthDay = parseMonth(strMonth);
            if (errorOccurred) {
                errorOccurred =  false;
                return "";
            }
            StringBuilder answer = new StringBuilder(strMonth + "\n");
            int i = 7 - DayOfWeek.SUNDAY.compareTo(monthDay.getDayOfWeek()) - 1;
            for (int j = 0; j < i; j++) {
                answer.append("             ");
            }
            Month month = monthDay.getMonth();
            while (monthDay.getMonth() == month)  {
                i++;
                if (monthDay.getDayOfMonth() < 10) {
                    answer.append(monthDay.getDayOfMonth() + " " + getStrType(calcType(monthDay)) + "   ");
                } else {
                    answer.append(monthDay.getDayOfMonth() + " " + getStrType(calcType(monthDay)) + "  ");
                }
                monthDay = monthDay.plusDays(1);
                if (i % 7 == 0) {
                    answer.append("\n");
                }
            }
            return answer.toString();
        } catch (IllegalArgumentException e ) {
            log.log(Level.SEVERE, "Failed to parse month: ", e);
            return "";
        } catch (NullPointerException e ) {
            log.log(Level.SEVERE, "Month string is null: ", e);
            return "";
        }

    }

    private String getWeek(LocalDate beginDate) {
        int typeToday;
        StringBuffer answer = new StringBuffer("");
        for (int i = 0; i < 7; i++) {
            typeToday = calcType(beginDate);
            answer.append(beginDate.format(outForm) + "   " + getStrType(typeToday) + "\n");
            beginDate =  beginDate.plusDays(1);
        }
        return answer.toString();
    }

    private int calcType(LocalDate date) {
        int daysDiff = (int) DAYS.between(initDate, date);
        int type = initType;
        int part  = (int)daysDiff % 4;
        if (daysDiff > 0) {
            for (int i = 0; i < part; i++) {
                type = (type == SECOND_FREE) ? IN_DAY : type + 1;
            }
        } else //if initial date > date
            {
            System.out.println("in rev");
            for (int i = 0; i < -part; i++) {
                type = (type == IN_DAY) ? SECOND_FREE: type - 1;
            }
        }
        return type;
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

    private LocalDate parseMonth(String strMonth) {
        Month month;
        switch (strMonth) {
            case "январь":
                month = Month.JANUARY;
                break;
            case "февраль":
                month = Month.FEBRUARY;
                break;
            case "март":
                month = Month.MARCH;
                break;
            case "апрель":
                month = Month.APRIL;
                break;
            case "май":
                month = Month.MAY;
                break;
            case "июнь":
                month = Month.JUNE;
                break;
            case "июль":
                month = Month.JULY;
                break;
            case "август":
                month = Month.AUGUST;
                break;
            case "сентябрь":
                month = Month.SEPTEMBER;
                break;
            case "октябрь":
                month = Month.OCTOBER;
                break;
            case "ноябрь":
                month = Month.NOVEMBER;
                break;
            case "декабрь":
                month = Month.DECEMBER;
                break;
            default:
                errorOccurred = true;
                log.info("Unknown month in date parsing");
                return LocalDate.now();
        }
        return LocalDate.of(initDate.getYear(), month, 1);
    }

    private LocalDate parseDate(String[] arrDate) {
        int year;
        String strDate;
        try {
            switch (arrDate.length) {
                //day.month.year
                case 1:
                    return LocalDate.parse(arrDate[0].subSequence(0, arrDate[0].length()), pointedForm);
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

    private static String getStrType(int type) {
        switch (type) {
            case IN_DAY:
                return  "день    ";
            case IN_NIGHT:
                return  "ночь    ";
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