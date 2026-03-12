import java.sql.*;

public class MainDB {
    String url = "jdbc:sqlite:fiche_personnages.db"; 
    Connection conn;

    void connect() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    void createDatabase()
            {
                try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS users ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " password TEXT"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'users' prête !");
            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS personnages ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " race TEXT NOT NULL,"
                            + " class TEXT NOT NULL,"
                            + " niveau INTEGER NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'personnages' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS fiches_personnages ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " biographie TEXT NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'fiches_personnages' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS statistiques ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " valeur TEXT NOT NULL,"
                            + " x INTEGER NOT NULL,"
                            + " y INTEGER NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'statistiques' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS competence ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " description TEXT NOT NULL,"
                            + " x INTEGER NOT NULL,"
                            + " y INTEGER NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'competence' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS equipement ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " description TEXT NOT NULL,"
                            + " x INTEGER NOT NULL,"
                            + " y INTEGER NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'equipement' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }

            try (Connection conn = DriverManager.getConnection(url);

                Statement stmt = conn.createStatement()) {

                System.out.println("✅ Connecté à SQLite !");

                String sqlCreate = "CREATE TABLE IF NOT EXISTS portrait ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " cheminimage TEXT NOT NULL,"
                            + " x INTEGER NOT NULL,"
                            + " y INTEGER NOT NULL"
                + ");";
                stmt.execute(sqlCreate);
                System.out.println("✅ Table 'portrait' prête !");

            } catch (SQLException e) {
                System.out.println("Error : " + e.getMessage());
            }
        }


    void insertIntoUsers(String nom, String password) {
            String sql = "INSERT INTO users(nom, password) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nom);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                System.out.println("User ajoute !");
            } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    // String sqlSelect = "SELECT id, nom, password FROM users";
                // ResultSet rs = stmt.executeQuery(sqlSelect);

                // System.out.println("\n📜 Liste des users dans la base :");
                // while (rs.next()) {
                //     System.out.println("ID: " + rs.getInt("id") + 
                //                     " | Nom: " + rs.getString("nom") + 
                //                     " | password: " + rs.getString("password"));
                //}
}

    void insertIntoPersonnages(String nom, String race, String classe, int niveau) {
        String sql = "INSERT INTO personnages(nom, race, class, niveau) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, race);
            pstmt.setString(3, classe);
            pstmt.setInt(4, niveau);
            pstmt.executeUpdate();
            System.out.println("Personnage ajoute !");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void insertIntoStatistiques(int id, String biographie) {
        String sql = "INSERT INTO fiches_personnages(id, biographie) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, biographie);
            pstmt.executeUpdate();
            System.out.println("Personnage ajoute !");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    void insertIntoFichesPersonnages(int id, String nom, int valeur, int x, int y) {
        String sql = "INSERT INTO statistiques(id, nom, valeur, x, y) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, nom);
            pstmt.setInt(3, valeur);
            pstmt.setInt(4, x);
            pstmt.setInt(5, y);
            pstmt.executeUpdate();
            System.out.println("Personnage ajoute !");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    String sqlCreate = "CREATE TABLE IF NOT EXISTS statistiques ("
                            + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " nom TEXT NOT NULL,"
                            + " valeur TEXT NOT NULL,"
                            + " x INTEGER NOT NULL,"
                            + " y INTEGER NOT NULL"
                + ");";

    void selectTest() {
        String sqlSelect = "SELECT id, nom, password FROM users";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlSelect)) {
            System.out.println("\n📜 Liste des users dans la base :");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                " | Nom: " + rs.getString("nom") +
                                " | password: " + rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        MainDB db = new MainDB();
        db.createDatabase();
        try {
            db.connect();
            db.insertIntoUsers("Kaito", "monpass");
            db.insertIntoPersonnages("Aragorn", "Humain", "Guerrier", 10);
            db.selectTest();
        } catch (SQLException e) {
            System.out.println("Erreur connexion: " + e.getMessage());
        }
    }
}