public class Constants {
    //Twilio account information
    public static final String ACCOUNT_SID = "AC181994597bdbbc2f83c85fe1e8cc287a";
    public static final String AUTH_TOKEN = "b52d5d02553af74de5945d78c83f905c";

    //Twilio constants
    public static final String TO = "To";
    public static final String FROM = "From";
    public static final String BODY = "Body";
    public static final String FROM_NUMBER = "6502851269";

    //Twilio msg
    public static final String MSG = "Your verification number is \n";

    //status codes
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;

    //keys
    public static final String NUMBER_KEY = "phone_number";
    public static final String PASSWORD_KEY = "password";

    //email
    public static final String FROM_EMAIL = "CS180Team7";
    public static final String ACTIVATE_EMAIL_TITLE = "<Placeholder> Account Activation";
    public static final String ACTIVATE_EMAIL_BASE = "Please click on the following link to activate your account:\n";

    //error msgs
    public static final String NUMBER_TOO_SHORT = "Phone number too short.";
    public static final String INVALID_NUMBER = "Invalid phone number.";
    public static final String CREATE_ACCOUNT_ERROR = "Error in account creation.";
}
