package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.overrides.SoundOverrideAction;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;

@Singleton
@Slf4j
public class GiveBone
{
	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	private static final Pattern STRAY_DOG_GIVEN_BONES_REGEX = Pattern.compile("You give the dog some nice.*bones.*");

	public boolean onChatMessage(ChatMessage chatMessage){
		if (!config.giveBone() || !STRAY_DOG_GIVEN_BONES_REGEX.matcher(chatMessage.getMessage()).matches())
		{
			return false;
		}
		if (config.showChatMessages())
		{
			client.addChatMessage(ChatMessageType.PUBLICCHAT, "Stella", "Who's a good doggy?", null);
		}
		soundEngine.playClip(Sound.EASTER_EGG_STRAYDOG_BONE, SoundOverrideAction.GIVE_BONE, executor);
		return true;
	}
}
