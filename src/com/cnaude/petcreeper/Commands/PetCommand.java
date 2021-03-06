/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cnaude.petcreeper.Commands;

import java.util.ArrayList;
import java.util.List;
import com.cnaude.petcreeper.Pet;
import com.cnaude.petcreeper.PetCreeper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author cnaude
 */
public class PetCommand implements CommandExecutor {

    private final PetCreeper plugin;
    
    private List<String> validCommands = new ArrayList<String>();

    public PetCommand(PetCreeper instance) {
        plugin = instance;
        validCommands.add("age");
        validCommands.add("color");
        validCommands.add("free");
        validCommands.add("give");
        validCommands.add("info");
        validCommands.add("kill");
        validCommands.add("list");
        validCommands.add("mode");
        validCommands.add("name");
        validCommands.add("reload");
        validCommands.add("saddle");
        validCommands.add("spawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                return false;
            }
            if (!plugin.hasPerm(p, "petcreeper.pet")) {
                plugin.message(p, ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }
            if (p.isInsideVehicle()) {
                if (p.getVehicle().getType().isAlive()) {
                    plugin.message(p, ChatColor.RED + "You can't use /" + plugin.config.commandPrefix + " when riding this " + p.getVehicle().getType().getName() + ".");
                    return true;
                }
            }
            if (args.length == 1) {
                if (args[0].matches("\\d+")) {
                    if (plugin.isPetOwner(p)) {
                        int idx = Integer.parseInt(args[0]) - 1;
                        if (idx >= 0 && idx < plugin.getPetsOf(p).size()) {
                            Pet pet = plugin.getPetsOf(p).get(idx);
                            plugin.teleportPet(pet, p.getLocation(), true, false);
                        } else {
                            plugin.message(p, ChatColor.RED + "Invalid pet ID.");
                        }
                    } else {
                        plugin.message(p, ChatColor.RED + "You have no pets. :(");
                    }
                } else if (args[0].toString().equalsIgnoreCase("all")) {
                    if (plugin.isPetOwner(p)) {
                        plugin.teleportPetsOf(p, p.getLocation(), true, false);
                    } else {
                        plugin.message(p, ChatColor.RED + "You have no pets. :(");
                    }
                } else if (args[0].toString().equals("?") || args[0].toString().equalsIgnoreCase("help")) {
                    //plugin.message(p, ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + plugin.config.commandPrefix + " [id|all]");
                    return false;
                } else {
                    dispatch(sender, args);
                } 
            } else {
                dispatch(sender, args);
            }
        } else {
            if (args.length == 1) {
                if (args[0].matches("reload")) {
                    Bukkit.dispatchCommand(sender, "petreload");
                }
            }
        }
        return true;
    }

    public void dispatch(CommandSender sender, String args[]) {
        if (args.length > 0) {
            args[0] = args[0].replaceAll("^\\s+", "");
            if (validCommands.contains(args[0])) {
                String commandLine = "";
                for (String s : args) {
                    commandLine = commandLine + " " + s;
                }                
                commandLine = commandLine.replaceAll("^\\s+", "");
                Bukkit.dispatchCommand(sender, "pet" + commandLine);
            } else {                
                Bukkit.dispatchCommand(sender, "help petcreeper");
            }
        } else {
            Bukkit.dispatchCommand(sender, "help petcreeper");
        }
    }
}
