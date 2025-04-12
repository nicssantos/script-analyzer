
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {

    private JFrame frame; // Creates JFrame
    private JLabel titleLabel; // Creates a JLabel for the title
    private JTextField fileInput1, fileInput2; // Creates text fields for 2 file inputs
    private JButton analyseButton, compareButton, mainProtagonistsButton, jaccardSimilarityButton; // Creates 2 buttons for analysing and comparing

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Theater Script Analyser");
        frame.setSize(480, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Sets the layout of main panel as Box Layout

        mainPanel.add(Box.createVerticalStrut(20)); // Creates some vertical space

        // Title Label
        titleLabel = new JLabel("Theater Script Analyser", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createVerticalStrut(25));

        // Row 1: File Input + Analyse Button
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Creates another panel for 1st row with Flow Layout
        row1.add(new JLabel("File Name (don't include \".txt\"):"));
        fileInput1 = new JTextField(15);
        row1.add(fileInput1);
        analyseButton = new JButton("Analyse");
        analyseButton.setPreferredSize(new Dimension(87, 27));
        analyseButton.addActionListener(this); // Add event listener for analyse button
        row1.add(analyseButton);
        mainPanel.add(row1);

        // Row 2: File Input + Compare Button
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Creates another panel for 2nd row with Flow Layout
        row2.add(new JLabel("                                                       "));
        fileInput2 = new JTextField(15);
        row2.add(fileInput2);
        compareButton = new JButton("Compare");
        compareButton.setPreferredSize(new Dimension(87, 27));
        compareButton.addActionListener(this); // Add event listener for compare button
        row2.add(compareButton);
        mainPanel.add(row2);

        // Row 3: Other features (getting main protagonists and Jaccard Similarity)
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row3.add(new JLabel("Other features:"));
        mainProtagonistsButton = new JButton("Get Main Protagonists");
        mainProtagonistsButton.addActionListener(this);
        row3.add(mainProtagonistsButton);
        jaccardSimilarityButton = new JButton("Jaccard Similarity");
        jaccardSimilarityButton.addActionListener(this);
        row3.add(jaccardSimilarityButton);
        mainPanel.add(row3);

        frame.add(mainPanel);  // Add to JFrame
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == analyseButton) { // Checks if analyse button was clicked
            String scriptFile = fileInput1.getText(); // Gets the file name input from user

            if (scriptFile.equals("")) { // Checks if text field is empty
                JOptionPane.showMessageDialog(frame, "No file received. Please type in the file name!");
            } else {
                if (!FileHandler.fileExists(scriptFile)) {
                    JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile + ".txt");
                } else {
                    JOptionPane.showMessageDialog(frame, "Analysing script: " + scriptFile + ".txt\nYou may close this window");
                    analyseScript(scriptFile);
                }

            }
        } else if (event.getSource() == compareButton) { // Checks if compare button was clicked
            String scriptFile1 = fileInput1.getText(); // Gets the 1st file name input from user
            String scriptFile2 = fileInput2.getText(); // Gets the 2nd file name input from user

            if (scriptFile1.equals("") && scriptFile2.equals("")) { // Checks if 2 fields are empty
                JOptionPane.showMessageDialog(frame, "Two fields are empty. Please type in 2 file names!");
            } else if (scriptFile1.equals("") || scriptFile2.equals("")) { // Checks if one of the fields is empty
                JOptionPane.showMessageDialog(frame, "One field is empty. Please type in 2 file names!");
            } else {
                if (!FileHandler.fileExists(scriptFile1) && !FileHandler.fileExists(scriptFile2)) {
                    JOptionPane.showMessageDialog(frame, "Error reading two files: " + scriptFile1 + ".txt" + ", " + scriptFile2 + ".txt");
                } else if (!FileHandler.fileExists(scriptFile1) || !FileHandler.fileExists(scriptFile2)) {
                    if (!FileHandler.fileExists(scriptFile1)) {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile1 + ".txt");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile2 + ".txt");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Analysing and comparing scripts: " + scriptFile1 + ".txt" + ", " + scriptFile2 + ".txt\nYou may close this window");
                    compareScripts(scriptFile1, scriptFile2);
                }
            }
        } else if (event.getSource() == mainProtagonistsButton) { // Checks if Get Main Protagonists button was clicked
            String scriptFile = fileInput1.getText();

            if (scriptFile.equals("")) { // Checks if text field is empty
                JOptionPane.showMessageDialog(frame, "No file received. Please type in the file name!");
            } else {
                if (!FileHandler.fileExists(scriptFile)) {
                    JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile + ".txt");
                } else {
                    JOptionPane.showMessageDialog(frame, "Getting main protagonists: " + scriptFile + ".txt\nYou may close this window");
                    getMainProtagonists(scriptFile);
                }
            }
        } else if (event.getSource() == jaccardSimilarityButton) { // Checks if Get Main Protagonists button was clicked
            String scriptFile1 = fileInput1.getText(); // Gets the 1st file name input from user
            String scriptFile2 = fileInput2.getText(); // Gets the 2nd file name input from user

            if (scriptFile1.equals("") && scriptFile2.equals("")) { // Checks if 2 fields are empty
                JOptionPane.showMessageDialog(frame, "Two fields are empty. Please type in 2 file names!");
            } else if (scriptFile1.equals("") || scriptFile2.equals("")) { // Checks if one of the fields is empty
                JOptionPane.showMessageDialog(frame, "One field is empty. Please type in 2 file names!");
            } else {
                if (!FileHandler.fileExists(scriptFile1) && !FileHandler.fileExists(scriptFile2)) {
                    JOptionPane.showMessageDialog(frame, "Error reading two files: " + scriptFile1 + ".txt" + ", " + scriptFile2 + ".txt");
                } else if (!FileHandler.fileExists(scriptFile1) || !FileHandler.fileExists(scriptFile2)) {
                    if (!FileHandler.fileExists(scriptFile1)) {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile1 + ".txt");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error reading file: " + scriptFile2 + ".txt");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Getting Jaccard Similarity: " + scriptFile1 + ".txt" + ", " + scriptFile2 + ".txt\nYou may close this window");
                    getJaccardSimilarity(scriptFile1, scriptFile2);
                }
            }
        }
    }

    private void analyseScript(String scriptFile) {
        Analyser analyser = new Analyser(scriptFile);
        analyser.analyse();
    }

    private void compareScripts(String scriptFile1, String scriptFile2) {
        Analyser analyser = new Comparator(scriptFile1, scriptFile2);
        analyser.analyse();
    }

    private void getMainProtagonists(String scriptFile) {
        Analyser analyser = new Analyser(scriptFile);
        analyser.getMainProtagonists();
    }

    private void getJaccardSimilarity(String scriptFile1, String scriptFile2) {
        Comparator analyser = new Comparator(scriptFile1, scriptFile2);
        analyser.getJaccardSimilarity();
    }

}
