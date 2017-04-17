package com.defaultapps.translator.utils;


public class Global {

    public static final String SOURCE_OR_TARGET = "source_or_target";

    public static final String LANG_CHANGED = "language_changed";
    public static final String TRANSLATE_UPDATE = "translate_update";
    public static final String HISTORY_UPDATE = "history_update";
    public static final String FAVORITES_UPDATE = "favorites_update";
    public static final String SELECT_TRANSLATE_FRAGMENT = "select_translate";

    private Global() {
        throw new AssertionError("This class should not have instances.");
    }
}
