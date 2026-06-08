package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ActorDeath;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

@Singleton
@Slf4j
public class Death
{

	@Inject
	private Client client;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;


	public boolean onActorDeath(ActorDeath actorDeath)
	{
		if (config.announceDeath() && actorDeath.getActor() == client.getLocalPlayer())
		{
			if (config.showChatMessages())
			{
				client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "It must be a glitch?!?!", null);
			}
			soundEngine.playClip(Sound.DEATH, executor);
			return true;
		}
		return false;
	}
}
