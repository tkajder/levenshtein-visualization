package tk.levenshtein.visualization;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LevehnsteinToolkitTests {


    /**
     * Verify that the hand-coded levenshtein distance algorithm
     * matches the apache commons levenshtein distance algorithm.
     */
    @Test
    public void testLevehnsteinDistance() {
        String[] strs = new String[]{"apple", "sunday", "saturday", "pen", "qi", "sitting", "kitten",
                "cougar", "pansy", "freedom", "bane", "tip", "rendition", "capital", "pants", "ape"};
        for (int i = 0; i < strs.length; i++) {
            for (int j = 0; j < strs.length; j++) {
                int apacheDistance = StringUtils.getLevenshteinDistance(strs[i], strs[j]);
                int computedDistance = LevenshteinToolkit.levenshteinDistance(strs[i], strs[j]);
                assertEquals(apacheDistance, computedDistance);
            }
        }
    }
}
