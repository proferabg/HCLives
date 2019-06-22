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
	private Objective objective2;
	
	public Board(HCLives p){
		scoreboard = p.getServer().getScoreboardManager().getNewScoreboard();
	    objective = scoreboard.registerNewObjective("HCLives", "dummy");
	    objective2 = scoreboard.registerNewObjective("HCLivesHealth", "health");
	    objective.setDisplayName("§cHCLives");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective2.setDisplaySlot(DisplaySlot.PLAYER_LIST);
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
		    p.setHealth(p.getHealth());
	    }
	}
	
	@SuppressWarnings("deprecation")
	public void setLives(Player p, int i){
		Team team = scoreboard.getPlayerTeam(p);
	    if (team == null) {
	    	addPlayer(p);
	    } else {
	    	Score score = objective.getScore(p.getName());
		    score.setScore(i);
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
		objective2.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		return scoreboard;
	}

}
