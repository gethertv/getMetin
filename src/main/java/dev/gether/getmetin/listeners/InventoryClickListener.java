package dev.gether.getmetin.listeners;

import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.*;
import dev.gether.getmetin.metin.MetinManager;
import dev.gether.getmetin.user.User;
import dev.gether.getmetin.utils.ColorFixer;
import dev.gether.getmetin.utils.NumberChecker;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class InventoryClickListener implements Listener {

    private final GetMetin plugin;
    public InventoryClickListener(GetMetin plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if(event.getClickedInventory()==null)
            return;

        if(!player.hasPermission("getmetin.admin"))
            return;

        User user = GetMetin.getInstance().getMetinManager().getUserAdmin().get(player.getUniqueId());
        if(user==null)
            return;

        if(user.getFinalInvCreator().equals(event.getInventory()))
        {
            event.setCancelled(true);
            if(user.getFinalInvCreator().equals(event.getClickedInventory()))
            {
                ItemInvData itemInvData = user.getFinalReward(event.getSlot());
                if (itemInvData!=null && event.getClick() == ClickType.SHIFT_RIGHT) {
                    if (itemInvData.getRemoveType() == RemoveType.COMMAND) {
                        plugin.getConfig().set("metin." + itemInvData.getKey() + ".final-rewards.commands." + itemInvData.getIndex(), null);
                        plugin.getMetinManager().getMetinData().forEach(metin ->
                        {
                            if (metin.getKey().equals(itemInvData.getKey())) {
                                metin.getFinalRewards().getCommands().removeIf(cmd -> cmd.getIndex().equals(itemInvData.getIndex()));
                            }
                        });
                    }

                    if (itemInvData.getRemoveType() == RemoveType.ITEM) {
                        plugin.getConfig().set("metin." + itemInvData.getKey() + ".final-rewards.items." + itemInvData.getIndex(), null);
                        plugin.getMetinManager().getMetinData().forEach(metin ->
                        {
                            if (metin.getKey().equals(itemInvData.getKey())) {
                                metin.getFinalRewards().getItems().removeIf(drop -> drop.getIndex().equals(itemInvData.getIndex()));
                            }
                        });
                    }

                    plugin.saveConfig();
                    player.sendMessage(ColorFixer.addColors("&aPomyslnie usunieto przedmioty!"));
                    user.openInventory();
                    return;
                }
                if(event.getSlot()==45)
                {
                    user.setCreateType(CreateType.HIT_REWARD);
                    user.openInventory();
                    return;
                }
                if(event.getSlot()== MetinManager.SLOT_ADD_ITEM)
                {
                    addItem(user, CreateType.FINAL_REWARD);
                    return;
                }
                if(event.getSlot()== MetinManager.SLOT_ADD_CMD)
                {
                    addCmd(user, CreateType.FINAL_REWARD);
                    return;
                }
            }
        }
        if(user.getItemCreator().equals(event.getInventory()))
        {
            if(event.getClickedInventory().equals(user.getItemCreator()))
            {
                if(event.getSlot()==12)
                    return;

                event.setCancelled(true);
                if(event.getSlot()==14)
                {
                    setChanceItem(user);
                    return;
                }
                if(event.getSlot()==MetinManager.SLOT_ADD)
                {
                    ItemStack item = event.getInventory().getItem(12);
                    if(item==null)
                    {
                        player.sendMessage(ColorFixer.addColors("&cMusisz dodac przedmiot!"));
                        return;
                    }
                    saveItem(item, user);
                    return;

                }
                if(event.getSlot()==MetinManager.SLOT_BACK)
                {
                    user.getPlayer().openInventory(user.getInventory());
                    return;
                }
            }
            return;
        }
        if(user.getCmdCreator().equals(event.getInventory()))
        {
            if(event.getClickedInventory().equals(user.getCmdCreator()))
            {
                event.setCancelled(true);
                if(event.getSlot()==12)
                {
                    setCmd(user);
                    return;
                }
                if(event.getSlot()==14)
                {
                    setChanceCmd(user);
                    return;
                }
                if(event.getSlot()==MetinManager.SLOT_ADD)
                {
                    saveCmd(user);
                    return;

                }
                if(event.getSlot()==MetinManager.SLOT_BACK)
                {
                    user.getPlayer().openInventory(user.getInventory());
                    return;
                }
            }
            return;
        }
        if(user.getInventory().equals(event.getInventory()))
        {
            event.setCancelled(true);
            if(user.getInventory().equals(event.getClickedInventory()))
            {
                ItemInvData itemInvData = user.getReward(event.getSlot());

                if(itemInvData!=null && event.getClick()== ClickType.SHIFT_RIGHT)
                {
                    if(itemInvData.getRemoveType()== RemoveType.COMMAND) {
                        plugin.getConfig().set("metin." + itemInvData.getKey() + ".commands." + itemInvData.getIndex(), null);
                        plugin.getMetinManager().getMetinData().forEach(metin ->
                        {
                            if(metin.getKey().equals(itemInvData.getKey()))
                            {
                                metin.getDropCmds().removeIf(cmd -> cmd.getIndex().equals(itemInvData.getIndex()));
                            }
                        });
                    }

                    if(itemInvData.getRemoveType()== RemoveType.ITEM) {
                        plugin.getConfig().set("metin." + itemInvData.getKey() + ".items." + itemInvData.getIndex(), null);
                        plugin.getMetinManager().getMetinData().forEach(metin ->
                        {
                            if(metin.getKey().equals(itemInvData.getKey()))
                            {
                                metin.getDropItems().removeIf(drop -> drop.getIndex().equals(itemInvData.getIndex()));
                            }
                        });
                    }

                    plugin.saveConfig();
                    player.sendMessage(ColorFixer.addColors("&aPomyslnie usunieto przedmioty!"));
                    user.openInventory();
                    return;
                }
                if(event.getSlot()== MetinManager.SLOT_ADD_ITEM)
                {
                    addItem(user, CreateType.HIT_REWARD);
                    return;
                }
                if(event.getSlot()== MetinManager.SLOT_ADD_CMD)
                {
                    addCmd(user, CreateType.HIT_REWARD);
                    return;
                }
                if(event.getSlot()== MetinManager.SLOT_SET_FINAL_ITEMS)
                {
                    user.setCreateType(CreateType.FINAL_REWARD);
                    addFinalItem(user, CreateType.HIT_REWARD);
                    return;
                }

            }
        }
    }

    private void setCmd(User user) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    String text = stateSnapshot.getText();
                    user.setCmd(text);
                    user.refreshCmd();
                    return List.of(AnvilGUI.ResponseAction.openInventory(user.getCmdCreator()));
                })
                .text("say {player}")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(ColorFixer.addColors("&0Podaj komende"))
                .plugin(plugin)
                .open(user.getPlayer());
    }

    private void setChanceItem(User user) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    String text = stateSnapshot.getText();
                    Player player = stateSnapshot.getPlayer();
                    double chance = 0;
                    if(!NumberChecker.isDouble(text)) {
                        player.sendMessage(ColorFixer.addColors("&cPodaj liczbe!"));
                        return List.of(AnvilGUI.ResponseAction.openInventory(user.getItemCreator()));
                    }
                    chance = Double.parseDouble(text);
                    if(chance<=0)
                    {
                        player.sendMessage(ColorFixer.addColors("&cNie mozesz podac liczby ujemnej!"));
                        return List.of(AnvilGUI.ResponseAction.openInventory(user.getItemCreator()));
                    }
                    user.setChance(chance);
                    user.refreshChance();
                    return List.of(AnvilGUI.ResponseAction.openInventory(user.getItemCreator()));
                })
                .text("0.00")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(ColorFixer.addColors("&0Podaj szanse"))
                .plugin(plugin)
                .open(user.getPlayer());
    }
    private void setChanceCmd(User user) {
        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    String text = stateSnapshot.getText();
                    Player player = stateSnapshot.getPlayer();
                    double chance = 0;
                    if(!NumberChecker.isDouble(text)) {
                        player.sendMessage(ColorFixer.addColors("&cPodaj liczbe!"));
                        return List.of(AnvilGUI.ResponseAction.openInventory(user.getCmdCreator()));
                    }
                    chance = Double.parseDouble(text);
                    if(chance<=0)
                    {
                        player.sendMessage(ColorFixer.addColors("&cNie mozesz podac liczby ujemnej!"));
                        return List.of(AnvilGUI.ResponseAction.openInventory(user.getCmdCreator()));

                    }
                    user.setChance(chance);
                    user.refreshChance();
                    return List.of(AnvilGUI.ResponseAction.openInventory(user.getCmdCreator()));
                })
                .text("0.00")
                .itemLeft(new ItemStack(Material.PAPER))
                .title(ColorFixer.addColors("&0Podaj szanse"))
                .plugin(plugin)
                .open(user.getPlayer());
    }

    private void saveCmd(User user) {
        if(user.getCmd()==null)
        {
            user.getPlayer().sendMessage(ColorFixer.addColors("&cKomenda nie moze byc pusta!"));
            return;
        }
        int keyItem = ThreadLocalRandom.current().nextInt(1, 10000 + 1);
        if(user.getCreateType()==CreateType.HIT_REWARD) {
            if (plugin.getConfig().isSet("metin." + user.getMetin().getKey() + ".commands." + keyItem)) {
                saveCmd(user);
                return;
            }

            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".commands." + keyItem + ".cmd", user.getCmd());
            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".commands." + keyItem + ".chance", user.getChance());
            plugin.saveConfig();
            user.getMetin().getDropCmds().add(new CmdDrop(String.valueOf(keyItem), user.getCmd(), user.getChance()));
        }
        if(user.getCreateType()==CreateType.FINAL_REWARD) {
            if (plugin.getConfig().isSet("metin." + user.getMetin().getKey() + ".final-rewards.commands." + keyItem)) {
                saveCmd(user);
                return;
            }

            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".final-rewards.commands." + keyItem + ".cmd", user.getCmd());
            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".final-rewards.commands." + keyItem + ".chance", user.getChance());
            plugin.saveConfig();
            user.getMetin().getFinalRewards().getCommands().add(new CmdDrop(String.valueOf(keyItem), user.getCmd(), user.getChance()));
        }
        user.getPlayer().sendMessage(ColorFixer.addColors("&aPomyslnie dodano komende!"));
        user.resetData();
        user.openInventory();
    }
    private void saveItem(ItemStack item, User user) {
        int keyItem = ThreadLocalRandom.current().nextInt(1, 10000 + 1);
        if(user.getCreateType()==CreateType.HIT_REWARD) {
            if (plugin.getConfig().isSet("metin." + user.getMetin().getKey() + ".items." + keyItem)) {
                saveItem(item, user);
                return;
            }

            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".items." + keyItem + ".item", item);
            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".items." + keyItem + ".chance", user.getChance());
            plugin.saveConfig();
            user.getMetin().getDropItems().add(new ItemDrop(String.valueOf(keyItem), item, user.getChance()));
        }
        if(user.getCreateType()==CreateType.FINAL_REWARD) {
            if (plugin.getConfig().isSet("metin." + user.getMetin().getKey() + ".final-rewards.items." + keyItem)) {
                saveItem(item, user);
                return;
            }

            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".final-rewards.items." + keyItem + ".item", item);
            plugin.getConfig().set("metin." + user.getMetin().getKey() + ".final-rewards.items." + keyItem + ".chance", user.getChance());
            plugin.saveConfig();
            user.getMetin().getFinalRewards().getItems().add(new ItemDrop(String.valueOf(keyItem), item, user.getChance()));
        }
        user.getPlayer().sendMessage(ColorFixer.addColors("&aPomyslnie dodano przedmiot!"));

        user.resetData();
        user.openInventory();
    }

    private void addFinalItem(User user, CreateType createType) {
        user.setCreateType(createType);
        user.getPlayer().openInventory(user.getFinalInvCreator());
    }

    private void addCmd(User user, CreateType createType) {
        user.setCreateType(createType);
        user.getPlayer().openInventory(user.getCmdCreator());
    }

    private void addItem(User user, CreateType createType) {
        user.setCreateType(createType);
        user.getPlayer().openInventory(user.getItemCreator());
    }
}
