package com.github.honestlygroovy.ophidiavenom;

import java.io.File;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.audio.AudioPlayer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class SoundEngine
{
	@Inject
	private OphidiavenomConfig config;

	@Inject
	private AudioPlayer audioPlayer;

	public void playClip(Sound sound, Executor executor)
	{
		executor.execute(() -> playClipInternal(sound));
	}

	public void playClip(Sound sound, ScheduledExecutorService executor, Duration initialDelay)
	{
		executor.schedule(() -> playClipInternal(sound), initialDelay.toMillis(), TimeUnit.MILLISECONDS);
	}

	public void playFile(File file, Executor executor)
	{
		if (file == null)
		{
			return;
		}
		executor.execute(() -> playFileInternal(file));
	}

	private void playFileInternal(File file)
	{
		if (SoundFileManager.getIsUpdating())
		{
			return;
		}

		float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);
		try
		{
			audioPlayer.play(file, gain);
		}
		catch (Exception exception)
		{
			log.warn("Failed to preview sound {}", file.getName(), exception);
		}
	}

	private void playClipInternal(Sound sound)
	{
		if (SoundFileManager.getIsUpdating())
		{
			return;
		}

		float gain = 20f * (float) Math.log10(config.announcementVolume() / 100f);
		try
		{
			File soundFile = SoundFileManager.getSoundStream(sound);

			if (soundFile == null)
			{
				log.warn("No audio file available for {}", sound.name());
				return;
			}

			audioPlayer.play(soundFile, gain);
		}
		catch (FileNotFoundException e)
		{
			log.warn("Sound file not found for " + sound, e);
		}
		catch (Exception e)
		{
			log.warn("Failed to play Ophidiavenom sound " + sound, e);
		}

	}

	public void close()
	{
		// No cleanup needed for AudioPlayer
	}
}