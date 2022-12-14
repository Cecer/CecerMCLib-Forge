package com.cecer1.projects.mc.cecermclib.forge.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonHelper {

    public static boolean hasString(JsonObject object, String element) {
        return hasPrimitive(object, element) && object.getAsJsonPrimitive(element).isString();
    }

    public static boolean isString(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    public static boolean hasNumber(JsonObject object, String element) {
        return hasPrimitive(object, element) && object.getAsJsonPrimitive(element).isNumber();
    }

    public static boolean isNumber(JsonElement element) {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
    }

    public static boolean hasBoolean(JsonObject object, String element) {
        return hasPrimitive(object, element) && object.getAsJsonPrimitive(element).isBoolean();
    }

    public static boolean isBoolean(JsonElement object) {
        return object.isJsonPrimitive() && object.getAsJsonPrimitive().isBoolean();
    }

    public static boolean hasArray(JsonObject object, String element) {
        return hasElement(object, element) && object.get(element).isJsonArray();
    }

    public static boolean hasJsonObject(JsonObject object, String element) {
        return hasElement(object, element) && object.get(element).isJsonObject();
    }

    public static boolean hasPrimitive(JsonObject object, String element) {
        return hasElement(object, element) && object.get(element).isJsonPrimitive();
    }

    public static boolean hasElement(JsonObject object, String element) {
        if (object == null) {
            return false;
        } else {
            return object.get(element) != null;
        }
    }

    public static String asString(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a string, was " + getType(element));
        }
    }

    public static String getString(JsonObject object, String element) {
        if (object.has(element)) {
            return asString(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a string");
        }
    }

    public static String getString(JsonObject object, String element, String defaultStr) {
        return object.has(element) ? asString(object.get(element), element) : defaultStr;
    }

    public static boolean asBoolean(JsonElement element, String name) {
        if (element.isJsonPrimitive()) {
            return element.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Boolean, was " + getType(element));
        }
    }

    public static boolean getBoolean(JsonObject object, String element) {
        if (object.has(element)) {
            return asBoolean(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Boolean");
        }
    }

    public static boolean getBoolean(JsonObject object, String element, boolean defaultBoolean) {
        return object.has(element) ? asBoolean(object.get(element), element) : defaultBoolean;
    }

    public static double asDouble(JsonElement object, String name) {
        if (object.isJsonPrimitive() && object.getAsJsonPrimitive().isNumber()) {
            return object.getAsDouble();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Double, was " + getType(object));
        }
    }

    public static double getDouble(JsonObject object, String element) {
        if (object.has(element)) {
            return asDouble(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Double");
        }
    }

    public static double getDouble(JsonObject object, String element, double defaultDouble) {
        return object.has(element) ? asDouble(object.get(element), element) : defaultDouble;
    }

    public static float asFloat(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Float, was " + getType(element));
        }
    }

    public static float getFloat(JsonObject object, String element) {
        if (object.has(element)) {
            return asFloat(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Float");
        }
    }

    public static float getFloat(JsonObject object, String element, float defaultFloat) {
        return object.has(element) ? asFloat(object.get(element), element) : defaultFloat;
    }

    public static long asLong(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsLong();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Long, was " + getType(element));
        }
    }

    public static long getLong(JsonObject object, String name) {
        if (object.has(name)) {
            return asLong(object.get(name), name);
        } else {
            throw new JsonSyntaxException("Missing " + name + ", expected to find a Long");
        }
    }

    public static long getLong(JsonObject object, String element, long defaultLong) {
        return object.has(element) ? asLong(object.get(element), element) : defaultLong;
    }

    public static int asInt(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Int, was " + getType(element));
        }
    }

    public static int getInt(JsonObject object, String element) {
        if (object.has(element)) {
            return asInt(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Int");
        }
    }

    public static int getInt(JsonObject object, String element, int defaultInt) {
        return object.has(element) ? asInt(object.get(element), element) : defaultInt;
    }

    public static byte asByte(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsByte();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Byte, was " + getType(element));
        }
    }

    public static byte getByte(JsonObject object, String element) {
        if (object.has(element)) {
            return asByte(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Byte");
        }
    }

    public static byte getByte(JsonObject object, String element, byte defaultByte) {
        return object.has(element) ? asByte(object.get(element), element) : defaultByte;
    }

    public static char asChar(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsCharacter();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Character, was " + getType(element));
        }
    }

    public static char getChar(JsonObject object, String element) {
        if (object.has(element)) {
            return asChar(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Character");
        }
    }

    public static char getChar(JsonObject object, String element, char defaultChar) {
        return object.has(element) ? asChar(object.get(element), element) : defaultChar;
    }

    public static BigDecimal asBigDecimal(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsBigDecimal();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a BigDecimal, was " + getType(element));
        }
    }

    public static BigDecimal getBigDecimal(JsonObject object, String element) {
        if (object.has(element)) {
            return asBigDecimal(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a BigDecimal");
        }
    }

    public static BigDecimal getBigDecimal(JsonObject object, String element, BigDecimal defaultBigDecimal) {
        return object.has(element) ? asBigDecimal(object.get(element), element) : defaultBigDecimal;
    }

    public static BigInteger asBigInteger(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsBigInteger();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a BigInteger, was " + getType(element));
        }
    }

    public static BigInteger getBigInteger(JsonObject object, String element) {
        if (object.has(element)) {
            return asBigInteger(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a BigInteger");
        }
    }

    public static BigInteger getBigInteger(JsonObject object, String element, BigInteger defaultBigInteger) {
        return object.has(element) ? asBigInteger(object.get(element), element) : defaultBigInteger;
    }

    public static short asShort(JsonElement element, String name) {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsShort();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a Short, was " + getType(element));
        }
    }

    public static short getShort(JsonObject object, String element) {
        if (object.has(element)) {
            return asShort(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a Short");
        }
    }

    public static short getShort(JsonObject object, String element, short defaultShort) {
        return object.has(element) ? asShort(object.get(element), element) : defaultShort;
    }

    public static JsonObject asObject(JsonElement element, String name) {
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a JsonObject, was " + getType(element));
        }
    }

    public static JsonObject getObject(JsonObject object, String element) {
        if (object.has(element)) {
            return asObject(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a JsonObject");
        }
    }

    public static JsonObject getObject(JsonObject object, String element, JsonObject defaultObject) {
        return object.has(element) ? asObject(object.get(element), element) : defaultObject;
    }

    public static JsonArray asArray(JsonElement element, String name) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + name + " to be a JsonArray, was " + getType(element));
        }
    }

    public static JsonArray getArray(JsonObject object, String element) {
        if (object.has(element)) {
            return asArray(object.get(element), element);
        } else {
            throw new JsonSyntaxException("Missing " + element + ", expected to find a JsonArray");
        }
    }

    public static JsonArray getArray(JsonObject object, String name, JsonArray defaultArray) {
        return object.has(name) ? asArray(object.get(name), name) : defaultArray;
    }

    public static <T> T deserialize(JsonElement element, String name, JsonDeserializationContext context, Class<? extends T> type) {
        if (element != null) {
            return context.deserialize(element, type);
        } else {
            throw new JsonSyntaxException("Missing " + name);
        }
    }

    public static <T> T deserialize(JsonObject object, String element, JsonDeserializationContext context, Class<? extends T> type) {
        if (object.has(element)) {
            return deserialize(object.get(element), element, context, type);
        } else {
            throw new JsonSyntaxException("Missing " + element);
        }
    }

    public static <T> T deserialize(JsonObject object, String element, T defaultValue, JsonDeserializationContext context, Class<? extends T> type) {
        return object.has(element) ? deserialize(object.get(element), element, context, type) : defaultValue;
    }

    public static String getType(JsonElement element) {
        String string = StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);
        if (element == null) {
            return "null (missing)";
        } else if (element.isJsonNull()) {
            return "null (json)";
        } else if (element.isJsonArray()) {
            return "an array (" + string + ")";
        } else if (element.isJsonObject()) {
            return "an object (" + string + ")";
        } else {
            if (element.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = element.getAsJsonPrimitive();
                if (jsonPrimitive.isNumber()) {
                    return "a number (" + string + ")";
                }

                if (jsonPrimitive.isBoolean()) {
                    return "a boolean (" + string + ")";
                }
            }

            return string;
        }
    }

    public static <T> T deserialize(Gson gson, Reader reader, Class<T> type, boolean lenient) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(type).read(jsonReader);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }

    public static <T> T deserialize(Gson gson, Reader reader, TypeToken<T> typeToken, boolean lenient) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            jsonReader.setLenient(lenient);
            return gson.getAdapter(typeToken).read(jsonReader);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }
}