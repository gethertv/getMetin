package dev.gether.getmetin.cmd;

import com.google.common.collect.Lists;
import dev.gether.getmetin.GetMetin;
import dev.gether.getmetin.data.MetinData;
import dev.gether.getmetin.file.MetinyFile;
import dev.gether.getmetin.metin.Metin;
import dev.gether.getmetin.metin.MetinManager;
import dev.gether.getmetin.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GetMetinCmd implements CommandExecutor, TabExecutor {

    private final GetMetin plugin;
    public GetMetinCmd(GetMetin plugin, String command)
    {
        this.plugin = plugin;
        plugin.getCommand(command).setExecutor(this);
        plugin.getCommand(command).setTabCompleter(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return false;

        Player player = (Player) sender;
        if(!player.hasPermission("getmetin.admin"))
            return false;

        if(args.length==1)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                plugin.reloadPlugin(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("removeloc"))
            {
                Block targetBlock = player.getTargetBlock(null, 5);
                if(targetBlock==null || targetBlock.getType()== Material.AIR)
                {
                    player.sendMessage(ColorFixer.addColors("&cMusisz patrzec na blok!"));
                    return false;
                }
                MetinData metinData = plugin.getMetinData().get(targetBlock.getLocation());
                if(metinData==null)
                {
                    player.sendMessage(ColorFixer.addColors("&cMusisz patrzyc sie na metina!"));
                    return true;
                }
                metinData.getHologram().destroy();
                metinData.getMetinLoc().getBlock().setType(Material.AIR);
                MetinyFile.getConfig().set("metin."+metinData.getKey(), null);
                MetinyFile.save();
                plugin.getMetinData().remove(targetBlock.getLocation());
                player.sendMessage(ColorFixer.addColors("&aPomyslnie usunieto lokalizacje metina!"));
                return true;
            }
        }
        if(args.length==2)
        {
            if(args[0].equalsIgnoreCase("delete"))
            {
                String name = args[1];
                Metin metin = plugin.getMetinManager().getMetin(name);
                if(metin==null)
                {
                    player.sendMessage(ColorFixer.addColors("&cPodany metin nie istnieje!"));
                    return false;
                }
                plugin.getConfig().set("metin."+metin.getKey(), null);
                plugin.saveConfig();
                plugin.getMetinManager().getMetinData().remove(metin);
                plugin.getMetinData().values().removeIf(metinData->
                {
                    if(metinData.getMetin().equals(metin))
                    {
                        MetinyFile.getConfig().set("metin."+metinData.getKey(), null);
                        MetinyFile.save();
                        return true;
                    }
                    return false;
                });
                player.sendMessage(ColorFixer.addColors("&aPomyslnie usunieto metina!"));
                return true;
            }
            if(args[0].equalsIgnoreCase("create"))
            {
                String name = args[1];
                List<String> hologram = Arrays.asList("&7---------------", "&a&lMETIN "+name, "&fHP: &6{actually-hp}&7/&c{max-hp}","&7---------------");
                plugin.getConfig().set("metin."+name+".hologram", hologram);
                plugin.getConfig().set("metin."+name+".material", "BEACON");
                plugin.getConfig().set("metin."+name+".hp", 20);
                plugin.getConfig().set("metin."+name+".name", name);
                plugin.getConfig().set("metin."+name+".items", new HashMap<>());
                plugin.getConfig().set("metin."+name+".commands", new HashMap<>());
                plugin.getConfig().set("metin."+name+".final-rewards.items", new HashMap<>());
                plugin.getConfig().set("metin."+name+".final-rewards.commands", new HashMap<>());
                plugin.saveConfig();
                player.sendMessage(ColorFixer.addColors("&aPomyslnie stworzono metin!"));
                return true;
            }
            if(args[0].equalsIgnoreCase("edit"))
            {
                Metin metin = plugin.getMetinManager().getMetin(args[1]);
                if(metin==null)
                {
                    player.sendMessage(ColorFixer.addColors("&cPodany metin nie istnieje!"));
                    return false;
                }
                plugin.getMetinManager().adminEditMetin(player, metin);
                return true;
            }
        }
        if(args.length==3)
        {
            if(args[0].equalsIgnoreCase("setlocation"))
            {
                Metin metin = plugin.getMetinManager().getMetin(args[1]);
                if(metin==null)
                {
                    player.sendMessage(ColorFixer.addColors("&cPodany metin nie istnieje!"));
                    return false;
                }
                if(!isNumber(args[2]))
                {
                    player.sendMessage(ColorFixer.addColors("&cBlad! Musisz podac sekundy! /getmetin setlocation <nazwa> <sekundy>"));
                    return false;
                }
                int second = Integer.parseInt(args[2]);
                String uuid = UUID.randomUUID().toString();
                MetinyFile.getConfig().set("metin."+ uuid +".loc", player.getLocation().getBlock().getLocation());
                MetinyFile.getConfig().set("metin."+ uuid+".key", metin.getKey());
                MetinyFile.getConfig().set("metin."+ uuid+".second", second);
                MetinyFile.save();
                player.sendMessage(ColorFixer.addColors("&aPomyslnie ustawiono metina!"));
                return true;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==2)
        {
            if(args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("setlocation"))
            {
                List<String> name = new ArrayList<>();
                plugin.getMetinManager().getMetinData().forEach(metin -> name.add(metin.getName()));
                return name;
            }
        }
        if(args.length==1)
        {
            return Arrays.asList("setlocation", "edit", "create", "delete", "removeloc");
        }
        return null;
    }

    private boolean isNumber(String input)
    {
        try {
            int a = Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ingored) {}

        return false;
    }
}
