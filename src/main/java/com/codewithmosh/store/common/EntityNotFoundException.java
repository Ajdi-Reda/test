package com.codewithmosh.store.common;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Integer entityId, String entityName) {
        super("The " + entityName + " with id " + entityId + " was not found");
    }
}
