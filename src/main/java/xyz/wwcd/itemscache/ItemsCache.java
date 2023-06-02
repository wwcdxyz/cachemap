package xyz.wwcd.itemscache;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class ItemsCache<Item, ID> {
    private final int refreshIntervalMinutes;
    private List<Item> itemsList;
    private Map<ID, Item> itemsMap;
    private Date lastRefresh = new Date();

    public ItemsCache(int refreshIntervalMinutes) {
        this.refreshIntervalMinutes = refreshIntervalMinutes;
    }

    public List<Item> getAllItems() {
        if (itemsList == null || itemsList.isEmpty() || isOutdated()) {
            logDebug(String.format("*** Start Refreshing {} Cache ***", this.getClass().getSimpleName()));
            itemsList = readAllItemsFromRepository();
            logDebug(String.format("Cache size: {} elements", itemsList.size()));
            //Collections.reverse(itemsList);
            itemsMap = itemsList.stream().collect(Collectors.toMap(this::getItemId, entity -> entity));
            //logDebug(String.format("Cache map size: {}", itemsMap.size()));
            lastRefresh = new Date();
            logDebug(String.format("*** Finished Refreshing Cache at {} ***", lastRefresh));
        }
        return itemsList;
    }

    public Item getItemById(ID itemId) {
        getAllItems(); // Making sure map and list are in sync
        return itemsMap.get(itemId);
    }

    public List<Item> getItemsByIds(Collection<ID> itemIds) {
        getAllItems(); // Making sure map and list are in sync
        return itemIds.stream().map(itemsMap::get).toList();
    }

    public Set<ID> getAllItemIds() {
        getAllItems(); // Making sure map and list are in sync
        return itemsMap.keySet();
    }

    private boolean isOutdated() {
        long diff = System.currentTimeMillis() - lastRefresh.getTime();
        var mins = TimeUnit.MILLISECONDS.toMinutes(diff);
        logDebug(String.format("Last catalog refresh {} minutes ago", mins));
        return mins > refreshIntervalMinutes;
    }

    protected abstract ID getItemId(Item entity);

    protected abstract List<Item> readAllItemsFromRepository();

    protected void logDebug(String message) {

    }

}