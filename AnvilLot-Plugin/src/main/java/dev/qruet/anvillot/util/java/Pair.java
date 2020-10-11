package dev.qruet.anvillot.util.java;

/**
 * @author qruet
 * @param <K> Key
 * @param <V> Value
 */
public class Pair<K, V> {

    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

}
