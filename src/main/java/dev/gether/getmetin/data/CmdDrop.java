package dev.gether.getmetin.data;

public class CmdDrop {
    private String index;
    private String command;
    private double chance;

    public CmdDrop(String index, String command, double chance) {
        this.index = index;
        this.command = command;
        this.chance = chance;
    }

    public String getIndex() {
        return index;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
