package com.kstorozh.evozhuk

const val USER_ID_NOT_SET = "USER_ID_NOT_SET"
const val INTENT_DATA_MILISEC = "INTENT_DATA_MILISEC"
const val INTENT_STOP_FLAG = "STOP_SERVICE"

const val DATE_FORMAT_TIMER = "%02d:%02d:%02d"
const val MINUTES_FORMAT_TIME_PICKER = "%02d"

const val DATE_FORMAT_NOTIFICATION_MESSAGE = "HH:mm dd MMMM"
const val DATE_FORMAT_BACK_DEVICE_SCREEN_TV = "HH:mm\ndd MMMM"
const val TIME_ZONE = "Europe/Kiev"
const val MEMORY_DECIMAL_FORMAT = "#.##"
const val YEAR_MONTH_DAY_FORMAT = "yyyy-MM-dd"
const val DAY_MONTH_FORMAT = "dd MMMM"
const val LOG_TAG: String = "MainActivity"
const val NOTIFICATION_SERVICE_NAME = "Notification service name"

const val OS = "android"

const val INFO_VERSION = "VERSION"
const val INFO_MODEL = "MODEL"
const val INFO_MAC = "MAC"
const val INFO_MEMORY = "MEMORY"
const val INFO_STORAGE = "STORAGE"
const val INFO_STORAGE_EMPTY = "STORAGE_EMPTY"
const val INFO_NOTE = "NOTE"

const val TIME_PICKER_INTERVAL: Int = 15
const val MINUETS_IN_HOUR = 60
const val SEC_IN_HOUR = 3600
const val MILISEC_IN_HOUR = 3600000L
const val HOUR_END_OF_WORK_DAY = 19
const val ONE_SECOND = 1000L

const val FIRST_HOUR = 8
const val LAST_HOUR = 20
const val MONTH_DELTA = 2

const val SPAN_COUNT = 2
const val SPACE_RECYCLER = 16

typealias DeviceInfoName = String
typealias DeviceInfoParam = String

const val TIME_TO_WAIT = 10000L // 10 sec
const val BATTERY_LEVEL_TO_CHARGE = 50

const val MUTUTS_BEFORE_TIME_IS_UP_NOTIFICATION = 15

/**
 * Time Picker
 */
const val NAME = "minute"
const val DEF_TYPE = "id"
const val PACKAGE = "android"

/**
 * Logs
 */

const val TIME_PICKER_ERROR = "Can not set to timePicker this data set: "
const val DELY_NAVIGATION_ERROR = "Already navigated to another screen"

const val CHOOSE_TIME_FRAGMENT_DIR = "ChooseTimeFragmentDirections"
const val BACK_DEVICE_FRAGMENT_DIR = "BackDeviceFragmentDirections"
