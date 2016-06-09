package com.cpx1989.hclives;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnEvents implements Listener{
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		if (HCLives.cfg.contains("Players." + p.getUniqueId())){
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			if (lives <= 0){
				p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fYou have reached the max amount of lives."));
			} else {
				HCLives.instance.sb.addPlayer(p);
				p.setScoreboard(HCLives.instance.sb.getScoreboard());
				TitleAPI.setTitle(p, ChatColor.RED + "Welcome to Vanilla", ChatColor.GRAY + "You have " + lives + " lives remaining", 1, 3, 1);
			}
		} else {
			HCLives.cfg.set("Players." + p.getUniqueId(), HCLives.cfg.getInt("Options.Lives"));
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			TitleAPI.setTitle(p, ChatColor.RED + "Welcome to Vanilla", ChatColor.GRAY + "You have " + lives + " lives remaining", 1, 3, 1);
			HCLives.instance.sb.addPlayer(p);
			p.setScoreboard(HCLives.instance.sb.getScoreboard());
		}
	}

}
