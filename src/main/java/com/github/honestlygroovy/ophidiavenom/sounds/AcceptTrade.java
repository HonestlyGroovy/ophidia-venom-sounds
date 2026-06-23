package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class AcceptTrade
{

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		// Accepting a trade logic, only play sound when message is sent by game
		if (config.acceptTrade() &&
			Text.standardize(chatMessage.getMessage()).equals("accepted trade.") &&
				chatMessage.getName().equals(""))
		{
			soundEngine.playClip(Sound.ACCEPTED_TRADE, executor);
			return true;
		}
		return false;
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		log.debug("In AcceptTrade Key: {}, newValue: {}", configChanged.getKey().equals("acceptTrade"), Boolean.valueOf(configChanged.getNewValue()));
		if (config.acceptTrade() && configChanged.getKey().equals("acceptTrade")) {
			log.debug("matches true and acceptTrade");
			soundEngine.playClip(Sound.ACCEPTED_TRADE, executor);
			return true;
		}
		return false;
	}
}
