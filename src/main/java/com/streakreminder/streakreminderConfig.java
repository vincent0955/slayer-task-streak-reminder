package com.streakreminder;

import net.runelite.api.coords.WorldArea;
import net.runelite.client.config.*;
import java.awt.Color;

@ConfigGroup("streakreminder")
public interface streakreminderConfig extends Config
{
	@ConfigSection(
			name = "Text Color Settings",
			description = "Change the color of the text notifications",
			position = 4
	)
	String ColorSettings = "Color Settings";

	@ConfigItem(
			keyName = "remindDistance",
			name = "Radius from slayer master",
			description = "Choose the distance to the slayer master before it reminds you",
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
			name = "Disable for 10 streak",
			description = "Disables reminders only for streaks of ten",
			position = 3
	)
	default boolean ignore10Streak()
	{
		return false;
	}

	//Color settings

	@ConfigItem(
			keyName = "streak10color",
			name = "10 streak color",
			description = "Change the color of the 10 streak reminder",
			position = 4,
			section = ColorSettings
	)
	default Color getStreak10Color()
	{
		return new Color(0, 200, 30);
	}

	@ConfigItem(
			keyName = "streak50color",
			name = "50 streak color",
			description = "Change the color of the 50 streak reminder",
			position = 5,
			section = ColorSettings
	)
	default Color getStreak50Color()
	{
		return new Color(255, 90, 178);
	}
	@ConfigItem(
			keyName = "streak100color",
			name = "100 streak color",
			description = "Change the color of the 100 streak reminder",
			position = 6,
			section = ColorSettings
	)
	default Color getStreak100Color()
	{
		return new Color(255, 90, 178);
	}
	@ConfigItem(
			keyName = "streak250color",
			name = "250 streak color",
			description = "Change the color of the 250 streak reminder",
			position = 7,
			section = ColorSettings
	)
	default Color getStreak250Color()
	{
		return new Color(255, 90, 178);
	}
	@ConfigItem(
			keyName = "streak1000color",
			name = "1000 streak color",
			description = "Change the color of the 1000 streak reminder",
			position = 8,
			section = ColorSettings
	)
	default Color getStreak1000Color()
	{
		return new Color(255, 90, 178);
	}

	// stored variables
	@ConfigItem(
			keyName = "RemindStatus",
			name = "",
			description = "",
			hidden = true
	)
	default int savedstatus()
	{
		return -1;
	}

	@ConfigItem(
			keyName = "RemindStatus",
			name = "",
			description = ""
	)
	void savedstatus(int status);

	@ConfigItem(
			keyName = "streak",
			name = "",
			description = "",
			hidden = true
	)
	default String savedstreak()
	{
		return "";
	}

	@ConfigItem(
			keyName = "streak",
			name = "",
			description = ""
	)
	void savedstreak(String streak);

//	@ConfigItem(
//			keyName = "slayerarea",
//			name = "",
//			description = "",
//			hidden = true
//	)
//	default WorldArea slayerarea()
//	{
//		return null;
//	}
//
//	@ConfigItem(
//			keyName = "slayerarea",
//			name = "",
//			description = ""
//	)
//	void slayerarea(WorldArea slayerarea);
}
