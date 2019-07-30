/**
 *   Database constance
 */
internal const val DEVICE_INFO_DB_NAME = "devices_database"

internal const val DEVICE_INFO_TABLE_NAME = "device_info"
internal const val TOKEN_TABLE_NAME = "token_table"
internal const val BOOKING_TABLE_NAME = "booking_table"

/**
 * API constance
 */
internal const val ACCESS_TOKEN = "X-ACCESS-TOKEN"
internal const val INIT_DEVISE_URL = " /api/devices"
internal const val UPDATE_DEVISE_URL = "/api/devices/:{id}"
internal const val TAKE_DEVISE_URL = "/api/booking"
internal const val RETURN_DEVISE_URL = "/api/booking"
internal const val GET_USERS_URL = "/api/users"
internal const val CREATE_USER_URL = "/api/users"
internal const val REMIND_PIN_URL = "/api/users/{id}/pin/remind"
internal const val LOGIN_URL = "/api/login"

internal const val BASE_URL = "http://wh.evo.dev"
//internal const val BASE_URL = "http://evozhuk.tk"
internal const val ERROR_STATUS_CODE = 422
internal const val UNAUTHORIZED_STATUS_CODE = 401
internal const val NOT_FOUND_STATUS_CODE = 400

internal const val LOG_TAG = "MainActivity"
