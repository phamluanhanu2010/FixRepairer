package com.strategy.intecom.vtc.fixrepairer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public enum TypeShowDialog {

    TYPE_SHOW_MESSAGE_INFO(0),

    TYPE_SHOW_MESSAGE_CONFIRM(1),

    TYPE_SHOW_MESSAGE_INPUT(2),

    TYPE_SHOW_MESSAGE_NEW_JOB_FAST(3),

    TYPE_SHOW_CHOICE_CITY_DISTRICT(4),

    TYPE_SHOW_SHORTED_BY(5),

    TYPE_SHOW_CHOICE_IMAGE(6),

    TYPE_SHOW_CHOICE_SKILL(7),

    TYPE_SHOW_ENABLE_NETWORK(8),

    TYPE_SHOW_MESSAGE_NEW_JOB_NORMAL(9);

    private static final Map<Integer, TypeShowDialog> typesByValue = new HashMap<>();

    private final int valuesDialogType;

    TypeShowDialog(int value) {
        this.valuesDialogType = value;
    }

    public int getValuesTypeDialog() {
        return valuesDialogType;
    }

    static {
        for (TypeShowDialog type : TypeShowDialog.values()) {
            typesByValue.put(type.valuesDialogType, type);
        }
    }

    public static TypeShowDialog forValue(int value) {
        TypeShowDialog type = typesByValue.get(value);
        if (type == null)
            return TypeShowDialog.TYPE_SHOW_MESSAGE_INFO;
        return typesByValue.get(value);
    }

}
