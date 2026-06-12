# Ophidiavenom Plugin 

##### A plugin for [RuneLite](https://runelite.net/)

Ophidiavenom announces when you complete an achievement!

Huge thanks to [Ophidiavenom](https://twitch.com/ophidiavenom) for providing custom recorded audio for this plugin!

Some `actions` might have multiple sounds, whenever there are multiple sounds, the sound being played will be chosen at random.
___
## General Troubleshooting
BEFORE TRYING ANYTHING ELSE, ENABLE THIS IN THE **RUNESCAPE** SETTINGS

![image](https://user-images.githubusercontent.com/62370532/208992085-e2c07494-d8bb-489e-b7f3-ed538175acbc.png)

Whenever this does not resolve your issue, please feel free to look in the [Issues](https://github.com/HonestlyGroovy/ophidia-venom-sounds/issues) section of this GitHub page to see if anyone else had this issue.
___

## Other information

### Currently implemented sounds include

You can find all the sound files [here](https://github.com/HonestlyGroovy/ophidia-venom-sounds/tree/sounds) and all the code [here](https://github.com/HonestlyGroovy/ophidia-venom-sounds/tree/master/src/main/java/com/github/honestlygroovy/ophidiavenom/sounds).

### Systems

We have implemented a few systems to support all of these features. 

#### Sound system

First and foremost we have implemented a sound system that consists of a [sound engine](https://github.com/HonestlyGroovy/ophidia-venom-sounds/blob/master/src/main/java/com/github/honestlygroovy/ophidiavenom/SoundEngine.java) and a [sound file manager](https://github.com/HonestlyGroovy/ophidia-venom-sounds/blob/master/src/main/java/com/github/honestlygroovy/ophidiavenom/SoundFileManager.java) to play all the sounds.

Sounds are downloaded to the local file system instead of being 'baked in' to the plugin build, allowing for further
expansion in the future while also 'supporting' user-swapped sounds for pre-existing events/actions (please refer to the warning section of `Customising your sounds`).

### Planned / Work In Progress expansions

- none

### Potential future expansions

- none at this moment

### Known Issues

PulseAudio on Linux can just refuse to accept the audio formats used despite claiming to accept them.
