import javax.swing.SwingUtilities;

import Visual.Menu;

public class App {
    public static void main(String[] args) throws Exception {
       SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Menu();
            }
        }); 
    }
}
