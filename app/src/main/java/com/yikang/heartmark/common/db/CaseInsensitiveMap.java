package com.yikang.heartmark.common.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class CaseInsensitiveMap extends HashMap {

    /**
     * The internal mapping from lowercase keys to the real keys.
     *
     * <p>
     * Any query operation using the key
     * ({@link #get(Object)}, {@link #containsKey(Object)})
     * is done in three steps:
     * <ul>
     * <li>convert the parameter key to lower case</li>
     * <li>get the actual key that corresponds to the lower case key</li>
     * <li>query the map with the actual key</li>
     * </ul>
     * </p>
     */
    private final Map lowerCaseMap = new HashMap();

    /**
     * Required for serialization support.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 1841673097701957808L;

    /**
     * @see java.util.Map#containsKey(Object)
     */
    public boolean containsKey(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase());
        return super.containsKey(realKey);
        // Possible optimisation here:
        // Since the lowerCaseMap contains a mapping for all the keys,
        // we could just do this:
        // return lowerCaseMap.containsKey(key.toString().toLowerCase());
    }

    /**
     * @see java.util.Map#get(Object)
     */
    public Object get(Object key) {
        Object realKey = lowerCaseMap.get(key.toString().toLowerCase());
        return super.get(realKey);
    }

    /**
     * @see java.util.Map#put(Object, Object)
     */
    public Object put(Object key, Object value) {
            /*
             * In order to keep the map and lowerCaseMap synchronized,
             * we have to remove the old mapping before putting the
             * new one. Indeed, oldKey and key are not necessaliry equals.
             * (That's why we call super.remove(oldKey) and not just
             * super.put(key, value))
             */
        Object oldKey = lowerCaseMap.put(key.toString().toLowerCase(), key);
        Object oldValue = super.remove(oldKey);
        super.put(key, value);
        return oldValue;
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map m) {
        Iterator iter = m.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            this.put(key, value);
        }
    }

    /**
     * @see java.util.Map#remove(Object)
     */
    public Object remove(Object key) {
        Object realKey = lowerCaseMap.remove(key.toString().toLowerCase());
        return super.remove(realKey);
    }
}
