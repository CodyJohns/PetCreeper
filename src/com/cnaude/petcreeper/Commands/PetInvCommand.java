package com.cnaude.petcreeper.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cnaude.petcreeper.Pet;
import com.cnaude.petcreeper.PetCreeper;

public class PetInvCommand implements CommandExecutor {
	
	private final PetCreeper plugin;

    public PetInvCommand(PetCreeper instance) {
        plugin = instance;
    }

	 public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
	        if (sender instanceof Player) {
	            Player p = (Player) sender;
	            if (!plugin.hasPerm(p, "petcreeper.seeInv")) {
	                plugin.message(p, ChatColor.RED + "You do not have permission to use this command.");
	                return true;
	            }
	            if (plugin.isPetOwner(p)) {
	                if (args.length == 1 && args[0].matches("\\d+")) {
	                    int idx = Integer.parseInt(args[0]) - 1;
	                    if (idx >= 0 && idx < plugin.getPetsOf(p).size()) {
	                    	Pet pet = plugin.getPetsOf(p).get(idx);
	                    	p.openInventory(plugin.getPetInventory(pet));
	                    } else {
	                        plugin.message(p, ChatColor.RED + "Invalid pet ID.");
	                    }
	                } else {
	                    plugin.message(p, ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + plugin.config.commandPrefix + "inv [id]");
	                }
	            } else {
	                plugin.message(p, ChatColor.RED + "You have no pets. :(");
	            }
	        }
	        return true;
	    }
}
