package tk.levenshtein.visualization;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.Dimension;


public class LevenshteinToolkit {

    /**
     * Generate the full m*n levenshtein matrix using the Wagner-Fischer algorithm.
     * The final distance between full strings is in the distances[m][n] space.
     *
     * @param s1
     * @param s2
     * @return
     */
    private static int[][] levenshteinMatrix(String s1, String s2) {
        int s1len = s1.length() + 1;
        int s2len = s2.length() + 1;
        int[][] distances = new int[s2len][s1len];

        for (int x = 1; x < s1len; x++) {
            distances[0][x] = x;
        }

        for (int y = 1; y < s2len; y++) {
            distances[y][0] = y;
        }

        for (int y = 1; y < s2len; y++) {
            for (int x = 1; x < s1len; x++) {
                int substitutionCost = 1;
                if (s1.charAt(x - 1) == s2.charAt(y - 1)) {
                    substitutionCost = 0;
                }

                int up = distances[y - 1][x] + 1;
                int left = distances[y][x - 1] + 1;
                int diagonal = distances[y - 1][x - 1] + substitutionCost;
                distances[y][x] = NumberUtils.min(up, left, diagonal);
            }
        }

        return distances;
    }

    /**
     * Generate a vertex as substrings, edge as the levenshtein distance between substrings graph.
     *
     * @param s1
     * @param s2
     * @return
     */
    private static SparseGraph<String, DistanceEdge> levenshteinGraph(String s1, String s2) {
        int[][] distances = levenshteinMatrix(s1, s2);

        SparseGraph<String, DistanceEdge> graph =
                new SparseGraph<String, DistanceEdge>();

        for (int x = 0; x < s1.length() + 1; x++) {
            for (int y = 0; y < s2.length() + 1; y++) {
                String src = s1.substring(0, x);
                String dest = s2.substring(0, y);
                int distance = distances[y][x];
                DistanceEdge edge = new DistanceEdge(distance);

                if (!src.equalsIgnoreCase(dest)) {
                    graph.addEdge(edge, src, dest);
                }
            }
        }

        return graph;
    }

    /**
     * Generate a visualization of the discrete edit paths in the matrix-based levenhstein distance calculation.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static VisualizationViewer<String, DistanceEdge> levenshteinVisualization(String s1, String s2) {
        SparseGraph<String, DistanceEdge> graph = LevenshteinToolkit.levenshteinGraph(s1, s2);

        KKLayout<String, DistanceEdge> graphLayout = new KKLayout<String, DistanceEdge>(graph);
        VisualizationViewer<String, DistanceEdge> vv = new VisualizationViewer<String, DistanceEdge>(graphLayout, new Dimension(800, 600));

        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<DistanceEdge>());
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<String, String>() {
            public String transform(String s) {
                if (s.isEmpty()) {
                    return "ε";
                } else {
                    return s;
                }
            }
        });

        DefaultModalGraphMouse gm = new DefaultModalGraphMouse<String, DistanceEdge>();
        vv.setGraphMouse(gm);
        vv.addKeyListener(gm.getModeKeyListener());

        return vv;
    }

    /**
     * Calculate the levenshtein distance between two strings.
     *
     * @param s1
     * @param s2
     * @return
     */
    public static int levenshteinDistance(String s1, String s2) {
        int[][] matrix = levenshteinMatrix(s1, s2);
        return matrix[s2.length()][s1.length()];
    }

    /**
     * Simple wrapper around int to make edges unique in the graph despite
     * having multiple distances being the same value.
     */
    private static class DistanceEdge {
        private int distance;

        public DistanceEdge(int distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            return String.valueOf(distance);
        }
    }

}
