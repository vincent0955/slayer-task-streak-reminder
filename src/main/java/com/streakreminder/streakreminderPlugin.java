package com.streakreminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.awt.Color;

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
	private String CurrentMessage;
	private int TasksCompleted;
	private String streak10 = "Your next slayer task will be a streak of 10";
	private String streak50 = "Your next slayer task will be a streak of 50";
	private String streak100 = "Your next slayer task will be a streak of 100";
	private String streak250 = "Your next slayer task will be a streak of 250";
	private String streak1000 = "Your next slayer task will be a streak of 1000";
	private String hex10 = "<col="+Integer.toHexString(config.getStreak10Color().getRGB()).substring(2)+">";
	private String hex50 = "<col="+Integer.toHexString(config.getStreak50Color().getRGB()).substring(2)+">";
	private String hex100 = "<col="+Integer.toHexString(config.getStreak100Color().getRGB()).substring(2)+">";
	private String hex250 = "<col="+Integer.toHexString(config.getStreak250Color().getRGB()).substring(2)+">";
	private String hex1000 = "<col="+Integer.toHexString(config.getStreak1000Color().getRGB()).substring(2)+">";
	//private String[] tasksCompstr = new String[] {};
	// reference: "You've completed 9 tasks and received 15 points, giving you a total of 120; return to a Slayer master."

	@Provides
	streakreminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(streakreminderConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() == ChatMessageType.PUBLICCHAT) {
			CurrentMessage = chatMessage.getMessage();
			if (CurrentMessage.contains("points, giving you a total of")) {

				//obtain the number of tasks completed from the string
				String[] tasksCompstr = CurrentMessage.split(" ", 3);
				int TasksCompleted = Integer.parseInt(tasksCompstr[2]);
				int lastDigit = TasksCompleted % 10;
				int last2Digits = TasksCompleted % 100;
				int last3Digits = TasksCompleted % 1000;

				if (last3Digits == 999){
					RemindStatus = true;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak1000 + hex1000, "");
				}

				else if (last3Digits == 249 || last3Digits == 499 || last3Digits == 749){
					RemindStatus = true;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak250 + hex250, "");
				}

				else if (last2Digits == 99) {
					RemindStatus = true;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak100 + hex100, "");
				}

				else if (last2Digits == 49) {
					RemindStatus = true;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak50 + hex50, "");
				}

				else if (lastDigit == 9) {
					RemindStatus = true;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"", streak10 + hex10, "");
				}
			}
		}

	}
//	@Override
//	protected void startUp() throws Exception
//	{
//		log.info("Example started!");
//	}
//
//	@Override
//	protected void shutDown() throws Exception
//	{
//		log.info("Example stopped!");
//	}

}
