package com.cpx1989.hclives;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HCTask {
	
	BukkitTask task;
	BukkitTask task1;
	
	public HCTask(){
		start();
	}
	
	public void start(){
		if(HCLives.instance.getServer().getScheduler().isCurrentlyRunning(task.getTaskId())) return;
		task = new BukkitRunnable(){
			@Override
			public void run() {
				int maxlives = HCLives.cfg.getInt("Options.Lives");
				
				for(Player p : Bukkit.getOnlinePlayers()){
					
					if(!HCLives.cfg.contains("Players." + p.getUniqueId())){ //not in config
						continue;
					}
					
					int lives = HCLives.cfg.getInt("Players." + p.getUniqueId()); //get lives
					
					if(lives >= maxlives){ //lives greater than max
						continue;
					}
					
					if (!HCLives.cfg.contains("LastDeath." + p.getUniqueId())){ //last death not in config
						HCLives.cfg.set("LastDeath." + p.getUniqueId(), System.currentTimeMillis());
						continue;
					}
					
					long lastdeath = HCLives.cfg.getLong("LastDeath." + p.getUniqueId()) + 3600000; //get lastdeath or last retored life + 1 hour
					
					if (lastdeath > System.currentTimeMillis()){ //check it player is due for another life
						continue;
					}
					
					//add life to player
					
					lives += 1;
					HCLives.cfg.set("LastDeath." + p.getUniqueId(), System.currentTimeMillis()); //set last restored life 
					HCLives.cfg.set("Players." + p.getUniqueId(), lives); //set lives
					
				}
			}
		}.runTaskTimer(HCLives.instance, 0L, 20L);
		
		task1 = new BukkitRunnable(){
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()){
					p.setHealth(p.getHealth());
					HCLives.instance.sb.setLives(p, HCLives.cfg.getInt("Players." + p.getUniqueId()));
				}
			}
		}.runTaskTimer(HCLives.instance, 0L, 5*20L);
	}
	
	

}
