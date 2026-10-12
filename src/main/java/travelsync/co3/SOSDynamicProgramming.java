package travelsync.co3;

/**
 * Sum Over Subsets (SOS) Dynamic Programming: O(N · 2^N) time.
 * Computes F[mask] = sum_{sub in mask} A[sub] for fast combinatorial subset queries.
 */
public class SOSDynamicProgramming {

    /**
     * Computes SOS DP for an array of size 2^n.
     * @param n Number of bits (dimension)
     * @param a Input values for each submask
     * @return Array F where F[mask] = sum_{sub \subseteq mask} a[sub]
     */
    public static long[] computeSOS(int n, long[] a) {
        if (n <= 0 || a == null) return new long[0];
        int size = 1 << n;
        long[] f = new long[size];
        System.arraycopy(a, 0, f, 0, Math.min(a.length, size));

        for (int i = 0; i < n; i++) {
            for (int mask = 0; mask < size; mask++) {
                if ((mask & (1 << i)) != 0) {
                    f[mask] += f[mask ^ (1 << i)];
                }
            }
        }

        return f;
    }
}
