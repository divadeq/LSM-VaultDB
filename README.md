# Own implementation of NoSQL Key-Value Database Engine
_Read this in: [English](README.md) / [Polski](README.pl.md)_

A NoSQL key-value database built from scratch in Java. The system is designed around a log-structured storage architecture (inspired by Bitcask), combining an in-memory index with persistent binary append-only storage on disk.

---

## System Architecture Overview

To achieve maximum throughput and minimal disk I/O bottlenecks, the engine splits data management into two layers:
1. **Persistent Layer (Disk):** An append-only binary file layout where every write operation is sequentially appended.
2. **Volatile Layer (RAM):** An active memory cache that holds the most frequently accessed keys to eliminate slow disk reads.

---

## Memory Management Layer (`LruEvictionPolicy`)

The core component governing the active RAM cache is the `LruEvictionPolicy` class. It prevents the application from running out of memory by evicting the least recently used keys when the storage capacity limit is reached.

### Data Structure Design
To ensure all cache management operations run in constant time **O(1)**, the policy uses a hybrid data structure:
* **Custom Doubly Linked List:** Maintains the exact order of data usage. The most recently accessed items are moved to the `head`, while the oldest, stale items naturally drift towards the `tail`.
* **Java `HashMap`:** Maps keys directly to their respective `Node` references inside the list, bypassing the need for expensive sequential searches.
