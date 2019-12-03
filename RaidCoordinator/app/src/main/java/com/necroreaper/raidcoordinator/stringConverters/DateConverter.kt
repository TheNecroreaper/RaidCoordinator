package com.necroreaper.raidcoordinator.stringConverters

import java.util.*

class DateConverter {
    companion object {
        fun convertDate (date: Date): String
        {
            var convertedString = ""
            var hours = date.hours
            var timePeriod = " AM"
            if (hours > 12) {
                hours -= 12
                timePeriod = " PM"
            }
            if (hours == 0) {
                hours = 12
            }
            convertedString += hours.toString() + ":" + "%02d".format(date.minutes) + timePeriod
            return convertedString
        }
    }
}