package dev.gether.getmetin.data;

import org.bukkit.inventory.ItemStack;

public class ItemDrop {
    private String index;
    private ItemStack itemStack;
    private double chance;

    public ItemDrop(String index, ItemStack itemStack, double chance) {
        this.index = index;
        this.itemStack = itemStack;
        this.chance = chance;
    }

    public String getIndex() {
        return index;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }



    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }
}
