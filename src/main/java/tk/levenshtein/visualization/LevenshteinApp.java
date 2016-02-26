package tk.levenshtein.visualization;

import edu.uci.ics.jung.visualization.VisualizationViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevenshteinApp {
    private JPanel mainPanel;
    private JTextField wordOneTextField;
    private JTextField wordTwoTextField;
    private JButton calculateDistanceButton;
    private JLabel wordsLabel;
    private JLabel distanceDescriptionLabel;
    private JButton visualizationButton;
    private JLabel distanceLabel;

    /**
     * Register the button action listeners to calculate levenshtein distance and visualizations.
     */
    public LevenshteinApp() {
        calculateDistanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                int distance = LevenshteinToolkit.levenshteinDistance(
                        wordOneTextField.getText(), wordTwoTextField.getText());
                distanceLabel.setText(String.valueOf(distance));
            }
        });

        visualizationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                VisualizationViewer vv = LevenshteinToolkit.levenshteinVisualization(
                        wordOneTextField.getText(), wordTwoTextField.getText());

                JFrame frame = new JFrame("Levenshtein Distance between Substrings");
                frame.getContentPane().add(vv);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Levenshtein Distance");
        frame.setContentPane(new LevenshteinApp().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
