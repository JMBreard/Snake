import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {

        int boardWidth = 600, boardHeight = 600;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Game game = new Game(boardWidth, boardHeight);
        frame.add(game);
        frame.pack();
        game.requestFocus();
    }
}
