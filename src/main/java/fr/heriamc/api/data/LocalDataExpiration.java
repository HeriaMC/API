package fr.starblade.api.data;

import java.util.concurrent.TimeUnit;

public record LocalDataExpiration(long duration, TimeUnit unit) {
}
