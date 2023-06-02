package xyz.wwcd.cachemap;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class CacheMap<K, V> {
    private final int refreshIntervalMinutes;
    private Map<K, V> map;
    private Date lastRefresh = new Date();

    public CacheMap(int refreshIntervalMinutes) {
        this.refreshIntervalMinutes = refreshIntervalMinutes;
    }

    public Map<K, V> getCacheMap() {
        if (map == null || map.isEmpty() || isOutdated()) {
            logDebug(String.format("*** Start Refreshing %s Cache ***", this.getClass().getSimpleName()));
            map = readDataFromRepository();
            logDebug(String.format("Cache size: %s elements", map.size()));
            lastRefresh = new Date();
            logDebug(String.format("*** Finished Refreshing Cache at %s ***", lastRefresh));
        }
        return map;
    }

    private boolean isOutdated() {
        long diff = System.currentTimeMillis() - lastRefresh.getTime();
        var mins = TimeUnit.MILLISECONDS.toMinutes(diff);
        logDebug(String.format("Last catalog refresh %s minutes ago", mins));
        return mins > refreshIntervalMinutes;
    }

    protected abstract Map<K, V> readDataFromRepository();

    protected void logDebug(String message) {

    }

}