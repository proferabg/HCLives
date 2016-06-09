package com.cpx1989.hclives;

import java.io.File;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class HCLCommand implements CommandExecutor {
	
	HCLives plugin;
	public HCLCommand(HCLives hcLives) {
		plugin = hcLives;
	}

	@Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		if (args.length == 0){
    		sender.sendMessage(plugin.getPrefix() + "Invalid arguments try " + ChatColor.RED + " /hcl help");
    	}
    	else if (args.length == 1){
    		if (args[0].equalsIgnoreCase("help"))
    		{	
    			sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    			sender.sendMessage(ChatColor.RED + "/hcl help" + ChatColor.GRAY + " - Shows all available commands!");
    			if(sender.hasPermission("hcl.reload")){
    				sender.sendMessage(ChatColor.RED + "/hcl reload" + ChatColor.GRAY + " - Reloads the plugin!");
    			}
    			if(sender.hasPermission("hcl.admin")){
    				sender.sendMessage(ChatColor.RED + "/hcl add <name/uuid> <amount>" + ChatColor.GRAY + " - Add some lives to the player!");
    			}
    			if(sender.hasPermission("hcl.admin")){
    				sender.sendMessage(ChatColor.RED + "/hcl set <name/uuid> <amount>" + ChatColor.GRAY + " - Set amount of lives for the player!");
    			}
    			sender.sendMessage(ChatColor.RED + "/hcl check" + ChatColor.GRAY + " - Check the amount of lives you have!");
    			if(sender.hasPermission("hcl.check.others")){
    				sender.sendMessage(ChatColor.RED + "/hcl check <name/uuid>" + ChatColor.GRAY + " - Check the amount of lives the player has!");
    			}
    			sender.sendMessage(ChatColor.RED + "/hcl toggle" + ChatColor.GRAY + " - Toggle the scoreboard visibility!");
    			sender.sendMessage(ChatColor.RED + "/hcl about" + ChatColor.GRAY + " - Shows CPx1989's bragging rights!");
    		}
    		else if (args[0].equalsIgnoreCase("about"))
    		{
    			sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    			sender.sendMessage(ChatColor.RED + "HCLives v" + plugin.getDescription().getVersion() + ChatColor.GRAY + " by CPx1989");
        		sender.sendMessage(ChatColor.GRAY + "Made for the hub at:");
        		sender.sendMessage(ChatColor.DARK_GREEN + "    GimmeCraft.com");
    		}
    		else if (args[0].equalsIgnoreCase("check"))
    		{
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
    				sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    				sender.sendMessage(ChatColor.GRAY + "You have  " + HCLives.cfg.getInt("Players." + play.getUniqueId()) + " lives remaining");
    			}
    			else {
			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Command must be run as player!");
    			}
    		}	
    		else if (args[0].equalsIgnoreCase("toggle"))
    		{
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
			    	sender.sendMessage(plugin.getPrefix() + ChatColor.GRAY + "Scoreboard toggled!");
			    	if(play.getScoreboard() != HCLives.instance.sb.getScoreboard()){
			    		play.setScoreboard(HCLives.instance.sb.getScoreboard());
			    	} else {
			    		play.setScoreboard(HCLives.instance.getServer().getScoreboardManager().getNewScoreboard());
			    	}
    			}
    			else {
			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "Command must be run as player!");
    			}
    		}	
    		else if (args[0].equalsIgnoreCase("reload")){
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
    			    if(play.hasPermission("hcl.reload")){
    			    	HCLives.cfg = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        				sender.sendMessage(plugin.getPrefix() + "Config Reloaded!");
    			    }
    			    else{
    			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
    			    }
    			}
    			else {
			    	HCLives.cfg = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
			    	sender.sendMessage(plugin.getPrefix() + "Config Reloaded!");
    			}
    		} 
    		else {
    			sender.sendMessage(plugin.getPrefix() + "Check your arguments or do " + ChatColor.RED + "/hcl help");
    		}
    	}
    	else if (args.length == 2){
    		if (args[0].equalsIgnoreCase("check")){
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
    				if(play.hasPermission("hcl.check.others")){
    					if (HCLives.cfg.contains("PlayerCache." + args[1])){
    						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
    						if (HCLives.cfg.contains("Players." + uuid)){
    							sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
        						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
    						} else {
        			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
        			    	}
    					} else if (isValidUUID(args[1]) && HCLives.cfg.contains("Players." + UUID.fromString(args[1]))){
    						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + UUID.fromString(args[1]) + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + UUID.fromString(args[1])) + " lives remaining");
    					} else {
    			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
    			    	}
    				} else{
    			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
    			    }
    			} else {
    				if (HCLives.cfg.contains("PlayerCache." + args[1])){
						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
						if (HCLives.cfg.contains("Players." + uuid)){
							sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
						} else {
    			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
    			    	}
					} else if (isValidUUID(args[1]) && HCLives.cfg.contains("Players." + UUID.fromString(args[1]))){
						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + UUID.fromString(args[1]) + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + UUID.fromString(args[1])) + " lives remaining");
					} else {
			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
			    	}
    			}
    		} else {
    			sender.sendMessage(plugin.getPrefix() + "Check your arguments or do " + ChatColor.RED + "/hcl help");
    		}
    	}
    	else if (args.length == 3){
    		if (args[0].equalsIgnoreCase("add")){	
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
    			    if(play.hasPermission("hcl.admin")){
    					if (HCLives.cfg.contains("PlayerCache." + args[1])){
    						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
    						if (HCLives.cfg.contains("Players." + uuid)){
    							int lives = HCLives.cfg.getInt("Players." + uuid);
        			    		int newLives = Integer.parseInt(args[2]);
        			    		lives += newLives;
        			    		HCLives.cfg.set("Players." + uuid, lives);
        						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
        						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
    						} else {
        			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
        			    	}
    					} else if (isValidUUID(args[1]) && HCLives.cfg.contains("Players." + UUID.fromString(args[1]))){
    			    		int lives = HCLives.cfg.getInt("Players." + UUID.fromString(args[1]));
    			    		int newLives = Integer.parseInt(args[2]);
    			    		lives += newLives;
    			    		HCLives.cfg.set("Players." + UUID.fromString(args[1]), lives);
    						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + UUID.fromString(args[1]) + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + UUID.fromString(args[1])) + " lives remaining");
    			    	} else {
    			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
    			    	}
    			    }
    			    else{
    			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
    			    }
    			}
    			else {
    				if (HCLives.cfg.contains("PlayerCache." + args[1])){
						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
						if (HCLives.cfg.contains("Players." + uuid)){
							int lives = HCLives.cfg.getInt("Players." + uuid);
    			    		int newLives = Integer.parseInt(args[2]);
    			    		lives += newLives;
    			    		HCLives.cfg.set("Players." + uuid, lives);
    						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
						} else {
    			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID or Name doesn't exist!");
    			    	}
					} else if (isValidUUID(args[1]) && HCLives.cfg.contains("Players." + UUID.fromString(args[1]))){
			    		int lives = HCLives.cfg.getInt("Players." + UUID.fromString(args[1]));
			    		int newLives = Integer.parseInt(args[2]);
			    		lives += newLives;
			    		HCLives.cfg.set("Players." + UUID.fromString(args[1]), lives);
						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + UUID.fromString(args[1]) + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + UUID.fromString(args[1])) + " lives remaining");
			    	} else {
			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID doesn't exist!");
			    	}
    			}
    		}
    		else if (args[0].equalsIgnoreCase("set")){
    			if(sender instanceof Player) {
            		Player play = (Player)sender;
    			    if(play.hasPermission("hcl.admin")){
    			    	if (HCLives.cfg.contains("PlayerCache." + args[1])){
    						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
    						int lives = Integer.parseInt(args[2]);
    						HCLives.cfg.set("Players." + uuid, lives);
    						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
    					} else if (isValidUUID(args[1])){
    						UUID uuid = UUID.fromString(args[1]);
    						int lives = Integer.parseInt(args[2]);
    						HCLives.cfg.set("Players." + UUID.fromString(args[1]), lives);
    						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
    						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + uuid + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
    					} else {
    			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID invalid or Name doesn't exist!");
    			    	}
    			    }
    			    else{
    			    	sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "You do not have permission!");
    			    }
    			}
    			else {
    				if (HCLives.cfg.contains("PlayerCache." + args[1])){
						UUID uuid = UUID.fromString(HCLives.cfg.getString("PlayerCache." + args[1]));
						int lives = Integer.parseInt(args[2]);
						HCLives.cfg.set("Players." + uuid, lives);
						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
						sender.sendMessage(ChatColor.GRAY + "Player " + ChatColor.RED + args[1] + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
					} else if (isValidUUID(args[1])){
						UUID uuid = UUID.fromString(args[1]);
						int lives = Integer.parseInt(args[2]);
						HCLives.cfg.set("Players." + UUID.fromString(args[1]), lives);
						sender.sendMessage(ChatColor.GRAY + " ------ " + ChatColor.RED + "HCLives" + ChatColor.GRAY + " ------ ");
						sender.sendMessage(ChatColor.GRAY + "UUID " + ChatColor.RED + uuid + ChatColor.GRAY +" has " + HCLives.cfg.getInt("Players." + uuid) + " lives remaining");
					} else {
			    		sender.sendMessage(plugin.getPrefix() + ChatColor.RED + "UUID invalid or Name doesn't exist!");
			    	}
    			}
    		}
    		else {
    			sender.sendMessage(plugin.getPrefix() + "Check your arguments or do " + ChatColor.RED + "/hcl help");
    		}
    	}
    	else {
			sender.sendMessage(plugin.getPrefix() + "Check your arguments or do " + ChatColor.RED + "/hcl help");
		}
		return false;
    }
	
	public boolean isValidUUID(String uuid){
		if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-4[0-9]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
			return true;
		} else {
			return false;
		}
	}

}
