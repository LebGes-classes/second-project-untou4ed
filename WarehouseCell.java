public class WarehouseCell extends Building{

    private Warehouse warehouseOwner;
    public WarehouseCell(String buildingName, String location, Warehouse warehouseOwner) {
        super(buildingName, location);
        this.warehouseOwner = warehouseOwner;
    }

    public Warehouse getWarehouseOwner() {
        return warehouseOwner;
    }

    public void setWarehouseOwner(Warehouse warehouseOwner) {
        this.warehouseOwner = warehouseOwner;
    }
}
