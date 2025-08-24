package xyz.iwolfking.woldsvaults.util;

public record Pair<K,V> (K left, V right) {
    public static <K,V> Pair<K,V> of(K left, V right) {
        return new Pair<>(left, right);
    }

    public K one() {
        return this.left;
    }

    public V two() {
        return this.right;
    }
}
