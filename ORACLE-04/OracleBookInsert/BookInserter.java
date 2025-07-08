import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BookInserter {
    public static void main(String[] args) {
        // Oracle bağlantı bilgileri
        String jdbcUrl = "jdbc:oracle:thin:@//34.173.93.147:1521/XEPDB1";
        String username = "SYS AS SYSDBA"; // Veya kendi kullanıcın
        String password = "ORACLE"; // Şifren

        // SQL insert sorgusu
        String insertSql = "INSERT INTO BOOK (ID, NAME, ISBN) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            // 100 kayıt ekle
            for (int i = 1; i <= 100; i++) {
                pstmt.setInt(1, i);
                pstmt.setString(2, "Book " + i);
                pstmt.setString(3, UUID.randomUUID().toString().substring(0, 13));
                pstmt.executeUpdate();
            }

            // NOT: Auto-commit zaten açık, conn.commit() YAZMA!
            System.out.println("100 kayıt başarıyla eklendi.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
