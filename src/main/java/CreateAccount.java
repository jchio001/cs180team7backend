import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created by jman0_000 on 10/7/2016.
 */
public class CreateAccount {
    public static void createAccount(HttpServletRequest req, HttpServletResponse resp, Connection conn,
                              JSONObject reqBody) throws IOException, JSONException{
        try {
            String email = reqBody.getString(Constants.EMAIL_KEY);

            String createSQL = "INSERT INTO account(account__email, account__password, account__active, account__code) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(createSQL, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, email);
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
                sendEmail(resp, email, code);
            } else {
                throw new SQLException(Constants.CREATE_ACCOUNT_ERROR);
            }

        } catch (SQLException e) {
            resp.setStatus(Constants.INTERNAL_SERVER_ERROR);
            resp.getWriter().print(Main.getStackTrace(e));
        }
    }

    public static final void sendEmail(HttpServletResponse resp, String to, int code)
        throws IOException {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", "heroku");
            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(Constants.ACTIVATE_EMAIL_TITLE);
            message.setText(Constants.ACTIVATE_EMAIL_BASE);
            Transport.send(message);
        } catch (Exception e) {
            resp.setStatus(Constants.INTERNAL_SERVER_ERROR);
            resp.getWriter().print(Main.getStackTrace(e));
        }
    }
}
