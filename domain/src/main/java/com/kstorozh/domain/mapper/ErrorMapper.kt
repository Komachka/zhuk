package com.kstorozh.domain.mapper

import com.kstorozh.dataimpl.DataError
import com.kstorozh.dataimpl.model.TmpErrorData
import com.kstorozh.domainapi.model.DomainErrorData
import com.kstorozh.domainapi.model.DomainErrors
import com.kstorozh.domainapi.model.ErrorStatus
import org.joda.time.format.ISODateTimeFormat
import java.lang.Exception

class ErrorMapper {

    fun mapToDomainError(error: DataError?): DomainErrors? {
        error?.let {
            var status: ErrorStatus? = null
            try {
                status = ErrorStatus.valueOf(error.errorStatus.toString())
            } catch (e: Exception) {}
            return DomainErrors(status, error.throwable, error.errorMessage,
                error.data?.let { mapToDomainErrorData(it) })
        }
        return null
    }

    private fun mapToDomainErrorData(tmpErrorData: TmpErrorData): DomainErrorData {
        val foramtter = ISODateTimeFormat.dateTimeParser()
        val start = foramtter.parseDateTime(tmpErrorData.start)
        val end = foramtter.parseDateTime(tmpErrorData.end)
        return DomainErrorData(tmpErrorData.bookingId, tmpErrorData.username, start.millis, end.millis)
    }
}