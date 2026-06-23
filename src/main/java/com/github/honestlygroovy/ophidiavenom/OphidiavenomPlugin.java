package com.github.honestlygroovy.ophidiavenom;


import com.github.honestlygroovy.ophidiavenom.livestreams.LivestreamManager;
import com.github.honestlygroovy.ophidiavenom.sounds.*;
import com.google.inject.Provides;

import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.OkHttpClient;

@Slf4j
@PluginDescriptor(
	name = "OphidiaVenom Plugin",
	description = "Replace and add in-game sounds by OphidiaVenom",
	tags = {"ophidiavenom", "stats", "levels", "quests", "diary", "announce"}
)

public class OphidiavenomPlugin extends Plugin
{
	@Inject
	private Client client;

	@Getter(AccessLevel.PACKAGE)
	@Inject
	private ClientThread clientThread;

	@Inject
	private SoundEngine soundEngine;

	@Inject
	private ScheduledExecutorService executor;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private LevelUp levelUp;

	@Inject
	private Death death;

	@Inject
	private AcceptTrade acceptTrade;

	@Inject
	private PetDog petDog;

	@Inject
	private PrayerDown prayerDown;

	@Inject
	private DeclineTrade declineTrade;

	@Inject
	private DismissRandomEvent dismissRandomEvent;

	@Inject
	private EnteringBankPin enteringBankPin;

	@Inject
	private Pet pet;

	@Inject
	private ToaChestLight toaChestLight;

	@Inject
	private ToaChestOpens toaChestOpens;

	@Inject
	private TobChestLight tobChestLight;

	@Inject
	private CollectionLog collectionLog;

	@Inject
	private QuestCompleted questCompleted;

	@Inject
	private CombatAchievements combatAchievements;

	@Inject
	private AchievementDiaries achievementDiaries;

	@Inject
	private PkChest pkChest;

	@Inject
	private CoxSounds coxSounds;

	@Inject
	private KillingPlayer killingPlayer;

	@Inject
	private LivestreamManager livestreamManager;

	@Inject
	private ChatRightClickManager chatRightClickManager;

	@Inject
	private CrabCheck crabCheck;

	@Inject
	private HunterRumour hunterRumour;

	@Inject
	private SlayerCompleted slayerCompleted;

	public static final String OPHIDIAVENOM = "OphidiaVenom";

	@Override
	protected void startUp() {
		clientThread.invoke(this::setupOldMaps);
		achievementDiaries.setLastLoginTick(-1);
		prayerDown.setLastLoginTick(-1);
		executor.submit(() -> {
			PlayerKillLineManager.Setup(okHttpClient);
			SoundFileManager.ensureDownloadDirectoryExists();
			SoundFileManager.downloadAllMissingSounds(okHttpClient);
		});
	}

	@Override
	protected void shutDown() {
		levelUp.clear();
		achievementDiaries.clearOldAchievementDiaries();
		soundEngine.close();
	}

	private void setupOldMaps()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			levelUp.clear();
			achievementDiaries.clearOldAchievementDiaries();
		}
		else
		{
			levelUp.setOldExperience();
			achievementDiaries.setOldAchievementDiaries();
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		collectionLog.onGameStateChanged(event);
		switch (event.getGameState())
		{
			case LOGIN_SCREEN:
			case HOPPING:
			case LOGGING_IN:
			case LOGIN_SCREEN_AUTHENTICATOR:
				levelUp.clear();
				achievementDiaries.clearOldAchievementDiaries();
			case CONNECTION_LOST:
				// set to -1 here in-case of race condition with varbits changing before this handler is called
				// when game state becomes LOGGED_IN
				//soundEngine.playClip(Sound.CLIENT_DISCONNECTS, executor);
				livestreamManager.resetStateForWorldHopOrLogin();

				achievementDiaries.setLastLoginTick(-1);
				prayerDown.setLastLoginTick(-1);
				collectionLog.setlastColLogSettingWarning();
				break;
			case LOGGED_IN:
				final int currentTick = client.getTickCount();
				achievementDiaries.setLastLoginTick(currentTick);
				prayerDown.setLastLoginTick(currentTick);
				break;
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		levelUp.onStatChanged(statChanged);
	}

	@Subscribe
	public void onActorDeath(ActorDeath actorDeath)
	{
		if (crabCheck.onActorDeath(actorDeath)) {}
		else if (death.onActorDeath(actorDeath)) {}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (acceptTrade.onChatMessage(chatMessage)) {}
		else if (petDog.onChatMessage(chatMessage)) {}
		else if (pet.onChatMessage(chatMessage)) {}
		else if (collectionLog.onChatMessage(chatMessage)) {}
		else if (questCompleted.onChatMessage(chatMessage)) {}
		else if (combatAchievements.onChatMessage(chatMessage)) {}
		else if (killingPlayer.onChatMessage(chatMessage)) {}
		else if (coxSounds.onChatMessage(chatMessage)) {}
		else if (hunterRumour.onChatMessage(chatMessage)) {}
		else if (slayerCompleted.onChatMessage(chatMessage)) {}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		tobChestLight.onVarbitChanged(event);
		collectionLog.onVarbitChanged(event);
		achievementDiaries.onVarbitChanged(event);
		killingPlayer.onVarbitChanged(event);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		petDog.onMenuOptionClicked(menuOptionClicked);
		declineTrade.onMenuOptionClicked(menuOptionClicked);
		dismissRandomEvent.onMenuOptionClicked(menuOptionClicked);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		pkChest.onWidgetLoaded(event);
	}

	@Provides
	OphidiavenomConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OphidiavenomConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}
		int currentTick = client.getTickCount();

		prayerDown.onGameTick(event);
		tobChestLight.onGameTick(event);
		coxSounds.onGameTick(event);
		livestreamManager.onGameTick(event);
		chatRightClickManager.onGameTick(event);

		// Should always happen after all tick events
		cleanupTicks(currentTick);
	}

	private void cleanupTicks(final int currentTick)
	{
		petDog.cleanupTicks(currentTick);
	}


	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		if (enteringBankPin.onSoundEffectPlayed(event)) {}
		else if (prayerDown.onSoundEffectPlayed(event)) {}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		toaChestOpens.onGameObjectSpawned(event);
		toaChestLight.onGameObjectSpawned(event);
		tobChestLight.onGameObjectSpawned(event);
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		tobChestLight.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged e) {
		if (!OphidiavenomConfig.CONFIG_GROUP.equals(e.getGroup()))
		{
			return;
		}
		if (acceptTrade.onConfigChanged(e)){}
		else if (achievementDiaries.onConfigChanged(e)){}
		else if (collectionLog.onConfigChanged(e)){}
		else if (combatAchievements.onConfigChanged(e)){}
		else if (coxSounds.onConfigChanged(e)){}
		else if (crabCheck.onConfigChanged(e)){}
		else if (death.onConfigChanged(e)){}
		else if (declineTrade.onConfigChanged(e)){}
		else if (dismissRandomEvent.onConfigChanged(e)){}
		else if (enteringBankPin.onConfigChanged(e)){}
		else if (hunterRumour.onConfigChanged(e)){}
		else if (killingPlayer.onConfigChanged(e)){}
		else if (levelUp.onConfigChanged(e)){}
		else if (livestreamManager.onConfigChanged(e)){}
		else if (pet.onConfigChanged(e)){}
		else if (petDog.onConfigChanged(e)){}
		else if (pkChest.onConfigChanged(e)){}
		else if (prayerDown.onConfigChanged(e)){}
		else if (questCompleted.onConfigChanged(e)){}
		else if (slayerCompleted.onConfigChanged(e)){}
		else if (toaChestLight.onConfigChanged(e)){}
		else if (toaChestOpens.onConfigChanged(e)){}
		else if (tobChestLight.onConfigChanged(e)){}
	}

	public static int TO_GROUP(int id)
	{
		return id >>> 16;
	}
}
