import java.util.Objects;

class HashTable <K, V> {
    class HashNode <K, V> {
        K key;
        V value;
        final int hashCode;
        HashNode<K, V> next;
        HashNode(K key, V value, int hashCode) {
            this.key = key;
            this.value = value;
            this.hashCode = hashCode;
        }
    }
    private int numBuckets;
    private HashNode<K, V>[] buckets;
    HashTable(int numBuckets) {
        this.numBuckets = numBuckets;
        this.buckets = new HashNode[numBuckets];
    }
    private int hashCode(K key) {
        return Objects.hashCode(key); // 0 if key == null
    }
    private int getBucketIndex(K key) {
        return Math.abs(hashCode(key) % numBuckets); // Hashcode can be negative, index cant
    }
    void put(K key, V value) { // Theta(1), sogar worstcase (vorne einfügen)
        int bucketIndex = getBucketIndex(key); // search index of bucket where key should be
        HashNode<K, V> head = buckets[bucketIndex]; // search head of bucket
        buckets[bucketIndex] = new HashNode<>(key, value, hashCode(key)); // create new node
        if (head != null)
            buckets[bucketIndex].next = head; // connect new node to head
    }
    V get(K key) { // Theta(1), O(n)
        int bucketIndex = getBucketIndex(key); // search index of bucket where key should be
        HashNode<K, V> head = buckets[bucketIndex]; // search head of bucket
        while (head != null) { // goes through the buckets list
            if (head.key.equals(key) && head.hashCode == hashCode(key)) // if key is found
                return head.value; // return value
            head = head.next; // move head to next
        }
        return null; // If key not found -> return null
    }
    V remove(K key) { // Theta(1), O(n)
        int bucketIndex = getBucketIndex(key); // search index of bucket where key should be
        HashNode<K, V> head = buckets[bucketIndex]; // search head of bucket
        HashNode<K, V> prev = null; // search prev of head
        while (head != null) { // goes through the buckets list
            if (head.key.equals(key) && head.hashCode == hashCode(key)) { // if key is found
                if (prev == null) // if key is the head
                    buckets[bucketIndex] = head.next; // move head to next
                else // if key is not the head
                    prev.next = head.next; // change references
                return head.value; // return removed value
            }
            prev = head; // move prev to head
            head = head.next; // move head to next
        }
        return null; // If key not found -> return null
    }
}
