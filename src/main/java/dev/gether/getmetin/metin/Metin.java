package dev.gether.getmetin.metin;

import dev.gether.getmetin.data.CmdDrop;
import dev.gether.getmetin.data.FinalRewards;
import dev.gether.getmetin.data.ItemDrop;
import dev.gether.getmetin.data.MetinType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Metin {

    private MetinType metinType;
    private EntityType entityType;
    private String key;
    private String name;

    private int maxHp;
    private Material material;
    private List<String> hologramLine;
    private List<ItemDrop> dropItems;
    private List<CmdDrop> dropCmds;
    private FinalRewards finalRewards;

    public Metin(MetinType metinType, EntityType entityType, String key, String name, int maxHp, Material material, List<String> hologramLine, List<ItemDrop> dropItems, List<CmdDrop> dropCmds, FinalRewards finalRewards) {
        this.metinType = metinType;
        this.entityType = entityType;
        this.key = key;
        this.name = name;
        this.maxHp = maxHp;
        this.material = material;
        this.hologramLine = hologramLine;
        this.dropItems = dropItems;
        this.dropCmds = dropCmds;
        this.finalRewards = finalRewards;
    }

    public MetinType getMetinType() {
        return metinType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getName() {
        return name;
    }

    public List<String> getHologramLine() {
        return hologramLine;
    }

    public List<ItemDrop> getDropItems() {
        return dropItems;
    }

    public List<CmdDrop> getDropCmds() {
        return dropCmds;
    }

    public String getKey() {
        return key;
    }

    public FinalRewards getFinalRewards() {
        return finalRewards;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMaxHp() {
        return maxHp;
    }


}
