package me.geekles.repaircost.utils.config;

public enum ConfigurationPathLibrary {

    MAX_REPAIR_COST("Default Max Repair Cost"),

    PLACEHOLDER("Placeholder"),
    PLACEHOLDER_MATERIAL("Placeholder.Material"),
    PLACEHOLDER_IDENTIFIER("Placeholder.ID"),
    PLACEHOLDER_ITEMMETA("Placeholder.ItemMeta"),
    PLACEHOLDER_DISPLAYNAME(PLACEHOLDER_ITEMMETA + ".Displayname"),
    PLACEHOLDER_LORE(PLACEHOLDER_ITEMMETA + ".Lore"),
    PLACEHOLDER_ENCHANTED(PLACEHOLDER_ITEMMETA + ".Enchanted");

    private String path;

    ConfigurationPathLibrary(String path){
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }

}
