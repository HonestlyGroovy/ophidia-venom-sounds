package com.github.honestlygroovy.ophidiavenom;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OphidiavenomPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(OphidiavenomPlugin.class);
		RuneLite.main(args);
	}
}