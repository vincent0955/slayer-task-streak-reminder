package com.streakreminder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("streakreminder")
public interface streakreminderConfig extends Config
{
	@ConfigItem(
			keyName = "remindDistance",
			name = "Distance to slayer master",
			description = "Choose the distance to Nieve/Duradel/Turael before it reminds you again",
			position = 1
	)
	default int remindDistance()
	{
		return 5;
	}
}
