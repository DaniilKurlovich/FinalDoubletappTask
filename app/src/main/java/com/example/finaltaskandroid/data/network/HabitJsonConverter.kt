package com.example.finaltaskandroid.data.network

import com.example.finaltaskandroid.data.models.Habit
import com.example.finaltaskandroid.data.models.HabitUID
import com.google.gson.*
import java.lang.reflect.Type


class HabitJsonSerializer: JsonSerializer<Habit> {
    override fun serialize(
        src: Habit?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject().apply {
        if (src != null) {
            if (src.uid != null) {
                addProperty("uid", src.uid)
            }

            addProperty("count", src.count)
            addProperty("frequency", src.frequency)
            addProperty("description", src.description)
            addProperty("title", src.title)
            addProperty("type", src.type)
            addProperty("priority", src.priority)
            addProperty("date", System.currentTimeMillis().toInt())
        }
    }
}


class HabitUidDeserializer: JsonDeserializer<HabitUID> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitUID = HabitUID(
        json.asJsonObject.get("uid").asString
    )
}


class HabitJsonDeserializer: JsonDeserializer<Habit> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Habit = Habit(
        json.asJsonObject.get("title").asString,
        json.asJsonObject.get("description").asString,
        json.asJsonObject.get("priority").asInt,
        json.asJsonObject.get("type").asInt,
        json.asJsonObject.get("frequency").asInt,
        json.asJsonObject.get("count").asInt,
        json.asJsonObject.get("uid").asString
    )
}