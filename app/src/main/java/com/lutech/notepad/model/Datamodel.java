package com.lutech.notepad.model;

import java.util.List;

public class Datamodel {
    private List<String> nestedList;
    private String itemText;
    private boolean isExpandable;

    public Datamodel(List<String> nestedList, String itemText) {
        this.nestedList = nestedList;
        this.itemText = itemText;
        isExpandable = false;
    }
    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }
    public List<String> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<String> nestedList) {
        this.nestedList = nestedList;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
    public boolean isExpandable() {
        return isExpandable;
    }
}
