package com.github.honestlygroovy.ophidiavenom;

public enum Sound
{
	ACCEPTED_TRADE("AcceptTrade", "AcceptTrade.wav"),//#17
	ACHIEVEMENT_DIARY("AchievementDiaryCompleted", "AchievementDiaryCompleted.wav"),
	COLLECTION_LOG_SLOT("CollectionLogSlotCompleted", "CollectionLogSlotCompleted.wav"),//#3
	COMBAT_TASK("CombatAchievementCompleted", "CombatAchievementCompleted.wav"),//#15
	CRAB_CHECK("CrabCheck", "CrabCheck.wav"),//#8
	DEATH("Death", "Death.wav"),//#2
	DECLINE_TRADE("DeclineTrade", "DeclineTrade.wav"),//#7
	DISMISSING_RANDOM_EVENT("Dismiss", "Dismiss.wav"),//#20
	TOA_CHEST_OPENS("GoodLuck", "GoodLuck.wav"),//#23
	HUNTER_RUMOUR("HunterRumourCompleted", "HunterRumourCompleted.wav"),//#8
	HUNTER_RUMOUR_NOT_COMPLETED("HunterRumourNotCompleted", "HunterRumourNotCompleted.wav"),//#8
	GO_LIVE("ImLive", "ImLive.wav"),
	LEVEL_99("Level99", "Level99.wav"),
	LEVEL_UP("LevelX", "LevelX.wav"),
	MAGE("Mage", "Mage.wav"),
	NEW_PET("NewPet", "NewPet.wav"),//#5
	NO_PRAYER("NoPrayer", "NoPrayer.wav"),//#4
	PETTING_DOG("PetDog", "PetDog.wav"),//#6
	CLICKING_PK_LOOT_CHEST("PKChest", "PKChest.wav"),//#26
	KILLING_SOMEONE_1("PKKill", "PKKill.wav"),//#9
	YOUR_PURPLE("PurpleChestYours", "PurpleChestYours.wav"),//#18
	QUEST("QuestCompleted", "QuestCompleted.wav"),
	RANGE("Range", "Range.wav"),
	SLAYER_TASK("SlayerTaskCompleted", "SlayerTaskCompleted.wav"),
	TYPING_IN_BANKPIN("Snap", "Snap.wav"),//#21
	WHITE_LIGHT_AFTER_RAID("WhiteChest", "WhiteChest.wav"),//#24
	NOT_YOUR_PURPLE("PurpleChestNotYours", "PurpleChestNotYours.wav");//#18

	private final String resourceName;
	private final String directory;

	Sound(String directory, String resourceName)
	{
		this.directory = directory;
		this.resourceName = resourceName;
	}


	public String getResourceName()
	{
		return resourceName;
	}

	public String getDirectory()
	{
		return directory;
	}
}
