package com.kstorozh.evozhuk.calendar_day

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.kstorozh.domainapi.GetBookingUseCase
import com.kstorozh.domainapi.model.Booking
import com.kstorozh.domainapi.model.BookingInputData
import com.kstorozh.evozhuk.BaseViewModel
import com.kstorozh.evozhuk.Event
import com.kstorozh.evozhuk.ONE_SECOND
import com.kstorozh.evozhuk.YEAR_MONTH_DAY_FORMAT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class DayViewModel : BaseViewModel(), KoinComponent, BookingParser {

    private val getBookingsUseCase: GetBookingUseCase by inject()
    private val applicationScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    val query = MutableLiveData<Pair<Long, Int>>()
    val bookingSlotsPerDay: LiveData<List<TimeSlot>> = Transformations.switchMap(query, ::getBookingSlotsPerDay)

    private val durationInMilisecLiveData = MutableLiveData<Long>().also { liveData ->
        applicationScope.launch {

            val result = getBookingsUseCase.getBookingLocal()
            result.data?.let {
                liveData.postValue(it.duration * ONE_SECOND)
            }
            result.domainError?.let {
                errors.postValue(Event(it))
            }
        }
    }

    private val bookingsLiveData = MutableLiveData<Map<String, List<Booking>>>().also { liveData ->
        applicationScope.launch {

            val result = getBookingsUseCase.getBookingLocal()
            result.data?.let {
                liveData.postValue(it.bookingMap)
            }
            result.domainError?.let {
                errors.postValue(Event(it))
            }
        }
    }

    private fun getBookingSlotsPerDay(params: Pair<Long, Int>): LiveData<List<TimeSlot>> {
        val dt = DateTime(params.first)
        val fmt = DateTimeFormat.forPattern(YEAR_MONTH_DAY_FORMAT)
        val dayInFormat = fmt.print(dt)
        return Transformations.switchMap(bookingsLiveData, Function {
            val bookingInDayList = it[dayInFormat]
            val liveData = MutableLiveData<List<TimeSlot>>()
            applicationScope.launch {
                val listOfTimeSlot: List<TimeSlot> = parseBookingToTimeSlot(bookingInDayList, params.second, params.first)
                liveData.postValue(listOfTimeSlot)
            }
            return@Function liveData
        })
    }

    fun getBookingInfo(dateInMilisec: Long, userId: Int) = apply {
        query.value = dateInMilisec to userId
    }

    private fun parseBookingToTimeSlot(list: List<Booking>?, userId: Int, dateInMilisec: Long): List<TimeSlot> {
        val listOfTimeSlot = mutableListOf<TimeSlot>()
        durationInMilisecLiveData.value?.let {
            listOfTimeSlot.addAll(createEmptySlots(dateInMilisec, it))
            listOfTimeSlot.fillBusySlots(list, userId, it)
        }
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
                startDate,
                endDate)
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