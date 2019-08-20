package com.kstorozh.evozhuk.calendar

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import java.util.*
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.R
import kotlinx.android.synthetic.main.fragment_calendar_day_view.*
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
    private var firstDay: Long = 0
    private var lastDay: Long = 0

        fun bookings(startDate: Long, endDate: Long): LiveData<Map<String, List<Booking>>> {
            firstDay = startDate
            lastDay = endDate
            applicationScope.launch {
                val result = getBookingsUseCase.loadBooking(startDate, endDate)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
                    durationInMilisecLiveData.postValue(it.duration * ONE_SECOND)
                }
                result.domainError?.let {
                    errors.postValue(Event(it))
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
                    SimpleDateFormat(YEAR_MONTH_DAY_FORMAT).parse(day)?.let {
                        calendar.time = it
                    }
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

    fun bookingSlotsPerDay(dateInMilisec: Long, userId: Int): LiveData<List<TimeSlot>> {
        val dt = DateTime(dateInMilisec)
        val fmt = DateTimeFormat.forPattern(YEAR_MONTH_DAY_FORMAT)
        val dayInFormat = fmt.print(dt)

        return Transformations.switchMap(bookingsLiveData, Function {
            val bookingInDayList = it[dayInFormat]
            val liveData = MutableLiveData<List<TimeSlot>>()
            applicationScope.launch {
                val listOfTimeSlot: List<TimeSlot> = parseBookingToTimeSlot(bookingInDayList, userId, dateInMilisec)
                liveData.postValue(listOfTimeSlot)
            }
            return@Function liveData
        })

    }

    private fun parseBookingToTimeSlot(list: List<Booking>?, userId: Int, dateInMilisec: Long): List<TimeSlot> {
        val listOfTimeSlot = createEmptySlots(dateInMilisec, durationInMilisecLiveData.value!!)
        listOfTimeSlot.fillBusySlots(list, userId, durationInMilisecLiveData.value!!)
        return listOfTimeSlot
    }

    fun createNewBooking(userId: String, startDate: Long, endDate: Long): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        applicationScope.launch {
                val result = getBookingsUseCase.createBooking(
                    BookingInputData(
                        userId,
                        startDate = Calendar.getInstance().apply { timeInMillis = startDate },
                        endDate = Calendar.getInstance().apply { timeInMillis = endDate }),
                    firstDay,
                    lastDay)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
                    durationInMilisecLiveData.postValue(it.duration * ONE_SECOND)
                    liveData.postValue(true)
                }
                result.domainError?.let {
                    errors.postValue(Event(it))
                    liveData.postValue(false)
                }
        }
        return liveData
    }
}