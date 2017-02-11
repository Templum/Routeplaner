package de.templum.routplaner.util;

import android.content.Context;

/**
 * Created by simon on 10.02.2017.
 */

public class Messages {
    public static final String NO_RESULT_FOUND = "NO RESULT FOUND";


    public class MessageFactory{
        private Context mContex;

        public MessageFactory(final Context ctx){
            mContex = ctx;
        }


        public String buildMessage(final String message){
            switch (message){
                case NO_RESULT_FOUND:
                    return "TODO";
                default:
                    return "";
            }
        }
    }
}
