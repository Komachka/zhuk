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
// internal const val ACCESS_TOKEN = "X-ACCESS-TOKEN"
internal const val ACCESS_TOKEN = "X-DEVICE-ID"
internal const val INIT_DEVISE_URL = "/api/mobile/devices"
internal const val UPDATE_DEVISE_URL = "/api/mobile/devices/:{id}"
internal const val TAKE_DEVISE_URL = "/api/mobile/booking"
internal const val RETURN_DEVISE_URL = "/api/mobile/booking/return/{id}"
internal const val GET_USERS_URL = "/api/mobile/users"
internal const val REMIND_PIN_URL = "/api/mobile/users/{id}/pin/remind"
internal const val LOGIN_URL = "/api/mobile/login"
internal const val GET_BOOKING_URL = "/api/mobile/booking?"
internal const val START_DATE = "start"
internal const val END_DATE = "end"

// internal const val BASE_URL = "http://wh.evo.dev"
internal const val BASE_URL = "http://evozhuk.tk"

internal const val ERROR_STATUS_CODE = 422
internal const val UNAUTHORIZED_STATUS_CODE = 401
internal const val NOT_FOUND_STATUS_CODE = 400

internal const val LOG_TAG = "MainActivity"

/**
 * Error messages
 */
internal const val LOGIN_ERROR = "api problem with login"
internal const val INIT_ERROR = "api problem with init device"
internal const val UPDATE_ERROR = "api problem with update device"
internal const val BOOKING_ERROR = "api problem with update device"
internal const val RETURN_ERROR = "api problem with update device"
internal const val GET_USERS_ERROR = "api problem with getting list of users"
internal const val REMIND_PIN_ERROR = "api problem with reminding pin"
internal const val BOOKING_CALENDAR_ERROR = "api problem with getting booking by date"
internal const val BOOKING_DATA_EMPTY_ERROR = "Booking data is empty"
