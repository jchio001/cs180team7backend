import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jman0_000 on 10/7/2016.
 */
public class CreateAccount {
    public static void createAccount(HttpServletRequest req, HttpServletResponse resp, Connection conn,
                              JSONObject reqBody) throws IOException, JSONException, TwilioRestException{
        try {
            String phoneNum = reqBody.getString(Constants.NUMBER_KEY);

            try {
                if (phoneNum.length() != 10) {
                    throw new JSONException(Constants.NUMBER_TOO_SHORT);
                }

                int tmp = Integer.parseInt(phoneNum);
            } catch (NumberFormatException e) {
                throw new JSONException(Constants.INVALID_NUMBER);
            }

            String createSQL = "INSERT INTO account(account__phone_number, account__password, account__active, account__code) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, phoneNum);
            stmt.setString(2, reqBody.getString(Constants.PASSWORD_KEY));
            stmt.setBoolean(3, false);

            Random random = new Random();
            int code = random.nextInt(9000) + 1000;
            stmt.setInt(4, code);

            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                resp.getWriter().print(id);
                sendText(resp, phoneNum, code);
            } else {
                throw new SQLException(Constants.CREATE_ACCOUNT_ERROR);
            }

        } catch (SQLException e) {
            resp.setStatus(Constants.INTERNAL_SERVER_ERROR);
            resp.getWriter().print(Main.getStackTrace(e));
        }
    }

    public static final void sendText(HttpServletResponse resp, String to, int code)
        throws TwilioRestException{
        TwilioRestClient client = new TwilioRestClient(Constants.ACCOUNT_SID, Constants.AUTH_TOKEN);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Constants.TO, to));
        params.add(new BasicNameValuePair(Constants.FROM, Constants.FROM_NUMBER));
        params.add(new BasicNameValuePair(Constants.BODY, Constants.MSG + Integer.toString(code)));

        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message msg = messageFactory.create(params);
    }
}
