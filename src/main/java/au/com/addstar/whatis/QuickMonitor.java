package au.com.addstar.whatis;

import au.com.addstar.whatis.EventHelper.EventCallback;
import au.com.addstar.whatis.eventhook.CancelHook;
import au.com.addstar.whatis.eventhook.DurationTarget;
import au.com.addstar.whatis.eventhook.HookRunner;
import au.com.addstar.whatis.util.Callback;
import au.com.addstar.whatis.util.filters.FilterSet;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.Map;

public class QuickMonitor {
	public static void checkForCancel(Callback<CancelHook> callback, DurationTarget target, Class<? extends Event>... events) {
		CancelHook hook = new CancelHook();
		for (Class<? extends Event> eventClass : events)
			hook.hook(eventClass);

		new HookRunner<>(target, hook, callback);
	}

	public static void checkForCancel(Callback<CancelHook> callback, DurationTarget target, FilterSet filters, Class<? extends Event>... events) {
		CancelHook hook = new CancelHook(filters);
		for (Class<? extends Event> eventClass : events)
			hook.hook(eventClass);

		new HookRunner<>(target, hook, callback);
	}

	// TODO: Need modify target
	public static void checkForModify(Callback<ModifyReport> callback, DurationTarget target, Class<? extends Event>... events) {

	}

	static void onCancelled(Event event) {

	}

	public static class CancelReport {
		public final Plugin plugin;
		public final Class<? extends Event> event;
		public final String handler;
		public final long time;

		public CancelReport(Class<? extends Event> eventClass, Plugin plugin, String handler) {
			this.plugin = plugin;
			this.handler = handler;
			time = System.currentTimeMillis();
			event = eventClass;
		}

		public CancelReport(Class<? extends Event> eventClass, RegisteredListener listener) {
			plugin = listener.getPlugin();

			StringBuilder location = new StringBuilder();
			for (EventCallback callback : EventHelper.resolveListener(eventClass, listener)) {
				if (location.length() > 0)
					location.append("\n OR \n");
				location.append(String.format("[%s%s] %s", callback.priority, callback.ignoreCancelled ? " Ignores Cancel" : "", callback.signature));
			}

			if (location.length() == 0)
				location = new StringBuilder(String.format("[%s%s] %s", listener.getPriority(), listener.isIgnoringCancelled() ? " Ignores Cancel" : "", listener.getListener().getClass().getName() + ".???"));

			handler = location.toString();
			time = System.currentTimeMillis();
			event = eventClass;
		}
	}

	public static class ModifyReport {
		public final Plugin plugin;
		public final String handler;

		public final Map<String, String> changes;

		public ModifyReport(Plugin plugin, String handler, Map<String, String> changes) {
			this.plugin = plugin;
			this.handler = handler;
			this.changes = changes;
		}
	}
}
