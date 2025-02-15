package au.com.addstar.whatis.util;

import org.apache.commons.lang.Validate;
import org.bukkit.World;

import java.util.HashMap;

public class ChunkCoord {
	public final int x;
	public final int z;

	public final World world;

	public ChunkCoord(int x, int z, World world) {
		this.x = x;
		this.z = z;
		this.world = world;
	}

	@Override
	public int hashCode() {
		return (x | z << 16) ^ world.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChunkCoord))
			return false;

		return x == ((ChunkCoord) obj).x && z == ((ChunkCoord) obj).z && world.equals(((ChunkCoord) obj).world);
	}

	private static final HashMap<Long, ChunkCoord> mCache = new HashMap<>();

	public static void clearCache() {
		mCache.clear();
	}

	/**
	 * Gets chunk coords for the location or reuses ones created before.
	 * WARNING: This is not world safe. This is intended to be used 1 world at a time then cleared
	 */
	public static ChunkCoord getChunkCoord(int x, int z, World world) {
		long hash = ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
		ChunkCoord coord = mCache.get(hash);
		if (coord == null) {
			coord = new ChunkCoord(x, z, world);
			mCache.put(hash, coord);
		}

		Validate.isTrue(coord.x == x && coord.z == z, String.format("Bad Lookup! hash: %s (%d,%d) (%d,%d)", hash, x, z, coord.x, coord.z));

		return coord;
	}
}
