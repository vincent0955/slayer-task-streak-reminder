package com.streakreminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

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

	private int RemindStatus = 0; // can be 0, 1, or 2
	private String CurrentStreak;
	private String CurrentMessage;
	private String streak10 = "<col=ff0000>Your next slayer task will be a streak of 10.";
	private String streak50 = "<col=ff0000>Your next slayer task will be a streak of 50.";
	private String streak100 = "<col=ff0000>Your next slayer task will be a streak of 100.";
	private String streak250 = "<col=ff0000>Your next slayer task will be a streak of 250.";
	private String streak1000 = "<col=ff0000>Your next slayer task will be a streak of 1000.";
	private WorldArea CurrentArea;
	private WorldPoint CurrentLocation;
	private WorldPoint nieveArea = new WorldPoint(2432,3423, 0);
	private WorldArea DuradelArea = new WorldArea(2869,2978,2870,2983, 0);
	private WorldArea TuraelArea = new WorldArea(2930, 3535, 2932, 3537,  0);
	private WorldArea ChaeldarArea = new WorldArea(2442,4429, 2446, 4434, 0);
	private WorldPoint KrystiliaArea = new WorldPoint(3109,3516, 0);

	@Provides
	streakreminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(streakreminderConfig.class);
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage) {
		if (chatMessage.getType().equals(ChatMessageType.GAMEMESSAGE)) { //remember to change back to gamemessage
			CurrentMessage = chatMessage.getMessage();
			if (CurrentMessage.contains("points, giving you a total of")) {
				//obtain the number of tasks completed from the string
				String[] tasksCompstr = CurrentMessage.split(" ", 4);
				int TasksCompleted = Integer.parseInt(tasksCompstr[2]);
				int lastDigit = TasksCompleted % 10;
				int last2Digits = TasksCompleted % 100;
				int last3Digits = TasksCompleted % 1000;

				if (last3Digits == 999){
					RemindStatus = 1;
					CurrentStreak = streak1000;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak1000,"");
				}

				else if (last3Digits== 249 || last3Digits == 499 || last3Digits == 749){
					RemindStatus = 1;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak250,"");
					CurrentStreak = streak250;
				}

				else if (last2Digits == 99) {
					RemindStatus = 1;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak100,"");
					CurrentStreak = streak100;
				}

				else if (last2Digits == 49) {
					RemindStatus = 1;
					client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak50,"");
					CurrentStreak = streak50;
				}

				else if (lastDigit == 9) {
					if (config.disable10Streak()) {
						RemindStatus = 0;
					}
					else {
						RemindStatus = 1;
						client.addChatMessage(ChatMessageType.GAMEMESSAGE,"",streak10,"");
						CurrentStreak = streak10;
					}
				}
			}
		}
	}
	@Subscribe
	public void onGameTick(GameTick event){
		if (RemindStatus == 1) {
			WorldPoint CurrentLocation = client.getLocalPlayer().getWorldLocation();
			WorldArea CurrentArea = client.getLocalPlayer().getWorldArea();
			if (CurrentLocation.distanceTo2D(nieveArea) <= config.remindDistance()
					|| CurrentLocation.distanceTo2D(KrystiliaArea) <= config.remindDistance()
					|| CurrentArea.distanceTo2D(DuradelArea) <= config.remindDistance()
					|| CurrentArea.distanceTo2D(TuraelArea) <= config.remindDistance()
					|| CurrentArea.distanceTo2D(ChaeldarArea) <= config.remindDistance()) {
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", CurrentStreak, "");
				RemindStatus = 2;
			}
		}
		// will remind you again when you walk out of range and then back in
		if (RemindStatus == 2) {
			WorldPoint CurrentLocation = client.getLocalPlayer().getWorldLocation();
			WorldArea CurrentArea = client.getLocalPlayer().getWorldArea();
			if (CurrentLocation.distanceTo2D(nieveArea) >= config.remindDistance() + 7
					|| CurrentLocation.distanceTo2D(KrystiliaArea) >= config.remindDistance() + 7
					|| CurrentArea.distanceTo2D(DuradelArea) >= config.remindDistance() + 7
					|| CurrentArea.distanceTo2D(TuraelArea) >= config.remindDistance() + 7
					|| CurrentArea.distanceTo2D(ChaeldarArea) >= config.remindDistance() + 7) {
				if (config.remindOnce()){
					RemindStatus = 0;
				}
				RemindStatus = 1;
			}
		}
		//resets if you get a new task
		Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
		if (npcDialog != null) {
			String npcText = npcDialog.getText();
			if (npcText.contains("Your new task is to kill")
					|| npcText.contains("You are to bring balance to")
					|| npcText.contains("You're now assigned to kill")
					|| npcText.contains("You're now assigned to bring balance to")
					|| npcText.contains("You're still meant to be currently assigned")) {
				RemindStatus = 0;
			}
		}
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged event){
		if (config.disable10Streak()){
			if (CurrentStreak.equals(streak10) && RemindStatus == 1 || RemindStatus == 2){
				CurrentStreak = null;
				RemindStatus = 0;
			}
		}
	}
}
