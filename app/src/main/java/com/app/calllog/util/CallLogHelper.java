package com.app.calllog.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.CallLog;

import com.app.calllog.objects.CallerInfo;

import java.sql.Date;
import java.util.ArrayList;

import static android.provider.CallLog.Calls.INCOMING_TYPE;
import static android.provider.CallLog.Calls.MISSED_TYPE;
import static android.provider.CallLog.Calls.OUTGOING_TYPE;
import static android.provider.CallLog.Calls.VOICEMAIL_TYPE;

/**
 * Created by Akash on 08-May-15.
 */
public class CallLogHelper {

    //Call Type Valuse in String
    public static final String kMISSEDCALL_TYPE_STR = "Missed call";
    public static final String kINCOMING_TYPE_STR = "Incomming call";
    public static final String kOUTGOING_TYPE_STR = "Outgoing call";
    public static final String kVOICEMAIN_TYPE_STR = "Outgoing call";

    //Call type Constant
    public static final int kMISSED_TYPE = MISSED_TYPE;
    public static final int kOUTGOING_TYPE = OUTGOING_TYPE;
    public static final int kINCOMING_TYPE = INCOMING_TYPE;
    public static final int kVOICEMAIL_TYPE = VOICEMAIL_TYPE;


    public ArrayList<CallerInfo> readCallLogs(ContentResolver resolver) {

        ArrayList<CallerInfo> callerInfoArrayList = new ArrayList<>();
        Cursor cursor = resolver.query(android.provider.CallLog.Calls.CONTENT_URI, null,
                null, null, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);

        while (cursor.moveToNext()) {
            String callNumber = cursor.getString(cursor
                    .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            callNumber = removeDashFromString(callNumber);
            String callName = cursor
                    .getString(cursor
                            .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));

            long callDate = cursor.getLong(cursor
                    .getColumnIndex(CallLog.Calls.DATE));
//            String callDate = cursor.getString(cursor
//                    .getColumnIndex(android.provider.CallLog.Calls.DATE));
//            SimpleDateFormat formatter = new SimpleDateFormat(
//                    "dd-MMM-yyyy HH:mm");
//            String dateString = formatter.format(new Date(Long
//                    .parseLong(callDate)));


//             String isCallNew = cursor.getString(cursor
//                    .getColumnIndex(android.provider.CallLog.Calls.NEW));

            long callDuration = cursor.getLong(cursor
                    .getColumnIndex(CallLog.Calls.DURATION));
            long isRead = cursor.getLong(cursor
                    .getColumnIndex(CallLog.Calls.IS_READ));
            int callType = cursor.getInt(cursor
                    .getColumnIndex(CallLog.Calls.TYPE));


            callerInfoArrayList.add(new CallerInfo(callName, callNumber, callDate, callDuration, callType, isRead));

        }
        return callerInfoArrayList;
    }

    // Use this function to remove - (dash) from number string or anystring
    public static String removeDashFromString(String str) {

        while (str.contains("-")) {
            str = str.substring(0, str.indexOf('-')) + str.substring(str.indexOf('-') + 1, str.length());
        }
        return str;
    }


    // call this method to get Call type in String
    public static String getCallTypeString(int callType) {
        String callTypestr;

        switch (callType) {
            case kOUTGOING_TYPE:
                callTypestr = kOUTGOING_TYPE_STR;
                break;
            case kINCOMING_TYPE:
                callTypestr = kINCOMING_TYPE_STR;
                break;
            case kMISSED_TYPE:
                callTypestr = kMISSEDCALL_TYPE_STR;
                break;
            case kVOICEMAIL_TYPE:
                callTypestr = kVOICEMAIN_TYPE_STR;
                break;
            default:
                callTypestr = "Unknown";
                break;
        }

        return callTypestr;
    }

    //this method is used for Convert Millies into Hours and minutes
    public static String getDuration(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        if (hours < 1)
            return minutes + ":" + seconds;
        return hours + ":" + minutes + ":" + seconds;
    }


    //call this method to get Date In String from millies
    public static String getDateTime(long milliseconds) {
        Date date = new Date(milliseconds);
        //return DateFormat.getDateTimeInstance().format(new Date());
        return date.toLocaleString();
    }
}
