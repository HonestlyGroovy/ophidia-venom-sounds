package com.github.honestlygroovy.ophidiavenom.livestreams;

import com.github.honestlygroovy.ophidiavenom.ChatRightClickManager;
import com.github.honestlygroovy.ophidiavenom.OphidiavenomConfig;
import com.github.honestlygroovy.ophidiavenom.RightClickAction;
import com.github.honestlygroovy.ophidiavenom.Sound;
import com.github.honestlygroovy.ophidiavenom.sounds.LivestreamLiveSound;
import com.google.gson.Gson;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.util.Text;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class LivestreamManager
{
	private static final Duration LIVE_SOUND_MAX_AGE = Duration.ofMinutes(180);

	private Livestream livestream = null;
	private int lastChecked = -1;
	private int lastSentMessage = -1;
	private boolean offlineAnnouncementSent = false;
	private boolean suppressLiveTransitionSound = false;

	@Inject
	private Client client;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private Gson gson;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private OphidiavenomConfig config;

	@Inject
	private ChatRightClickManager chatRightClickManager;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private LivestreamLiveSound livestreamLiveSound;

	public void onGameTick(GameTick gameTick)
	{
		if (!config.livestream())
		{
			return;
		}

		handleTickReset();
		sendLiveLivestreamMessage(false);

		int currentTick = client.getTickCount();
		if (lastChecked == -1 || currentTick < lastChecked || currentTick - lastChecked > 100)
		{
			executor.submit(() -> {
				sendRequest(currentTick);
			});

			lastChecked = currentTick;
		}
	}

	public void resetStateForWorldHopOrLogin()
	{
		// Reset poll/message timing so we immediately refresh after state transitions.
		// Keep the last livestream snapshot so hop/login doesn't count as a live transition.
		lastChecked = -1;
		lastSentMessage = -1;
		//suppressLiveTransitionSound = true;
	}
	public static long extract(String text, String regex)
	{

		Matcher matcher = Pattern.compile(regex).matcher(text);
		if (matcher.find())
		{
			return Long.parseLong(matcher.group(1));
		}
		return 0;
	}

	public static Duration parseUptimeToDuration(String uptime)
	{
		String text = uptime.toLowerCase();

		long hours = extract(text, "(\\d+)\\s*hour");
		long minutes = extract(text, "(\\d+)\\s*minute");
		long seconds = extract(text, "(\\d+)\\s*second");

		if (hours == 0 && minutes == 0 && seconds == 0)
		{
			return null;
		}

		return Duration.ofHours(hours)
				.plusMinutes(minutes)
				.plusSeconds(seconds);
	}

	private void sendRequest(final int currentTick)
	{

		String title = "";
		String uptime = "";
		Boolean live = false;

		Request request = new Request.Builder().url("https://decapi.me/twitch/status/ophidiavenom").build();

		try (Response response = okHttpClient.newCall(request).execute()) {
			if (!response.isSuccessful() || response.body() == null) {
				return;
			}
			else {
				title = response.body().string();

			}
			response.close();
		}
		catch(IOException e)
			{

				return;
			}
        catch(Exception e)
			{

				return;
			}

		Request request2 = new Request.Builder().url("https://decapi.me/twitch/uptime/ophidiavenom").build();

		try (Response response2 = okHttpClient.newCall(request2).execute()) {
			if (!response2.isSuccessful() || response2.body() == null) {

				return;

			}
			else {
				String val = response2.body().string();
				if (val.contains("offline")) {

					live = false;
				} else {

					live = true;

					Duration duration = parseUptimeToDuration(val);

					if (duration == null) {
						return;
					}

					uptime = Instant.now().minus(duration).truncatedTo(ChronoUnit.MINUTES).toString();

					}

				}
			response2.close();
			}

		catch(IOException e)
		{
			return;
		}
		catch(Exception e)
		{
			return;
		}

		StringBuilder jsonResponse = new StringBuilder("{");
		jsonResponse.append("\n");
		jsonResponse.append("\"live\": ");
		jsonResponse.append(live.toString());
		jsonResponse.append(",");
		jsonResponse.append("\n");
		jsonResponse.append("\"title\": ");
		jsonResponse.append("\"").append(title).append("\"");
		jsonResponse.append(",");
		jsonResponse.append("\n");
		jsonResponse.append("\"wentLiveAt\": ");
		jsonResponse.append("\"").append(uptime).append("\"");
		jsonResponse.append("\n");
		jsonResponse.append("}");

		Livestream newLivestream = gson.fromJson(jsonResponse.toString(), Livestream.class);

		if (newLivestream == null)
		{
			return;
		}

		if (livestream != null &&
			newLivestream.isLive() == livestream.isLive() &&
			Objects.equals(newLivestream.getTitle(), livestream.getTitle()) &&
			Objects.equals(newLivestream.getWentLiveAt(), livestream.getWentLiveAt()))
		{
			lastChecked = currentTick;
			return;
		}

		final Livestream previousLivestream = livestream;
		final boolean wasLive = previousLivestream != null && previousLivestream.isLive();
		final boolean isLive = newLivestream.isLive();
		final boolean becameOffline = previousLivestream != null && wasLive && !isLive;

		livestream = newLivestream;
		clientThread.invokeLater(() -> {
			if (isLive)
			{
				sendLiveLivestreamMessage(true);
			}
			if (shouldPlayLiveSound(previousLivestream, newLivestream, suppressLiveTransitionSound))
			{
				offlineAnnouncementSent = false;
				suppressLiveTransitionSound = true;
				livestreamLiveSound.playSound();
			}
			else if (becameOffline && !offlineAnnouncementSent)
			{
				sendOfflineLivestreamMessage();
				offlineAnnouncementSent = true;
				suppressLiveTransitionSound = false;
			}
		});
	}


	private void sendLiveLivestreamMessage(boolean force)
	{
		final int currentTick = client.getTickCount();

		// Only send once every x minutes, unless we force send (in case he goes live)
		if (!force &&
			lastSentMessage != -1 &&
			currentTick >= lastSentMessage &&
			currentTick - lastSentMessage < config.livestreamInterval() * 100)
		{
			return;
		}

		// Only send if OphidiaVenom is live
		if (livestream == null || !livestream.isLive())
		{
			return;
		}

		lastSentMessage = currentTick;

		ChatMessageBuilder chatMessage = new ChatMessageBuilder();
		String hex = Integer.toHexString(config.livestreamColor().getRGB()).substring(2);
		final String title = livestream.getTitle() == null ? "" : livestream.getTitle().trim();
		chatMessage
			.append(ChatColorType.NORMAL)
			.append("OphidiaVenom is live! ");

		if (!title.isEmpty())
		{
			chatMessage
				.append(ChatColorType.HIGHLIGHT)
				.append(title);
		}

		queueLivestreamMessage(chatMessage.build(), hex);
	}

	private void sendOfflineLivestreamMessage()
	{
		ChatMessageBuilder chatMessage = new ChatMessageBuilder();
		chatMessage
			.append(ChatColorType.HIGHLIGHT)
			.append("OphidiaVenom went offline.");

		String hex = Integer.toHexString(config.livestreamColor().getRGB()).substring(2);
		queueLivestreamMessage(chatMessage.build(), hex);
	}

	private void queueLivestreamMessage(String chatMessage, String hex)
	{
		String message = chatMessage.replaceAll("colHIGHLIGHT", "col=" + hex);
		RightClickAction rightClickAction = new RightClickAction("Open Livestream", "https://twitch.tv/ophidiavenom");
		chatRightClickManager.putInMap(message, rightClickAction);
		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}

	private boolean shouldPlayLiveSound(
		Livestream previousLivestream,
		Livestream newLivestream,
		boolean wasSuppressingLiveSound)
	{
		if (!config.livestreamPlaySound() || wasSuppressingLiveSound || !newLivestream.isLive())
		{
			return false;
		}

		final Instant newWentLiveAt = parseWentLiveAt(newLivestream.getWentLiveAt());
		if (newWentLiveAt == null)
		{
			return false;
		}

		final Instant previousWentLiveAt = parseWentLiveAt(
			previousLivestream == null ? null : previousLivestream.getWentLiveAt());
		if (Objects.equals(newWentLiveAt, previousWentLiveAt))
		{
			return false;
		}

		final Duration age = Duration.between(newWentLiveAt, Instant.now());
		return !age.isNegative() && age.compareTo(LIVE_SOUND_MAX_AGE) <= 0;
	}

	private static Instant parseWentLiveAt(String wentLiveAt)
	{
		if (wentLiveAt == null || wentLiveAt.isEmpty())
		{
			return null;
		}

		try
		{
			return Instant.parse(wentLiveAt);
		}
		catch (DateTimeParseException e)
		{
			return null;
		}
	}

	private void handleTickReset()
	{
		final int currentTick = client.getTickCount();
		if (lastChecked != -1 && currentTick < lastChecked)
		{
			lastChecked = -1;
		}
		if (lastSentMessage != -1 && currentTick < lastSentMessage)
		{
			lastSentMessage = -1;
		}
	}

	public boolean onConfigChanged (ConfigChanged configChanged)
	{
		if (config.livestreamPlaySound() && configChanged.getKey().equals("livestreamPlaySound")) {
			livestreamLiveSound.playSound();
			return true;
		}
		return false;
	}
}
