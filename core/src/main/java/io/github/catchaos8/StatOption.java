package io.github.catchaos8;

public class StatOption {
    public String name;
    public String description;
    public Runnable applyUpgrade;
    public int weight;
    public float luckChange;

    public StatOption(int weight, String name, String description, Runnable applyUpgrade, float luckChange) {
        this.name = name;
        this.applyUpgrade = applyUpgrade;
        this.weight = weight;
        this.description = description;
        this.luckChange = luckChange;
    }
}
