package travelsync.co6;

import java.math.BigInteger;
import java.util.Random;

/**
 * Monte Carlo Algorithm: Miller-Rabin Probabilistic Primality Test.
 * Computes primality in O(k · log^3 N) with error probability bounded by (1/4)^k.
 * Immune to Carmichael numbers (e.g., 561, 1105, 1729) that fool Fermat's test.
 */
public class MillerRabin {

    public static class PrimalityResult {
        private final BigInteger candidate;
        private final boolean isProbablePrime;
        private final int roundsTested;
        private final double maxErrorProbability;
        private final BigInteger compositeWitness; // If found composite
        private final long executionTimeNs;

        public PrimalityResult(BigInteger candidate, boolean isProbablePrime, int roundsTested,
                               double maxErrorProbability, BigInteger compositeWitness, long executionTimeNs) {
            this.candidate = candidate;
            this.isProbablePrime = isProbablePrime;
            this.roundsTested = roundsTested;
            this.maxErrorProbability = maxErrorProbability;
            this.compositeWitness = compositeWitness;
            this.executionTimeNs = executionTimeNs;
        }

        public BigInteger getCandidate() { return candidate; }
        public boolean isProbablePrime() { return isProbablePrime; }
        public int getRoundsTested() { return roundsTested; }
        public double getMaxErrorProbability() { return maxErrorProbability; }
        public BigInteger getCompositeWitness() { return compositeWitness; }
        public long getExecutionTimeNs() { return executionTimeNs; }

        @Override
        public String toString() {
            if (isProbablePrime) {
                return String.format("%s is PROBABLE PRIME (%d witness rounds, Error Bound <= %.2e, Time: %.3f ms)",
                        candidate, roundsTested, maxErrorProbability, executionTimeNs / 1_000_000.0);
            } else {
                return String.format("%s is DEFINITELY COMPOSITE (Witness base: %s, Time: %.3f ms)",
                        candidate, compositeWitness != null ? compositeWitness : "Trial Check", executionTimeNs / 1_000_000.0);
            }
        }
    }

    /**
     * Tests primality using k independent random witness bases.
     */
    public static PrimalityResult isPrime(BigInteger n, int k) {
        return isPrime(n, k, new Random());
    }

    public static PrimalityResult isPrime(BigInteger n, int k, Random rng) {
        long start = System.nanoTime();

        // Edge cases
        if (n.compareTo(BigInteger.TWO) < 0) {
            return new PrimalityResult(n, false, 0, 0.0, null, System.nanoTime() - start);
        }
        if (n.equals(BigInteger.TWO) || n.equals(BigInteger.valueOf(3))) {
            return new PrimalityResult(n, true, k, 0.0, null, System.nanoTime() - start);
        }
        if (!n.testBit(0)) { // Even number > 2
            return new PrimalityResult(n, false, 0, 0.0, BigInteger.TWO, System.nanoTime() - start);
        }

        // Write n - 1 as 2^s * d
        BigInteger nMinusOne = n.subtract(BigInteger.ONE);
        BigInteger d = nMinusOne;
        int s = 0;
        while (!d.testBit(0)) {
            d = d.shiftRight(1);
            s++;
        }

        // Witness loop
        for (int i = 0; i < k; i++) {
            BigInteger a = getRandomBase(n, rng);
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) {
                continue;
            }

            boolean compositeInRound = true;
            for (int r = 1; r < s; r++) {
                x = x.modPow(BigInteger.TWO, n);
                if (x.equals(nMinusOne)) {
                    compositeInRound = false;
                    break;
                }
            }

            if (compositeInRound) {
                return new PrimalityResult(n, false, i + 1, 0.0, a, System.nanoTime() - start);
            }
        }

        double errorProb = Math.pow(0.25, k);
        return new PrimalityResult(n, true, k, errorProb, null, System.nanoTime() - start);
    }

    private static BigInteger getRandomBase(BigInteger n, Random rng) {
        BigInteger nMinusFour = n.subtract(BigInteger.valueOf(4));
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), rng);
        } while (a.compareTo(nMinusFour) > 0);
        return a.add(BigInteger.TWO); // a in [2, n - 2]
    }
}
