/*
 * $Id: PropertyReaderTest.java 2319 2010-07-30 13:46:58Z andrewinkler $
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

/**
 * Testet das Auslesen der Property-Dateien.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 2319 $ $LastChangedDate: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public class PropertyReaderTest {

    private static final String PROPERTY_FILE =
            "hibernate-mysql-test.properties";

    /**
     * Der Test mit {@link Properties} funktioniert.
     *
     * @throws Exception Da ging was schief.
     */
    @Test
    public void testReadPropertyFiles() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(PROPERTY_FILE);
        assertNotNull(is);
        Properties props = new Properties();
        props.load(is);

        assertEquals("sportwetten",
            props.getProperty("hibernate.connection.username"));
        assertEquals("sportwetten",
            props.getProperty("hibernate.connection.password"));
        assertEquals("jdbc:mysql://localhost/pirates-test",
            props.getProperty("hibernate.connection.url"));
        assertEquals("com.mysql.jdbc.Driver",
            props.getProperty("hibernate.connection.driver_class"));
        assertEquals("org.hibernate.dialect.MySQLDialect",
            props.getProperty("hibernate.dialect"));
    }

    /**
     * Genau der gleiche Test funktioniert im Projekt gluehloch-util. Verläßt
     * der Code das Projekt und wird z.B. hier ausgeführt, liefert der Test
     * einen Fehler zurück.
     */
    @Test
    public void testReadPropertyFileByPropertyHolder() {
        PropertyHolder ph =
                new PropertyHolder("delete_me.properties",
                    PROPERTY_FILE,
                    this.getClass());
        ph.load();
        GlueConfig gc = ph.interpolatedConfiguration();

        assertEquals("sportwetten",
            gc.getProperty("hibernate.connection.username"));
        assertEquals("sportwetten",
            gc.getProperty("hibernate.connection.password"));
        assertEquals("DIALEKT: org.hibernate.dialect.MySQLDialect",
            gc.getProperty("test"));
    }

}
