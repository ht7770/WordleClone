import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class wordleView extends JFrame implements Observer {


    private wordleController controller;
    private wordleModel model;

    JButton[][] wordGrid = new JButton[6][5];
    JButton[] keyboard = new JButton[29];
    JFrame baseFrame = new JFrame("Wordle Clone");
    JPanel basePanel = new JPanel();
    JPanel wordGridPanel = new JPanel();
    JPanel keyboardPanel = new JPanel();





    public wordleView(wordleModel model, wordleController controller){
        this.controller = controller;
        this.model = model;
        controller.setView(this);

        setUpPanels();


        model.addObserver(this);
        update(model, null);

    }

    private void setUpPanels(){

        this.setContentPane(basePanel);
        this.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));

        // Adds JButtons to word grid
        for (int i = 0; i< wordGrid.length; i++){
            for (int j = 0; j< wordGrid[i].length; j++){
                wordGrid[i][j] = new JButton();
                wordGridPanel.add(wordGrid[i][j]);
            }
        }
        // Add wordGrid panel to the base panel with a grid layout to keep it symmetrical
        wordGridPanel.setLayout(new GridLayout(6,5));
        wordGridPanel.setVisible(true);
        basePanel.add(wordGridPanel);


        // Add letters of alphabet to buttons on the application
        int ASCII = 65;
        for(int i = 0; i <=25; i++) {
            // Cast ASCII value as a character
            char letter = (char) ASCII;
            // Add label to button of letter
            keyboard[i] = new JButton(Character.toString(letter));
            keyboard[i].setBackground(Color.gray);
            keyboard[i].setForeground(Color.black);
            keyboard[i].setPreferredSize(new Dimension(40,40));

            // Add action listener to JButton so if clicked then calls addLetter function in controller
            keyboard[i].addActionListener((ActionEvent e) -> { controller.addLetter(letter);});
            keyboardPanel.add(keyboard[i]);
            ASCII = ASCII + 1;

        }

        // Add submit button to keyboard with action listener to confirmGuess
        keyboard[26] = new JButton("Submit");
        keyboard[26].addActionListener((ActionEvent e) -> { controller.confirmGuess();});
        keyboardPanel.add(keyboard[26]);


        // Add backspace button to keyboard with action listener to deleteLetter
        keyboard[27] = new JButton ("Delete");
        keyboard[27].addActionListener((ActionEvent e) -> { controller.deleteLetter();});
        keyboardPanel.add(keyboard[27]);


        // Add keyboard panel to base panel
        basePanel.add(keyboardPanel);
        keyboardPanel.setLayout(new GridLayout(5,5));
        keyboardPanel.setVisible(true);


        // add the collection of panels to the base frame
        baseFrame.add(basePanel);
        baseFrame.setVisible(true);
        baseFrame.setResizable(true);
        baseFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        baseFrame.setSize(600, 750);

    }

    @Override
    public void update(Observable o, Object arg) {
        // Fetch colours that alphabet corresponds to
        ArrayList<Character> greenLetters  = controller.getGreenLetters();
        ArrayList<Character> yellowLetters  = controller.getYellowLetters();
        ArrayList<Character> greyLetters  = controller.getGreyLetters();

        //Get the character word grid that will match to the grid of buttons
        Character[][] modelWordGrid = controller.getWordGrid();
        int guesses = controller.getGuesses();




        for(int i = 0; i <=25; i++){
            // loop through each button and get text
            Character buttonLabel = (keyboard[i].getText()).charAt(0);
            // compare the button label to the list of coloured letters
            if(greenLetters.contains(buttonLabel)){
                keyboard[i].setBackground(Color.GREEN);
                keyboard[i].setOpaque(true);
            }
            else if(yellowLetters.contains(buttonLabel)){
                keyboard[i].setBackground(Color.YELLOW);
                keyboard[i].setOpaque(true);
            }
            else if(greyLetters.contains(buttonLabel)){
                keyboard[i].setBackground(Color.DARK_GRAY);
                keyboard[i].setOpaque(true);
            }
        }

        // loop through the wordGrid and the modelWordGrid simultaneously to map the values from model to the view
        for (int i = 0; i< wordGrid.length; i++){
            for (int j = 0; j< wordGrid[i].length; j++){
                String wordGridLabel = String.valueOf(modelWordGrid[i][j]);
                wordGrid[i][j].setText(wordGridLabel);

            }
        }

        // loop through the wordGrid but only on confirmed guesses so no colours show up for the current guess
        for(int z = 0; z < wordGrid[guesses-1].length; z++) {
            String wordGridLabel = wordGrid[guesses-1][z].getText();
            if (greenLetters.contains(wordGridLabel.charAt(0))) {
                wordGrid[guesses-1][z].setBackground(Color.GREEN);
                wordGrid[guesses-1][z].setOpaque(true);
            } else if (yellowLetters.contains(wordGridLabel.charAt(0))) {
                wordGrid[guesses-1][z].setBackground(Color.YELLOW);
                wordGrid[guesses-1][z].setOpaque(true);
            } else if (greyLetters.contains(wordGridLabel.charAt(0))) {
                wordGrid[guesses-1][z].setBackground(Color.DARK_GRAY);
                wordGrid[guesses-1][z].setOpaque(true);
            }
        }
        // if a guess has happened then make the new game button visible
        if (guesses == 1 && (keyboard[28] == null)){

            keyboard[28] = new JButton("New Game?");

            // action listener triggers newgame function in controller, this starts a new view and a new model and controller
            keyboard[28].addActionListener((ActionEvent e) -> {
                baseFrame.setVisible(false);
                controller.startNewGame();
            });
            keyboardPanel.add(keyboard[28]);
        }










    }
}
