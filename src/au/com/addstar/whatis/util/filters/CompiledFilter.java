package au.com.addstar.whatis.util.filters;

import static au.com.addstar.whatis.util.filters.FilterOp.*;

public class CompiledFilter
{
	private ExecutionPath mHandle;
	private FilterOp mOperation;
	
	private Object mValue;
	
	public CompiledFilter(ExecutionPath path, FilterOp op, Object value)
	{
		mHandle = path;
		mOperation = op;
		mValue = value;
	}
	
	public boolean matches(Object instance)
	{
		Object current = mHandle.invoke(instance);
		
		if(mValue instanceof String && !(current instanceof String))
			current = Stringifier.asString(current);
		
		if(mOperation == Equals || mOperation == NotEquals)
		{
			boolean matches = false;
			if(current == null)
				matches = (mValue == null);
			else
				matches = current.equals(mValue);
			
			return (mOperation == Equals ? matches : !matches);
		}
		else if(mOperation == Contains || mOperation == NotContains)
		{
			String sCur = (String)current;
			String sVal = (String)mValue;
			
			boolean matches = sCur.toLowerCase().contains(sVal.toLowerCase());
			
			return (mOperation == Contains ? matches : !matches);
		}
		else
			throw new AssertionError("Missing operation type " + mOperation);
	}
}
