package com.streakreminder;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
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

	@Inject
	private ChatMessageManager chatMessageManager;

	private String CurrentMessage;
	private String streak10 = "Streak reminder: Your next slayer task will be a streak of 10.";
	private String streak50 = "Streak reminder: Your next slayer task will be a streak of 50.";
	private String streak100 = "Streak reminder: Your next slayer task will be a streak of 100.";
	private String streak250 = "Streak reminder: Your next slayer task will be a streak of 250.";
	private String streak1000 = "Streak reminder: Your next slayer task will be a streak of 1000.";
	private WorldArea nieveArea = new WorldArea(2432,3423, 1, 1, 0);
	private WorldArea DuradelArea = new WorldArea(2869,2978,1,5, 0);
	private WorldArea TuraelArea = new WorldArea(2930, 3535, 2, 2,  0);
	private WorldArea ChaeldarArea = new WorldArea(2442,4429, 4, 5, 0);
	private WorldArea TestArea = new WorldArea(2951, 3320, 3, 3, 0);

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private int RemindStatus;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private String CurrentStreak;

	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	public WorldArea SlayerMasterArea;

	@Provides
	streakreminderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(streakreminderConfig.class);
	}

	//retrieve stored variables
	@Override
	protected void startUp() throws Exception
	{
		//setSlayerMasterArea(config.slayerarea()); for some reason this doesn't work
		setRemindStatus(config.savedstatus());
		setCurrentStreak(config.savedstreak());
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType().equals(ChatMessageType.GAMEMESSAGE))
		{
			CurrentMessage = chatMessage.getMessage();
			if (CurrentMessage.contains("points, giving you a total of"))
			{
				//obtain the number of tasks completed from the string
				String[] tasksCompstr = CurrentMessage.split(" ", 4);
				int TasksCompleted = Integer.parseInt(tasksCompstr[2]);
				int lastDigit = TasksCompleted % 10;
				int last2Digits = TasksCompleted % 100;
				int last3Digits = TasksCompleted % 1000;
				RemindStatus = 1;

				if (last3Digits == 999)
				{
					CurrentStreak = ColorUtil.wrapWithColorTag(streak1000, config.getStreak1000Color());
				}

				else if (last3Digits== 249 || last3Digits == 499 || last3Digits == 749)
				{
					CurrentStreak = ColorUtil.wrapWithColorTag(streak250, config.getStreak250Color());
				}

				else if (last2Digits == 99)
				{
					CurrentStreak = ColorUtil.wrapWithColorTag(streak100, config.getStreak100Color());
				}

				else if (last2Digits == 49)
				{
					CurrentStreak = ColorUtil.wrapWithColorTag(streak50, config.getStreak50Color());
				}

				else if (lastDigit == 9)
				{
					if (config.ignore10Streak())
					{
						RemindStatus = 0;
					}
					else {
						CurrentStreak = ColorUtil.wrapWithColorTag(streak10, config.getStreak10Color());
					}
				}
				sendChatMessage(CurrentStreak);
				save();
			}
		}
	}
	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (RemindStatus == 1)
		{
			WorldPoint CurrentLocation = client.getLocalPlayer().getWorldLocation();
			if (CurrentLocation.distanceTo(nieveArea) <= config.remindDistance())
			{
				sendChatMessage(CurrentStreak);
				SlayerMasterArea = nieveArea;
				RemindStatus = 2;
				save();
			}
			else if (CurrentLocation.distanceTo(DuradelArea) <= config.remindDistance())
			{
				sendChatMessage(CurrentStreak);
				SlayerMasterArea = DuradelArea;
				RemindStatus = 2;
				save();
			}
			else if (CurrentLocation.distanceTo(TuraelArea) <= config.remindDistance())
			{
				sendChatMessage(CurrentStreak);
				SlayerMasterArea = TuraelArea;
				RemindStatus = 2;
				save();
			}
			else if (CurrentLocation.distanceTo(ChaeldarArea) <= config.remindDistance())
			{
				sendChatMessage(CurrentStreak);
				SlayerMasterArea = ChaeldarArea;
				RemindStatus = 2;
				save();
			}
			else if (CurrentLocation.distanceTo(TestArea) <= config.remindDistance())
			{
				sendChatMessage(CurrentStreak);
				SlayerMasterArea = TestArea;
				RemindStatus = 2;
				save();
			}

		}
		// will remind you again when you walk out of range and then back in
		if (RemindStatus == 2)
		{
			WorldPoint CurrentLocation = client.getLocalPlayer().getWorldLocation();
			if (CurrentLocation.distanceTo(SlayerMasterArea) >= config.remindDistance() + 15)
			{
				if (config.remindOnce())
				{
					RemindStatus = 0;
					config.savedstatus(RemindStatus);
				}
				RemindStatus = 1;
				save();
			}

		}
		//resets if you get a new task
		Widget npcDialog = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
		if (npcDialog != null)
		{
			String npcText = npcDialog.getText();
			if (npcText.contains("Your new task is to kill")
					|| npcText.contains("You are to bring balance to")
					|| npcText.contains("You're now assigned to kill")
					|| npcText.contains("You're now assigned to bring balance to")
					|| npcText.contains("You're still meant to be currently assigned"))
			{
				RemindStatus = 0;
				config.savedstatus(RemindStatus);
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (config.ignore10Streak())
		{
			if (CurrentStreak.equals(streak10) && RemindStatus == 1 || RemindStatus == 2)
			{
				CurrentStreak = "";
				RemindStatus = 0;
				config.savedstatus(RemindStatus);
			}
		}
	}

	private void sendChatMessage(String chatMessage)
	{
		final String message = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(Color.RED, chatMessage)
				//.append(chatMessage)
				.build();

		chatMessageManager.queue(
				QueuedMessage.builder()
						.type(ChatMessageType.GAMEMESSAGE)
						.runeLiteFormattedMessage(message)
						.build());
	}
	//saves data
	private void save()
	{
		config.savedstatus(RemindStatus);
		config.savedstreak(CurrentStreak);
		//config.slayerarea(SlayerMasterArea);
	}
}
