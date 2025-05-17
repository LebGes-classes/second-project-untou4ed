public class PickUpPoint extends Building {
    private WarehouseCell WarehouseCellOwner;

    public PickUpPoint(String buildingName, String location,
                       WarehouseCell WarehouseCellOwner) {
        super(buildingName, location);
        this.WarehouseCellOwner = WarehouseCellOwner;
    }

    public WarehouseCell getWarehouseCellOwner() {
        return WarehouseCellOwner;
    }

    public void setWarehouseCellOwner(WarehouseCell warehouseCellOwner) {
        WarehouseCellOwner = warehouseCellOwner;
    }
}
