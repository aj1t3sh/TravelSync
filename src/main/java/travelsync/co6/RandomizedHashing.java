package travelsync.co6;

import java.util.Random;

/**
 * Universal Hashing and Double Hashing collision-avoidance framework.
 * Demonstrates provable collision bound: Pr[h(x) = h(y)] <= 1/m for universal hash families.
 */
public class RandomizedHashing {

    private static final long PRIME = 2_147_483_647L; // 2^31 - 1 (Mersenne prime)

    private final long a;
    private final long b;
    private final int tableSize;

    public RandomizedHashing(int tableSize) {
        this(tableSize, new Random());
    }

    public RandomizedHashing(int tableSize, Random rng) {
        this.tableSize = tableSize;
        this.a = 1 + (Math.abs(rng.nextLong()) % (PRIME - 1));
        this.b = Math.abs(rng.nextLong()) % PRIME;
    }

    /**
     * Universal hash function: h_{a,b}(x) = ((a·x + b) mod p) mod m.
     */
    public int hash(long key) {
        long val = (Math.abs(key) * a + b) % PRIME;
        return (int) (val % tableSize);
    }

    /**
     * Double Hashing probe sequence: h(k, i) = (h1(k) + i · h2(k)) mod m.
     */
    public int probe(long key, int step) {
        int h1 = hash(key);
        int h2 = 1 + (int) (Math.abs(key) % (tableSize - 1)); // Non-zero second hash
        return (h1 + step * h2) % tableSize;
    }

    public int getTableSize() { return tableSize; }
    public long getA() { return a; }
    public long getB() { return b; }
}
