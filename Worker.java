public class Worker extends Human{
    private int id;
    private String name;
    private String surname;
    private Building workPlace;

    public Worker(String name, String surname, Building workPlace) {
        this.name = name;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Building getWorkPlace() {
        return workPlace;
    }
    public void setWorkPlace(Building workPlace) {
        this.workPlace = workPlace;
    }

    public void printGeneralInformation() {
        System.out.println("Name: " + name + " Surname: " + surname + " Working place name " + workPlace.getBuildingName() + " and location " + workPlace.getLocation());
    }
}

