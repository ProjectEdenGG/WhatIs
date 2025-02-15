package au.com.addstar.whatis.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DependencyCommand implements ICommand {

	@Override
	public String getName() {
		return "dependency";
	}

	@Override
	public String[] getAliases() {
		return new String[]{"depends", "dependencies"};
	}

	@Override
	public String getPermission() {
		return "whatis.dependencies";
	}

	@Override
	public String getUsageString(String label, CommandSender sender) {
		return label + " (for|of) <plugin>";
	}

	@Override
	public String getDescription() {
		return "Shows the either what dependencies <plugin> has, or what plugins depend on <plugin>";
	}

	@Override
	public boolean canBeConsole() {
		return true;
	}

	@Override
	public boolean canBeCommandBlock() {
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		if (args.length != 2)
			return false;

		Plugin plugin = Bukkit.getPluginManager().getPlugin(args[1]);
		if (plugin == null) {
			sender.sendMessage(ChatColor.RED + "Cannot find plugin " + args[1]);
			return true;
		}

		boolean lower;

		if (args[0].equalsIgnoreCase("for"))
			lower = true;
		else if (args[0].equalsIgnoreCase("of"))
			lower = false;
		else {
			sender.sendMessage(ChatColor.RED + "Expected 'for' or 'of'");
			return true;
		}

		if (lower) {
			// Show dependencies of plugin

			sender.sendMessage(ChatColor.YELLOW + "Dependencies for " + plugin.getName() + ":");

			StringBuilder depString = new StringBuilder();
			for (String depend : safeList(plugin.getDescription().getDepend())) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Required: " + ChatColor.WHITE + depString);

			depString = new StringBuilder();
			for (String depend : safeList(plugin.getDescription().getSoftDepend())) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Optional: " + ChatColor.WHITE + depString);

			depString = new StringBuilder();
			for (String depend : safeList(plugin.getDescription().getLoadBefore())) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Loading Before: " + ChatColor.WHITE + depString);
		} else {
			// Show plugins that have plugin as a dependency

			sender.sendMessage(ChatColor.YELLOW + "Plugins dependent on " + plugin.getName() + ":");

			Collection<String> requiredBy = new ArrayList<>();
			Collection<String> optionalBy = new ArrayList<>();
			Collection<String> loadBefore = new ArrayList<>();

			for (Plugin other : Bukkit.getPluginManager().getPlugins()) {
				for (String depend : safeList(other.getDescription().getDepend())) {
					if (depend.equals(plugin.getName())) {
						requiredBy.add(other.getName());
						break;
					}
				}

				for (String depend : safeList(other.getDescription().getSoftDepend())) {
					if (depend.equals(plugin.getName())) {
						optionalBy.add(other.getName());
						break;
					}
				}

				for (String depend : safeList(other.getDescription().getLoadBefore())) {
					if (depend.equals(plugin.getName())) {
						loadBefore.add(other.getName());
						break;
					}
				}
			}

			StringBuilder depString = new StringBuilder();
			for (String depend : requiredBy) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Required By: " + ChatColor.WHITE + depString);

			depString = new StringBuilder();
			for (String depend : optionalBy) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Used By: " + ChatColor.WHITE + depString);

			depString = new StringBuilder();
			for (String depend : loadBefore) {
				if (depString.length() > 0)
					depString.append(", ");
				depString.append(depend);
			}

			if (depString.length() == 0)
				depString = new StringBuilder("*None*");

			sender.sendMessage(ChatColor.GOLD + "Loading After: " + ChatColor.WHITE + depString);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String label, String[] args) {
		if (args.length == 2) {
			List<String> matching = new ArrayList<>();
			String toMatch = args[1].toLowerCase();
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				if (plugin.getName().toLowerCase().startsWith(toMatch.toLowerCase()))
					matching.add(plugin.getName());
			}

			return matching;
		}

		return null;
	}

	private List<String> safeList(List<String> list) {
		if (list == null)
			return Collections.emptyList();
		return list;
	}
}
