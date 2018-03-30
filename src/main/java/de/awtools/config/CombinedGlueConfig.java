/*
 * ============================================================================
 * Project awtools-config Copyright (c) 2004-2016 by Andre Winkler. All rights
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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Verwaltet mehrere {@link GlueConfig}s. Eine Konfiguration überschreibt ggf.
 * die Eigenschaften der nachfolgenden Konfiguration.
 * 
 * @author Andre Winkler
 */
public final class CombinedGlueConfig extends AbstractGlueConfig {

    /**
     * Die Liste der verwalteten Konfigurationen. Die Reihenfolge definiert eine
     * Ordnung über die Konfigurationen. Auf der Suche nach einer Eigenschaft
     * wird mit der Konfiguration an Index 0 begonnen. Ist diese Eigenschaft
     * nicht definiert, wird die Suche mit der Konfiguration an Stelle 1
     * fortgeführt usw.<br>
     * Die Konfiguration an Position 0 wird für das Ändern und die Neuanlage von
     * Eigenschaften verwendet.
     */
    private final List<GlueConfig> configs = new LinkedList<>();

    /**
     * Eine weitere Konfiguration hinzufügen.
     *
     * @param config
     *            Eine Konfiguration.
     *
     * @see #configs
     */
    public void addConfig(final GlueConfig config) {
        configs.add(config);
    }

    public void load() throws IOException {
        for (GlueConfig cc : configs) {
            cc.load();
        }
    }

    /**
     * Gespeichert wird nur die erste Konfiguration!
     */
    public void save() throws IOException {
        if (configs.size() == 0) {
            throw new IllegalStateException("There is no configuration added!");
        }
        GlueConfig cc = configs.get(0);
        cc.save();
    }

    /**
     * Modifikationen oder Neuanlagen von Eigenschaften werden in die erste
     * Konfiguration geschrieben.
     *
     * @param key
     *            Der Schlüssel.
     * @param value
     *            Wert der Eigenschaft.
     */
    public void setProperty(final String key, final String value) {
        if (configs.size() == 0) {
            throw new IllegalStateException("There is no configuration added!");
        }
        GlueConfig cc = configs.get(0);
        cc.setProperty(key, value);
    }

    /**
     * Verwaltet den internen Zugang zu der Eigenschaft
     * {@link CombinedGlueConfig#configs}.
     * 
     * @param key
     *            Der Schlüssel.
     * @return Die gefundene Eigenschaft
     */
    @Override
    protected Object doGetProperty(final String key) {
        Object result = null;
        for (GlueConfig gc : configs) {
            String value = gc.getString(key);
            if (StringUtils.isNotBlank(value)) {
                result = value;
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getKeyIterator() {
        @SuppressWarnings("rawtypes")
        Iterator[] iterators = new Iterator[configs.size()];
        int index = 0;
        for (GlueConfig gc : configs) {
            iterators[index] = gc.getKeyIterator();
            index++;
        }
        return (IteratorUtils.chainedIterator(iterators));
    }

}
