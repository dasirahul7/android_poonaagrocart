package com.poona.agrocart.ui.order_summary.model;

public class DeliverySlot {
    String SlotName;
    boolean active;

    public DeliverySlot(String slotName, boolean active) {
        SlotName = slotName;
        this.active = active;
    }

    public String getSlotName() {
        return SlotName;
    }

    public void setSlotName(String slotName) {
        SlotName = slotName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
