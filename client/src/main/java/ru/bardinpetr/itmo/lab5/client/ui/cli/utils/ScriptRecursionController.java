package ru.bardinpetr.itmo.lab5.client.ui.cli.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for checking script parsing recursions
 */
public class ScriptRecursionController {
    private final Set<String> visitedPaths = new HashSet<>();

    /**
     * Clear all. Should be called at root of script parsing
     */
    public void clear() {
        visitedPaths.clear();
    }

    /**
     * Adds new path to visited list. Checks if entering script is safe.
     * Should be called before parsing nested script.
     *
     * @param path Path of new script file
     * @return true if operation valid, false if recursion detected
     */
    public boolean enter(String path) {
        if (visitedPaths.contains(path))
            return false;
        visitedPaths.add(path);
        return true;
    }

    /**
     * Removes script from used.
     * Should be called when the processing on nested script ended.
     *
     * @param path Path of script file
     */
    public void leave(String path) {
        visitedPaths.remove(path);
    }

    /**
     * @return Count of nested scripts
     */
    public int getDepth() {
        return visitedPaths.size();
    }
}
