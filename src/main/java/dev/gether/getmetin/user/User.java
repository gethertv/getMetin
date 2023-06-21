package dev.gether.getmetin.user;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.*;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.metin.MetinManager;
import dev.gether.getmetin.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private Player player;

    private Metin metin;
    private Inventory inventory;
    private Inventory cmdCreator;
    private Inventory itemCreator;
    private Inventory finalInvCreator;

    private double chance = 0;
    private String cmd;
    private ItemStack itemStack;
    private String chanceString = "&7Szansa: &f{chance}";

    private HashMap<Integer, ItemInvData> dataTempRewards = new HashMap<>();
    private HashMap<Integer, ItemInvData> dataTempFinalRewards = new HashMap<>();

    private CreateType createType;


    public User(Player player, Metin metin) {
        this.player = player;
        this.metin = metin;
        this.inventory = Bukkit.createInventory(null, 54, ColorFixer.addColors("&0Metin - Edycja"));
        this.cmdCreator = Bukkit.createInventory(null, 27, ColorFixer.addColors("&0Metin - Tworzenie komendy"));
        this.itemCreator = Bukkit.createInventory(null, 27, ColorFixer.addColors("&0Metin - Tworzenie itemu"));
        this.finalInvCreator = Bukkit.createInventory(null, 54, ColorFixer.addColors("&0Metin - Ostatni drop"));
        fillBackground();
        implementItems();
        implementRewards();
        implementsFinalRewards();
        this.createType = CreateType.HIT_REWARD;
        player.openInventory(inventory);
    }

    private void implementsFinalRewards() {
        int i = 0;
        for (CmdDrop cmdDrop : metin.getFinalRewards().getCommands()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&aSzansa: " + cmdDrop.getChance()));
            List<String> lore = new ArrayList<>();
            if (cmdDrop.getCommand() != null)
                lore.add("&f&o"+cmdDrop.getCommand());

            lore.add("&7");
            lore.add("&eSHIFT + PRAWY &f-ABY USUNAC");
            itemMeta.setLore(ColorFixer.addColors(lore));
            item.setItemMeta(itemMeta);
            finalInvCreator.setItem(i, item);
            dataTempFinalRewards.put(i, new ItemInvData(metin.getKey(), cmdDrop.getIndex(), RemoveType.COMMAND));
            i++;
        }

        for (ItemDrop itemDrop : metin.getFinalRewards().getItems()) {
            ItemStack item = itemDrop.getItemStack().clone();
            ItemMeta itemMeta = item.getItemMeta();
            String disp = itemMeta.getDisplayName();
            itemMeta.setDisplayName(ColorFixer.addColors((disp != null ? disp : "") + " &7Szansa: " + itemDrop.getChance()));
            List<String> lore = new ArrayList<>();
            if(itemMeta.getLore()!=null)
                lore.addAll(itemMeta.getLore());

            lore.add("&7");
            lore.add("&eSHIFT + PRAWY &f-ABY USUNAC");
            item.setItemMeta(itemMeta);
            finalInvCreator.setItem(i, item);
            dataTempFinalRewards.put(i, new ItemInvData(metin.getKey(), itemDrop.getIndex(), RemoveType.ITEM));
            i++;
        }
    }

    private void implementRewards() {
        int i = 0;

        for (CmdDrop cmdDrop : metin.getDropCmds()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&aSzansa: " + cmdDrop.getChance()));
            List<String> lore = new ArrayList<>();
            if (cmdDrop.getCommand() != null)
                lore.add("&f&o"+cmdDrop.getCommand());

            lore.add("&7");
            lore.add("&eSHIFT + PRAWY &f-ABY USUNAC");
            itemMeta.setLore(ColorFixer.addColors(lore));
            item.setItemMeta(itemMeta);
            inventory.setItem(i, item);
            dataTempRewards.put(i, new ItemInvData(metin.getKey(), cmdDrop.getIndex(), RemoveType.COMMAND));
            i++;
        }

        for (ItemDrop itemDrop : metin.getDropItems()) {
            ItemStack item = itemDrop.getItemStack().clone();
            ItemMeta itemMeta = item.getItemMeta();
            String disp = itemMeta.getDisplayName();
            itemMeta.setDisplayName(ColorFixer.addColors((disp != null ? disp : "") + " &7Szansa: " + itemDrop.getChance()));
            List<String> lore = new ArrayList<>();
            if(itemMeta.getLore()!=null)
                lore.addAll(itemMeta.getLore());

            lore.add("&7");
            lore.add("&eSHIFT + PRAWY &f-ABY USUNAC");

            itemMeta.setLore(ColorFixer.addColors(lore));
            item.setItemMeta(itemMeta);
            inventory.setItem(i, item);
            dataTempRewards.put(i, new ItemInvData(metin.getKey(), itemDrop.getIndex(), RemoveType.ITEM));
            i++;
        }
    }

    private void implementItems() {
        {
            ItemStack itemStack = new ItemStack(Material.CLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&a&lDodaj przedmiot"));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(MetinManager.SLOT_ADD_ITEM, itemStack);
            finalInvCreator.setItem(MetinManager.SLOT_ADD_ITEM, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&e&lDodaj komende"));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(MetinManager.SLOT_ADD_CMD, itemStack);
            finalInvCreator.setItem(MetinManager.SLOT_ADD_CMD, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.BEACON);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&c&lOstatnia nagroda"));
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(MetinManager.SLOT_SET_FINAL_ITEMS, itemStack);
        }

        // creator cmd
        {
            String chance = chanceString;
            ItemStack itemStack = new ItemStack(Material.CLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors(chance.replace("{chance}", String.valueOf(this.chance))));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(14, itemStack);
            itemCreator.setItem(14, itemStack);
        }
        {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&aKliknij aby ustawic komende"));
            List<String> command = new ArrayList<>();
            if(cmd!=null)
                command.add("&f&o"+cmd);

            itemMeta.setLore(ColorFixer.addColors(command));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(12, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&a&lDodaj"));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(MetinManager.SLOT_ADD, itemStack);
            itemCreator.setItem(MetinManager.SLOT_ADD, itemStack);
        }
        {
            ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&c&lAnuluj"));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(MetinManager.SLOT_BACK, itemStack);
            itemCreator.setItem(MetinManager.SLOT_BACK, itemStack);

            itemMeta.setDisplayName(ColorFixer.addColors("&c&lCofnij"));
            itemStack.setItemMeta(itemMeta);
            finalInvCreator.setItem(45, itemStack);
        }
    }

    private void fillBackground() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ColorFixer.addColors("&7"));
        itemStack.setItemMeta(itemMeta);
        for (int i = 36; i < inventory.getSize(); i++) {
            inventory.setItem(i, itemStack);
            finalInvCreator.setItem(i, itemStack);
        }
        // creator cmd
        for (int i = 0; i < cmdCreator.getSize(); i++) {
            if(i==14 || i==12)
                continue;

            cmdCreator.setItem(i, itemStack);
        }
        // creator item
        for (int i = 0; i < itemCreator.getSize(); i++) {
            if(i==14 || i==12)
                continue;

            itemCreator.setItem(i, itemStack);
        }
    }

    public void openInventory() {
        dataTempRewards.clear();
        dataTempFinalRewards.clear();
        GetMetin.getInstance().reloadConfig();
        inventory.clear();
        finalInvCreator.clear();
        fillBackground();
        implementItems();
        implementRewards();
        implementsFinalRewards();

        if(getCreateType()==CreateType.HIT_REWARD)
            player.openInventory(inventory);

        if(getCreateType()==CreateType.FINAL_REWARD)
            player.openInventory(finalInvCreator);
    }
    public double getChance() {
        return chance;
    }

    public String getCmd() {
        return cmd;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Metin getMetin() {
        return metin;
    }

    public Inventory getCmdCreator() {
        return cmdCreator;
    }

    public Inventory getFinalInvCreator() {
        return finalInvCreator;
    }


    public Inventory getItemCreator() {
        return itemCreator;
    }

    public void refreshChance()
    {
        {
            String chance = chanceString;
            ItemStack itemStack = new ItemStack(Material.CLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors(chance.replace("{chance}", String.valueOf(this.chance))));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(14, itemStack);
            itemCreator.setItem(14, itemStack);
        }
    }

    public void refreshCmd() {
        {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ColorFixer.addColors("&aKliknij aby ustawic komende"));
            List<String> command = new ArrayList<>();
            if(cmd!=null)
                command.add("&f&o"+cmd);

            itemMeta.setLore(ColorFixer.addColors(command));
            itemStack.setItemMeta(itemMeta);

            cmdCreator.setItem(12, itemStack);
        }
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public Player getPlayer() {
        return player;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public ItemInvData getReward(int slot) {
        return dataTempRewards.get(slot);
    }
    public ItemInvData getFinalReward(int slot) {
        return dataTempFinalRewards.get(slot);
    }

    public CreateType getCreateType() {
        return createType;
    }

    public void setCreateType(CreateType createType) {
        this.createType = createType;
    }



    public void resetData()
    {
        cmd = null;
        chance = 0;
    }

}
