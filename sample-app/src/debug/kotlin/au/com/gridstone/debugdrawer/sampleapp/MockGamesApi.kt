package au.com.gridstone.debugdrawer.sampleapp

import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit

class MockGamesApi(mockRetrofit: MockRetrofit) : GamesApi {
  private val delegate: BehaviorDelegate<GamesApi> =
      mockRetrofit.create(GamesApi::class.java)

  override suspend fun getGames(): GamesResponse {
    val response = GamesResponse(mockGames)
    return delegate.returningResponse(response).getGames()
  }

  private val mockGames = listOf(
      Game(27668,
           "The Witness",
           null,
           "An exploration-focused puzzle-adventure game led by the creator of the 2008 indie game Braid. While exploring a quiet but colorful island, players must solve a series of maze-like puzzles on numerous electronic puzzle consoles.",
           Image("https://www.giantbomb.com/api/image/screen_medium/2994285-box_tw.png",
                 "https://www.giantbomb.com/api/image/scale_large/2994285-box_tw.png"),
           "2016-01-26 00:00:00",
           listOf(Platform("MAC"), Platform("PC"), Platform("Xbox One"), Platform("PlayStation 4"))
      ),
      Game(31772,
           "Journey",
           null,
           "Journey is thatgamecompany's third release for Sony. Roam the lands discovering the history of an ancient civilization on a trek toward a distant mountain. Go at it alone or explore with strangers online.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2388618-2172218_journey_detailcropped.png",
               "https://www.giantbomb.com/api/image/scale_large/2388618-2172218_journey_detailcropped.png"),
           "2012-03-13 00:00:00",
           listOf(Platform("PlayStation 3"), Platform("PlayStation 4"))
      ),
      Game(41484,
           "The Witcher 3: Wild Hunt",
           "The Witcher III: Wild Hunt",
           "CD Projekt RED's third Witcher combines the series' non-linear storytelling with a sprawling open world that concludes the saga of Geralt of Rivia.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2945734-the%20witcher%203%20-%20wild%20hunt.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2945734-the%20witcher%203%20-%20wild%20hunt.jpg"),
           "2015-05-19 00:00:00",
           listOf(Platform("PC"), Platform("Xbox One"), Platform("PlayStation 4"))
      ),
      Game(13594,
           "The Legend of Zelda: Majora's Mask",
           "LoZ: MM\\r\\nZelda no Densetsu: Majora no Kamen\\r\\nゼルダの伝説 ムジュラの仮面\\r\\nZelda Gaiden\\r\\nThe Legend of Zelda: Majora's Mask 3D",
           "The follow-up to Ocarina of Time sees the series stalwart Link embark on a journey to save the land of Termina from being crushed by the moon in three days. To defeat the Skull Kid, Link has to live the same three days over and over again.",
           Image("https://www.giantbomb.com/api/image/scale_small/2204861-box_tlozmm.png",
                 "https://www.giantbomb.com/api/image/scale_large/2204861-box_tlozmm.png"),
           "2000-04-27 00:00:00",
           listOf(Platform("Nintendo 64"), Platform("Nintendo 3DS"))
      ),
      Game(20716,
           "Braid",
           null,
           "Manipulate time to complete puzzles in this 2D platform game made by indie developer Jonathan Blow.",
           Image("https://www.giantbomb.com/api/image/scale_small/1331143-braid_box.png",
                 "https://www.giantbomb.com/api/image/scale_large/1331143-braid_box.png"),
           "2008-08-06 00:00:00",
           listOf(Platform("PC"), Platform("MAC"), Platform("Xbox 360"), Platform("PlayStation 3"))
      ),
      Game(36989,
           "The Last of Us",
           "TLOU",
           "Joel and Ellie must survive in a post-apocalyptic world where a deadly parasitic fungus infects people's brains in this  PS3 exclusive third-person action-adventure game from Naughty Dog.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2419553-397060_386577108098888_427807760_n.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2419553-397060_386577108098888_427807760_n.jpg"),
           "2013-06-14 00:00:00",
           listOf(Platform("PlayStation 3"), Platform("PlayStation 4"))
      ),
      Game(49529,
           "Snakebird",
           null,
           "A puzzle game from Noumenon Games for PC, Linux, and Mac.",
           Image("https://www.giantbomb.com/api/image/scale_small/2745292-4225139406-capsu.jpg",
                 "https://www.giantbomb.com/api/image/scale_large/2745292-4225139406-capsu.jpg"),
           "2015-05-04 00:00:00",
           listOf(Platform("PC"), Platform("MAC"), Platform("Android"), Platform("iPhone"))
      ),
      Game(39301,
           "Europa Universalis IV",
           "Europa Universalis 4",
           "The newest game in Paradox Interactive's grand strategy franchise, focusing on Europe in the Age of Discovery.",
           Image("https://www.giantbomb.com/api/image/scale_small/2331106-box_eu4.png",
                 "https://www.giantbomb.com/api/image/scale_large/2331106-box_eu4.png"),
           "2013-08-13 00:00:00",
           listOf(Platform("PC"), Platform("MAC"), Platform("Linux"))
      ),
      Game(15589,
           "Diablo II: Lord of Destruction",
           "D2:LoD, LoD, Diablo 2: Lord of Destruction",
           "Diablo II:  Lord of Destruction features a new act and 2 new character classes, the Assassin and the Druid, as well as new items.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/184452-diablo_ii___lord_of_destruction_coverart.png",
               "https://www.giantbomb.com/api/image/scale_large/184452-diablo_ii___lord_of_destruction_coverart.png"),
           "2001-06-29 00:00:00",
           listOf(Platform("PC"), Platform("MAC"))
      ),
      Game(15738,
           "Donkey Kong Country",
           "DKC\\r\\nSuper Donkey Kong\\r\\nDonkey Kong 2001",
           "For the first time in Nintendo history, take control of Mario's former adversary as he travels through the island (with his nephew Diddy Kong) to recover his stolen banana hoard in this side-scrolling platformer from Rare (now Rareware).",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2832009-dkc.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2832009-dkc.jpg"),
           "1994-11-24 00:00:00",
           listOf(Platform("Super Nintendo Entertainment System"), Platform("Game Boy Advance"))
      ),
      Game(1520,
           "Fire Emblem",
           "Fire Emblem: Rekka no Ken\\n\\nFire Emblem: Blazing Sword\\n\\nFire Emblem 7",
           "The seventh game in the Fire Emblem series and a prequel to Fire Emblem: Fuin no Tsurugi, released for the Game Boy Advance. It is the first Fire Emblem game to be released outside of Japan.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2103129-box_fe.png",
               "https://www.giantbomb.com/api/image/scale_large/2103129-box_fe.png"),
           "2003-04-25 00:00:00",
           listOf(Platform("Game Boy Advance"))
      ),
      Game(17367,
           "The Elder Scrolls III: Morrowind",
           "Morrowind\\r\\nElder Scrolls 3",
           "The third entry in Bethesda's series of expansive first-person role-playing games. Arriving on the island of Vvardenfell as a prisoner, the player character is caught up in an ancient prophecy, as well as a power struggle between factions, races, and gods incarnate.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/610334-morrowind_title.jpg",
               "https://www.giantbomb.com/api/image/scale_large/610334-morrowind_title.jpg"),
           "2002-05-01 00:00:00",
           listOf(Platform("PC"), Platform("XBOX"))
      ),
      Game(13076,
           "Age of Mythology",
           "AoM",
           "Age of Mythology is a real-time strategy game based on the myths and heroic epics of the ancient civilizations of Scandinavia, Greece, and Egypt, players control the Atlantean hero Arkantos and can invoke powerful god powers to turn the tide of battle.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/627182-256px_age_of_mythology_liner.jpg",
               "https://www.giantbomb.com/api/image/scale_large/627182-256px_age_of_mythology_liner.jpg"),
           "2002-10-31 00:00:00",
           listOf(Platform("PC"), Platform("MAC"))
      ),
      Game(21380,
           "Prince of Persia: The Two Thrones",
           "Prince of Persia: Rival Swords (PSP, Wii)\\nPoP: TTT\\nPoP: T2T",
           "The Two Thrones is the fourth and final game in the Sands Of Time series. It tells of the Princes quest to finish the corrupted, power hungry Vizier and his negative parts of his mind.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2280504-box_popttt.png",
               "https://www.giantbomb.com/api/image/scale_large/2280504-box_popttt.png"),
           "2005-12-01 00:00:00",
           listOf(Platform("PC"), Platform("GameCube"), Platform("XBOX"), Platform("PlayStation 2"))
      ),
      Game(57732,
           "Sundered",
           null,
           "Sundered is a replayable metroidvania from the creators of Jotun.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2912516-sundered_key%20art_1920x1080_all%20platforms.png",
               "https://www.giantbomb.com/api/image/scale_large/2912516-sundered_key%20art_1920x1080_all%20platforms.png"),
           "2017-07-28 00:00:00",
           listOf(Platform("PC"), Platform("PlayStation 4"))
      ),
      Game(53436,
           "Pyre",
           null,
           "Lead a band of exiles through an ancient competition in the third game from Supergiant Games.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2945692-pyre%20v1.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2945692-pyre%20v1.jpg"),
           "2017-07-25 00:00:00",
           listOf(Platform("PC"), Platform("MAC"), Platform("PlayStation 4"))
      ),
      Game(61962,
           "Getting Over It with Bennett Foddy",
           null,
           "A high-difficulty climbing game inspired by Sexy Hiking.",
           Image(
               "https://www.giantbomb.com/api/image/scale_small/2968850-6302838751-heade.jpg",
               "https://www.giantbomb.com/api/image/scale_large/2968850-6302838751-heade.jpg"),
           "2017-12-06 00:00:00",
           listOf(Platform("PC"), Platform("MAC"))
      )
  )
}
