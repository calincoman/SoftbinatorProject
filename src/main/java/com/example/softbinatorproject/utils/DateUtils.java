package com.example.softbinatorproject.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class DateUtils {
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
