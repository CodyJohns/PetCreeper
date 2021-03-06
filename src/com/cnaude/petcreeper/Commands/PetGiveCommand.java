/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cnaude.petcreeper.Commands;

import com.cnaude.petcreeper.Pet;
import com.cnaude.petcreeper.PetCreeper;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 *
 * @author cnaude
 */
public class PetGiveCommand implements CommandExecutor {

    private final PetCreeper plugin;

    public PetGiveCommand(PetCreeper instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;                       
                if (!plugin.hasPerm(p, "petcreeper.give")) {
                    plugin.message(p, ChatColor.RED + "You do not have permission to use this command.");
                    return true;
                }
                if (plugin.isPetOwner(p)) {
                    if (args.length == 2 && args[0].matches("\\d+")) {
                        int idx = Integer.parseInt(args[0]) - 1;
                        if (idx >= 0 && idx < plugin.getPetsOf(p).size()) {
                            Pet pet = plugin.getPetsOf(p).get(idx);
                            Entity e = plugin.getEntityOfPet(pet);
                            Player rec = plugin.getServer().getPlayer(args[1]);
                            if (rec != null && rec instanceof Player) {
                                if (plugin.isPetOwner(rec)) {
                                    if (plugin.getPetsOf(rec).size() >= plugin.config.maxPetsPerPlayer) {
                                        plugin.message(p, ChatColor.RED + "Player " + rec.getName() + " already has maximum number of pets!");
                                        return true;
                                    }
                                }
                                plugin.untamePetOf(p, e, false);
                                if (plugin.tamePetOf(rec, e, true)) {
                                    plugin.message(p, ChatColor.GREEN + "You gave your pet " + ChatColor.YELLOW
                                            + pet.petName + ChatColor.GREEN + " to " + ChatColor.YELLOW + rec.getName()
                                            + ChatColor.GREEN + ".");
                                } else {
                                    plugin.message(p, ChatColor.RED + "Error give pet to player!");
                                }
                            } else {
                                plugin.message(p, ChatColor.RED + "Invalid player.");
                            }
                        } else {
                            plugin.message(p, ChatColor.RED + "Invalid pet ID.");
                        }
                    } else {
                        plugin.message(p, ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + plugin.config.commandPrefix + "give [id] [player]");
                    }
                } else {
                    plugin.message(p, ChatColor.RED + "You have no pets. :(");
                }
            }                                
        return true;
    }
}
