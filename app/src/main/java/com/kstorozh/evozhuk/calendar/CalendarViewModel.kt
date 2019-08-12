package com.kstorozh.evozhuk.calendar

import android.util.Log
import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import com.kstorozh.evozhuk.R
import java.util.*
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class CalendarViewModel : BaseViewModel(), KoinComponent {

    private val getBookingsUseCase: GetBookingUseCase by inject()
    private val applicationScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val bookingsLiveData = MutableLiveData<Map<String, Booking>>()

        fun bookings(startDate: Long, endDate: Long): LiveData<Map<String, Booking>> {

            Log.d(LOG_TAG, "here")
            applicationScope.launch {
                val result = getBookingsUseCase.loadBooking(startDate, endDate)
                result.data?.let {
                    bookingsLiveData.postValue(it.bookingMap)
                }
                result.domainError?.let {
                    errors.postValue(it)
                }
            }

            return bookingsLiveData
        }

        fun getBookingDaysInfo(): MutableLiveData<List<EventDay>> {

            val events = ArrayList<EventDay>()

            val calendar = Calendar.getInstance()
            events.add(EventDay(calendar, R.drawable.sample_two_icons))

            val calendar1 = Calendar.getInstance()
            calendar1.add(Calendar.DAY_OF_MONTH, 2)
            events.add(EventDay(calendar1, R.drawable.my_book_icon))

            val calendar11 = Calendar.getInstance()
            calendar11.add(Calendar.DAY_OF_MONTH, 30)
            events.add(EventDay(calendar11, R.drawable.my_book_icon))

            val calendar2 = Calendar.getInstance()
            calendar2.add(Calendar.DAY_OF_MONTH, 5)
            events.add(EventDay(calendar2, R.drawable.other_book_icon))

            val calendar3 = Calendar.getInstance()
            calendar3.add(Calendar.DAY_OF_MONTH, 7)
            events.add(EventDay(calendar3, R.drawable.sample_two_icons))

            val calendar4 = Calendar.getInstance()
            calendar4.add(Calendar.DAY_OF_MONTH, 13)
            events.add(EventDay(calendar4, R.drawable.sample_two_icons))

            val liveData = MutableLiveData<List<EventDay>>()
            liveData.value = events
            return liveData
        }
}