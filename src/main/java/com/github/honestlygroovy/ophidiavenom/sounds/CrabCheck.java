package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;

import net.runelite.api.events.ActorDeath;

import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

@Singleton
@Slf4j
public class CrabCheck {
    private static final String GEMSTONE_CRAB_ACTOR_NAME = "Gemstone Crab";

    @Inject
    private Client client;

    @Inject
    private OphidiavenomConfig config;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    public boolean onActorDeath(ActorDeath actorDeath) {
        if (config.crabCheck() && GEMSTONE_CRAB_ACTOR_NAME.equals(actorDeath.getActor().getName())) {
            if (client.getLocalPlayer().getInteracting() == actorDeath.getActor() && GEMSTONE_CRAB_ACTOR_NAME.equals(actorDeath.getActor().getName())) {
                if (config.showChatMessages()) {
                    client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "The Gemstone Crab has moved!", null);
                }
                soundEngine.playClip(Sound.CRAB_CHECK, executor);
                return true;
            }
        }
        return false;
    }
}

