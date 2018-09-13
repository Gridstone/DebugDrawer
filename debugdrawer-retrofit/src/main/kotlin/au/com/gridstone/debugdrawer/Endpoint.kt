package au.com.gridstone.debugdrawer

import retrofit2.mock.NetworkBehavior

/**
 * Represents a network resource, often correlating to backend environments such as development,
 * staging, and production.
 *
 * Endpoints can also be listed as being mock, which informs [RetrofitModule] that [NetworkBehavior]
 * can be used to modify the behaviour of requests.
 */
data class Endpoint(val name: String, val url: String, val isMock: Boolean = false)
