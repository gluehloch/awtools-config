/*
 * $Id: PropertyHolder.java 2319 2010-07-30 13:46:58Z andrewinkler $
 * ============================================================================
 * Project awtools-config
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

/**
 * Verwaltet die Benutzerzeigenschaften. Die Eigenschaften ermitteln sich
 * aus 3 Informationsquellen:
 * <ul>
 *   <li>Laden aus dem Klassenpfad.</li>
 *   <li>Laden aus dem Benutzerverzeichnis.</li>
 *   <li>Laden aus den Systemeinstellungen.</li>
 * </ul>
 * Die dateibasierten Quellen kann ein Dateiname angegeben werden.
 *
 * @version $LastChangedRevision: 2319 $ $LastChangedDate: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 */
public final class PropertyHolder implements GlueConfig {

    /** Der Logger der Klasse. */
    private final Logger log = org.slf4j.LoggerFactory.getLogger(PropertyHolder.class);

    /** Die Eigenschaften aus <code>java.lang.System</code>. */
    private GlueConfig systemProperties;

    /** Die Eigenschaften aus dem Heimatverzeichnis des Anwenders. */
    private GlueConfig userHomeProperties;

    /** Die Eigenschaften aus dem Klassenpfad der Anwendung. */
    private GlueConfig classpathProperties;

    /** Die zusammengeführten Eigenschaften. */
    private CombinedGlueConfig properties;

    /**
     * Konstruktor.
     *
     * @param _userHomeFileName Der Name der Eigenschaftendatei, die in
     *     <b>USER_HOME</b> zu finden ist.
     * @param _classpathFileName Der Name der Eigenschaftendatei, die im
     *     <b>CLASSPATH</b> zu finden ist.
     */
    public PropertyHolder(final String _userHomeFileName,
            final String _classpathFileName) {

        this(_userHomeFileName, _classpathFileName, PropertyHolder.class);
    }

    /**
     * Konstruktor.
     *
     * @param _userHomeFileName Der Name der Eigenschaftendatei, die in
     *     <b>USER_HOME</b> zu finden ist.
     * @param _classpathFileName Der Name der Eigenschaftendatei, die im
     *     <b>CLASSPATH</b> zu finden ist.
     * @param _classLoader Der Classloader dieser Klasser verwenden.
     */
    public PropertyHolder(final String _userHomeFileName,
            final String _classpathFileName, final Class<?> _classLoader) {

        Validate.isTrue(StringUtils.isNotBlank(_userHomeFileName));
        Validate.isTrue(StringUtils.isNotBlank(_classpathFileName));
        Validate.notNull(_classLoader);

        userHomeFileName = _userHomeFileName;
        classpathFileName = _classpathFileName;
        classLoader = _classLoader;
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#load()
     */
    public void load() {
        loadClasspath();
        loadHomepath();

        properties = new CombinedGlueConfig();
        systemProperties = new SystemGlueConfig();
        properties.addConfig(systemProperties);
        properties.addConfig(userHomeProperties);
        properties.addConfig(classpathProperties);
        try {
            properties.load();
        } catch (IOException ex) {
            log.debug("Some io-exceptions", ex);
        }

        if (log.isDebugEnabled()) {
            writeDebugInfos("UserHome-Properties:", userHomeProperties);
            writeDebugInfos("Classpath-Properties:", classpathProperties);
        }
    }

    /**
     * Lädt die Konfiguration aus dem Home-Verzeichnis des Anwenders.
     */
    private void loadHomepath() {
        if (log.isDebugEnabled()) {
            log.debug("Load homepath resource '" + getUserHomeFileName() + "'.");
        }

        try {
            File userHomePropertyFile =
                    new File(SystemUtils.USER_HOME, getUserHomeFileName());
            if (!userHomePropertyFile.exists()) {
                userHomePropertyFile.createNewFile();
            }

            userHomeProperties =
                    new PropertiesGlueConfig(userHomePropertyFile.toURI()
                        .toURL());
        } catch (MalformedURLException ex) {
            log.debug("MalformedURLException", ex);
            throw new IllegalStateException(ex);
        } catch (IOException ex) {
            log.debug("Can´t create user_home property file!", ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Lädt die Konfiguration aus dem Klassenpfad.
     */
    private void loadClasspath() {
        URL resource = classLoader.getResource(getClasspathFileName());
        if (log.isDebugEnabled()) {
            log.debug("Load classpath resource '" + resource + "'.");
        }

        if (resource != null) {
            classpathProperties = new PropertiesGlueConfig(resource);
        } else {
            throw new IllegalStateException("Classpath resource not found!");
        }
    }

    /**
     * Schreibt Debug-Informationen.
     *
     * @param configName Der Name der Konfiguration.
     * @param config Eine Konfiguration.
     */
    private void writeDebugInfos(final String configName,
        final GlueConfig config) {

        StringBuilder sb = new StringBuilder(configName);
        sb.append(IOUtils.LINE_SEPARATOR).append(config.debugOutput());
        log.debug(sb.toString());
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#save()
     */
    public void save() throws IOException {
        userHomeProperties.save();
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getProperty(java.lang.String)
     */
    public Object getProperty(final String key) {
        return properties.getProperty(key);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getProperty(java.lang.String, java.lang.Object)
     */
    public Object getProperty(final String key, final Object defaultValue) {
        Object value = properties.getProperty(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getBool(java.lang.String)
     */
    public boolean getBool(final String key) {
        return properties.getBool(key);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getBool(java.lang.String, boolean)
     */
    public boolean getBool(final String key, final boolean defaultValue) {
        return properties.getBool(key, defaultValue);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getString(java.lang.String)
     */
    public String getString(final String key) {
        return properties.getString(key);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getString(java.lang.String, java.lang.String)
     */
    public String getString(final String key, final String defaultValue) {
        return properties.getString(key, defaultValue);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getInt(java.lang.String)
     */
    public int getInt(final String key) {
        return properties.getInt(key);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getInt(java.lang.String, int)
     */
    public int getInt(final String key, final int defaultValue) {
        return properties.getInt(key, defaultValue);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getLong(java.lang.String)
     */
    public long getLong(final String key) {
        return properties.getLong(key);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getLong(java.lang.String, long)
     */
    public long getLong(final String key, final long defaultValue) {
        return properties.getLong(key, defaultValue);
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getFile(java.lang.String)
     */
    public File getFile(final String key) {
        return new File(properties.getString(key));
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getFile(java.lang.String, java.io.File)
     */
    public File getFile(final String key, final File defaultValue) {
        return new File(properties.getString(key,
            defaultValue.getAbsolutePath()));
    }

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#setProperty(java.lang.String, java.lang.String)
     */
    public void setProperty(final String key, final String value) {
        userHomeProperties.setProperty(key, value);
    }

    // ------------------------------------------------------------------------

    /** Der Name der Datei im Benutzerverzeichnis. */
    private final String userHomeFileName;

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getUserHomeFileName()
     */
    public String getUserHomeFileName() {
        return userHomeFileName;
    }

    // ------------------------------------------------------------------------

    /** Der Name der Datei im Klassenpfad. */
    private final String classpathFileName;

    /** Der zu verwendende ClassLoader. */
    private final Class<?> classLoader;

    /* (non-Javadoc)
     * @see de.gluehloch.util.configuration.TestXyz#getClasspathFileName()
     */
    public String getClasspathFileName() {
        return classpathFileName;
    }

    // ------------------------------------------------------------------------

    public GlueConfig interpolatedConfiguration() {
        return properties.interpolatedConfiguration();
    }

    public Iterator<String> getKeyIterator() {
        return properties.getKeyIterator();
    }

    public String debugOutput() {
        return toString();
    }

    public Properties getProperties() {
        return properties.getProperties();
    }

}
