package au.com.addstar.whatis.eventhook;

import au.com.addstar.whatis.QuickMonitor.CancelReport;
import au.com.addstar.whatis.util.filters.FilterSet;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.plugin.RegisteredListener;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;

public class CancelHook extends EventHookSession {
	private final IdentityHashMap<Event, CancelReport> mCancelled;
	private FilterSet mFilters;

	public CancelHook() {
		mCancelled = new IdentityHashMap<>();
	}

	public CancelHook(FilterSet filters) {
		mCancelled = new IdentityHashMap<>();
		mFilters = filters;
	}

	@Override
	public synchronized void recordStep(Event event, RegisteredListener listener, boolean initiallyCancelled) {
		if (!shouldInclude(event) || !matchesHandlerFilters(event, listener))
			return;

		synchronized (mCancelled) {
			boolean cancelled = (event instanceof Cancellable ? ((Cancellable) event).isCancelled() : false);

			if (!initiallyCancelled && cancelled)
				mCancelled.put(event, new CancelReport(event.getClass(), listener));
			else if (initiallyCancelled && !cancelled)
				mCancelled.remove(event);
		}
		super.recordStep(event, listener, initiallyCancelled);
	}

	public boolean matchesHandlerFilters(Event event, RegisteredListener listener) {
		if (mFilters == null)
			return true;

		return (mFilters.matchesHandler(listener) && mFilters.matches(event));
	}

	public Collection<CancelReport> getReports() {
		synchronized (mCancelled) {
			return Collections.unmodifiableCollection(mCancelled.values());
		}
	}
}
