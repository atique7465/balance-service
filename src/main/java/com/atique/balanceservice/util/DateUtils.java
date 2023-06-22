package com.atique.balanceservice.util;

import com.atique.balanceservice.exceptionresolvers.enums.ErrorCode;
import com.atique.balanceservice.exceptionresolvers.exceptions.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author atiQue
 * @since 15'Jun 2023 at 12:25 AM
 */

@Service
@Slf4j
public class DateUtils {

    public static Date convertToDate(String dateStr, String format) {

        if (!StringUtils.hasLength(dateStr) || !StringUtils.hasLength(format)) {
            log.error("Empty dateStr or format provided");
            throw new InvalidDataException();
        }

        Date date;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException ex) {
            log.error("Date parse exception occurred. expected format: {} found: {}", format, dateStr);
            throw new InvalidDataException(ErrorCode.INVALID_DATE_FORMAT, ex);
        }

        return date;
    }

    public static String convertToString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String convertDateStrFormat(String dateStr, String currFormat, String newFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
        return sdf.format(convertToDate(dateStr, currFormat));
    }
}
