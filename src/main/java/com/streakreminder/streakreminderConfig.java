package com.streakreminder;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.*;

@ConfigGroup("streakreminder")
public interface streakreminderConfig extends Config
{
	@ConfigItem(
			keyName = "streak10color",
			name = "10 streak color",
			description = "Change the color of the 10 streak reminder",
			position = 1
	)
	default Color getStreak10Color() { return new Color(255,0,0); }
	@ConfigItem(
			keyName = "streak50color",
			name = "50 streak color",
			description = "Change the color of the 50 streak reminder",
			position = 2
	)
	default Color getStreak50Color() { return new Color(102, 155, 255); }
	@ConfigItem(
			keyName = "streak100color",
			name = "100 streak color",
			description = "Change the color of the 100 streak reminder",
			position = 3
	)
	default Color getStreak100Color() { return new Color(135, 255, 100); }
	@ConfigItem(
			keyName = "streak250color",
			name = "250 streak color",
			description = "Change the color of the 250 streak reminder",
			position = 4
	)
	default Color getStreak250Color() { return new Color(255, 135, 0); }
	@ConfigItem(
			keyName = "streak1000color",
			name = "1000 streak color",
			description = "Change the color of the 1000 streak reminder",
			position = 5
	)
	default Color getStreak1000Color()
	{
		return new Color(255, 90, 178);
	}
}
