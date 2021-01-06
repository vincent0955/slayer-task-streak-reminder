package com.streakreminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


@Slf4j
@PluginDescriptor(
		name = "Slayer Task Streak Reminder",
		description = "Reminds you with a colored chat message whenever your next task is a point boosted streak",
		tags = {"points", "konar"}
)

public class streakreminderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private streakreminderConfig config;

	private boolean RemindStatus = false;
	private String CurrentStreak = null;
	private String CurrentMessage;
	private String streak10 = "Your next slayer task will be a streak of <col=ff0000>10.";
	private String streak50 = "Your next slayer task will be a streak of <col=ff0000>50.";
	private String streak100 = "Your next slayer task will be a streak of <col=ff0000>100.";
	private String streak250 = "Your next slayer task will be a streak of <col=ff0000>250.";
	private String streak1000 = "Your next slayer task will be a streak of <col=ff0000>1000.";
	private WorldPoint nieveArea = new WorldPoint(2954,3317, 0);
	private WorldPoint DuradelArea = new WorldPoint(0,0, 0); //placeholder tiles
	private WorldPoint TuraelArea = new WorldPoint(0,0, 0);

	@Provides
	streakreminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(streakreminderConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType().equals(ChatMessageType.PUBLICCHAT)) {
			CurrentMessage = chatMessage.getMessage();
			if (CurrentMessage.contains("points, giving you a total of")) {
				//obtain the number of tasks completed from the string
				String[] tasksCompstr = CurrentMessage.split(" ", 4);
				int TasksCompleted = Integer.parseInt(tasksCompstr[2]);
				int lastDigit = TasksCompleted % 10;
				int last2Digits = TasksCompleted % 100;
				int last3Digits = TasksCompleted % 1000;

				if (last3Digits == 999){
					RemindStatus = true;
					CurrentStreak = streak1000;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak1000,"");
				}

				else if (last3Digits==249 || last3Digits == 499 || last3Digits == 749){
					RemindStatus = true;
					CurrentStreak = streak250;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak250,"");
				}

				else if (last2Digits == 99) {
					RemindStatus = true;
					CurrentStreak = streak100;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak100,"");
				}

				else if (last2Digits == 49) {
					RemindStatus = true;
					CurrentStreak = streak50;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak50,"");
				}

				else if (lastDigit == 9) {
					RemindStatus = true;
					CurrentStreak = streak10;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak10,"");
				}
			}
		}
	}
	@Subscribe
	public void onGameTick(GameTick event){
		if (RemindStatus) {
			WorldPoint CurrentLocation = client.getLocalPlayer().getWorldLocation();
			if (CurrentLocation.distanceTo2D(nieveArea) <= config.remindDistance()) {
				client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", CurrentStreak,"");
				RemindStatus = false;
			}
		}
	}
}
