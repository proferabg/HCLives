package com.probgtech.hcl;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinEvents implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		HCLives.cfg.set("PlayerCache."+p.getName(), p.getUniqueId().toString());
		if (HCLives.cfg.contains("Players." + p.getUniqueId())){
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			if (lives <= 0 && !p.hasPermission("hcl.bypass")){
				p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fYou have reached the max amount of lives."));
			} else {
				HCLives.instance.sb.addPlayer(p);
				p.setScoreboard(HCLives.instance.sb.getScoreboard());
				p.sendTitle(ChatColor.RED + "Welcome to Vanilla", ChatColor.GRAY + "You have " + lives + " lives remaining", 1, 3, 1);
			}
		} else {
			HCLives.cfg.set("Players." + p.getUniqueId(), HCLives.cfg.getInt("Options.Lives"));
			HCLives.cfg.set("LastDeath." + p.getUniqueId(), 0);
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			p.sendTitle(ChatColor.RED + "Welcome to Vanilla", ChatColor.GRAY + "You have " + lives + " lives remaining", 1, 3, 1);
			HCLives.instance.sb.addPlayer(p);
			p.setScoreboard(HCLives.instance.sb.getScoreboard());
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player p = event.getPlayer();
		HCLives.cfg.set("PlayerCache."+p.getName(), p.getUniqueId().toString());
		if (p.isBanned()){
			HCLives.instance.getServer().getBannedPlayers().remove(p);
		}
		if (HCLives.cfg.contains("Players." + p.getUniqueId())){
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			if (lives <= 0 && !p.hasPermission("hcl.bypass")){
				event.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fYou have reached the max amount of lives."));
				event.disallow(Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fYou have reached the max amount of lives."));
				event.setResult(Result.KICK_OTHER);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		Player p = event.getPlayer();
		HCLives.instance.sb.removePlayer(p);
		HCLives.instance.sb.getScoreboard().resetScores(p);
	}
	
}
