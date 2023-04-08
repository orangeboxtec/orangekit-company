package com.orangebox.kit.company

import java.text.SimpleDateFormat
import java.util.*

object WorkingHourUtils {
    fun businessHourDesc(hours: List<WorkingHour?>?): String {
        val desc = StringBuilder()
        if (hours != null) {
            val df = SimpleDateFormat("EEE", Locale("pt", "BR"))
            val dates: MutableMap<String, MutableList<String>> = LinkedHashMap()
            val cal = Calendar.getInstance()
            cal[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            for (i in 0..6) {
                val weekDay = cal[Calendar.DAY_OF_WEEK]
                val wh = hours.stream()
                    .filter { p: WorkingHour? -> p!!.weekDay == weekDay }
                    .findFirst()
                    .orElse(null)
                if (wh != null) {
                    val time = wh.hourBegin + " - " + wh.hourEnd
                    if (!dates.containsKey(time)) {
                        dates[time] = ArrayList()
                    }
                    dates[time]!!.add(df.format(cal.time))
                }
                cal.add(Calendar.DAY_OF_WEEK, 1)
            }
            var count = 0
            for (hour in dates.keys) {
                val days: List<String> = dates[hour]!!
                if (count != 0) {
                    if (count + 1 < dates.size) {
                        desc.append(", ")
                    } else {
                        desc.append(" e ")
                    }
                }
                if (days.size == 1) {
                    desc.append(days[0])
                } else if (days.size == 2) {
                    desc.append(days[0])
                    desc.append(" à ")
                    desc.append(days[1])
                } else {
                    desc.append(days[0])
                    desc.append(" - ")
                    desc.append(days[days.size - 1])
                }
                desc.append(" ")
                desc.append(hour)
                count++
            }
        }
        return desc.toString()
    }
}