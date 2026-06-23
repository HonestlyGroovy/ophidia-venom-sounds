package com.github.honestlygroovy.ophidiavenom.sounds;

// Special thanks to: https://github.com/while-loop/runelite-plugins/tree/runewatch

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class DeclineTrade
{

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final int PLAYER_TRADE_OFFER_GROUP_ID = 335;
	private static final int PLAYER_TRADE_CONFIRMATION_GROUP_ID = 334;
	private static final String DECLINE_MSG = "Decline";
	private static final List<Integer> TRADE_SCREEN_GROUP_IDS = Arrays.asList(
		PLAYER_TRADE_OFFER_GROUP_ID,
		PLAYER_TRADE_CONFIRMATION_GROUP_ID
	);

	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		int groupId = OphidiavenomPlugin.TO_GROUP(menuOptionClicked.getParam1());
		String option = menuOptionClicked.getMenuOption();
		MenuAction action = menuOptionClicked.getMenuAction();

		if (config.declineTrade() && TRADE_SCREEN_GROUP_IDS.contains(groupId))
		{

			// Decline trade
			if (option.equals(DECLINE_MSG))
			{
				soundEngine.playClip(Sound.DECLINE_TRADE, executor);
			}
		}
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		if (config.declineTrade() && configChanged.getKey().equals("declineTrade")) {
			soundEngine.playClip(Sound.DECLINE_TRADE, executor);
			return true;
		}
		return false;
	}
}
