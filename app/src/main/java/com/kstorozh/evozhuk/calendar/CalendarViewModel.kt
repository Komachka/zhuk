package com.kstorozh.evozhuk.calendar

import android.util.Log
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

    fun getBookingSlotsPerDay(milisec: Long, userId: Int): LiveData<List<TimeSlot>> {
        val dt = DateTime(milisec)
        val fmt = DateTimeFormat.forPattern(YEAR_MONTH_DAY_FORMAT)
        val dayInFormat = fmt.print(dt)

        return Transformations.switchMap(bookingsLiveData,
            Function<Map<String, List<Booking>>, LiveData<List<TimeSlot>>> {map->
                val liveData = MutableLiveData<List<TimeSlot>>()
                val bookingInDayList = map[dayInFormat]
                bookingInDayList?.let {
                    val listOfTimeSlot:List<TimeSlot> = parseBookingToTimeSlot(it, userId)
                    liveData.value = listOfTimeSlot
                }
                return@Function liveData

            })
    }

    private fun parseBookingToTimeSlot(list: List<Booking>, userId: Int): List<TimeSlot> {

     val mapOfTimeSlot = mutableMapOf<Int, TimeSlot>()
        for (i in 8..20) // TODO move to const first and last hour
     {
         val startDate = if (i < 10) "0${i}:00" else "$i:00"
         val endDate = if (i + 1 < 10) "0${i + 1}:00" else "${i + 1}:00"
         mapOfTimeSlot[i] = TimeSlot(
             isMyBooking = false,
             isOtherBooking = false,
             slotStartDate = startDate,
             slotEndDate = endDate
         )
     }
     list.forEach {
         val dateTimeStart = DateTime(it.startDate)
         //handle first slot
         if (mapOfTimeSlot.containsKey(dateTimeStart.hourOfDay)) // if there are two slots with the same start hour time the last one will be main
         {
             val item = mapOfTimeSlot[dateTimeStart.hourOfDay]
             item!!.booking = it
             if (item.booking!!.userId == userId)
                 item.isMyBooking = true
             else
                 item.isOtherBooking = true
         }
         //handle other slots
         if (it.duration > SEC_IN_HOUR) {
             var time = it.duration - SEC_IN_HOUR
             while (time/ SEC_IN_HOUR > 0)
             {
                 val startDateTmp = dateTimeStart.millis  + ((time/ SEC_IN_HOUR) * SEC_IN_HOUR * 1000)
                 val dateTimeStartTmp = DateTime(startDateTmp)
                 if (mapOfTimeSlot.containsKey(dateTimeStartTmp.hourOfDay)) // if there are two slots with the same start hour time the last one will be main
                 {
                     val item = mapOfTimeSlot[dateTimeStartTmp.hourOfDay]
                     item!!.booking = it
                     if (item.booking!!.userId == userId)
                         item.isMyBooking = true
                     else
                         item.isOtherBooking = true
                 }
                 time -= SEC_IN_HOUR
             }
         }





     }


        return mapOfTimeSlot.values.toList()
    }


}