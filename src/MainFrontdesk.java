import java.util.Scanner;
import adt.BST;
import entity.Guest;
import control.FrontDesk;
import boundary.FrontDeskUI;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        FrontDesk frontDesk = new FrontDesk();

        FrontDeskUI ui = new FrontDeskUI();
        ui.start();

    }
}