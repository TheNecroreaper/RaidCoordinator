package com.necroreaper.raidcoordinator.stringConverters

import com.google.firebase.Timestamp

class DateConverter {
    companion object {
        fun convertDate (time: Timestamp): String
        {
            var date = time.toDate()
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