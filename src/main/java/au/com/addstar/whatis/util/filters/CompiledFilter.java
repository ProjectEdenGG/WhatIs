package au.com.addstar.whatis.util.filters;

import static au.com.addstar.whatis.util.filters.FilterOp.Contains;
import static au.com.addstar.whatis.util.filters.FilterOp.Equals;
import static au.com.addstar.whatis.util.filters.FilterOp.GreaterThan;
import static au.com.addstar.whatis.util.filters.FilterOp.GreaterThanEqual;
import static au.com.addstar.whatis.util.filters.FilterOp.LessThan;
import static au.com.addstar.whatis.util.filters.FilterOp.LessThanEqual;
import static au.com.addstar.whatis.util.filters.FilterOp.NotContains;
import static au.com.addstar.whatis.util.filters.FilterOp.NotEquals;

public class CompiledFilter {
	private final ExecutionPath mHandle;
	private final FilterOp mOperation;

	private final Object mValue;

	public CompiledFilter(ExecutionPath path, FilterOp op, Object value) {
		mHandle = path;
		mOperation = op;
		mValue = value;
	}

	@SuppressWarnings("unchecked")
	public boolean matches(Object instance) {
		Object current = mHandle.invoke(instance);

		if (mValue instanceof String && !(current instanceof String))
			current = Stringifier.asString(current);

		if (mOperation == Equals || mOperation == NotEquals) {
			boolean matches = false;
			if (current == null)
				matches = (mValue == null);
			else
				matches = current.equals(mValue);

			return (mOperation == Equals ? matches : !matches);
		} else if (mOperation == Contains || mOperation == NotContains) {
			String sCur = (String) current;
			String sVal = (String) mValue;

			boolean matches = sCur.toLowerCase().contains(sVal.toLowerCase());

			return (mOperation == Contains ? matches : !matches);
		} else if (mOperation == LessThan || mOperation == GreaterThanEqual) {
			boolean matches = false;
			if (!(current instanceof Comparable<?>) || current.getClass() != mValue.getClass())
				return false;
			else
				matches = ((Comparable<Object>) current).compareTo(mValue) < 0;

			return (mOperation == LessThan ? matches : !matches);
		} else if (mOperation == GreaterThan || mOperation == LessThanEqual) {
			boolean matches = false;
			if (!(current instanceof Comparable<?>) || current.getClass() != mValue.getClass())
				return false;
			else
				matches = ((Comparable<Object>) current).compareTo(mValue) > 0;

			return (mOperation == GreaterThan ? matches : !matches);
		} else
			throw new AssertionError("Missing operation type " + mOperation);
	}
}
