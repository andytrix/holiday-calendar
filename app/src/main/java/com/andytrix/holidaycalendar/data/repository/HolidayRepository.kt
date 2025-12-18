package com.andytrix.holidaycalendar.data.repository

import com.andytrix.holidaycalendar.data.api.RetrofitClient
import com.andytrix.holidaycalendar.data.model.PublicHoliday

class HolidayRepository {

    suspend fun getPublicHolidays(year: Int, countryCode: String): List<PublicHoliday> {
        return RetrofitClient.holidayApi.getPublicHolidays(
            year = year,
            countryCode = countryCode
        )
    }
}
