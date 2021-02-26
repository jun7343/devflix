package com.devflix.config;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ObjectUtils;
import pl.allegro.tech.boot.autoconfigure.handlebars.HandlebarsHelper;

import java.io.IOException;

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
}
