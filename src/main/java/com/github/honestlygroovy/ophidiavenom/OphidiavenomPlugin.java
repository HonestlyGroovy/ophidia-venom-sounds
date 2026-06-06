package com.github.honestlygroovy.ophidiavenom;


import com.github.honestlygroovy.ophidiavenom.livestreams.LivestreamManager;
import com.github.honestlygroovy.ophidiavenom.sounds.AcceptTrade;
import com.github.honestlygroovy.ophidiavenom.sounds.AchievementDiaries;
import com.github.honestlygroovy.ophidiavenom.sounds.CollectionLog;
import com.github.honestlygroovy.ophidiavenom.sounds.CombatAchievements;
import com.github.honestlygroovy.ophidiavenom.sounds.CoxSounds;
import com.github.honestlygroovy.ophidiavenom.sounds.Death;
import com.github.honestlygroovy.ophidiavenom.sounds.DeclineTrade;
import com.github.honestlygroovy.ophidiavenom.sounds.DismissRandomEvent;
import com.github.honestlygroovy.ophidiavenom.sounds.EnteringBankPin;
import com.github.honestlygroovy.ophidiavenom.sounds.GiveBone;
import com.github.honestlygroovy.ophidiavenom.sounds.HairDresser;
import com.github.honestlygroovy.ophidiavenom.sounds.KillingPlayer;
import com.github.honestlygroovy.ophidiavenom.sounds.LevelUp;
import com.github.honestlygroovy.ophidiavenom.sounds.Pet;
import com.github.honestlygroovy.ophidiavenom.sounds.PetDog;
import com.github.honestlygroovy.ophidiavenom.sounds.PkChest;
import com.github.honestlygroovy.ophidiavenom.sounds.PrayerDown;
import com.github.honestlygroovy.ophidiavenom.sounds.QuestCompleted;
import com.github.honestlygroovy.ophidiavenom.sounds.ToaChestLight;
import com.github.honestlygroovy.ophidiavenom.sounds.ToaChestOpens;
import com.github.honestlygroovy.ophidiavenom.sounds.TobChestLight;
import com.github.honestlygroovy.ophidiavenom.sounds.TurnOnRun;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ActorDeath;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
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
	private OphidiavenomConfig config;

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
	private DebugScripts debugScripts;

	@Inject
	private PrayerDown prayerDown;

	@Inject
	private TurnOnRun turnOnRun;

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
	private GiveBone giveBone;

	@Inject
	private HairDresser hairDresser;

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
	private ClientToolbar clientToolbar;

	@Inject
	@Named("developerMode")
	private boolean developerMode;

	private NavigationButton soundOverridesNavigationButton;

	public static final String OPHIDIAVENOM = "OphidiaVenom";

	@Override
	protected void startUp() throws Exception
	{
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
	protected void shutDown() throws Exception
	{
		levelUp.clear();
		achievementDiaries.clearOldAchievementDiaries();
		soundEngine.close();
	}

	private static BufferedImage createOverridesIcon()
	{
		final int size = 16;
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		try
		{
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

			Color speakerColor = new Color(235, 235, 235);
			graphics.setColor(speakerColor);

			int[] speakerX = {3, 6, 9, 9, 6, 3};
			int[] speakerY = {6, 6, 3, 13, 10, 10};
			graphics.fillPolygon(speakerX, speakerY, speakerX.length);

			graphics.setStroke(new java.awt.BasicStroke(1.4f));
			Color waveColor = new Color(255, 152, 41);
			graphics.setColor(waveColor);
			graphics.drawArc(7, 4, 4, 8, -55, 110);
			graphics.drawArc(9, 2, 5, 12, -55, 110);
		}
		finally
		{
			graphics.dispose();
		}
		return image;
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
		death.onActorDeath(actorDeath);
	}


	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (acceptTrade.onChatMessage(chatMessage))
		{
			return;
		}
		else if (petDog.onChatMessage(chatMessage))
		{
			return;
		}
		else if (pet.onChatMessage(chatMessage))
		{
			return;
		}
		else if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE && chatMessage.getType() != ChatMessageType.SPAM)
		{
			return;
		}
		else if (collectionLog.onChatMessage(chatMessage))
		{
			return;

		}
		else if (questCompleted.onChatMessage(chatMessage))
		{
			return;
		}
		else if (combatAchievements.onChatMessage(chatMessage))
		{
			return;
		}
		else if (giveBone.onChatMessage(chatMessage))
		{
			return;
		}
		else if (killingPlayer.onChatMessage(chatMessage))
		{
			return;
		}
		else if (coxSounds.onChatMessage(chatMessage))
		{
			return;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		/*
		if (developerMode && config.developerLogging())
		{
			debugScripts.onVarbitChanged(event);
		}
		*/


		turnOnRun.onVarbitChanged(event);
		tobChestLight.onVarbitChanged(event);
		collectionLog.onVarbitChanged(event);
		achievementDiaries.onVarbitChanged(event);
		killingPlayer.onVarbitChanged(event);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		/*
		if (developerMode && config.developerLogging())
		{
			debugScripts.onMenuOptionClicked(menuOptionClicked);
		}

		 */

		petDog.onMenuOptionClicked(menuOptionClicked);
		turnOnRun.onMenuOptionClicked(menuOptionClicked);
		declineTrade.onMenuOptionClicked(menuOptionClicked);
		dismissRandomEvent.onMenuOptionClicked(menuOptionClicked);

	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		/*
		if (developerMode && config.developerLogging())
		{
			debugScripts.onWidgetLoaded(event);
		}

		 */

		hairDresser.onWidgetLoaded(event);
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
		final Player local = client.getLocalPlayer();
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
		enteringBankPin.onSoundEffectPlayed(event);
		prayerDown.onSoundEffectPlayed(event);

	}

	@Subscribe
	public void onWallObjectSpawned(final WallObjectSpawned event)
	{
		toaChestLight.onWallObjectSpawned(event);
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		toaChestOpens.onGameObjectSpawned(event);
		tobChestLight.onGameObjectSpawned(event);
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		tobChestLight.onGameObjectDespawned(event);
	}

	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent scriptCallbackEvent)
	{
		/*
		if (developerMode && config.developerLogging())
		{
			debugScripts.onScriptCallbackEvent(scriptCallbackEvent);
		}

		 */
	}

	public static int TO_GROUP(int id)
	{
		return id >>> 16;
	}
}
