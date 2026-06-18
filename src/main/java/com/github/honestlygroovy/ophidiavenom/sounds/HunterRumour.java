package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.concurrent.ScheduledExecutorService;

import static com.github.honestlygroovy.ophidiavenom.OphidiavenomPlugin.OPHIDIAVENOM;

public class HunterRumour {

    @Inject
    private Client client;

    @Inject
    private OphidiavenomConfig config;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    private static final String HUNTER_RUMOUR_MESSAGE = Text.standardize("You find a rare piece of the creature! You should take it back to the Hunter Guild.");
    private static final String HUNTER_RUMOUR_FULL_INV_MESSAGE = Text.standardize("You find a rare piece of the creature! Though without space in your inventory, it drops to the ground.");
    private static final String HUNTER_RUMOUR_FULL_INV_DISCARDED_MESSAGE = Text.standardize("You have found a rare piece of the creature! You then discard it as you had no inventory space to pick it up.");

    public boolean onChatMessage(ChatMessage chatMessage)
    {
        // Accepting a trade logic, only play sound when message is sent by game
        if (config.hunterRumour() &&
                HUNTER_RUMOUR_MESSAGE.equals(Text.standardize(chatMessage.getMessage())) || HUNTER_RUMOUR_FULL_INV_MESSAGE.equals(Text.standardize(chatMessage.getMessage())))
        {
            soundEngine.playClip(Sound.HUNTER_RUMOUR, executor);
            return true;
        }
        else if (config.hunterRumour() &&
                HUNTER_RUMOUR_FULL_INV_DISCARDED_MESSAGE.equals(Text.standardize(chatMessage.getMessage())))
        {
            if (config.showChatMessages())
            {
                client.addChatMessage(ChatMessageType.PUBLICCHAT, OPHIDIAVENOM, "Hunter Rumour: not completed! You need more inventory space!", null);
                soundEngine.playClip(Sound.HUNTER_RUMOUR_NOT_COMPLETED, executor);
                return true;
            }
        }
        return false;
    }

    public boolean onConfigChanged (ConfigChanged configChanged)
    {
        if (config.hunterRumour() && configChanged.getKey().equals("hunterRumour")) {
            soundEngine.playClip(Sound.HUNTER_RUMOUR, executor);
            return true;
        }
        return false;
    }
}
