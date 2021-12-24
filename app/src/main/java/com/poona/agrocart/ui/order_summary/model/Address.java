package com.poona.agrocart.ui.order_summary.model;

public class Address {
    String addTitle,addContent;

    public Address(String addTitle, String addContent) {
        this.addTitle = addTitle;
        this.addContent = addContent;
    }

    public String getAddTitle() {
        return addTitle;
    }

    public void setAddTitle(String addTitle) {
        this.addTitle = addTitle;
    }

    public String getAddContent() {
        return addContent;
    }

    public void setAddContent(String addContent) {
        this.addContent = addContent;
    }
}
