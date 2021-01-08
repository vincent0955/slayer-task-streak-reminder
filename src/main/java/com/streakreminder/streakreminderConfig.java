package com.streakreminder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("streakreminder")
public interface streakreminderConfig extends Config
{
	@ConfigItem(
			keyName = "remindDistance",
			name = "Radius from slayer master",
			description = "Choose the distance to the slayer master before it reminds you again",
			position = 1
	)
	default int remindDistance()
	{
		return 10;
	}

	@ConfigItem(
			keyName = "remindOnce",
			name = "Only remind the first time in range of slayer master",
			description = "Only reminds you the first time you walk into range of the slayer master",
			position = 2
	)
	default boolean remindOnce() { return false; }

	@ConfigItem(
			keyName = "disable10Streak",
			name = "Disable reminder for streaks of 10",
			description = "Disables reminders only for streaks of ten",
			position = 3
	)
	default boolean disable10Streak() { return false; }
}
