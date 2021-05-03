package app;

import GUI.GUI;

public class App
{
    public static void main(String[] args) {
        GUI gui = new GUI();
        /*Audit audit = Audit.getInstance();
        Scanner scanner = new Scanner(System.in);
        MainService service = MainService.getInstance();
        while (true)
        {
            System.out.println("1. Add artist");
            System.out.println("2. Add location");
            System.out.println("3. Exit");
            String option = scanner.next();
            switch (option) {
                case "1":
                    service.addArtist();
                    break;
                case "2":
                    DataService.readLocation();
                    break;
                case "3":
                    service.saveData();
                    System.exit(0);
                default:
                    System.out.println("Try again");
            }
        }*/
    }
}
