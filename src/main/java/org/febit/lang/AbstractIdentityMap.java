// Copyright (c) 2013-present, febit.org. All Rights Reserved.
package org.febit.lang;

/**
 *
 * @author zqq90
 */
abstract class AbstractIdentityMap<V> {

    private static final int MAXIMUM_CAPACITY = 1 << 29;

    private Entry<V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public AbstractIdentityMap(int initialCapacity) {
        int initlen;
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initlen = MAXIMUM_CAPACITY;
        } else {
            initlen = 16;
            while (initlen < initialCapacity) {
                initlen <<= 1;
            }
        }
        this.table = new Entry[initlen];
        this.threshold = (int) (initlen * 0.75f);
    }

    public AbstractIdentityMap() {
        this(64);
    }

    public final int size() {
        return size;
    }

    protected final V _get(final Object key) {
        Entry<V> e;
        final Entry<V>[] tab;
        e = (tab = table)[key.hashCode() & (tab.length - 1)];
        while (e != null) {
            if (key == e.key) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    protected final boolean _containsKey(final Object key) {
        Entry<V> e;
        final Entry<V>[] tab;
        e = (tab = table)[key.hashCode() & (tab.length - 1)];
        while (e != null) {
            if (key == e.key) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    protected final void _remove(final Object key) {
        Entry<V> e;
        Entry<V> prev = null;
        final Entry<V>[] tab;
        final int index;
        e = (tab = table)[index = (key.hashCode() & (tab.length - 1))];

        while (e != null) {
            if (key == e.key) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                size--;
                return;
            }
            prev = e;
            e = e.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        if (size < threshold) {
            return;
        }
        final Entry<V>[] oldTable = table;
        final int oldCapacity = oldTable.length;

        final int newCapacity = oldCapacity << 1;
        if (newCapacity > MAXIMUM_CAPACITY) {
            if (threshold == MAXIMUM_CAPACITY - 1) {
                throw new IllegalStateException("Capacity exhausted.");
            }
            threshold = MAXIMUM_CAPACITY - 1;
            return;
        }
        final int newMark = newCapacity - 1;
        final Entry<V>[] newTable = new Entry[newCapacity];

        for (int i = oldCapacity; i-- > 0;) {
            int index;
            for (Entry<V> old = oldTable[i], e; old != null;) {
                e = old;
                old = old.next;

                index = e.id & newMark;
                e.next = newTable[index];
                newTable[index] = e;
            }
        }

        this.threshold = (int) (newCapacity * 0.75f);
        //Note: must at Last
        this.table = newTable;
    }

    @SuppressWarnings("unchecked")
    protected final V _putIfAbsent(Object key, V value) {
        final int id;
        int index;

        Entry<V>[] tab;
        Entry<V> e = (tab = table)[index = (id = key.hashCode()) & (tab.length - 1)];
        for (; e != null; e = e.next) {
            if (key == e.key) {
                return e.value;
            }
        }

        if (size >= threshold) {
            resize();
            tab = table;
            index = id & (tab.length - 1);
        }

        // creates the new entry.
        tab[index] = new Entry(id, key, value, tab[index]);
        size++;
        return value;
    }

    @SuppressWarnings("unchecked")
    protected final void _put(Object key, V value) {
        final int id;
        int index;

        Entry<V>[] tab;
        Entry<V> e = (tab = table)[index = (id = key.hashCode()) & (tab.length - 1)];
        for (; e != null; e = e.next) {
            if (key == e.key) {
                e.value = value;
                return;
            }
        }

        if (size >= threshold) {
            resize();
            tab = table;
            index = id & (tab.length - 1);
        }

        // creates the new entry.
        tab[index] = new Entry(id, key, value, tab[index]);
        size++;
    }

    private static final class Entry<V> {

        final int id;
        final Object key;
        V value;
        Entry<V> next;

        Entry(int id, Object key, V value, Entry<V> next) {
            this.value = value;
            this.id = id;
            this.key = key;
            this.next = next;
        }
    }
}
