package ir.maktab58.homework6.service;

public interface OnlineTaxiInterface {
    void addAGroupOfDrivers();

    void driverSignupOrLogin();

    void addAGroupOfPassengers();

    void passengerSignupOrLogin();

    void showAllDriversInformation();

    void showAllPassengersInformation();

    void showOngoingTravels();

    default void printProperMessage(String mode, String typeOfGroup){
        if (mode.equalsIgnoreCase("show") && typeOfGroup.equalsIgnoreCase("passengers"))
            System.out.println("Consider that only admin could see list of passengers.");
        if (mode.equalsIgnoreCase("show") && typeOfGroup.equalsIgnoreCase("drivers"))
            System.out.println("Consider that only admin could see list of drivers.");
        if (mode.equalsIgnoreCase("add") && typeOfGroup.equalsIgnoreCase("passengers"))
            System.out.println("Consider that only admin could add a group of passengers to passengers list.");
        if (mode.equalsIgnoreCase("add") && typeOfGroup.equalsIgnoreCase("drivers"))
            System.out.println("Consider that only admin could add a group of drivers to drivers list.");
        if (mode.equalsIgnoreCase("show") && typeOfGroup.equalsIgnoreCase("ongoing travels"))
            System.out.println("Consider that only admin could see ongoing travels list.");
    }
}
