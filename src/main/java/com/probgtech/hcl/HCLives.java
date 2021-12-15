package com.probgtech.hcl;

import java.util.logging.Logger;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class HCLives extends JavaPlugin {
	
	static PluginDescriptionFile pluginyml;
	Logger logger;
	public Board sb;
	public static HCLives instance;
	public static FileConfiguration cfg;
	public int threadID;
	
	public void onEnable() {
		//Get logger before anything else
		logger = Logger.getLogger("Minecraft");
		
		//save instance
		instance = this;
		
		//get description
		pluginyml = getDescription();

		//get config
		cfg = getConfig();
		cfg.options().copyDefaults(true);
		saveConfig();
		configThread();
		
		//log authors
		logger.info(ChatColor.stripColor(getPrefix()) + "Plugin by " + pluginyml.getAuthors());

		getCommand("hclives").setExecutor(new HCLCommand(this));

		//register scoreboard
		sb = new Board(this);
		
		//register command listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerDeathEvents(), this);
		pm.registerEvents(new PlayerJoinEvents(), this);
		pm.registerEvents(new PlayerRespawnEvents(), this);
		
		for (Player p : this.getServer().getOnlinePlayers()){
			sb.addPlayer(p);
			p.setScoreboard(sb.getScoreboard());
		}
		
		new HCTask();

		new Metrics(this, 13606);

	}

	@SuppressWarnings("deprecation")
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		saveConfig();
		for (Player p : this.getServer().getOnlinePlayers()){
			sb.getScoreboard().resetScores(p);
			p.setScoreboard(getServer().getScoreboardManager().getNewScoreboard());
		}
	}
	
	public String getPrefix(){
		return ChatColor.translateAlternateColorCodes('&', "&7[&cHCLives&7] ");
	}
	
	public static HCLives getInstance(){
		return instance;
	}
	
	public void configThread(){
		threadID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				saveConfig();
				logger.info(ChatColor.stripColor(getPrefix()) + "Saving config.");
			}
		}, 0, 300 * 20L);
	}
}
