import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBase {
    private Connection connection;
    private Statement statement;

    // Конструктор
    public DataBase() {
        connect();
    }

    // Подключение к базе данных
    private void connect() {
        try {
            //Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/organization.sqlite3");
            this.statement = connection.createStatement();
            System.out.println("База данных подключена!");
        } catch (Exception e) {
            System.out.println("Ошибка подключения к бд");
            e.printStackTrace();
        }
    }

    public void addWarehouse(String name, String location) {
        String sql = "INSERT INTO warehouses (name, location) VALUES (?, ?)";

        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            prepStat.setString(1, name);
            prepStat.setString(2, location);
            prepStat.executeUpdate();
            System.out.println("Склад добавлен");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getWarehouses(ArrayList<Warehouse> warehouses) {
        HashMap<Product, Integer> availableProducts = new HashMap<>();
        String sql = "SELECT * FROM available_products";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                availableProducts.put(new Product(rs.getString("name"), rs.getInt("price")), rs.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT * FROM warehouses";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Warehouse warehouse = new Warehouse("", "");
                updateBuiding(rs, warehouse);
                warehouse.setReceivableProducts(availableProducts);
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getWarehouseCells(ArrayList<WarehouseCell> warehousesCells, ArrayList<Warehouse> warehouses) {
        String sql = "SELECT * FROM warehouse_cells";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                WarehouseCell warehouseCell = new WarehouseCell("", "", new Warehouse("", ""));
                updateBuiding(rs, warehouseCell);
                for (Warehouse warehouse : warehouses) {
                    if (warehouse.getId() == rs.getInt("id_warehouse_owner")) {
                        warehouseCell.setWarehouseOwner(warehouse);
                        break;
                    }
                }
                warehouseCell.setReceivableProducts(warehouseCell.getWarehouseOwner().getGiveableProducts());
                warehousesCells.add(warehouseCell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getPickUpPoints(ArrayList<PickUpPoint> pickUpPoints, ArrayList<WarehouseCell> warehouseCells) {
        String sql = "SELECT * FROM pick_up_points";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                PickUpPoint pickUpPoint = new PickUpPoint("", "", new WarehouseCell("", "", new Warehouse("", "")));
                updateBuiding(rs, pickUpPoint);
                for (WarehouseCell warehouseCell : warehouseCells) {
                    if (warehouseCell.getId() == rs.getInt("id_of_warehouse_cell_owner")) {
                        pickUpPoint.setWarehouseCellOwner(warehouseCell);
                        break;
                    }
                }
                pickUpPoint.setReceivableProducts(pickUpPoint.getWarehouseCellOwner().getGiveableProducts());
                pickUpPoints.add(pickUpPoint);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBuiding(ResultSet rs, Building building) throws SQLException {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int idOfResponsiblePerson = rs.getInt("id_of_responsible_person");
                int soldAmount = rs.getInt("sold_amount");
                int recievedAmound = rs.getInt("sold_amount");
                int profit = rs.getInt("profit");
                building.setId(id);
                building.setBuildingName(name);
                building.setLocation(location);
                building.setIdOfResponsiblePerson(idOfResponsiblePerson);
                building.setSoldAmount(soldAmount);
                building.setReceivedAmount(recievedAmound);
                building.setProfit(profit);
    }

//    public ResultSet getAllWarehouses() {
//        String sql = "SELECT * FROM warehouses";
//
//        try (ResultSet resultSet = statement.executeQuery(sql)) {
//            return resultSet;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public void addWarehouseCell(String name, String location, int id_of_warehouse_owner) {
        String sql = "INSERT INTO warehouse_cells (name, location, id_warehouse_owner) VALUES (?, ?, ?)";

        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            prepStat.setString(1, name);
            prepStat.setString(2, location);
            prepStat.setInt(3, id_of_warehouse_owner);
            prepStat.executeUpdate();
            System.out.println("Ячейка склада добавлена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPickUpPoint(String name, String location, int id_of_warehouse_cell_owner) {
        String sql = "INSERT INTO pick_up_points (name, location, id_of_warehouse_cell_owner) VALUES (?, ?, ?)";

        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            prepStat.setString(1, name);
            prepStat.setString(2, location);
            prepStat.setInt(3, id_of_warehouse_cell_owner);
            prepStat.executeUpdate();
            System.out.println("Пункт выдачи добавлена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Соединение с базой данных закрыто");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAvailableProducts(HashMap<Product, Integer> availableProducts) {
        String sql = "SELECT * FROM available_products";

        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(rs.getString("name"), rs.getInt("price"));
                availableProducts.put(product, rs.getInt("amount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getBuildingsByOwner(Building building) {
        String sql = "";
        if (building instanceof Warehouse) {
            sql = "SELECT * FROM warehouse_cells WHERE id_warehouse_owner = ?";
        }
        try (ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Worker> getBuildingWorkers(Building building) {
        List<Worker> workers = new ArrayList<>();
        String sql = "SELECT * FROM workers WHERE type_of_organization = ? AND id_of_organization = ?";

        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            if (building instanceof Warehouse) {
                prepStat.setString(1, "warehouse");
            } else if(building instanceof WarehouseCell) {
                prepStat.setString(1, "warehouse_cell");
            } else if(building instanceof PickUpPoint) {
                prepStat.setString(1, "pick_up_point");
            }
            prepStat.setInt(2, building.getId());
            ResultSet rs = prepStat.executeQuery();
            while (rs.next()) {
                Worker worker = new Worker(rs.getString("name"),
                        rs.getString("surname"), building);
                workers.add(worker);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workers;
    }

    public void deleteWorker(Worker worker) {
        String sql = "DELETE FROM workers WHERE id = ?";
        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            prepStat.setInt(1, worker.getId());
            prepStat.executeUpdate();
            System.out.println("Рабочий удален из базы данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWorker(Worker worker) {
        String sql = "INSERT INTO workers VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            prepStat.setString(2, worker.getName());
            if (worker.getWorkPlace() instanceof Warehouse) {
                prepStat.setString(3, "warehouse");
            } else if(worker.getWorkPlace() instanceof WarehouseCell) {
                prepStat.setString(3, "warehouse_cell");
            } else if(worker.getWorkPlace() instanceof PickUpPoint) {
                prepStat.setString(3, "pick_up_point");
            }
            prepStat.setInt(4, worker.getWorkPlace().getId());
            prepStat.setString(5, worker.getSurname());
            prepStat.executeUpdate();
            System.out.println("Рабочий добавлен в базу данных");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
