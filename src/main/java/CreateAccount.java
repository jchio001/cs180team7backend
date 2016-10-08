import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by jman0_000 on 10/7/2016.
 */
public class CreateAccount {
    public static void createAccount(HttpServletRequest req, HttpServletResponse resp, Connection conn,
                              JSONObject reqBody) throws IOException, JSONException{
        try {
            String createSQL = "INSERT INTO account(account__email, account__password, account__active, account__code) " +
                "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(createSQL);
            stmt.setString(1, reqBody.getString(Constants.EMAIL_KEY));
            stmt.setString(2, reqBody.getString(Constants.PASSWORD_KEY));
            stmt.setBoolean(3, false);
            Random random = new Random();
            stmt.setInt(4, random.nextInt(8999) + 1000);
            stmt.executeQuery();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int id = keys.getInt(1);
                resp.getWriter().print(id);
            } else {
                throw new SQLException(Constants.CREATE_ACCOUNT_ERROR);
            }

        } catch (SQLException e) {
            resp.setStatus(Constants.INTERNAL_SERVER_ERROR);
            resp.getWriter().print(e.getStackTrace());
        }
    }
}
