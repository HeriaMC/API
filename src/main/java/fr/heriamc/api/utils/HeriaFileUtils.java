package fr.starblade.api.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public final class SFileUtils {

    public static <T> T fromJsonFile(File file, Gson gson, Class<T> classOfT) {
        if(!file.exists()) return null;
        try (FileReader reader = new FileReader(file, StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, classOfT);
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

}
