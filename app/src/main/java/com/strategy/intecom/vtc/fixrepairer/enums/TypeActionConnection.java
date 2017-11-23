package com.strategy.intecom.vtc.fixrepairer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/2/16.
 */
public enum TypeActionConnection {

    TYPE_ACTION(0),

    TYPE_ACTION_LOGIN(1),

    TYPE_ACTION_REGISTER(2),

    TYPE_ACTION_FORGOT_PASSWORD(3),

    TYPE_ACTION_GET_LIST_COMMONINFO(4),

    TYPE_ACTION_GET_LIST_FIELD(5),

    TYPE_ACTION_GET_ORDER_LIST(6),

    TYPE_ACTION_ORDER_DELETE(7),

    TYPE_ACTION_HIRE_LIST(8),

    TYPE_ACTION_CONFIRM_CODE(9),

    TYPE_ACTION_CONFIRM_PASSCODE(10),

    TYPE_ACTION_GET_PASSCODE(11),

    TYPE_ACTION_ORDER_ACCEPT(12),

    TYPE_ACTION_ORDER_UPDATE_FINISHED(13),

    TYPE_ACTION_ORDER_UPDATE_CANCEL(14),

    TYPE_ACTION_ORDER_UPDATE_WORKING(15),

    TYPE_ACTION_ORDER_UPDATE_COMING(16),

    TYPE_ACTION_UPDATE_IS_WORKING(17),

    TYPE_ACTION_UPDATE_AGENCY(18),

    TYPE_ACTION_SEARCH(19),

    TYPE_ACTION_SOCKET_ORDER_OFFER(20),

    TYPE_ACTION_SOCKET_ORDER_ACCEPT(21),

    TYPE_ACTION_SOCKET_RECEIVE_LOCATION(22),

    TYPE_ACTION_ORDER_GET_DETAIL(23),

    TYPE_ACTION_MESSAGE_READ(24),

    TYPE_ACTION_NOTIFICATION_LIST(25),

    TYPE_ACTION_SEND_FEEDBACK(26),

    TYPE_ACTION_LOGOUT(27),

    TYPE_ACTION_CHANGE_PASSWORD(28),

    TYPE_ACTION_UPLOAD_AVATAR(28),

    TYPE_ACTION_GET_COUNT_SKILL(29),

    TYPE_ACTION_CHECK_PHONE_NUM(30);

    private static final Map<Integer, TypeActionConnection> typesByValue = new HashMap<>();

    private final int valuesConnectionType;

    TypeActionConnection(int value) {
        this.valuesConnectionType = value;
    }

    public int getValuesTypeDialog() {
        return valuesConnectionType;
    }

    static {
        for (TypeActionConnection type : TypeActionConnection.values()) {
            typesByValue.put(type.valuesConnectionType, type);
        }
    }

    public static TypeActionConnection forValue(int value) {
        TypeActionConnection type = typesByValue.get(value);
        if (type == null)
            return TypeActionConnection.TYPE_ACTION;
        return typesByValue.get(value);
    }
}
