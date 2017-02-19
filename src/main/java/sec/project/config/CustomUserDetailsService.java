package sec.project.config;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.h2.tools.RunScript;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    Connection connection = null;

    @PostConstruct
    public void init() {
        // Open connection to a database
        String databaseAddress = "jdbc:h2:file:./database";

        try {
            connection = DriverManager.getConnection(databaseAddress, "sa", "");
        } catch (SQLException ex) {
            Logger.getLogger(CustomUserDetailsService.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            // If database has not yet been created, insert content
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
            RunScript.execute(connection, new FileReader("sql/database-import.sql"));
            System.out.println("Added default content into DB");
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ResultSet resultSet;
        String password;
        String user;

        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM User WHERE name='" + username + "'");
            
            //Fix for the injection vulnerability:
            //String query = "SELECT * FROM User WHERE name = ?";
            //PreparedStatement pstmt = connection.prepareStatement(query);
            //pstmt.setString(1, username);
            //resultSet = pstmt.executeQuery();
            
            resultSet.next();
            user = resultSet.getString("name");
            password = resultSet.getString("password");


        } catch (SQLException ex) {
            Logger.getLogger(CustomUserDetailsService.class.getName()).log(Level.SEVERE, null, ex);
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user,
                password,
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
