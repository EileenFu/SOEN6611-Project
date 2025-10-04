package model;

import utils.Enums.ZoneType;

public abstract class Product {
    protected ZoneType zone;
    protected boolean valid;
    protected boolean isPrinted;

    // Constructor
    public Product(ZoneType zone, boolean isPrinted) {
        this.zone = zone;
        this.isPrinted = isPrinted;
        this.valid = true;
    }

    public void invalidate() {
        this.valid = false;
    }

    // Shared getters
    public ZoneType getZone() { return zone; }
    public boolean isValid() { return valid; }
    public boolean isPrinted() { return isPrinted; }

    @Override
    public abstract String toString();
}