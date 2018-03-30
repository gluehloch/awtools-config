/*
 * ============================================================================
 * Project awtools-config Copyright (c) 2000-2016 by Andre Winkler. All rights
 * reserved.
 * ============================================================================
 * GNU LESSER GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING,
 * DISTRIBUTION AND MODIFICATION
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package de.awtools.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet das Auslesen der Benutzerkonfiguration.
 * 
 * @author by Andre Winkler
 */
public class PropertyHolderTest {

    private File userHomeFile;

    /**
     * Testet das Auslesen der Benutzerkonfiguration aus verschiedenen
     * Dateiquellen.
     *
     * @throws Exception
     *             Da ging was schief.
     */
    @Test
    public void testLoadPropertyFiles() throws Exception {
        PropertyHolder config = new PropertyHolder(
                "util-user-home-test.properties",
                "/property-holder-test.properties");
        config.load();

        // "file1" stammt aus der Systemumgebung und überschreibt den Eintrag
        // in der Property Datei im USER_HOME Verzeichnis.
        assertEquals("aus dem System", config.getProperty("file1"));

        // "file2" stammt aus der Property Datei im USER_HOME Verzeichnis.
        assertEquals("Datei2", config.getProperty("file2"));

        // "file2" stammt aus der Property Datei im USER_HOME Verzeichnis.
        assertEquals("Datei3", config.getProperty("file3"));

        // "file4" stammt aus der Property Datei im Klasssenpfad.
        assertEquals("old_file_4", config.getProperty("file4"));

        // "classpathvalue1" stammt aus der Property Datei im Klassenpfad
        // und wird durch die Datei in USER_HOME überschrieben.
        assertEquals("Bin_überschrieben_in_User_Home",
                config.getProperty("classpathvalue1"));

        // "Aus_dem_System" stammt aus der Systemumgebung.
        assertEquals("Wert aus dem System",
                config.getProperty("Aus_dem_System"));
    }

    @Test
    public void testSavePropertyFileToHomeDirectory() throws Exception {
        PropertyHolder config = new PropertyHolder(
                "util-user-home-test.properties",
                "/property-holder-test.properties");
        config.load();

        config.setProperty("winkler", "andre");
        config.save();
        config.load();

        assertThat(config.getString("winkler"), equalTo("andre"));
    }

    @Before
    public void setUp() throws IOException {
        userHomeFile = new File(SystemUtils.USER_HOME,
                "util-user-home-test.properties");
        File propertyFile = new File(
                "src/test/resources/de/awtools/config/copy-to-user-home.properties");
        FileUtils.copyFile(propertyFile, userHomeFile);

        System.setProperty("file1", "aus dem System");
        System.setProperty("Aus_dem_System", "Wert aus dem System");
    }

    @After
    public void tearDown() {
        userHomeFile.delete();
    }

}
