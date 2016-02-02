/*
 * $Id: MapGlueConfig.java 2319 2010-07-30 13:46:58Z andrewinkler $
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;

/**
 * Eine In-Memory-Konfiguration. Diese {@link GlueConfig} Implementierung hat
 * keine persistente Repräsentation.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public final class MapGlueConfig extends AbstractGlueConfig {

    /** Die interne Eigenschaftsverwaltung. */
    private final Map<String, String> map = new HashMap<String, String>();

    /**
     * Konstruktor. Ohne Eigenschaften.
     */
    public MapGlueConfig() {
    }

    /**
     * Konstruktor.
     *
     * @param properties Die zu übernehmenden Eigenschaften.
     */
    public MapGlueConfig(final Map<String, String> properties) {
        map.putAll(properties);
    }

    @Override
    protected Object doGetProperty(final String key) {
        return (map.get(key));
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getKeyIterator() {
        return (IteratorUtils.unmodifiableIterator(map.keySet().iterator()));
    }

    public void load() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void save() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void setProperty(final String key, final String value) {
        map.put(key, value);
    }

}
