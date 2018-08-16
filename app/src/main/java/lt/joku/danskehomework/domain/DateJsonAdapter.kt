package lt.joku.danskehomework.domain

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.util.*

class DateJsonAdapter : TypeAdapter<Date>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, date: Date) {
        // Not needed in this project

//        // implement write: combine firstName and lastName into name
//        out.beginObject()
//        out.name("name")
//        out.value(user.firstName + " " + user.lastName)
//        out.endObject()
        // implement the write method
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): Date? {
        var dateAsString = ""
        if (reader.hasNext()) dateAsString = reader.nextString()
        if (dateAsString.length < 4) return null
        val year = dateAsString.substring(0, 4).toInt()
        return GregorianCalendar(year, 1, 1).time
    }
}