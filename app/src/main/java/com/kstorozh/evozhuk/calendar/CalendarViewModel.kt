package com.kstorozh.evozhuk.calendar

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import java.util.*
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.ManageDeviceUseCases
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.*
import com.kstorozh.evozhuk.R
import com.kstorozh.evozhuk.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class CalendarViewModel(
    private val getBookingsUseCase: GetBookingUseCase,
    manageDiveceUseCase: ManageDeviceUseCases,
    private val applicationScope: CoroutineScope
) : BaseViewModel(manageDiveceUseCase, applicationScope) {

    private val bookingsLiveData = MutableLiveData<Map<String, List<Booking>>>()
    private var firstDay: Long = 0
    private var lastDay: Long = 0

        fun bookings(startDate: Long, endDate: Long): LiveData<Map<String, List<Booking>>> {
            firstDay = startDate
            lastDay = endDate
            applicationScope.launch {
                val result = getBookingsUseCase.getUpdatedBookingData(startDate, endDate)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
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
}