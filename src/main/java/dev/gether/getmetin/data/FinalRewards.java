package dev.gether.getmetin.data;

import java.util.List;

public class FinalRewards {
    private List<CmdDrop> commands;
    private List<ItemDrop> items;

    public FinalRewards(List<CmdDrop> commands, List<ItemDrop> items) {
        this.commands = commands;
        this.items = items;
    }

    public List<CmdDrop> getCommands() {
        return commands;
    }

    public List<ItemDrop> getItems() {
        return items;
    }
}
