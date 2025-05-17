import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    static ArrayList<Building> buildings = new ArrayList<>();
    static ArrayList<Warehouse> warehouses = new ArrayList<>();
    static ArrayList<WarehouseCell> warehouseCells = new ArrayList<>();
    static ArrayList<PickUpPoint> pickUpPoints = new ArrayList<>();
    static HashMap<Product, Integer> availableProducts = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static DataBase dataBase = new DataBase();

    public static void main(String[] args) {
        // обновляем списки предприятий
        dataBase.getWarehouses(warehouses);
        dataBase.getWarehouseCells(warehouseCells, warehouses);
        dataBase.getPickUpPoints(pickUpPoints, warehouseCells);
        dataBase.getAvailableProducts(availableProducts);
        buildings.addAll(warehouses);
        buildings.addAll(warehouseCells);
        buildings.addAll(pickUpPoints);
        warehouses.stream().forEach(Building::printGeneralInformation);
        warehouseCells.stream().forEach(Building::printGeneralInformation);
        pickUpPoints.stream().forEach(Building::printGeneralInformation);

        openMenu();
    }

    public static void openMenu() {
        while (true) {
            Tools.clearConsole();
            System.out.println("Выберите действие: ");
            System.out.println("1. Открыть новое предприятие");
            System.out.println("2. Выбрать существующее предприятие");
            System.out.println("3. Изменить товары");
            String answer = scanner.nextLine();

            if (answer.equals("1")) {
                newBuilding();
            }

            if (answer.equals("2")) {
                chooseBuilding();
            }

            if (answer.equals("3")) {
                changeProducts();
            }
        }
    }

    // взаимодействие с существующим предприятием
    public static int chooseBuilding() {
        Tools.clearConsole();
        System.out.println("Выберите тип предприятия");
        System.out.println("1. Склад");
        System.out.println("2. Ячейка склада");
        System.out.println("3. Пункт выдачи");
        int answer = scanner.nextInt();
        if (!Tools.isCorrectAnswer(answer, 1, 3)) {
            Tools.waitForInput(scanner);
            chooseBuilding();
            return 1;
        }

        switch (answer) {
            case 1:
                printBuildings(Warehouse.class);
                answer = scanner.nextInt();
                if (!Tools.isCorrectAnswer(answer, 1, warehouses.size())) {
                    Tools.waitForInput(scanner);
                } else {
                    chooseAction(warehouses.get(answer - 1));
                }
                break;
            case 2:
                printBuildings(WarehouseCell.class);
                break;
            case 3:
                printBuildings(PickUpPoint.class);
                break;
        }
        return 0;
    }

    private static void changeProducts() {
        Tools.clearConsole();
        System.out.println("1. Добавить новый товар в ассортимент");
        System.out.println("2. Удалить товар из ассортимента");
        System.out.println("3. Переместить товары");
        System.out.println("4. Назад");
        int answer = scanner.nextInt();
        if (!Tools.isCorrectAnswer(answer, 1, 4)) {
            Tools.waitForInput(scanner);
        } else {
            switch (answer) {
                case 1:
                    newProduct();
                    break;
                case 2:
                    moveProduct();
                    break;
                case 3:
                    removeProduct();
                    break;
            }
        }
    }

    private static void chooseAction(Building building) {
        building.printGeneralInformation();
        System.out.println("1. Сменить имя");
        System.out.println("2. Сменить адрес");
        System.out.println("3. Сменить ответственного");
        System.out.println("4. Посмотреть работников");
        System.out.println("5. Назначить нового работника");
        System.out.println("6. Посмотреть информацию о товарах");
        System.out.println("7. Посмотреть информацию о выручке");

        int answer;
        if (building instanceof Warehouse) {
            System.out.println("6. Посмотреть подконтрольные ячейки склада");
            answer = scanner.nextInt();
            if (!Tools.isCorrectAnswer(answer, 1, 6)) {
                Tools.waitForInput(scanner);
                chooseAction(building);
                return;
            }
        } else if (building instanceof WarehouseCell) {
            System.out.println("6. Посмотреть подконтрольные пункты выдачи");
            answer = scanner.nextInt();
            if (!Tools.isCorrectAnswer(answer, 1, 6)) {
                Tools.waitForInput(scanner);
                chooseAction(building);
                return;
            }
        } else {
            answer = scanner.nextInt();
            if (!Tools.isCorrectAnswer(answer, 1, 5)) {
                Tools.waitForInput(scanner);
                chooseAction(building);
                return;
            }
        }
        Tools.clearConsole();
        switch (answer) {
            case 1:
                System.out.println("Введите новое имя");
                String name = scanner.nextLine();
                building.setBuildingName(name);
                break;
            case 2:
                System.out.println("Введите новый адрес");
                String location = scanner.nextLine();
                building.setLocation(location);
                break;
            case 3:
                List<Worker> workers = dataBase.getBuildingWorkers(building);
                if (workers.isEmpty()) {
                    System.out.println("У предприятия нет работников. Нельзя выбрать ответственного");
                } else {
                    System.out.println("Выберите нового работника");
                    printWorkers(workers);
                    answer = scanner.nextInt();
                    if (!Tools.isCorrectAnswer(answer, 1, workers.size())) {
                        Tools.waitForInput(scanner);
                        chooseAction(building);
                    } else {
                        building.setIdOfResponsiblePerson(workers.get(answer - 1).getId());
                    }
                }
                break;
            case 4:
                List<Worker> workers2 = dataBase.getBuildingWorkers(building);
                if (workers2.isEmpty()) {
                    System.out.println("У предприятия нет рабочих");
                } else {
                    System.out.println("Рабочие предприятия");
                    printWorkers(workers2);
                    answer = scanner.nextInt();
                    System.out.println(workers2.size() + 1 + ". Вернуться");
                    if (answer == 7) {
                        chooseAction(building);
                    }
                    if (!Tools.isCorrectAnswer(answer, 1, workers2.size())) {
                        Tools.waitForInput(scanner);
                        chooseAction(building);
                    } else {
                        chooseWorker(workers2.get(answer - 1));
                        chooseAction(building);
                    }
                }
                break;
            case 5:
                System.out.println("Введите имя работника: ");
                name = scanner.nextLine();
                System.out.println("Введите фамилию работника: ");
                String surname = scanner.nextLine();
                Worker worker = new Worker(name, surname, building);

                break;
            case 6:
                if (building instanceof Warehouse) {
                    for (WarehouseCell cell : warehouseCells) {
                        if (cell.getWarehouseOwner().equals(building)) {
                            cell.printGeneralInformation();
                        }
                    }
                } else if (building instanceof WarehouseCell) {
                    for (PickUpPoint point : pickUpPoints) {
                        if (point.getWarehouseCellOwner().equals(building)) {
                            point.printGeneralInformation();
                        }
                    }
                }
                break;
        }
    }


    // создаем новое предприятие
    public static int openBuilding(Class<? extends Building> buildingClass) {
        if (buildingClass == WarehouseCell.class) {
            if (warehouses.isEmpty()) {
                System.out.println("Ячейка скалада не может быть создана," +
                        " без единого склада");
                Tools.waitForInput(scanner);
                return 1;
            }
        } else if (buildingClass == PickUpPoint.class) {
            if (warehouseCells.isEmpty()) {
                System.out.println("Пункт выдачи не может быть создан," +
                        " без единой ячейки склада");
                Tools.waitForInput(scanner);
                return 1;
            }
        }
        System.out.println("Введите название предприятия:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("Введите адрес предприятия: ");
        String location = scanner.nextLine();
        int answer;
        if (buildingClass == WarehouseCell.class) {
            System.out.println("Выберите к какому складу будет принадлежать ячейка");
            printBuildings(Warehouse.class);
            answer = scanner.nextInt();
            if (answer < 1 || answer > warehouses.size()) {
                System.out.println("Такого варианта нет.");
                Tools.waitForInput(scanner); // пауза перед возвратом
                newBuilding();
            } else {
                Warehouse warehouseOwner = warehouses.get(answer - 1);
                WarehouseCell warehouseCell = new WarehouseCell(name, location, warehouseOwner);
                warehouseCell.setId(warehouseCells.getLast().getId() + 1);
                warehouseCell.setReceivableProducts(warehouseCell.getWarehouseOwner().getGiveableProducts());
                warehouseCells.add(warehouseCell);
                buildings.add(warehouseCell);
                dataBase.addWarehouseCell(name, location, warehouseOwner.getId());
            }

        } else if (buildingClass == PickUpPoint.class) {
            System.out.println("Выберите к какой ячейке склада будет привязан пункт выдачи");
            printBuildings(WarehouseCell.class);
            answer = scanner.nextInt();
            if (answer < 1 || answer > warehouseCells.size()) {
                System.out.println("Такого варианта нет.");
                Tools.waitForInput(scanner);
                newBuilding();
            } else {
                WarehouseCell warehouseCellOwner = warehouseCells.get(answer - 1);
                PickUpPoint pickUpPoint = new PickUpPoint(name, location, warehouseCellOwner);
                pickUpPoint.setId(pickUpPoints.getLast().getId() + 1);
                pickUpPoint.setReceivableProducts(pickUpPoint.getWarehouseCellOwner().getGiveableProducts());
                pickUpPoints.add(pickUpPoint);
                buildings.add(pickUpPoint);
                dataBase.addPickUpPoint(name, location, warehouseCellOwner.getId());
            }
        } else {
            Warehouse warehouse = new Warehouse(name, location);
            warehouse.setId(warehouses.getLast().getId() + 1);
            warehouse.setReceivableProducts(availableProducts);
            buildings.add(warehouse);
            warehouses.add(warehouse);
            dataBase.addWarehouse(name, location);
        }
        Tools.clearConsole();
        System.out.println("Успешно создано");
        Tools.waitForInput(scanner);
        return 0;
    }

    public static int newBuilding() {
        Tools.clearConsole();
        System.out.println("Выберите тип предприятия:");
        System.out.println("1. Склад");
        System.out.println("2. Ячейка склада");
        System.out.println("3. Пункт выдачи");
        System.out.println("4. Отмена");
        String answer = scanner.nextLine();
        if (Integer.parseInt(answer) <= 0 || Integer.parseInt(answer) >= 4) {
            if (!answer.equals("4")) {
                System.out.println("Такого варианта нет");
                Tools.waitForInput(scanner);
                newBuilding();
            }
            openMenu();
            return 0;
        }
        switch (answer) {
            case "1":
                openBuilding(Warehouse.class);
                break;
            case "2":
                openBuilding(WarehouseCell.class);
                break;
            case "3":
                openBuilding(PickUpPoint.class);
                break;
        }
        return 0;
    }

    // вывести список предприятий с индексами
    public static void printBuildings(Class<? extends Building> buildingClass) {
        int ind = 0;
        for (Building building : buildings) {
            if (buildingClass.isInstance(building)) {
                System.out.print(ind + 1 + ". ");
                building.printGeneralInformation();
            }
        }
        //buildings.stream().filter(buildingClass::isInstance).forEach(work.Building::printGeneralInformation);
    }

    public static void printWorkers(List<Worker> workers) {
        int ind = 0;
        for (Worker worker : workers) {
            System.out.println(ind + 1 + ". ");
            worker.printGeneralInformation();

        }
    }

    private static void chooseWorker(Worker worker) {
        System.out.println("1. Назначить ответственным");
        System.out.println("2. Уволить");
        System.out.println("3. Обратно");
        int answer = scanner.nextInt();
        if (!Tools.isCorrectAnswer(answer, 1, 3)) {
            Tools.waitForInput(scanner);
        } else {
            switch (answer) {
                case 1:
                    worker.getWorkPlace().setIdOfResponsiblePerson(worker.getId());
                    System.out.println("Новый ответственный назначен");
                    Tools.waitForInput(scanner);
                    break;
                case 2:
                    dataBase.deleteWorker(worker);
                    System.out.println("Сотрудник уволен.");
                    Tools.waitForInput(scanner);
                    break;
            }
            chooseAction(worker.getWorkPlace());
        }
    }

}
