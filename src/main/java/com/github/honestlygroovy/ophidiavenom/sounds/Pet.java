package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

@Singleton
@Slf4j
public class Pet
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private int receivedPetTick = -1;
	private final String normalPetMessage = "You have a funny feeling like you're being followed.";
	private final String backpackPetMessage = "You feel something weird sneaking into your backpack.";
	private final String wouldHavePetMessage = "You have a funny feeling like you would have been followed...";

	public boolean onChatMessage(ChatMessage chatMessage)
	{
		if (!config.receivedPet())
		{
			return false;
		}
		String message = Text.standardize(chatMessage.getMessage());
		// Make sure it's one of the pet messages and not sent by a user to troll
		if ((message.equals(normalPetMessage) || message.equals(backpackPetMessage) || message.equals(wouldHavePetMessage)) &&
			chatMessage.getType() == ChatMessageType.GAMEMESSAGE)
		{
			receivedPetTick = client.getTickCount();
			soundEngine.playClip(Sound.NEW_PET, executor);
			return true;
		}

		return false;
	}

	public int getReceivedPetTick()
	{
		return receivedPetTick;
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		if (config.receivedPet() && configChanged.getKey().equals("receivedPet")) {
			soundEngine.playClip(Sound.NEW_PET, executor);
			return true;
		}
		return false;
	}
}
