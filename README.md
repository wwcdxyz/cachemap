# In-memory cache for Items

Simple in-memory lazy-cache implementation for caching items from external repositories.
Useful for caching not-frequently changing data, when it is located in external data source.
- Speedup response time - when original datasource has response time is slow.
- Avoid redundant calls to external data source:
  - Save quota for paid APIs
  - Avoid exceeding rate limits

## Caching strategy: Read-through cache
```text
                                                     2. Miss: Read from                                
_______    1. Hit: Read from Cache   _________         External Source      ___________________
| App | <--------------------------- | Cache | <--------------------------- | External Source |
-------                              ---------                              -------------------
```

## Usage
1. Include as a dependency into your project (or just copy/paste source code)
2. Define class that extends `xyz.wwcd.itemscache.ItemsCache`
3. Declare as a static field in your domain service. 

> To avoid redundant cache initializations by multiple instances of a domain service, check if cache have been already initialized.
