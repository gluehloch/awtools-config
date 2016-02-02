/*
 * $Id: AbstractGlueConfig.java 2319 2010-07-30 13:46:58Z andrewinkler $
 * ============================================================================
 * Project awtools-config
 * Copyright (c) 2004-2010 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU LESSER GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.awtools.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility Klasse für {@link GlueConfig} Implementierungen.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
abstract class AbstractGlueConfig implements GlueConfig {

    /** Ein Logger für die Klasse. */
    private final Logger log = LoggerFactory.getLogger(AbstractGlueConfig.class);

    public boolean getBool(final String key) {
        return (getBool(key, false));
    }

    public final boolean getBool(final String key, final boolean defaultValue) {
        String bool = getString(key);
        boolean result = defaultValue;
        if (StringUtils.isNotBlank(bool)) {
            result = Boolean.parseBoolean(bool);
        }
        return result;
    }

    public final File getFile(final String key) {
        return (getFile(key, null));
    }

    public final File getFile(final String key, final File defaultValue) {
        String value = getString(key);
        File result = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            result = new File(value);
        }
        return result;
    }

    public final int getInt(final String key) {
        return (getInt(key, 0));
    }

    public final int getInt(final String key, final int defaultValue) {
        String value = getString(key);
        int result = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                log.debug("NumberFormatException", ex);
            }
        }
        return result;
    }

    public final long getLong(final String key) {
        return (getLong(key, 0));
    }

    public final long getLong(final String key, final long defaultValue) {
        String value = getString(key);
        long result = defaultValue;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = Long.parseLong(value);
            } catch (NumberFormatException ex) {
                log.debug("NumberFormatException", ex);
            }
        }
        return result;
    }

    public final String getString(final String key) {
        return (getString(key, null));
    }

    public final String getString(final String key, final String defaultValue) {
        Object result = getProperty(key);
        return ((result == null) ? defaultValue : result.toString());
    }

    public final Object getProperty(final String key) {
        return (getProperty(key, null));
    }

    public final Object getProperty(final String key, final Object defaultValue) {
        Object value = doGetProperty(key);
        return ((value == null) ? defaultValue : value);
    }

    public final Properties getProperties() {
        Properties clone = new Properties();
        for (Iterator<String> i = getKeyIterator(); i.hasNext();) {
            String key = i.next();
            Object value = getProperty(key);
            if (value != null) {
                clone.put(key, value);
            }
        }
        return clone;
    }

    public final GlueConfig interpolatedConfiguration() {
        Map<String, String> substitutes = new HashMap<String, String>();
        for (Iterator<String> i = getKeyIterator(); i.hasNext();) {
            String key = i.next();
            substitutes.put(key, getString(key));
        }

        for (String key : substitutes.keySet()) {
            String value = substitutes.get(key);
            value = AbstractGlueConfig.replacePlaceholder(value, substitutes);
            if (value != null) {
                substitutes.put(key, value);
            }
        }

        return (new MapGlueConfig(substitutes));
    }

    public String debugOutput() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> i = getKeyIterator(); i.hasNext();) {
            String key = i.next();
            Object value = getProperty(key);
            sb.append(key).append(" = ").append(value).append(
                IOUtils.LINE_SEPARATOR);            
        }
        return sb.toString();
    }

    /**
     * Verwaltet den internen Zugang zu den Eigenschaften.
     * 
     * @param key Der Schlüssel.
     * @return Die gefundene Eigenschaft 
     */
    protected abstract Object doGetProperty(final String key);

    // --------------------------------------------------------------------------------------------

    /**
     * Ersetzt die ${...} Platzhalter in einem String. Die Ersetzung werden
     * in einer Map gelagert. Die Schlüssel repräsentieren die Platzhalter
     * im String. Die Ersetzungen sind die Werte der Schlüssel in der
     * <code>placeholders</code> Map.
     *
     * @param string Der zu prüfende String.
     * @param placeholders Die Ersetzungen.
     * @return Der überarbeitete String.
     */
    public static String replacePlaceholder(final String string,
        final Map<String, String> placeholders) {

        String result = string;
        String[] keys = StringUtils.substringsBetween(string, "${", "}");

        if (keys != null) {
            for (String key : keys) {
                String value = placeholders.get(key);
                if (value != null) {
                    StringBuilder sb =
                            new StringBuilder("${").append(key).append("}");
                    result = StringUtils.replace(result, sb.toString(), value);
                }
            }
        }

        return result;
    }

}
