/*
 * $Id: PropertiesGlueConfig.java 2319 2010-07-30 13:46:58Z andrewinkler $
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Eine Konfiguration die im Hintergrund ein {@link Properties} Objekt
 * verwendet.
 * 
 * @author Andre Winkler
 */
public final class PropertiesGlueConfig extends AbstractGlueConfig {

    /** Die interne Repräsentation der Eigenschaften. */
    private final Properties properties = new Properties();

    /** Die Ablage der Eigenschaft. */
    private final URL propertiesResource;

    /**
     * Konstruktor.
     *
     * @param _propertiesResource Die URL der Ablage.
     */
    public PropertiesGlueConfig(final URL _propertiesResource) {
        propertiesResource = _propertiesResource;
    }

    /**
     * Liefert die URL der zugeordnetebn Property-Datei.
     *
     * @return Die URL der zugeordneten Property-Datei.
     */
    URL getURL() {
        return propertiesResource;
    }

    public void setProperty(final String key, final String value) {
        properties.setProperty(key, value);
    }

    /**
     * Verwaltet den internen Zugang zu der Eigenschaft
     * {@link PropertiesGlueConfig#properties}.
     * 
     * @param key Die gesuchte Eigenschaft zu diesem Schlüssel.
     * @return Die gefundene Eigenschaft 
     */
    @Override
    protected Object doGetProperty(final String key) {
        Object result = null;
        String value = properties.getProperty(key);
        if (StringUtils.isNotBlank(value)) {
            result = value;
        }
        return result;
    }

    public void load() throws IOException {
        InputStream is = null;
        try {
            is = propertiesResource.openStream();
            properties.clear();
            properties.load(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void save() throws IOException {
        Date date = new Date();
        String tmp = DateFormat.getDateInstance().format(date);

        if (propertiesResource.getProtocol().equals("file")) {
            File outfile = new File(propertiesResource.getFile());
            OutputStream out = null;
            try {
                out = new FileOutputStream(outfile);
                properties.store(out, "Saved on: " + tmp);
            } finally {
                IOUtils.closeQuietly(out);
            }
        } else {
            throw new IllegalStateException("Unsupported URL protocol: "
                + propertiesResource.getProtocol());
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getKeyIterator() {
        Iterator<String> unmodifiableIterator = (Iterator<String>)
                IteratorUtils.unmodifiableIterator(properties.keySet()
                    .iterator());
        return unmodifiableIterator;
    }

}
