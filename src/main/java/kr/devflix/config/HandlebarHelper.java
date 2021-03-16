package kr.devflix.config;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@HandlebarsHelper
public class HandlebarHelper {

    public CharSequence eq(final Object arg1, final Object arg2, final Options options) throws IOException {
        if (StringUtils.equals(String.valueOf(arg1), String.valueOf(arg2))) {
            return options.fn();
        } else {
            return options.inverse();
        }
    }

    public CharSequence notEq(final Object arg1, final Object arg2, final Options options) throws IOException {
        if (StringUtils.equals(String.valueOf(arg1), String.valueOf(arg2))) {
            return options.inverse();
        } else {
            return options.fn();
        }
    }

    public String textOverflow(final String str, int size) {
        if (str.length() > size) {
            return str.substring(0, size) + "...";
        } else {
            return str;
        }
    }

    public CharSequence isNewPost(final Date date, final Options options) throws IOException {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());
        currentDate.add(Calendar.DATE, -2);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.compareTo(currentDate) <= 0) {
            return options.inverse();
        } else {
            return options.fn();
        }
    }

    public String df(final Date date, final String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);

        return dateFormat.format(date);
    }

    public int sub(int arg1, int arg2) {
        return arg1 - arg2;
    }

    public String unEscape(String arg) {
        return StringEscapeUtils.unescapeHtml4(arg);
    }

    public String removeHtmlTag(String arg) {
        if (StringUtils.isBlank(arg)) {
            return "";
        }

        return Jsoup.parse(StringEscapeUtils.unescapeHtml4(arg)).text();
    }

    public Object getItem(List<Object> arg1, final int index) {
        if (arg1.size() > 0 && arg1.size() > index) {
            return arg1.get(index);
        }

        return null;
    }

    public CharSequence contain(List<Object> arg1, final Object arg2, final Options options) throws IOException {
        if (arg1.contains(arg2)) {
            return options.fn();
        } else {
            return options.inverse();
        }
    }

    public CharSequence eqList(final String context, final Options options) throws IOException {
        for (Object obj : options.params) {
            if (StringUtils.equals(context, (CharSequence) obj)) {
                return options.fn();
            }
        }

        return options.inverse();
    }
}
