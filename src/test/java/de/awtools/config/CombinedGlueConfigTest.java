/*
 * $Id: CombinedGlueConfigTest.java 2319 2010-07-30 13:46:58Z andrewinkler $
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

import java.net.URL;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Klasse {@link CombinedGlueConfig}.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public class CombinedGlueConfigTest {

    private static final String[][] PROPERTIES = new String[][] {
        { "value1", "Christian" },
        { "value2", "${value1}_Bein" },
        { "test.int.10", "11" },
        { "test.int.numberFormatException" , "NumberFormatException" },
        { "test.long.10", "11" },
        { "test.long.numberFormatException", "NumberFormatException" },
        { "test.string.andre", "Andre_2" },
        { "test.boolean.true", "true" },
        { "test.boolean.false", "false" },
        { "test.file", "c:\\temp"}
    };

    private CombinedGlueConfig cc;

    @Test
    public void testCombinedGlueConfigGetAllProperties() {
        Properties copy = cc.getProperties();
        GlueConfigTestUtils.assertProperties(copy, PROPERTIES);
    }

    @Test
    public void testCombinedGlueConfigOverrideProperties() {
        assertEquals("Christian", cc.getProperty("value1"));
        assertEquals("${value1}_Bein", cc.getProperty("value2"));
        assertEquals("11", cc.getProperty("test.int.10"));
        assertEquals("Andre_2", cc.getProperty("test.string.andre"));
        assertEquals("c:\\temp", cc.getProperty("test.file"));
    }

    @Before
    public void setUp() throws Exception {
        cc = new CombinedGlueConfig();
        URL url1 = this.getClass().getResource("test_1.properties");
        GlueConfig config1 = new PropertiesGlueConfig(url1);
        URL url2 = this.getClass().getResource("test_2.properties");
        GlueConfig config2 = new PropertiesGlueConfig(url2);
        URL url3 = this.getClass().getResource("test_3.properties");
        GlueConfig config3 = new PropertiesGlueConfig(url3);

        cc.addConfig(config1);
        cc.addConfig(config2);
        cc.addConfig(config3);
        cc.load();
    }

}
