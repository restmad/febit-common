// Copyright (c) 2013-present, febit.org. All Rights Reserved.
package org.febit.lang;

import java.util.NoSuchElementException;
import org.febit.lang.iter.BaseIter;

/**
 *
 * @author zqq90
 */
public final class IntMap<V> implements Iterable<IntMap.Entry<V>> {

    private static final int MAXIMUM_CAPACITY = 1 << 29;

    private Entry<V>[] table;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public IntMap(int initialCapacity) {
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

    public IntMap() {
        this(64);
    }

    public int size() {
        return size;
    }

    public V get(final int key) {
        Entry<V> e;
        final Entry<V>[] tab;
        e = (tab = table)[key & (tab.length - 1)];
        while (e != null) {
            if (key == e.key) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    public boolean containsKey(final int key) {
        Entry<V> e;
        final Entry<V>[] tab;
        e = (tab = table)[key & (tab.length - 1)];
        while (e != null) {
            if (key == e.key) {
                return true;
            }
            e = e.next;
        }
        return false;
    }

    public int[] exportKeys() {
        int[] keys = new int[this.size];
        int i = 0;
        for (Entry<V> entry : table) {
            while (entry != null) {
                keys[i++] = entry.key;
                entry = entry.next;
            }
        }
        return keys;
    }

    public void clear() {
        this.table = new Entry[this.table.length];
    }

    public void remove(final int key) {
        Entry<V> e;
        Entry<V> prev = null;
        final Entry<V>[] tab;
        final int index;
        e = (tab = table)[index = (key & (tab.length - 1))];

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

                index = e.key & newMark;
                e.next = newTable[index];
                newTable[index] = e;
            }
        }

        this.threshold = (int) (newCapacity * 0.75f);
        //Note: must at Last
        this.table = newTable;
    }

    @SuppressWarnings("unchecked")
    public V putIfAbsent(int key, V value) {
        int index;

        Entry<V>[] tab;
        Entry<V> e = (tab = table)[index = key & (tab.length - 1)];
        for (; e != null; e = e.next) {
            if (key == e.key) {
                return e.value;
            }
        }

        if (size >= threshold) {
            resize();
            tab = table;
            index = key & (tab.length - 1);
        }

        // creates the new entry.
        tab[index] = new Entry(key, value, tab[index]);
        size++;
        return value;
    }

    @SuppressWarnings("unchecked")
    public void put(int key, V value) {
        int index;

        Entry<V>[] tab;
        Entry<V> e = (tab = table)[index = key & (tab.length - 1)];
        for (; e != null; e = e.next) {
            if (key == e.key) {
                e.value = value;
                return;
            }
        }

        if (size >= threshold) {
            resize();
            tab = table;
            index = key & (tab.length - 1);
        }

        // creates the new entry.
        tab[index] = new Entry(key, value, tab[index]);
        size++;
    }

    public Iter<Entry<V>> iterator() {
        return new BaseIter<Entry<V>>() {
            int cursor;
            Entry<V> currEntry;

            @Override
            public boolean hasNext() {
                final Entry<V>[] tab = IntMap.this.table;
                while (currEntry == null && cursor < tab.length) {
                    currEntry = IntMap.this.table[cursor++];
                }
                if (currEntry != null) {
                    return true;
                }
                return false;
            }

            @Override
            public Entry<V> next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Entry<V> result = currEntry;
                currEntry = currEntry.next;
                return result;
            }
        };
    }

    public static final class Entry<V> {

        final int key;
        V value;
        Entry<V> next;

        Entry(int key, V value, Entry<V> next) {
            this.value = value;
            this.key = key;
            this.next = next;
        }

        public int getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
