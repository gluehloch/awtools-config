/*
 * $Id: PropertyPlaceholderTest.java 2319 2010-07-30 13:46:58Z andrewinkler $
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

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

/**
 * Testet das Property-Replacement.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public class PropertyPlaceholderTest {

    private PropertiesGlueConfig config;
    private GlueConfig interpolated;

    @Before
    public void setUp() throws IOException {
        URL resource =
                getClass().getResource(
                    "/de/awtools/config/test.properties");
        config = new PropertiesGlueConfig(resource);
        config.load();
        interpolated = config.interpolatedConfiguration();
    }

    @Test
    public void testPropertyReplacement() throws Exception {
        assertEquals("Andre", interpolated.getProperty("value1"));
        assertEquals("Andre_Winkler", interpolated.getProperty("value2"));
    }

    @Test
    public void testCommonsConfiguration() throws Exception {
        GlueConfig config2 = config.interpolatedConfiguration();

        assertEquals("Andre", config.getProperty("value1"));
        assertEquals("${value1}_Winkler", config.getProperty("value2"));

        assertEquals("Andre", config2.getProperty("value1"));
        assertEquals("Andre_Winkler", config2.getProperty("value2"));
    }

}
