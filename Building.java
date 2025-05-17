import java.util.HashMap;
import java.util.HashSet;

public abstract class Building {
    private int id;
    private String buildingName;
    private String location;
    private int idOfResponsiblePerson;
    private HashMap<Product, Integer> receivableProducts;
    private HashMap<Product, Integer> giveableProducts;
    private int soldAmount;
    private int receivedAmount;
    private int profit;

    public Building(String buildingName, String location) {
        this.buildingName = buildingName;
        this.location = location;
        this.receivableProducts = new HashMap<>();
        this.giveableProducts = new HashMap<>();
        this.soldAmount = 0;
        this.receivedAmount = 0;
        this.profit = 0;
    }

//    public void hireWorker(Worker worker) {
//        if(workers.contains(worker)) {
//            System.out.println("work.Worker is already hired");
//        } else {
//            workers.add(worker);
//        }
//    }
//
//    public void dismissWorker(Worker worker) {
//        if(!workers.contains(worker)) {
//            System.out.println("work.Worker is not hired");
//        } else {
//            workers.remove(worker);
//        }
//    }
//
//    public void changeResponsiblePerson(Worker worker) {
//        if(workers.contains(worker)) {
//            responsiblePerson = worker;
//        } else {
//            System.out.println("work.Worker is not hired");
//        }
//    }

    public int getIdOfResponsiblePerson() {
        return idOfResponsiblePerson;
    }

    public void setIdOfResponsiblePerson(int idOfResponsiblePerson) {
        this.idOfResponsiblePerson = idOfResponsiblePerson;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(int receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public int getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(int soldAmount) {
        this.soldAmount = soldAmount;
    }

    public HashMap<Product, Integer> getGiveableProducts() {
        return giveableProducts;
    }

    public void setGiveableProducts(HashMap<Product, Integer> giveableProducts) {
        this.giveableProducts = giveableProducts;
    }

    public HashMap<Product, Integer> getReceivableProducts() {
        return receivableProducts;
    }

    public void setReceivableProducts(HashMap<Product, Integer> receivableProducts) {
        this.receivableProducts = receivableProducts;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void printGeneralInformation() {
        System.out.println("Name: "+ buildingName + ", location: " + location +
                ", responsiblePersonId: " + idOfResponsiblePerson);
    }

    public void printProductInformation() {
        System.out.println("Products: available to give: ");
        giveableProducts.forEach((product, amount) ->
                System.out.print(product + " - " + amount + ", "));

        System.out.println("Products: available to receive: ");
        receivableProducts.forEach((product, amount) ->
                System.out.print(product + " - " + amount + ", "));
    }

    public void printProfitInformation() {
        System.out.println("In total, the enterprise received goods in the amount of: " + receivedAmount);
        System.out.println("And gaved back in the amount of: " + soldAmount);
        System.out.println("Profit: " + profit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
