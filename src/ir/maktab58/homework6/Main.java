package ir.maktab58.homework6;

import ir.maktab58.homework6.models.OnlineTaxiSys;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OnlineTaxiSys onlineTaxiSys = new OnlineTaxiSys();
        Scanner inputLine = new Scanner(System.in);
        while (true){
            System.out.println("**********Welcome**********");
            System.out.println("1) Add a group of drivers");
            System.out.println("2) Add a group of passengers");
            System.out.println("3) Driver signup or login");
            System.out.println("4) Passenger signup or login");
            System.out.println("5) show a list of drivers");
            System.out.println("6) show a list of passengers");
            System.out.println("7) exit");
            String choice = inputLine.nextLine();
            if (choice.equals("1")) {

            } else if (choice.equals("2")) {

            } else if (choice.equals("3")) {

            } else if (choice.equals("4")) {

            } else if (choice.equals("5")) {
                onlineTaxiSys.showAllDriversInformation();
            } else if (choice.equals("6")) {
                onlineTaxiSys.showAllPassengersInformation();
            } else if (choice.equals("7")) {
                break;
            } else {
                System.out.println("Invalid input command. Your choice must be an integer between 1 to 7.");
            }
        /*Date date = new Date(2001 - 1900, 9 - 1, 9);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String DateStr = dateFormat.format(date);
        System.out.println(DateStr);
        String[] dateElements = DateStr.split("-");
        int year = Integer.parseInt(dateElements[0]) - 1900;
        int month = Integer.parseInt(dateElements[1]) - 1;
        int day = Integer.parseInt(dateElements[2]);
        Date date1 = new Date(year, month, day);
        System.out.println(dateFormat.format(date1));*/
            //long date = Date.parse("1999-9-8");
            //System.out.println(date);
        }
    }
}
