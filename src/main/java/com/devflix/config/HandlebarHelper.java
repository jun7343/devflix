package com.devflix.config;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ObjectUtils;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@HandlebarsHelper
public class HandlebarHelper {

    public CharSequence eq(final Object arg1, final Object arg2, final Options options) throws IOException {
        if (ObjectUtils.notEqual(arg1, arg2)) {
            return options.inverse();
        } else {
            return options.fn();
        }
    }

    public CharSequence notEq(final Object arg1, final Object arg2, final Options options) throws IOException {
        if (ObjectUtils.notEqual(arg1, arg2)) {
            return options.fn();
        } else {
            return options.inverse();
        }
    }

    public String textOverflow(final String str) {
        if (str.length() > 100) {
            return str.substring(0, 100) + "...";
        } else {
            return str;
        }
    }

    public CharSequence isNewPost(final Date date, final Options options) throws IOException {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -15);

        Calendar currentDate = Calendar.getInstance();

        calendar.setTime(date);

        System.out.println("dd = " + calendar.compareTo(currentDate));

        if (calendar.compareTo(currentDate) > 0) {
            return options.inverse();
        } else {
            return options.fn();
        }
    }

    public String df(final Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);

        return dateFormat.format(date);
    }
}
