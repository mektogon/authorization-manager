package ru.dorofeev.shareddatabase.model.enums;

/**
 * Статус аккаунта пользователя
 */
public enum UserStatusType {

    /**
     * Статус 'Создан'
     */
    CREATED,

    /**
     * Статус 'Удален'
     */
    DELETED,

    /**
     * Статус 'Активный'
     */
    ACTIVE,

    /**
     * Статус 'Не активный'
     */
    NOTACTIVE,

    /**
     * Статус 'Забанен'
     */
    BANNED,

    /**
     * Статус 'Временный' - 'Гость'
     */
    TEMPORARY
}
