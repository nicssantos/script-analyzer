# script-analyzer
------------------------------
 THEATHER SCRIPT ANALYSER APP
------------------------------

This program analyses and compares theater script/s. It processes lines of scripts and extracts detailed information which is saved into an analysis report file (.txt). 


 HOW TO RUN?
-------------

1. Ensure you have JDK 8 or above installed.
2. Make sure the script files and file containing stop words (named "StopWords-1") are in the same folder as the source codes.
3. Open your Command Prompt or Terminal.
4. In the command prompt, change the working directory to the directory where the files (source codes + script files + stop words file) are stored. 
5. Compile the Main class using javac Main.java
6. Finally, run the program using java Main


 HOW TO USE?
-------------

To analyse a script:
1. Type in the file name in the first field without the file extension (.txt).
2. Click "Analyse" button. To get the main protagonists only, click "Get Main Protagonists" button.
3. Check your same folder for the generated analysis report.

To compare two scripts:
1. Type in the file names in the fields without the file extension (.txt).
2. Click "Compare" button. To get the Jaccard similarity score only, click "Jaccard Similarity" button.
3. Check your same folder for the generated analysis report.


 FEATURES
----------

MAIN FEATURES:

Script Analyser:-
Generates a formatted analysis report in txt format that includes the title of the play, no. of protagonists, list of protagonists in the order of their apperance, list of the locations in the play, list of top-tier protagonists by no. of speech turns in descending order, top-tier protagonists by speech length in descending order, list of overall main protagonists (speech turns + speech length), and play details in a hierarchical structure. It also provides a lexical analysis report on the same play, including the no. of all words in the play, letter frequency in descending order, total no. of unique words, and the 100 most frequent words excluding stop words and protagonists name in the play in descending order.

Script Comparator:-
Generates a formatted comparison report in txt format that includes the titles of two plays, no. of protagonists in each play, list of main protagonists in each play, and the 30 most frequent words said by each of the main protagonist. It also provides a lexical analysis report on the two plays, including the 100 most frequent words (excluding stop words) in each play in descending order,  the 100 most frequent words (excluding stop words) in both plays combined in descending order, the most frequent words excluding common words found in the two previous lists, list of unique words in each play, and the Jaccard Similarity score between two plays.

OTHER FEATURES:

Main Protagonist Getter:
Generates an analysis report in txt format that includes the title and the lists of main protagonists in the play.


Jaccard Similarity Calculator:
Generates a comparison report in txt format that includes the titles of two plays and the Jaccard Similarity score between them.





