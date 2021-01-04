package com.streakreminder;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class streakreminderPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(streakreminderPlugin.class);
		RuneLite.main(args);
	}
}