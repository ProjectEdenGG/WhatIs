package au.com.addstar.whatis;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.addstar.whatis.commands.CommandFinder;

public class WhatIs extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		CommandFinder.init();
		
		CommandDispatcher whatis = new CommandDispatcher("whatis", "");
		whatis.registerCommand(new EventViewCommand());
		whatis.registerCommand(new CommandCommand());
		File reportDir = new File(getDataFolder(), "reports");
		reportDir.mkdirs();
		whatis.registerCommand(new ReportCommand(reportDir));
		
		getCommand("whatis").setExecutor(whatis);
		getCommand("whatis").setTabCompleter(whatis);
		
		Bukkit.getScheduler().runTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				getLogger().info("Building event name map");
				EventHelper.buildEventMap();
			}
		});
	}
	
	public static File getPluginSource(Plugin plugin)
	{
		if(plugin instanceof JavaPlugin)
		{
			try
			{
				Field field = JavaPlugin.class.getDeclaredField("file");
				field.setAccessible(true);
				return (File)field.get(plugin);
			}
			catch(Exception e)
			{
				// Wont happen
				return null;
			}
		}
		
		return null;
	}
}
