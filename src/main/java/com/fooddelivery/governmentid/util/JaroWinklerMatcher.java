package com.fooddelivery.governmentid.util;

public class JaroWinklerMatcher {

    /**  
     * Computes the Jaro-Winkler similarity score between two strings.  
     * Returns a normalized score between 0.0 (no match) and 1.0 (exact match).  
     */  
    public static double computeSimilarity(String s1, String s2) {  
        if (s1 == null || s2 == null) return 0.0;  
        if (s1.equals(s2)) return 1.0;

        s1 = s1.toUpperCase().trim();  
        s2 = s2.toUpperCase().trim();

        int len1 = s1.length();  
        int len2 = s2.length();  
        if (len1 == 0 || len2 == 0) return 0.0;

        // Calculate maximum distance for character matching  
        int maxMatchDistance = Math.max(0, Math.max(len1, len2) / 2 - 1);  
        boolean[] s1Matches = new boolean[len1];  
        boolean[] s2Matches = new boolean[len2];  
          
        int matches = 0;  
          
        // Phase 1: Count exact matches within the allowed spatial window  
        for (int i = 0; i < len1; i++) {  
            int start = Math.max(0, i - maxMatchDistance);  
            int end = Math.min(i + maxMatchDistance + 1, len2);  
            for (int j = start; j < end; j++) {  
                if (!s2Matches[j] && s1.charAt(i) == s2.charAt(j)) {  
                    s1Matches[i] = true;  
                    s2Matches[j] = true;  
                    matches++;  
                    break;  
                }  
            }  
        }

        if (matches == 0) return 0.0;

        // Phase 2: Calculate transpositions for out-of-sequence matches  
        int transpositions = 0;  
        int k = 0;  
        for (int i = 0; i < len1; i++) {  
            if (s1Matches[i]) {  
                while (!s2Matches[k]) k++;  
                if (s1.charAt(i) != s2.charAt(k)) transpositions++;  
                k++;  
            }  
        }

        // Phase 3: Compute base Jaro score  
        double jaro = ((matches / (double) len1) +   
                       (matches / (double) len2) +   
                       ((matches - (transpositions / 2.0)) / matches)) / 3.0;

        // Phase 4: Apply Winkler Modification (Prefix scaling)  
        int prefixMatch = 0;  
        for (int i = 0; i < Math.min(4, Math.min(len1, len2)); i++) {  
            if (s1.charAt(i) == s2.charAt(i)) {  
                prefixMatch++;  
            } else {  
                break;  
            }  
        }

        // Apply standard scaling factor of 0.1  
        return jaro + (prefixMatch * 0.1 * (1.0 - jaro));  
    }  
}
