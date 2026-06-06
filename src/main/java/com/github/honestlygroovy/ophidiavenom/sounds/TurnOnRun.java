package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.*;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.VarbitChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

@Singleton
@Slf4j
public class TurnOnRun extends TimedSoundBase
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private boolean isRunning = false;

	private final String message = "FAST! I said FAST!";
	private final String runOption = "Toggle Run";

	TurnOnRun()
	{
		super(10);
	}

	public void onVarbitChanged(VarbitChanged varbitChanged)
	{
		// Running varbit
		if (varbitChanged.getVarbitId() == OphidiavenomVarbits.RUNNING.Id && varbitChanged.getVarpId() == OphidiavenomVarbits.RUNNING.VarpId)
		{
			isRunning = varbitChanged.getValue() == OphidiavenomVarbitValues.RUNNING_TRUE.Value;
		}
	}

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();

		// Turning on run
		if (config.turnOnRun() && option.equals(runOption))
		{
			int currentTick = client.getTickCount();
			if (!isRunning && canPlaySound(currentTick))
			{
				if (config.showChatMessages())
				{
					client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, message, null);
				}
				soundEngine.playClip(Sound.TURNING_ON_RUN, executor);
				setLastPlayedTickTick(currentTick);
			}
		}
	}
}
