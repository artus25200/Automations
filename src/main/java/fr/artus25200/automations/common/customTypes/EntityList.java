/*
 * Copyright (c) 2022. $name
 */

package fr.artus25200.automations.common.customTypes;

import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntityList {
	public Entity[] entities;

	public EntityList(Entity ... entities){
		this.entities = entities;
	}
}
