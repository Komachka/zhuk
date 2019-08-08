package com.kstorozh.evozhuk.calendar

import androidx.lifecycle.*
import com.applandeo.materialcalendarview.EventDay
import com.kstorozh.evozhuk.R
import java.util.*
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking
import org.koin.core.KoinComponent
import org.koin.core.inject

class CalendarViewModelFactory(private val startDate: Long, private val endDate: Long) :
    ViewModelProvider.Factory, KoinComponent {
    private val getBookingsUseCase: GetBookingUseCase by inject()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CalendarViewModel(getBookingsUseCase, startDate, endDate) as T
    }
}

    class CalendarViewModel(
        private val getBookingsUseCase: GetBookingUseCase,
        private val startDate: Long,
        private val endDate: Long
    ) : ViewModel() {

        private val bookingsLiveData: LiveData<List<Booking>> by lazy {
            val liveData = MutableLiveData<List<Booking>>()
            getBookingsUseCase.loadBooking(startDate, endDate) { liveData.value = it }
            return@lazy liveData
        }

        fun bookings(startDate: String, endDate: String): LiveData<List<Booking>> = bookingsLiveData

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