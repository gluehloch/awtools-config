/*
 * $Id: PropertiesGlueConfigTest.java 2319 2010-07-30 13:46:58Z andrewinkler $
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
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testet die Klasse {@link PropertiesGlueConfig} 
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 2319 $ $Date: 2010-07-30 15:46:58 +0200 (Fr, 30 Jul 2010) $
 */
public class PropertiesGlueConfigTest {

    private static final String[][] PROPERTIES =
            new String[][] {
                    { "value1", "Andre" },
                    { "value2", "${value1}_Winkler" },
                    { "test.boolean.true", "true" },
                    { "test.boolean.false", "false" },
                    { "test.file", "c:\\temp" },
                    { "test.int.10", "10" },
                    { "test.int.numberFormatException", "NumberFormatException" },
                    { "test.long.10", "10" },
                    { "test.long.numberFormatException",
                            "NumberFormatException" },
                    { "test.string.andre", "Andre" } };

    private PropertiesGlueConfig config;

    @Test
    public void testPropertiesGlueConfigDebugOutput() {
        String str = config.debugOutput();
        Assert.assertNotNull(str);
    }

    @Test
    public void testPropertiesGlueConfigLoadSave() throws Exception {
        InputStream is = config.getURL().openStream();

        File copyFile = File.createTempFile("glueconfigtest", "properties");        
        FileOutputStream out = new FileOutputStream(copyFile);

        try {
            IOUtils.copy(is, out);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(out);
        }

        PropertiesGlueConfig configCopy =
                new PropertiesGlueConfig(copyFile.toURI().toURL());
        configCopy.load();
        GlueConfigTestUtils.assertProperties(configCopy, PROPERTIES);

        configCopy.setProperty("test.add.new.property2", "value");
        configCopy.save();

        PropertiesGlueConfig configCopy2 =
            new PropertiesGlueConfig(copyFile.toURI().toURL());
        configCopy2.load();
        GlueConfigTestUtils.assertProperties(configCopy2, PROPERTIES);
        Assert.assertEquals("value", configCopy2.getProperty("test.add.new.property2"));
    }

    @Test
    public void testPropertiesGlueConfigSetter() {
        config.setProperty("test.add.new.property", "value");
        Assert.assertEquals("value", config.getString("test.add.new.property"));
    }

    @Test
    public void testPropertiesGlueConfigGetter() {
        Properties props = config.getProperties();
        for (int index = 0; index < PROPERTIES.length; index++) {
            String key = PROPERTIES[index][0];
            Assert.assertEquals(PROPERTIES[index][1], props.get(key));
        }

        GlueConfigTestUtils.assertProperties(config, PROPERTIES);
    }

    @Test
    public void testPropertiesGlueConfigTypeGetter() {
        Assert.assertEquals(false, config.getBool("test.boolean.undefined"));
        Assert.assertEquals(true, config.getBool("test.boolean.true"));
        Assert.assertEquals(true, config.getBool("test.boolean.true", true));
        Assert.assertEquals(true, config.getBool("test.boolean.true", false));
        Assert.assertEquals(false, config.getBool("test.boolean.false"));
        Assert.assertEquals(false, config.getBool("test.boolean.false", true));
        Assert.assertEquals(false, config.getBool("test.boolean.false", false));
        Assert.assertEquals(false, config.getBool("test.boolean.undefined", false));
        Assert.assertEquals(true, config.getBool("test.boolean.undefined", true));

        Assert.assertEquals(new File("c:\\temp"), config.getFile("test.file"));
        Assert.assertEquals(new File("c:\\temp"), config.getFile("test.file",
            new File("c:\\undefined")));
        Assert.assertEquals(new File("c:\\undefined"), config.getFile(
            "test.file.undefined", new File("c:\\undefined")));

        Assert.assertEquals(10, config.getInt("test.int.10"));
        Assert.assertEquals(10, config.getInt("test.int.10", 20));
        Assert.assertEquals(0, config.getInt("test.int.undefined"));
        Assert.assertEquals(10, config.getInt("test.int.undefined", 10));
        Assert.assertEquals(0, config.getInt("test.int.numberFormatException"));

        Assert.assertEquals(10, config.getLong("test.long.10"));
        Assert.assertEquals(10, config.getLong("test.long.10", 20));
        Assert.assertEquals(0, config.getLong("test.long.undefined"));
        Assert.assertEquals(10, config.getLong("test.long.undefined", 10));
        Assert.assertEquals(0, config.getLong("test.long.numberFormatException"));

        Assert.assertEquals("Andre", config.getString("test.string.andre"));
        Assert.assertEquals("Andre", config.getString("test.string.andre", "irgendwas"));
        Assert.assertEquals("Lars", config.getString("test.string.undefined", "Lars"));

        Assert.assertEquals("Andre", config.getProperty("test.string.andre"));
        Assert.assertEquals("Andre", config.getProperty("test.string.andre", "irgendwas"));
        Assert.assertEquals("Lars", config.getProperty("test.string.undefined", "Lars"));
    }

    @Before
    public void setUp() throws Exception {
        URL testResource = this.getClass().getResource("test.properties");
        config = new PropertiesGlueConfig(testResource);
        config.load();
    }

}
