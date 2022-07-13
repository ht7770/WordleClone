import java.util.*;
import java.io.*;


public class wordleModel extends Observable {


    // Flags

    private boolean wordMustExist = true;
    private boolean useTestWord = true;
    private boolean showGoalWord = false;

    private Stack<Character> wordGoal;
    private int guesses = 0;
    private boolean correct;
    private Stack<Character> currentGuess;
    private ArrayList<String> wordsGuessed;
    private ArrayList<String> commonWords;
    private ArrayList<String> allWords;
    private ArrayList<Character> yellowLetters;
    private ArrayList<Character> greenLetters;
    private ArrayList<Character> greyLetters;
    private String wordGoalString;

    private Character[][] wordGrid;



    public wordleModel(){
        wordGoal = new Stack<Character>();
        currentGuess = new Stack<Character>();
        wordsGuessed = new ArrayList<String>();
        commonWords = new ArrayList<String>();
        allWords = new ArrayList<String>();
        yellowLetters = new ArrayList<Character>();
        greenLetters  = new ArrayList<Character>();
        greyLetters = new ArrayList<Character>();

        wordGrid = new Character[6][5];
        Arrays.stream(wordGrid).forEach(a -> Arrays.fill(a, ' '));
        correct = false;
        loadWordList();

        // Choose a random word from common words
        Random rand = new Random();
        wordGoalString = commonWords.get(rand.nextInt(commonWords.size())).toUpperCase();

        if(useTestWord){
            wordGoalString = "SPEAK";
        }
        if(showGoalWord){
            System.out.println("The goal word is: " + wordGoalString);
        }
        for(int i = 0; i < wordGoalString.length(); i++){
            wordGoal.push(wordGoalString.charAt(i));
        }
        // add common words to all words after choosing
        allWords.addAll(commonWords);



    }

    private void loadWordList(){
        try {
            File words = new File("/Users/henrythomas/Desktop/Computer Science Year 4/Advanced Object-Oriented Programming/Coursework/Files needed for CW/words.txt");
            File common = new File("/Users/henrythomas/Desktop/Computer Science Year 4/Advanced Object-Oriented Programming/Coursework/Files needed for CW/common.txt");
            Scanner scan = new Scanner(common);
            while (scan.hasNextLine()) {
                commonWords.add(scan.nextLine());
            }
            scan.close();

            Scanner scan2 = new Scanner(words);
            while (scan2.hasNextLine()) {
                allWords.add(scan2.nextLine());
            }
            scan2.close();
        } catch (FileNotFoundException error){
            System.out.println("Word list couldn't be read!");
        }
    }


    public void addLetter(Character letter) {
        if (currentGuess.size() <= 4) {
            currentGuess.push(letter);
            wordGrid[guesses][currentGuess.size()-1] = letter;
            setChanged();
            notifyObservers();

        }
    }

    public void removeLetter(){
        if (currentGuess.size() > 0){
            currentGuess.pop();
            wordGrid[guesses][currentGuess.size()] = ' ';
            setChanged();
            notifyObservers();
        }
    }

    public ArrayList<Character> getYellowLetters(){
        return yellowLetters;
    }

    public ArrayList<Character> getGreenLetters() {
        return greenLetters;
    }

    public ArrayList<Character> getGreyLetters() {
        return greyLetters;
    }

    public Character[][] getWordGrid(){
        return wordGrid;
    }

    public int getGuesses(){
        return guesses;
    }

    public String confirmGuess(){
        String attemptedGuess = "";
        String response = "";
        if(currentGuess.size() != 5){
            return response = "size";
        }
        else{
            // Change guess stack into string
            for(int i = 0; i < 5; i++){
                attemptedGuess = attemptedGuess + currentGuess.get(i);
            }

            // compare guess stack with word goal stack
            if(attemptedGuess.equals(wordGoalString)){
                correct = true;
                System.out.println("Guess is correct!");
                guesses = guesses + 1;
                for(int j= 0; j < 5; j++){
                    if(!(greenLetters.contains(currentGuess.get(j)))){
                        greenLetters.add(currentGuess.get(j));
                    }
                }
                setChanged();
                notifyObservers();
                return response = "correct";
            }

            // check that the guess is within the common words
            else if(allWords.contains(attemptedGuess.toLowerCase()) && wordMustExist){
                wordsGuessed.add(attemptedGuess);
                // reduces guess counter by 1
                guesses = guesses + 1;
                if(guesses == 6){
                    setChanged();
                    notifyObservers();
                    return response = "max";
                }
                else{
                    // adding any letters that were in the correct place to list of green letters
                    for(int i = 0; i < 5; i++){
                        if(currentGuess.get(i) == wordGoal.get(i)){
                            greenLetters.add(currentGuess.get(i));
                            // if the new green letter was a yellow letter previously then remove it
                            if(yellowLetters.contains(currentGuess.get(i))){
                                yellowLetters.remove(currentGuess.get(i));
                            }

                        }
                        // if letter exists within word goal but does not match current letter then letter is added to yellow lists
                        else if (wordGoal.contains(currentGuess.get(i)) && currentGuess.get(i) != wordGoal.get(i)){
                            yellowLetters.add(currentGuess.get(i));
                        }
                        // if letter does not exist in word goal then add to list of grey letters
                        else if (!wordGoal.contains(currentGuess.get(i))){
                            greyLetters.add(currentGuess.get(i));
                        }

                    }
                }


            }

            // if flag is set to false then will not check if word is real or not
            else if(!wordMustExist){
                wordsGuessed.add(attemptedGuess);
                // reduces guess counter by 1
                guesses = guesses + 1;
                if(guesses == 6){
                    setChanged();
                    notifyObservers();
                    return response = "max";
                }
                else{
                    // adding any letters that were in the correct place to list of green letters
                    for(int i = 0; i < 5; i++){
                        if(currentGuess.get(i) == wordGoal.get(i)){
                            greenLetters.add(currentGuess.get(i));
                            // if the new green letter was a yellow letter previously then remove it
                            if(yellowLetters.contains(currentGuess.get(i))){
                                yellowLetters.remove(currentGuess.get(i));
                            }

                        }
                        // if letter exists within word goal but does not match current letter then letter is added to yellow lists
                        else if (wordGoal.contains(currentGuess.get(i)) && currentGuess.get(i) != wordGoal.get(i)){
                            yellowLetters.add(currentGuess.get(i));
                        }
                        // if letter does not exist in word goal then add to list of grey letters
                        else if (!wordGoal.contains(currentGuess.get(i))){
                            greyLetters.add(currentGuess.get(i));
                        }

                    }
                }


            }
            System.out.println("Guess is incorrect");
            currentGuess.clear();
            for(int z = 0; z < 5; z++){
                wordGrid[guesses][z] = ' ';
            }

            setChanged();
            notifyObservers();
            return response = "incorrect";
        }

    }














}
