package com.github.honestlygroovy.ophidiavenom.sounds;

import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.SoundEngine;
import com.github.honestlygroovy.ophidiavenom.SoundIds;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.AreaSoundEffectPlayed;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;

@Singleton
@Slf4j
public class YamaSwaps
{

    @Inject
    private Client client;

    @Inject
    private OphidiavenomConfig config;

    @Inject
    private SoundEngine soundEngine;

    @Inject
    private ScheduledExecutorService executor;

    public boolean onAreaSoundEffectPlayed(AreaSoundEffectPlayed event)
    {
        int soundId = event.getSoundId();

        if (config.yamaSwaps())
        {
            if (soundId == SoundIds.YAMA_RANGE.Id)
            {
                event.consume();
                soundEngine.playClip(Sound.RANGE, executor);
                return true;
            }
            else if (soundId == SoundIds.YAMA_MAGE.Id)
            {
                event.consume();
                soundEngine.playClip(Sound.MAGE, executor);
                return true;
            }
        }
        return false;
    }

    public boolean onConfigChanged (ConfigChanged configChanged)
    {
        if (config.yamaSwaps() && configChanged.getKey().equals("yamaSwaps")) {
            soundEngine.playClip(Sound.RANGE, executor);
            return true;
        }
        else if (!config.yamaSwaps() && configChanged.getKey().equals("yamaSwaps")) {
            soundEngine.playClip(Sound.MAGE, executor);
            return true;
        }
        return false;
    }
}