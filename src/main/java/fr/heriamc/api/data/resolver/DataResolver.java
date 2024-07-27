package fr.heriamc.api.data.resolver;

import org.bson.Document;

import java.util.Set;

public final class DataResolver {

    public static Document resolveJson(Defaultable<?> defaultable, Document actualDocument){
        Document defaultDocument = Document.parse(defaultable.getDefault().toJson());

        if(defaultDocument.size() > actualDocument.size()){
            Set<String> allKeys = defaultDocument.keySet();

            for (String key : allKeys) {
                if (!actualDocument.containsKey(key)) {
                    actualDocument.append(key, defaultDocument.get(key));
                }
            }
        }

        return actualDocument;
    }
}
