package au.com.addstar.whatis;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;

import au.com.addstar.whatis.commands.CommandFinder;

public class CommandCommand implements ICommand
{
	@Override
	public String getName()
	{
		return "command";
	}

	@Override
	public String[] getAliases()
	{
		return new String[] {"cmd"};
	}

	@Override
	public String getPermission()
	{
		return null;
	}

	@Override
	public String getUsageString( String label, CommandSender sender )
	{
		return label + " <command>";
	}

	@Override
	public String getDescription()
	{
		return "Checks what plugin owns a command";
	}

	@Override
	public boolean canBeConsole()
	{
		return true;
	}

	@Override
	public boolean canBeCommandBlock()
	{
		return false;
	}

	@Override
	public boolean onCommand( CommandSender sender, String label, String[] args )
	{
		if(args.length != 1)
			return false;
		
		String cmd = args[0];
		
		Command realCmd = ((CraftServer)Bukkit.getServer()).getCommandMap().getCommand(cmd);
		
		if(realCmd == null)
		{
			sender.sendMessage(ChatColor.RED + "/" + cmd + " is not a registered command.");
			return true;
		}
		
		CommandFinder.displayCommand(realCmd, cmd, sender);
		
		return true;
	}

	@Override
	public List<String> onTabComplete( CommandSender sender, String label, String[] args )
	{
		return null;
	}

}
