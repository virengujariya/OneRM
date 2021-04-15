package me.fitbod.repetition.db

import androidx.room.TypeConverter
import java.time.LocalDate

interface BaseTypeConverter<A, B> {
    fun from(value: A): B
    fun to(value: B): A
}

class DateTypeConverter : BaseTypeConverter<Long, LocalDate> {
    @TypeConverter
    override fun from(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    override fun to(value: LocalDate): Long {
        return value.toEpochDay()
    }
}