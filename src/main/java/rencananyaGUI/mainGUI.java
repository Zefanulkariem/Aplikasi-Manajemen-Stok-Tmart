package rencananyaGUI;

import tmart.DataInitiate;

public class mainGUI {
    public static void main(String[] args) {
    DataInitiate.init();
    new Login().setVisible(true);
    }
}
