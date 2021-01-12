package com.streakreminder;

import net.runelite.api.coords.WorldArea;
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
	default boolean remindOnce()
	{
		return false;
	}

	@ConfigItem(
			keyName = "ignore10Streak",
			name = "Disable reminder for streaks of 10",
			description = "Disables reminders only for streaks of ten",
			position = 3
	)
	default boolean ignore10Streak()
	{
		return false;
	}
	// stored variables
	@ConfigItem(
			keyName = "RemindStatus",
			name = "",
			description = "",
			hidden = true
	)
	default int status()
	{
		return -1;
	}

	@ConfigItem(
			keyName = "RemindStatus",
			name = "",
			description = ""
	)
	void status(int status);

	@ConfigItem(
			keyName = "streak",
			name = "",
			description = "",
			hidden = true
	)
	default String streak()
	{
		return "";
	}

	@ConfigItem(
			keyName = "streak",
			name = "",
			description = ""
	)
	void streak(String streak);

	@ConfigItem(
			keyName = "slayerarea",
			name = "",
			description = "",
			hidden = true
	)
	default WorldArea slayerarea()
	{
		return null;
	}

	@ConfigItem(
			keyName = "area",
			name = "",
			description = ""
	)
	void slayerarea(WorldArea slayerarea);
}
