package com.cpx1989.hclives;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Board {
	
	private Scoreboard scoreboard;
	private Objective objective;
	
	public Board(HCLives p){
		scoreboard = p.getServer().getScoreboardManager().getNewScoreboard();
	    objective = scoreboard.registerNewObjective("HCLives", "dummy");
	    objective.setDisplayName("§cHCLives");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	@SuppressWarnings("deprecation")
	public void addPlayer(Player p) {
		int lives = HCLives.cfg.getInt("Players."+p.getUniqueId());
	    Team team = scoreboard.getPlayerTeam(p);
	    if (team == null) {
	    	team = scoreboard.registerNewTeam(p.getName());
		    team.addPlayer(p);
		    Score score = objective.getScore(p.getName());
		    score.setScore(lives);
	    }
	}
	
	@SuppressWarnings("deprecation")
	public void removePlayer(Player p){
	    Team team = scoreboard.getPlayerTeam(p);
	    if (team != null){
	    	team.removePlayer(p);
	    	team.unregister();
	    }
	}
	
	public Scoreboard getScoreboard(){
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return scoreboard;
	}

}
