package com.booksaw.betterTeams.integrations.hologram;

import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

public class DHHologramManager extends HologramManager {
	/*
	 * Creates a new DecentHolograms hologram via reflection (no compile-time dep).
	 */
	@Override
	public LocalHologram createLocalHolo(Location location, HologramType type) {
		try {
			Class<?> dhapi = Class.forName("eu.decentsoftware.holograms.api.DHAPI");
			Method createHologram = dhapi.getMethod("createHologram", String.class, Location.class);
			Object hologram = createHologram.invoke(null, UUID.randomUUID().toString(), location);
			return new DHHologramImpl(hologram);
		} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			// If DecentHolograms API is unavailable, create a no-op hologram wrapper.
			return new DHHologramImpl(null);
		}
	}

	/*
	 * A wrapper class for interfacing with DecentHolograms holograms via reflection.
	 */
	private static final class DHHologramImpl implements LocalHologram {
		private final Object hologram; // DecentHolograms Hologram instance, or null if unavailable

		public DHHologramImpl(Object hologram) {
			this.hologram = hologram;
		}

		@Override
		public void appendText(String text) {
			if (hologram == null) return;
			try {
				Class<?> dhapi = Class.forName("eu.decentsoftware.holograms.api.DHAPI");
				Method addLine = dhapi.getMethod("addHologramLine", hologram.getClass(), String.class);
				addLine.invoke(null, hologram, text);
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
			}
		}

		@Override
		public void clearLines() {
			if (hologram == null) return;
			try {
				Class<?> dhapi = Class.forName("eu.decentsoftware.holograms.api.DHAPI");
				Method setLines = dhapi.getMethod("setHologramLines", hologram.getClass(), java.util.List.class);
				setLines.invoke(null, hologram, Collections.emptyList());
			} catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
			}
		}

		@Override
		public void delete() {
			if (hologram == null) return;
			try {
				Method delete = hologram.getClass().getMethod("delete");
				delete.invoke(hologram);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
			}
		}

		@Override
		public Location getLocation() {
			if (hologram == null) return new Location(null, 0, 0, 0);
			try {
				Method getLocation = hologram.getClass().getMethod("getLocation");
				return (Location) getLocation.invoke(hologram);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				return new Location(null, 0, 0, 0);
			}
		}
	}
}
