public class wordleGUI {

    public static void main(String[] args){
        initialise();
    }

    public static void initialise(){
        wordleModel model = new wordleModel();
        wordleController controller = new wordleController(model);
        wordleView view = new wordleView(model, controller);
    }
}
