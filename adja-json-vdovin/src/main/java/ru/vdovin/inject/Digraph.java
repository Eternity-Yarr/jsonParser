package ru.vdovin.inject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Digraph<V> {

    private Map<V, List<V>> dependency = new HashMap<>();

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (V v : dependency.keySet()) s.append("\n    " + v + " -> " + dependency.get(v));
        return s.toString();
    }

    public void add(V vertex) {
        if (dependency.containsKey(vertex)) return;
        dependency.put(vertex, new ArrayList<>());
    }

    public void add(V from, V to) {
        this.add(from);
        this.add(to);
        dependency.get(from).add(to);
    }

    public void remove (V from, V to) {
        if (!(this.contains(from) && this.contains(to)))
            throw new IllegalArgumentException("Nonexistent vertex");
        dependency.get(from).remove(to);
    }


    public boolean contains(V vertex) {
        return dependency.containsKey(vertex);
    }

    public boolean isCircular(V to, V from) {
        if (to.equals(from)) {
            return true;
        }

        for (V nxt : dependency.get(from)) {
            if (isCircular(to, nxt)) {
                return true;
            }
        }
        return false;
    }

}
