import java.util.ArrayList;

public class wordleController {

    private wordleModel model;
    private wordleView view;


    public wordleController(wordleModel model){
        this.model = model;
    }

    public void setView(wordleView view){
        this.view = view;

    }

    public void addLetter(Character letter){
        model.addLetter(letter);
    }

    public void confirmGuess(){
        model.confirmGuess();
    }

    public void deleteLetter(){
        model.removeLetter();
    }

    public ArrayList<Character> getYellowLetters(){
        return model.getYellowLetters();
    }

    public ArrayList<Character> getGreenLetters(){
        return model.getGreenLetters();
    }

    public ArrayList<Character> getGreyLetters(){
        return model.getGreyLetters();
    }

    public Character[][] getWordGrid(){
        return model.getWordGrid();
    }

    public int getGuesses(){
        return model.getGuesses();
    }

    public void startNewGame(){
        wordleGUI.initialise();
    }
}
