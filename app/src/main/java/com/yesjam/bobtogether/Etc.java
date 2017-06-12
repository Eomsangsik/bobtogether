package com.yesjam.bobtogether;

import com.yesjam.bobtogether.Adapter.MainFirstRecyclerAdapter;
import com.yesjam.bobtogether.Adapter.MainSecondRecyclerAdapter;

import java.util.ArrayList;


public class Etc {

    public static Boolean running = false;
    public static final int PUSH_NOTIFICATION_REQUEST_CODE = 0;
    public static final int MAKE_BOB_REQUEST_CODE = 1;
    public static final int CONDITION_REQUEST_CODE = 2;
    public static final int REQ_CODE_SELECT_IMAGE = 6;

    public static final int TRUE = 3;
    public static final int FALSE = 4;
    public static final int YET = 5;

    private static final String IP = "192.168.1.57";
    private static final String SERVER_URL = "http://" + IP + ":3000";
    public static final String SIGN_UP = SERVER_URL + "/signUp";
    public static final String MAKE_BOB = SERVER_URL + "/makeBob";
    public static final String CALL_BOBS = SERVER_URL + "/callBobs";
    public static final String CALL_CHATS = SERVER_URL + "/callChats";
    public static final String JOIN_BOB = SERVER_URL + "/joinBob";
    public static final String JOIN_BOB_CHECK = SERVER_URL + "/joinBobCheck";
    public static final String FULL_ROOM_CHECK = SERVER_URL + "/fullRoomCheck";
    public static final String CHAT_WAIT_ROOM_INFO = SERVER_URL + "/chatWaitRoomInfo";

    public static final String IMAGE_FOOD = SERVER_URL;

    public static final String GOOGLE_CLIENT_ID = "642817675133-emldf8n2dedi329vts7sl39e53ttj6ok.apps.googleusercontent.com";


    public final static String PUBNUB_PUBLISH_KEY = "pub-c-b6867b1e-282e-43a5-a887-06d4d9075462";
    public final static String PUBNUB_SUBSCRIBE_KEY = "sub-c-43cc839e-80b0-11e6-82db-0619f8945a4f";


    public static ArrayList<MainFirstRecyclerAdapter.firstItem> firstContentsList = new ArrayList<>();
    public static ArrayList<MainSecondRecyclerAdapter.secondItem> secondContentsList = new ArrayList<>();

    public static ArrayList<Integer> bobNumber = new ArrayList<>();
    public static ArrayList<String> chatRoomUid = new ArrayList<>();

    public static int chatPagingNum = 0;
    public static int bobPagingNum = 0;

    public static boolean bobPagingBool = false;
    public static boolean chatPagingBool = false;

}
