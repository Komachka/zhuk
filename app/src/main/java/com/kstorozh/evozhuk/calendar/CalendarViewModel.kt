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

class CalendarViewModel : BaseViewModel(), KoinComponent, BookingParser {

    private val getBookingsUseCase: GetBookingUseCase by inject()
    private val applicationScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val bookingsLiveData = MutableLiveData<Map<String, List<Booking>>>()
    private val durationInMilisecLiveData = MutableLiveData<Long>()


        fun bookings(startDate: Long, endDate: Long): LiveData<Map<String, List<Booking>>> {
            applicationScope.launch {
                val result = getBookingsUseCase.loadBooking(startDate, endDate)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
                    durationInMilisecLiveData.postValue(it.duration * ONE_SECOND)
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
                val bookingInDayList = map[dayInFormat]
                val listOfTimeSlot: List<TimeSlot> = parseBookingToTimeSlot(bookingInDayList, userId, dateInMilisec)
                val timeSlotPerDayLiveData = MutableLiveData<List<TimeSlot>>()
                timeSlotPerDayLiveData.value = listOfTimeSlot
                return@Function timeSlotPerDayLiveData
            })
    }

    private fun parseBookingToTimeSlot(list: List<Booking>?, userId: Int, dateInMilisec: Long): List<TimeSlot> {
        val listOfTimeSlot = createEmptySlots(dateInMilisec, durationInMilisecLiveData.value!!)
        listOfTimeSlot.fillBusySlots(list, userId, durationInMilisecLiveData.value!!)
        return listOfTimeSlot
    }

    /*fun createNewBooking(userId: String, millisec: Long, millisec1: Long): LiveData<Boolean> {

    }*/


}