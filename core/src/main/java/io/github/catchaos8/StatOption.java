package io.github.catchaos8;

public class StatOption {
    public String name;
    public String description;
    public Runnable applyUpgrade;
    public int weight;

    public StatOption(int weight, String name, String description, Runnable applyUpgrade) {
        this.name = name;
        this.applyUpgrade = applyUpgrade;
        this.weight = weight;
        this.description = description;
    }
}
