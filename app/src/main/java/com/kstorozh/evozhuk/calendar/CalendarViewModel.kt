package com.kstorozh.evozhuk.calendar

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import java.util.*
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import org.joda.time.format.DateTimeFormat

class CalendarViewModel : BaseViewModel(), KoinComponent {

    private val getBookingsUseCase: GetBookingUseCase by inject()
    private val applicationScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val bookingsLiveData = MutableLiveData<Map<String, List<Booking>>>()
    private val durationLiveData = MutableLiveData<Long>()

        fun bookings(startDate: Long, endDate: Long): LiveData<Map<String, List<Booking>>> {
            applicationScope.launch {
                val result = getBookingsUseCase.loadBooking(startDate, endDate)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
                    durationLiveData.postValue(it.duration)
                }
                result.domainError?.let {
                    errors.postValue(it)
                }
            }
            return bookingsLiveData
        }

        fun getBookingEvents(startDate: Long, endDate: Long, userId: Int): LiveData<List<EventDay>> {
            return Transformations.switchMap(bookings(startDate, endDate),
                Function<Map<String, List<Booking>>, LiveData<List<EventDay>>> {
                val mutableLiveData = MutableLiveData<List<EventDay>>()
                val events = mutableListOf<EventDay>()
                it.forEach { (day, bookingList) ->
                    val calendar = Calendar.getInstance()
                    calendar.time = SimpleDateFormat(YEAR_MONTH_DAY_FORMAT).parse(day)
                    events.add(EventDay(calendar, chooseImageIcon(bookingList, userId)))
                }
                mutableLiveData.value = events
                return@Function mutableLiveData
            })
        }

    private fun chooseImageIcon(bookingList: List<Booking>, userId: Int): Int {
        var isOnlyMyBooking = false
        var isOnlyAnotherPersonBooking = false
        bookingList.forEach { booking ->
            if (booking.userId == userId) isOnlyMyBooking = true
            else isOnlyAnotherPersonBooking = true
        }
        return if (isOnlyMyBooking && isOnlyAnotherPersonBooking) R.drawable.sample_two_icons
        else if (isOnlyMyBooking) R.drawable.my_book_icon
        else R.drawable.other_book_icon
    }

    fun getBookingSlotsPerDay(dateInMilisec: Long, userId: Int): LiveData<List<TimeSlot>> {
        val dt = DateTime(dateInMilisec)
        val fmt = DateTimeFormat.forPattern(YEAR_MONTH_DAY_FORMAT)
        val dayInFormat = fmt.print(dt)
        return Transformations.switchMap(bookingsLiveData,
            Function<Map<String, List<Booking>>, LiveData<List<TimeSlot>>> { map ->
                val liveData = MutableLiveData<List<TimeSlot>>()
                val bookingInDayList = map[dayInFormat]
                bookingInDayList?.let {
                    val listOfTimeSlot: List<TimeSlot> = parseBookingToTimeSlot(it, userId, dateInMilisec)
                    liveData.value = listOfTimeSlot
                }
                return@Function liveData
            })
    }

    private fun parseBookingToTimeSlot(list: List<Booking>, userId: Int, dateInMilisec: Long): List<TimeSlot> {
        val mapOfTimeSlot = mutableMapOf<LongRange, TimeSlot>()
        val slotDuration = durationLiveData.value
        val sdt = DateTime(dateInMilisec).withHourOfDay(8)
        val edt = DateTime(dateInMilisec).withHourOfDay(20)
        val firstHour = sdt.millis / 1000
        val lastHour = edt.millis / 1000
        val step = slotDuration!! // in seconds
        var iSec = firstHour
        while (iSec <= lastHour) {
            val tmpStartDate = DateTime(iSec * 1000)
            val tmpEndDate = DateTime((iSec + step) * 1000)
            val startDate = tmpStartDate.getStringHourMinuteDate()
            val endDate = tmpEndDate.getStringHourMinuteDate()
            mapOfTimeSlot[(iSec..(iSec + step))] = TimeSlot(
                isMyBooking = false,
                isOtherBooking = false,
                isContinue = false,
                timeLable = startDate,
                slotStartDate = startDate,
                slotEndDate = endDate
            )
            iSec += step
        }

        list.forEach { booking ->
            val dateTimeStart = DateTime(booking.startDate)
            val dateTimeEnd = DateTime(booking.endDate)
            var r: LongRange? = null
            mapOfTimeSlot.forEach { (range, slot) ->
                if (range.contains(dateTimeStart.millis / 1000))
                    r = range
            }
            r?.let {
                val item = mapOfTimeSlot[it]
                item!!.booking = booking
                item.isContinue = false
                item.slotEndDate = dateTimeEnd.getStringHourMinuteDate()
                item.slotStartDate = dateTimeStart.getStringHourMinuteDate()
                if (item.booking!!.userId == userId)
                    item.isMyBooking = true
                else
                    item.isOtherBooking = true
            }
            var time = booking.duration
            while (time / step > 0.0) {
                val startDateTmp = dateTimeStart.millis + ((time / step) * step * 1000)
                val dateTimeStartTmp = DateTime(startDateTmp)
                var r: LongRange? = null
                mapOfTimeSlot.forEach { (range, slot) ->
                    if (range.contains(dateTimeStartTmp.millis / 1000))
                        r = range
                }
                r?.let {
                    val item = mapOfTimeSlot[it]
                    item!!.booking = booking
                    item.isContinue = true
                    if (item.booking!!.userId == userId)
                        item.isMyBooking = true
                    else
                        item.isOtherBooking = true
                }
                time -= step
            }
        }
        return mapOfTimeSlot.values.toList()
    }

    private fun DateTime.getStringHourMinuteDate(): String {
        val minute = if (this.minuteOfHour <10) "0${this.minuteOfHour}" else "${this.minuteOfHour}"
        val hour = if (this.hourOfDay <10) "0${this.hourOfDay}" else "${this.hourOfDay}"
        return "$hour:$minute"
    }
}