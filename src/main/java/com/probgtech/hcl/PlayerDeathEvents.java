package com.probgtech.hcl;

import java.lang.reflect.InvocationTargetException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEvents implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		final Player p = event.getEntity();
		if (HCLives.cfg.contains("Players." + p.getUniqueId())){
			int lives = HCLives.cfg.getInt("Players." + p.getUniqueId());
			lives -= 1;
			p.getWorld().strikeLightningEffect(p.getLocation());
			HCLives.instance.getServer().broadcastMessage("");
			HCLives.instance.getServer().broadcastMessage(HCLives.instance.getPrefix() + ChatColor.translateAlternateColorCodes('&', "&7"+p.getName()+" has died and has "+ lives + " lives remaining."));
			HCLives.instance.getServer().broadcastMessage("");
			HCLives.cfg.set("LastDeath." + p.getUniqueId(), System.currentTimeMillis());
			if (lives <= 0){
				lives = 0;
				HCLives.cfg.set("Players." + p.getUniqueId(), lives);
				p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fYou have reached the max amount of lives."));
			} else {
				HCLives.cfg.set("Players." + p.getUniqueId(), lives);
			}
			HCLives.instance.sb.removePlayer(p);
			HCLives.instance.sb.getScoreboard().resetScores(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(HCLives.instance, new Runnable() {
				public void run() {
					Object nmsPlayer;
					try {
						nmsPlayer = p.getClass().getMethod("getHandle").invoke(p);
						Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
			            Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand$EnumClientCommand");
			            for(Object ob : enumClass.getEnumConstants()){
			                if(ob.toString().equals("PERFORM_RESPAWN")){
			                    packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
			                }
			            }

			            Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
			            con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException
							| SecurityException | InstantiationException | ClassNotFoundException | NoSuchFieldException e) {
						e.printStackTrace();
					}
				}
			}, 20L);
		} else {
			p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&cHCLives: &fAn error has occurred."));
		}
	}

}
