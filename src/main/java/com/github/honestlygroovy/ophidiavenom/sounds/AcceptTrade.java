package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
@Slf4j
public class AcceptTrade
{

	@Inject
	private Client client;

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
			chatMessage.getName() == "")
		{
			soundEngine.playClip(Sound.ACCEPTED_TRADE, executor);
			return true;
		}
		return false;
	}
}
