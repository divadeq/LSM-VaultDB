package kvdatabase.infrastructure.cache;

import java.util.HashMap;

//Least Recently Used (LRU) eviction policy implementation for cache management.

public class LruEvictionPolicy<T> {
    private static class Node<T> {
        T key;
        Node<T> prev;
        Node<T> next;

        Node(T key) {
            this.key = key;
        }
    }

    HashMap<T, Node<T>> nodeMap = new HashMap<>();
    Node<T> head;
    Node<T> tail;

    public void keyAccessed(T key) {
        if (nodeMap.containsKey(key)) {
            Node<T> nodeToMove = nodeMap.get(key);
            if (nodeToMove == head) return;
            if (nodeToMove == tail) {
                tail = nodeToMove.prev;
                tail.next = null;
            } else {
                nodeToMove.prev.next = nodeToMove.next;
                nodeToMove.next.prev = nodeToMove.prev;
            }
            nodeToMove.next = head;
            nodeToMove.prev = null;
            head.prev = nodeToMove;
            head = nodeToMove;
        } else {
            Node<T> newNode = new Node<>(key);
            nodeMap.put(key, newNode);
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        }
    }

    public T evict() {
        if (tail == null) return null;
        T keyToRemove = tail.key;
        nodeMap.remove(keyToRemove);
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        return keyToRemove;
    }

    public void remove(T key) {
        if (!nodeMap.containsKey(key)) {
            return;
        }
        Node<T> nodeToRemove = nodeMap.get(key);
        nodeMap.remove(key);

        if (nodeToRemove.prev != null) {
            nodeToRemove.prev.next = nodeToRemove.next;
        } else {
            head = nodeToRemove.next;
        }

        if (nodeToRemove.next != null) {
            nodeToRemove.next.prev = nodeToRemove.prev;
        } else {
            tail = nodeToRemove.prev;
        }
    }
}
