/*
 * $Id: GlueConfig.java 2319 2010-07-30 13:46:58Z andrewinkler $
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
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Die Schnittstelle für eine Konfiguration.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public interface GlueConfig {

    /**
     * Laden der Eigenschaften.
     *
     * @throws IOException Fehler bei Laden der Konfiguration.
     */
    public void load() throws IOException;

    /**
     * Speichert die vom Benutzer veränderten Eigenschaften im
     * Benutzerverzeichnis ab.
     *
     * @throws IOException Fehler bei Speichern der Konfiguration.
     */
    public void save() throws IOException;

    /**
     * Liefert die geladenen Eigenschaften als unveränderliche Map zurück.
     *
     * @return Die geladenen Eigenschaften.
     */
    public Properties getProperties();

    /**
     * Liefert den Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public Object getProperty(final String key);

    /**
     * Liefert den Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
     * @return Der Wert der Eigenschaft.
     */
    public Object getProperty(final String key, final Object defaultValue);

    /**
     * Liefert einen Iterator über alle Schlüssel der Konfiguration.
     *
     * @return Ein Iterator über alle Schlüssel.
     */
    public Iterator<String> getKeyIterator();

    /**
     * Liefert den Boolean-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public boolean getBool(final String key);

    /**
     * Liefert den Boolean-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
     * @return Der Wert der Eigenschaft.
     */
    public boolean getBool(final String key, final boolean defaultValue);

    /**
     * Liefert den String-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public String getString(final String key);

    /**
     * Liefert den String-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
     * @return Der Wert der Eigenschaft.
     */
    public String getString(final String key, final String defaultValue);

    /**
     * Liefert den int-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public int getInt(final String key);

    /**
     * Liefert den int-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
     * @return Der Wert der Eigenschaft.
     */
    public int getInt(final String key, final int defaultValue);

    /**
     * Liefert den long-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public long getLong(final String key);

    /**
     * Liefert den long-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
     * @return Der Wert der Eigenschaft.
     */
    public long getLong(final String key, final long defaultValue);

    /**
     * Liefert den File-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @return Der Wert der Eigenschaft.
     */
    public File getFile(final String key);

    /**
     * Liefert den File-Wert einer Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param defaultValue Dieser Wert wird geliefert, wenn kein Wert unter
     *     dem Schlüssel <code>key</code> gefunden werden kann.
    
     * @return Der Wert der Eigenschaft.
     */
    public File getFile(final String key, final File defaultValue);

    /**
     * Setzt eine Eigenschaft.
     *
     * @param key Der Schlüssel der Eigenschaft.
     * @param value Der Wert der Eigenschaft.
     */
    public void setProperty(final String key, final String value);

    /**
     * Liefert eine Konfiguration in der alle Platzhalter ${...} durch ihre
     * Werte ersetzt sind.
     *
     * @return Eine Konfiguration ohne Platzhalter.
     */
    public GlueConfig interpolatedConfiguration();

    /**
     * Ausgaben zu Debugging-Zwecken.
     *
     * @return Eine String-Repräsentation des Zustands.
     */
    public String debugOutput();
 
}
