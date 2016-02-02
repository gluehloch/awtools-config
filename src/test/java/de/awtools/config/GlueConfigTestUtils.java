/*
 * $Id: GlueConfigTestUtils.java 2319 2010-07-30 13:46:58Z andrewinkler $
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

import java.util.Properties;

/**
 * Utility Klasse für Testfälle im Zusammenhang mit {@link GlueConfig}.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public class GlueConfigTestUtils {

    /**
     * Vergleicht die {@link GlueConfig} mit der erwarteten Menge an
     * Eigenschaften.
     *
     * @param _config Die zu prüfende Konfiguration.
     * @param properties Die erwarteten Eigenschaften.
     */
    public static void assertProperties(final GlueConfig _config,
        final String[][] properties) {

        for (int index = 0; index < properties.length; index++) {
            String key = properties[index][0];
            assertEquals(properties[index][1], _config.getProperty(key));
        }
    }

    /**
     * Vergleicht die {@link GlueConfig} mit der erwarteten Menge an
     * Eigenschaften.
     *
     * @param _properties Die zu prüfende Eigenschaften.
     * @param properties Die erwarteten Eigenschaften.
     */
    public static void assertProperties(final Properties _properties,
        final String[][] properties) {

        for (int index = 0; index < properties.length; index++) {
            String key = properties[index][0];
            assertEquals(properties[index][1], _properties.getProperty(key));
        }
    }

}
