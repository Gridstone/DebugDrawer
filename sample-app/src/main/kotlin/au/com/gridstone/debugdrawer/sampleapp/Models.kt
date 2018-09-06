package au.com.gridstone.debugdrawer.sampleapp

data class GamesResponse(val results: List<Game>)

data class Game(val id: Long,
                val name: String,
                val aliases: String?,
                val deck: String,
                val image: Image,
                val original_release_date: String,
                val platforms: List<Platform>)

data class Platform(val name: String)

data class Image(val small_url: String, val super_url: String)
