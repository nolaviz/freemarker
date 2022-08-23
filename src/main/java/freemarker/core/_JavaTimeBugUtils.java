/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package freemarker.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import freemarker.log.Logger;

/**
 * Used internally only, might change without notice!
 * To work around with java.time bugs.
 */
public final class _JavaTimeBugUtils {
    private static final Logger LOG = Logger.getLogger("freemarker.runtime");
    // There are around 160 predefined locales, so this should be enough even if the application uses some variants.
    private static final int HAS_GOOD_STANDALONE_MONTH_LEAK_ALERT_CACHE_SIZE = 1024;

    private static final LocalDate PROBE_LOCAL_DATE = LocalDate.of(2021, 12, 1);

    private static final ConcurrentHashMap<Locale, Boolean> HAS_GOOD_SHORT_STANDALONE_MONTH
            = _JavaVersion.FEATURE == 8 ? new ConcurrentHashMap<>() : null;
    private static final ConcurrentHashMap<Locale, Boolean> HAS_GOOD_FULL_STANDALONE_MONTH
            = _JavaVersion.FEATURE == 8 ? new ConcurrentHashMap<>() : null;

    private _JavaTimeBugUtils() {
        throw new AssertionError();
    }

    /**
     * To deal with https://bugs.openjdk.java.net/browse/JDK-8146356
     */
    public static boolean hasGoodShortStandaloneMonth(Locale locale) {
        return hasGoodStandaloneMonth(
                locale,
                TextStyle.SHORT_STANDALONE, _JavaTimeBugUtils.HAS_GOOD_SHORT_STANDALONE_MONTH);
    }

    /**
     * To deal with https://bugs.openjdk.java.net/browse/JDK-8146356
     */
    public static boolean hasGoodFullStandaloneMonth(Locale locale) {
        return hasGoodStandaloneMonth(
                locale,
                TextStyle.FULL_STANDALONE,
                _JavaTimeBugUtils.HAS_GOOD_FULL_STANDALONE_MONTH);
    }

    private static boolean hasGoodStandaloneMonth(
            Locale locale, TextStyle textStyle,
            ConcurrentHashMap<Locale, Boolean> cacheMap) {
        if (cacheMap == null) {
            // We are using a Java version where the standalone formatting bug was already fixed
            return true;
        }

        Boolean good = cacheMap.get(locale);
        if (good != null) {
            return good;
        }

        if (cacheMap.size() >= HAS_GOOD_STANDALONE_MONTH_LEAK_ALERT_CACHE_SIZE) {
            boolean triggered = false;
            synchronized (_JavaTimeBugUtils.class) {
                if (cacheMap.size() >= HAS_GOOD_STANDALONE_MONTH_LEAK_ALERT_CACHE_SIZE) {
                    triggered = true;
                    cacheMap.clear();
                }
            }
            if (triggered) {
                LOG.warn("Global HAS_GOOD_STANDALONE_MONTH cache for " + textStyle
                        + " has exceeded " + HAS_GOOD_STANDALONE_MONTH_LEAK_ALERT_CACHE_SIZE
                        + " entries => cache flushed. "
                        + "Typical cause: Something generates high variety of Locale objects.");
            }
        }

        String formattedMonth = new DateTimeFormatterBuilder()
                .appendText(ChronoField.MONTH_OF_YEAR, textStyle)
                .toFormatter(locale)
                .format(PROBE_LOCAL_DATE);
        good = !formattedMonth.equals("12");
        cacheMap.put(locale, good);
        return good;
    }
}