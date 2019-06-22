package com.cpx1989.hclives;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class TitleAPI {
	
	static Class<?> craftplayer, packtitle, pack, chatser, ichat, cct;
	@SuppressWarnings("rawtypes")
	static Class<Enum> enumtitle;
	static Method csm, etm;

	public enum TitleEnum{
		TITLE, SUBTITLE, TIMES, RESET, CLEAR;
	}
	
	public static void setTitle(Player player, String title, String subtitle, int fIn, int dur, int fOut){
		if (subtitle != null){
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
			setTitle(player, subtitle, 2);
		}
		if (fIn > -1 && fOut > -1 && dur > -1 && title != null){
			title = ChatColor.translateAlternateColorCodes('&', title);
			setTitleTimes(player, title, fIn, dur, fOut);
		} else if (fIn > -1 && fOut > -1 && dur > -1){
			setTimes(player, fIn, dur, fOut);
		} else if (title != null){
			title = ChatColor.translateAlternateColorCodes('&', title);
			setTitle(player, title, 1);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setTitleTimes(Player player, String title, int fIn, int dur, int fOut) {
		try {
			String nmsver = Bukkit.getServer().getClass().getPackage().getName();
			nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
			craftplayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
	    	packtitle = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle");
	    	pack = Class.forName("net.minecraft.server." + nmsver + ".Packet");
	    	Object p = craftplayer.cast(player);
    		Object ppot = null;
    		Object enumf = null;
	    	Object cbc = null;
			if (nmsver.equalsIgnoreCase("v1_8_R1")) {
		    	enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".EnumTitleAction");
		    	etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
	    		enumf = enumtitle.cast(etm.invoke(enumtitle, "title"));
		    	chatser = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
			} else if (nmsver.startsWith("v1_8_") || nmsver.startsWith("v1_9_") || nmsver.startsWith("v1_10_")){
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle$EnumTitleAction");
		    	etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
	    		enumf = enumtitle.cast(etm.invoke(enumtitle, "title"));
		    	chatser = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent$ChatSerializer");
			} else {
				HCLives.instance.logger.warning("The Minecraft Title feature is only available in MC 1.8+");
				return;
			}
	    	ichat = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
	    	csm = chatser.getDeclaredMethod("a", new Class<?>[] {String.class});
    		cbc = ichat.cast(csm.invoke(chatser, "{\"text\": \"" + title + "\"}"));
			ppot = packtitle.getConstructor(new Class<?>[] {enumtitle, ichat, int.class, int.class, int.class}).newInstance(new Object[] {enumf, cbc, fIn, dur, fOut});
    		Method m1 = craftplayer.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {pack});
    		m5.invoke(pc, ppot);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}

	private static void setTimes(Player player, int fIn, int dur, int fOut) {
		try {
    		Object p = craftplayer.cast(player);
    		Object ppot = null;
    		ppot = packtitle.getConstructor(new Class<?>[] {int.class, int.class, int.class}).newInstance(new Object[] {fIn, dur, fOut});
    		Method m1 = craftplayer.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {pack});
    		m5.invoke(pc, ppot);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setTitle(Player player, String s, int i){
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		String m;
		if (i == 2) m = "subtitle";
		else m = "title";
		try {
			craftplayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
	    	packtitle = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle");
	    	pack = Class.forName("net.minecraft.server." + nmsver + ".Packet");
    		Object p = craftplayer.cast(player);
    		Object ppot = null;
    		Object cbc = null;
    		Object enumf = null;
			if (nmsver.equalsIgnoreCase("v1_8_R1")) {
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".EnumTitleAction");
				etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
				enumf = enumtitle.cast(etm.invoke(enumtitle, m));
		    	chatser = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
			} else if(nmsver.startsWith("v1_8_") || nmsver.startsWith("v1_9_") || nmsver.startsWith("v1_10_")){
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle$EnumTitleAction");
		    	etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
	    		enumf = enumtitle.cast(etm.invoke(enumtitle, m));
		    	chatser = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent$ChatSerializer");
			} else {
				HCLives.instance.logger.warning("The Minecraft Title feature is only available in MC 1.8+");
				return;
			}
	    	ichat = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
	    	csm = chatser.getDeclaredMethod("a", new Class<?>[] {String.class});
    		cbc = ichat.cast(csm.invoke(chatser, "{\"text\": \"" + s + "\"}"));
    		ppot = packtitle.getConstructor(new Class<?>[] {enumtitle, ichat}).newInstance(new Object[] {enumf, cbc});
    		Method m1 = craftplayer.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {pack});
    		m5.invoke(pc, ppot);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void clear(Player player){
		try {
			String nmsver = Bukkit.getServer().getClass().getPackage().getName();
			nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
			craftplayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
	    	packtitle = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle");
	    	pack = Class.forName("net.minecraft.server." + nmsver + ".Packet");
    		Object p = craftplayer.cast(player);
    		Object ppot = null;
    		Object enumf = null;
    		if (nmsver.equalsIgnoreCase("v1_8_R1")) {
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".EnumTitleAction");
				etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
				enumf = enumtitle.cast(etm.invoke(enumtitle, "clear"));
			} else if(nmsver.startsWith("v1_8_") || nmsver.startsWith("v1_9_") || nmsver.startsWith("v1_10_")){
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle$EnumTitleAction");
		    	etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
	    		enumf = enumtitle.cast(etm.invoke(enumtitle, "clear"));
			} else {
				HCLives.instance.logger.warning("The Minecraft Title feature is only available in MC 1.8+");
				return;
			}
    		ppot = packtitle.getConstructor(new Class<?>[] {enumtitle, ichat}).newInstance(new Object[] {enumf, null});
    		Method m1 = craftplayer.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {pack});
    		m5.invoke(pc, ppot);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void reset(Player player){
		try {
			String nmsver = Bukkit.getServer().getClass().getPackage().getName();
			nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
			craftplayer = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
	    	packtitle = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle");
	    	pack = Class.forName("net.minecraft.server." + nmsver + ".Packet");
    		Object p = craftplayer.cast(player);
    		Object ppot = null;
    		Object enumf = null;
    		if (nmsver.equalsIgnoreCase("v1_8_R1")) {
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".EnumTitleAction");
				etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
				enumf = enumtitle.cast(etm.invoke(enumtitle, "reset"));
			} else if(nmsver.startsWith("v1_8_") || nmsver.startsWith("v1_9_") || nmsver.startsWith("v1_10_")){
				enumtitle = (Class<Enum>) Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutTitle$EnumTitleAction");
		    	etm = enumtitle.getMethod("a", new Class<?>[] {String.class});
	    		enumf = enumtitle.cast(etm.invoke(enumtitle, "reset"));
			} else {
				HCLives.instance.logger.warning("The Minecraft Title feature is only available in MC 1.8+");
				return;
			}
    		ppot = packtitle.getConstructor(new Class<?>[] {enumtitle, ichat}).newInstance(new Object[] {enumf, null});
    		Method m1 = craftplayer.getDeclaredMethod("getHandle", new Class<?>[] {});
    		Object h = m1.invoke(p);
    		Field f1 = h.getClass().getDeclaredField("playerConnection");
    		Object pc = f1.get(h);
    		Method m5 = pc.getClass().getDeclaredMethod("sendPacket",new Class<?>[] {pack});
    		m5.invoke(pc, ppot);
		} catch (Exception ex) {
    		ex.printStackTrace();
    	}
	}

}