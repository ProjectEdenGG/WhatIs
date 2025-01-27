package au.com.addstar.whatis.entities;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

public enum EntityCategory {
	NPC(Villager.class),
	Player(org.bukkit.entity.Player.class),
	Item(org.bukkit.entity.Item.class),
	Hanging(org.bukkit.entity.Hanging.class),
	Mob(org.bukkit.entity.Monster.class),
	Animal(org.bukkit.entity.Creature.class),
	Vehicle(org.bukkit.entity.Vehicle.class),
	Other(null)
	;

	private final Class<? extends Entity> entityClass;

	EntityCategory(Class<? extends Entity> entityClass) {
		this.entityClass = entityClass;
	}

	public static EntityCategory from(EntityType type) {
		for (EntityCategory category : values())
			if (category.entityClass != null && type.getEntityClass() != null)
				if (category.entityClass.isAssignableFrom(type.getEntityClass()))
					return category;

		return EntityCategory.Other;
	}
}
