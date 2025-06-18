package PetPals;

import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class Staff extends javax.swing.JFrame {
    
    static Login login = new Login();
    String next;

    public Staff() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        adoptionFormTabClicked();
        tableHeader();
    }
    
    void tableHeader() {
        JTableHeader ptheader = petsTable.getTableHeader();
        ptheader.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
        ptheader.setForeground(new Color(238,134,175));
        
        JTableHeader ahtheader = adoptionHistoryTable.getTableHeader();
        ahtheader.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
        ahtheader.setForeground(new Color(238,134,175));
        
        JTableHeader phtheader = petHistoryTable.getTableHeader();
        phtheader.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
        phtheader.setForeground(new Color(238,134,175));
        
        JTableHeader adhtheader = adopterHistoryTable.getTableHeader();
        adhtheader.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
        adhtheader.setForeground(new Color(238,134,175));
        
        JTableHeader utheader = usersTable.getTableHeader();
        utheader.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 12));
        utheader.setForeground(new Color(238,134,175));
    }
    
    void adoptionFormTabClicked() {
        AdoptionForm.setVisible(true);
        PetsPage.setVisible(false);
        AdoptionsPage.setVisible(false);
        SettingsPage.setVisible(false);
        
        adoptionFormTab.setBackground(new Color(238,134,175));
        petsTab.setBackground(new Color(120,184,198));
        adoptionsTab.setBackground(new Color(120,184,198));
        settingsTab.setBackground(new Color(120,184,198));
    }
    
    void addAdopter() {
        String aFN = aFirstName.getText();
        String aLN = aLastName.getText();
        String aE = aEmail.getText();
        String aP = aPhone.getText();
        String aA = aAddress.getText();
        String petN = afPetName.getText();
        Date date = adoptionDate.getDate();
        
        if(date == null){
             JOptionPane.showMessageDialog(null, "Date is empty");
        }
        else if(aFN.equals("") || aFN.equals("Enter First Name")){
            JOptionPane.showMessageDialog(null, "Adopter First Name is empty.");
        }
        else if(aLN.equals("") || aLN.equals("Enter Last Name")){
            JOptionPane.showMessageDialog(null, "Adopter Last Name is empty.");
        }
        else if(aE.equals("") || aE.equals("Enter Email")){
            JOptionPane.showMessageDialog(null, "Adopter Email is empty.");
        }
        else if(aP.equals("") || aP.equals("Enter Phone")){
            JOptionPane.showMessageDialog(null, "Adopter Phone is empty.");
        }  
        else if(aA.equals("") || aA.equals("Enter Address")){
            JOptionPane.showMessageDialog(null, "Adopter Address is empty.");
        }
        else if(petN.equals("") || petN.equals("Pet Name")){
             JOptionPane.showMessageDialog(null, "No pet chosen.");
        }
        else {
        try {
            String adID = afSearchTFA.getText();
            
            if (adID.equals("") || adID.equals("Enter Adopter ID")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                PreparedStatement ps = Database.connect().prepareStatement("INSERT INTO `adopters_table`(`FirstName`, `LastName`, `Email`, `Phone`, `Address`) VALUES (?,?,?,?,?) ");
                ps.setString(1, aFN);
                ps.setString(2, aLN);
                ps.setString(3, aE);
                ps.setString(4, aP);
                ps.setString(5, aA);
                ps.executeUpdate();
            }
            
        getAdopterID();
        addAdoption();
        petAdopted();
            
        //Set fields to blank
        aFirstName.setText("");
        aLastName.setText("");
        aEmail.setText("");
        aPhone.setText("");
        aAddress.setText("");
        adoptionDate.setDate(null);
        afSearchTFA.setText("");
        afSearchTF.setText("");
        afPetName.setText("Pet Name");
        afSex.setText("Pet Sex");
        afPetType.setText("Type of Pet");
        afBreed.setText("Pet Breed");
        afWeight.setText("Pet Weight");
        afColor.setText("Pet Color");

        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
     void addAdoption() {
        String petID = PetIDL.getText();
        String adopterID = AdopterIDL.getText();
        
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-d");
        String aDate = df.format(adoptionDate.getDate());
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            PreparedStatement ps = Database.connect().prepareStatement("INSERT INTO `adoptions_table`(`PetID`, `AdopterID`, `AdoptionDate`) VALUES (?,?,?) ");
            ps.setString(1, petID);
            ps.setString(2, adopterID);
            ps.setString(3, aDate);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Adoption Record submitted!");
            
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void petAdopted() {
        String pID = PetIDL.getText();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            PreparedStatement ps = Database.connect().prepareStatement("UPDATE `pets_table` SET `Available`='Adopted' WHERE `PetID` = '"+ pID +"'");
            ps.executeUpdate();
        
            showPetsTable();
            
        } catch (SQLException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void afSearch() {
        String afS = afSearchTF.getText();
        
        if(afS.equals("") || afS.equals("Enter Pet ID or Name")){
            JOptionPane.showMessageDialog(null, "Enter Pet ID or Name.");
        } else {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `pets_table` WHERE (`PetName` ='" + afS + "' OR `PetID` ='" + afS + "') AND `Available` = 'Available'");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                afPetName.setText(rs.getString("PetName"));
                afSex.setText(rs.getString("PetSex"));
                afPetType.setText(rs.getString("Species"));
                afBreed.setText(rs.getString("Breed"));
                afWeight.setText(rs.getString("Weight"));
                afColor.setText(rs.getString("Color"));
                getPetID();
            } else {
                JOptionPane.showMessageDialog(null, "Pet not available.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
    void afSearchA() {
        String afSA = afSearchTFA.getText();
        
        if(afSA.equals("") || afSA.equals("Enter Adopter ID")){
            JOptionPane.showMessageDialog(null, "Enter Adopter ID.");
        } else {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `adopters_table` WHERE `AdopterID` ='" + afSA + "'");
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                aFirstName.setText(rs.getString("FirstName"));
                aLastName.setText(rs.getString("LastName"));
                aEmail.setText(rs.getString("Email"));
                aPhone.setText(rs.getString("Phone"));
                aAddress.setText(rs.getString("Address"));
            } else {
                JOptionPane.showMessageDialog(null, "No record of adopter.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
    void getPetID() {
        String afPN = afPetName.getText();
        
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `pets_table` WHERE `PetName` = '"+ afPN +"'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String pID = rs.getString("PetID");
                PetIDL.setText(pID);
            } else {
                UserNameL.setText("PetID not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void getAdopterID() {
        String afFN = aFirstName.getText();
        String afLN = aLastName.getText();
        
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `adopters_table` WHERE `FirstName` = '" + afFN + "' AND `LastName` = '" + afLN + "'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String aID = rs.getString("AdopterID");
                AdopterIDL.setText(aID);
            } else {
                UserNameL.setText("AID not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void petsTabClicked() {
        AdoptionForm.setVisible(false);
        PetsPage.setVisible(true);
        AdoptionsPage.setVisible(false);
        SettingsPage.setVisible(false);
        
        adoptionFormTab.setBackground(new Color(120,184,198));
        petsTab.setBackground(new Color(238,134,175));
        adoptionsTab.setBackground(new Color(120,184,198));
        settingsTab.setBackground(new Color(120,184,198));
        
        showPetsTable();
    }
    
    void showPetsTable() {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `pets_table` WHERE `Available` = 'Available'");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel tm = (DefaultTableModel) petsTable.getModel();
            tm.setRowCount(0);
            
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("PetID"));
                vector.add(rs.getString("PetName"));
                vector.add(rs.getString("PetSex"));
                vector.add(rs.getString("Species"));
                vector.add(rs.getString("Breed"));
                vector.add(rs.getString("Weight"));
                vector.add(rs.getString("Color"));
                vector.add(rs.getString("Available"));
                tm.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void adoptionsTabClicked(){
        AdoptionForm.setVisible(false);
        PetsPage.setVisible(false);
        AdoptionsPage.setVisible(true);
        SettingsPage.setVisible(false);
        
        adoptionFormTab.setBackground(new Color(120,184,198));
        petsTab.setBackground(new Color(120,184,198));
        adoptionsTab.setBackground(new Color(238,134,175));
        settingsTab.setBackground(new Color(120,184,198));
        
        adoptionHistoryTabClicked();
    }
    
    void adoptionHistoryTabClicked() {
        adoptionHistoryPage.setVisible(true);
        petHistoryPage.setVisible(false);
        adopterHistoryPage.setVisible(false);
        
        adoptionHistoryB.setBackground(new Color(251,203,227));
        petHistoryB.setBackground(new Color(238,134,175));
        adopterHistoryB.setBackground(new Color(238,134,175));
        
        adoptionHistoryL.setForeground(new Color(238,134,175));
        petHistoryL.setForeground(Color.WHITE);
        adopterHistoryL.setForeground(Color.WHITE);
        
        showAdoptionHistory();
    }
    
    void showAdoptionHistory() {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `adoptions_table`");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel tm = (DefaultTableModel) adoptionHistoryTable.getModel();
            tm.setRowCount(0);
            
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("PetID"));
                vector.add(rs.getString("AdopterID"));
                vector.add(rs.getString("AdoptionDate"));
                tm.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void showPetHistory() {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `pets_table` WHERE `Available` = 'Adopted'");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel tm = (DefaultTableModel) petHistoryTable.getModel();
            tm.setRowCount(0);
            
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("PetID"));
                vector.add(rs.getString("PetName"));
                vector.add(rs.getString("PetSex"));
                vector.add(rs.getString("Species"));
                vector.add(rs.getString("Breed"));
                vector.add(rs.getString("Weight"));
                vector.add(rs.getString("Color"));
                vector.add(rs.getString("Available"));
                tm.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void showAdopterHistory() {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `adopters_table`");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel tm = (DefaultTableModel) adopterHistoryTable.getModel();
            tm.setRowCount(0);
            
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("AdopterID"));
                vector.add(rs.getString("FirstName"));
                vector.add(rs.getString("LastName"));
                vector.add(rs.getString("Email"));
                vector.add(rs.getString("Phone"));
                vector.add(rs.getString("Address"));
                tm.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
    void settingsTabClicked() {
        AdoptionForm.setVisible(false);
        PetsPage.setVisible(false);
        AdoptionsPage.setVisible(false);
        SettingsPage.setVisible(true);
        
        adoptionFormTab.setBackground(new Color(120,184,198));
        petsTab.setBackground(new Color(120,184,198));
        adoptionsTab.setBackground(new Color(120,184,198));
        settingsTab.setBackground(new Color(238,134,175));
        
        editProfileTabClicked();
    }
    
    void displayUserName() {
        login.info(next);
        String userID = login.userId;
        
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `UserID` = '"+ userID +"'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String firstn = rs.getString("UserFirstName");
                String lastn = rs.getString("UserLastName");
                UserNameL.setText((firstn + " " + lastn).toUpperCase());
                UserNameL1.setText((firstn + " " + lastn).toUpperCase());

            } else {
                UserNameL.setText("Name not found");
                UserNameL1.setText("Name not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void displayUsername() {
        login.info(next);
        String userID = login.userId;
        
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `UserID` = '"+ userID +"'");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String usern = rs.getString("Username");
                usernameL.setText(usern);
                usernameL1.setText(usern);

            } else {
                usernameL.setText("Username not found");
                usernameL1.setText("Username not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void editProfileTabClicked() {
        editProfilePage.setVisible(true);
        changePasswordPage.setVisible(false);
        viewUsersPage.setVisible(false);
        
        editProfileTab.setBackground(new Color(251,203,227));
        changePasswordTab.setBackground(Color.WHITE);
        viewUsersTab.setBackground(Color.WHITE);
        
        displayUserName();
        displayUsername();
    }
    
    void editUserProfile() {
    login.info(next);
    String userID = login.userId;
    
    String uFN = userFNTF.getText();
    String uLN = userLNTF.getText();
    String uUN = userUNTF.getText();

    // Validate input fields
    if(uFN.equals("") && uLN.equals("") && uUN.equals("")) {
        JOptionPane.showMessageDialog(null, "No fields to update.");
        return;
    }

    try {
        // Retrieve current user data
        PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `UserID` = ?");
        ps.setString(1, userID);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            String currentFN = rs.getString("UserFirstName");
            String currentLN = rs.getString("UserLastName");
            String currentUN = rs.getString("Username");
            
            if (!uFN.equals("") && !uFN.equals("Enter New First Name")) {
                currentFN = uFN;
            }
            if (!uLN.equals("") && !uLN.equals("Enter New Last Name")) {
                currentLN = uLN;
            }
            if (!uUN.equals("") && !uUN.equals("Enter New Username")) {
                if (checkUsername(uUN)) {
                    JOptionPane.showMessageDialog(null, "Username already exists!");
                    return;
                }
                currentUN = uUN;
            }

            PreparedStatement ps1 = Database.connect().prepareStatement(
                "UPDATE `users_table` SET `UserFirstName` = ?, `UserLastName` = ?, `Username` = ? WHERE `UserID` = ?");
            ps1.setString(1, currentFN);
            ps1.setString(2, currentLN);
            ps1.setString(3, currentUN);
            ps1.setString(4, userID);
            ps1.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Profile Updated!");

            displayUserName();
            displayUsername();

            userFNTF.setText("");
            userLNTF.setText("");
            userUNTF.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "User not found!");
        }
        
        rs.close();
        ps.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public boolean checkUsername(String username) {
    boolean checkUN = false;
    
    try {
        PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `Username` = ?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            checkUN = true;
        }
        
        rs.close();
        ps.close();
    } catch (SQLException ex) {
        Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
    }
    return checkUN;    
}
    
    void changePasswordTabClicked() {
        editProfilePage.setVisible(false);
        changePasswordPage.setVisible(true);
        viewUsersPage.setVisible(false);
        
        editProfileTab.setBackground(Color.WHITE);
        changePasswordTab.setBackground(new Color(251,203,227));
        viewUsersTab.setBackground(Color.WHITE);
        
        displayUserName();
        displayUsername();
    }
    
    void changePassword() {
        login.info(next);
        String userID = login.userId;
        
        String opw = jPasswordField1.getText();
        String npw = jPasswordField2.getText();
        String cnpw = jPasswordField3.getText();

        if(opw.equals("") || opw.equals("Enter Old Password")){
            JOptionPane.showMessageDialog(null, "Enter Old Password.");
        }
        else if(npw.equals("") || npw.equals("Enter New Password")){
            JOptionPane.showMessageDialog(null, "Enter New Password.");
        }
        else if(cnpw.equals("") || cnpw.equals("Enter New Password")){
            JOptionPane.showMessageDialog(null, "Confirm New Password.");   
        }
        else if (!cnpw.equals(npw)){
            JOptionPane.showMessageDialog(null, "New Passwords do not match.");
        }
        else {
           try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `UserID` = '"+ userID +"'");
            ResultSet rs = ps.executeQuery();
       
            if (checkPassword(opw)) {
                try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                PreparedStatement ps1 = Database.connect().prepareStatement("UPDATE `users_table` SET `Password`='" + npw + "'"
                    + "WHERE UserID = '" + userID + "' AND Password = '" + opw + "'");
                ps1.executeUpdate();
                JOptionPane.showMessageDialog(null, "Password Updated!");

                jPasswordField1.setText("");
                jPasswordField2.setText("");
                jPasswordField3.setText("");
                } 
                catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                } 
                
            } else {
                JOptionPane.showMessageDialog(null, "Old Password is incorrect!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        }
    }
    
    public boolean checkPassword(String password){
        // to check if password is the same
        boolean checkpassword = false;
         
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table` WHERE `Password` = ?");
            ps.setString(1, password);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                checkpassword = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkpassword;    
    }
    
    void viewUsersTabClicked() {
        editProfilePage.setVisible(false);
        changePasswordPage.setVisible(false);
        viewUsersPage.setVisible(true);
        
        editProfileTab.setBackground(Color.WHITE);
        changePasswordTab.setBackground(Color.WHITE);
        viewUsersTab.setBackground(new Color(251,203,227));
        
        showUsers();
    }
    
    void showUsers() {
        try {
            PreparedStatement ps = Database.connect().prepareStatement("SELECT * FROM `users_table`");
            ResultSet rs = ps.executeQuery();

            DefaultTableModel tm = (DefaultTableModel) usersTable.getModel();
            tm.setRowCount(0);
            
            while (rs.next()) {
                Vector vector = new Vector();
                vector.add(rs.getString("UserID"));
                vector.add(rs.getString("UserFirstName"));
                vector.add(rs.getString("UserLastName"));
                vector.add(rs.getString("UserEmail"));
                vector.add(rs.getString("Username"));
                vector.add(rs.getString("Password"));
                vector.add(rs.getString("Role"));
                tm.addRow(vector);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void deleteUser() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        int selectedIndex = usersTable.getSelectedRow();
        try {
            String rn1 = model.getValueAt(selectedIndex, 0).toString();
            int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to delete this user?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                PreparedStatement ps = Database.connect().prepareStatement("DELETE FROM users_table WHERE UserID = ?");
                ps.setString(1, rn1);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "User deleted!");
                
                showUsers();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Navigation = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        adoptionFormTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        petsTab = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        adoptionsTab = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        settingsTab = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        logoutTab = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        Pages = new javax.swing.JPanel();
        AdoptionForm = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        afSearchTF = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        afPetName = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        afSex = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        afPetType = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        afBreed = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        afWeight = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        aAddress = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        aPhone = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        aEmail = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        aFirstName = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        aLastName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        afColor = new javax.swing.JTextField();
        adoptionDate = new com.toedter.calendar.JDateChooser();
        jLabel29 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        afSearch = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        afSubmit = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        AdopterIDL = new javax.swing.JLabel();
        PetIDL = new javax.swing.JLabel();
        afClear = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        afSearchTFA = new javax.swing.JTextField();
        afSearchA = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        PetsPage = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        petsTable = new javax.swing.JTable();
        mpSearchTF = new javax.swing.JTextField();
        AdoptionsPage = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        recordsSearch = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        adoptionHistoryPage = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        adoptionHistoryTable = new javax.swing.JTable();
        petHistoryPage = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        petHistoryTable = new javax.swing.JTable();
        adopterHistoryPage = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        adopterHistoryTable = new javax.swing.JTable();
        adoptionHistoryB = new javax.swing.JPanel();
        adoptionHistoryL = new javax.swing.JLabel();
        petHistoryB = new javax.swing.JPanel();
        petHistoryL = new javax.swing.JLabel();
        adopterHistoryB = new javax.swing.JPanel();
        adopterHistoryL = new javax.swing.JLabel();
        SettingsPage = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        settingsNav = new javax.swing.JPanel();
        editProfileTab = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        changePasswordTab = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        viewUsersTab = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        editProfilePage = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        UserNameL = new javax.swing.JLabel();
        usernameL = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel42 = new javax.swing.JLabel();
        userFNTF = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        userLNTF = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        userUNTF = new javax.swing.JTextField();
        editProfileB = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        changePasswordPage = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        UserNameL1 = new javax.swing.JLabel();
        usernameL1 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jPasswordField3 = new javax.swing.JPasswordField();
        changePassB = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        viewUsersPage = new javax.swing.JPanel();
        userSearch = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        deleteUserp = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PetPals: Pet Adoption Management System");

        Navigation.setBackground(new java.awt.Color(255, 255, 255));
        Navigation.setPreferredSize(new java.awt.Dimension(250, 100));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/PetPalsLogo250.png"))); // NOI18N

        adoptionFormTab.setBackground(new java.awt.Color(120, 184, 198));
        adoptionFormTab.setPreferredSize(new java.awt.Dimension(93, 90));
        adoptionFormTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adoptionFormTabMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ADOPTION FORM");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/form_iconw.png"))); // NOI18N
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout adoptionFormTabLayout = new javax.swing.GroupLayout(adoptionFormTab);
        adoptionFormTab.setLayout(adoptionFormTabLayout);
        adoptionFormTabLayout.setHorizontalGroup(
            adoptionFormTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adoptionFormTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        adoptionFormTabLayout.setVerticalGroup(
            adoptionFormTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adoptionFormTabLayout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        petsTab.setBackground(new java.awt.Color(120, 184, 198));
        petsTab.setPreferredSize(new java.awt.Dimension(34, 90));
        petsTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                petsTabMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("PETS");

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/pets_iconw.png"))); // NOI18N
        jLabel45.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout petsTabLayout = new javax.swing.GroupLayout(petsTab);
        petsTab.setLayout(petsTabLayout);
        petsTabLayout.setHorizontalGroup(
            petsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(petsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        petsTabLayout.setVerticalGroup(
            petsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, petsTabLayout.createSequentialGroup()
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        adoptionsTab.setBackground(new java.awt.Color(120, 184, 198));
        adoptionsTab.setPreferredSize(new java.awt.Dimension(109, 90));
        adoptionsTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adoptionsTabMouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("RECORDS");

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/records_iconw.png"))); // NOI18N
        jLabel52.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout adoptionsTabLayout = new javax.swing.GroupLayout(adoptionsTab);
        adoptionsTab.setLayout(adoptionsTabLayout);
        adoptionsTabLayout.setHorizontalGroup(
            adoptionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adoptionsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        adoptionsTabLayout.setVerticalGroup(
            adoptionsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adoptionsTabLayout.createSequentialGroup()
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTab.setBackground(new java.awt.Color(120, 184, 198));
        settingsTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsTabMouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("SETTINGS");

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/settings_iconw.png"))); // NOI18N
        jLabel53.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout settingsTabLayout = new javax.swing.GroupLayout(settingsTab);
        settingsTab.setLayout(settingsTabLayout);
        settingsTabLayout.setHorizontalGroup(
            settingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        settingsTabLayout.setVerticalGroup(
            settingsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, settingsTabLayout.createSequentialGroup()
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logoutTab.setBackground(new java.awt.Color(120, 184, 198));
        logoutTab.setPreferredSize(new java.awt.Dimension(0, 90));
        logoutTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutTabMouseClicked(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("LOG OUT");

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/logout_iconw.png"))); // NOI18N
        jLabel56.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout logoutTabLayout = new javax.swing.GroupLayout(logoutTab);
        logoutTab.setLayout(logoutTabLayout);
        logoutTabLayout.setHorizontalGroup(
            logoutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        logoutTabLayout.setVerticalGroup(
            logoutTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logoutTabLayout.createSequentialGroup()
                .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addContainerGap())
        );

        javax.swing.GroupLayout NavigationLayout = new javax.swing.GroupLayout(Navigation);
        Navigation.setLayout(NavigationLayout);
        NavigationLayout.setHorizontalGroup(
            NavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(petsTab, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(adoptionsTab, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(settingsTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutTab, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(adoptionFormTab, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        NavigationLayout.setVerticalGroup(
            NavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adoptionFormTab, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(petsTab, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adoptionsTab, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutTab, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Pages.setPreferredSize(new java.awt.Dimension(1030, 640));
        Pages.setLayout(new java.awt.CardLayout());

        AdoptionForm.setBackground(new java.awt.Color(238, 134, 175));

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 25)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("ADOPTION FORM");

        jPanel2.setBackground(new java.awt.Color(251, 203, 227));

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(238, 134, 175));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("PET INFORMATION");

        afSearchTF.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afSearchTF.setForeground(new java.awt.Color(153, 153, 153));
        afSearchTF.setText("Enter Pet ID or Name");
        afSearchTF.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        afSearchTF.setPreferredSize(new java.awt.Dimension(200, 35));
        afSearchTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                afSearchTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                afSearchTFFocusLost(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(238, 134, 175));
        jLabel14.setText("Sex");

        afPetName.setEditable(false);
        afPetName.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afPetName.setForeground(new java.awt.Color(153, 153, 153));
        afPetName.setText("Pet Name");
        afPetName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        jLabel15.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(238, 134, 175));
        jLabel15.setText("Name");

        afSex.setEditable(false);
        afSex.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afSex.setForeground(new java.awt.Color(153, 153, 153));
        afSex.setText("Pet Sex");
        afSex.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(238, 134, 175));
        jLabel16.setText("Type of Pet");

        afPetType.setEditable(false);
        afPetType.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afPetType.setForeground(new java.awt.Color(153, 153, 153));
        afPetType.setText("Type of Pet");
        afPetType.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        jLabel17.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(238, 134, 175));
        jLabel17.setText("Breed");

        afBreed.setEditable(false);
        afBreed.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afBreed.setForeground(new java.awt.Color(153, 153, 153));
        afBreed.setText("Pet Breed");
        afBreed.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        jLabel18.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(238, 134, 175));
        jLabel18.setText("Weight");

        afWeight.setEditable(false);
        afWeight.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afWeight.setForeground(new java.awt.Color(153, 153, 153));
        afWeight.setText("Pet Weight");
        afWeight.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        aAddress.setColumns(20);
        aAddress.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aAddress.setForeground(new java.awt.Color(153, 153, 153));
        aAddress.setLineWrap(true);
        aAddress.setRows(5);
        aAddress.setText("Enter Address");
        aAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        aAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aAddressFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(aAddress);

        jLabel20.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(238, 134, 175));
        jLabel20.setText("Address");

        aPhone.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aPhone.setForeground(new java.awt.Color(153, 153, 153));
        aPhone.setText("Enter Phone");
        aPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        aPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aPhoneFocusGained(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(238, 134, 175));
        jLabel21.setText("Phone");

        aEmail.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aEmail.setForeground(new java.awt.Color(153, 153, 153));
        aEmail.setText("Enter Email");
        aEmail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        aEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aEmailFocusGained(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(238, 134, 175));
        jLabel22.setText("Email");

        aFirstName.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aFirstName.setForeground(new java.awt.Color(153, 153, 153));
        aFirstName.setText("Enter First Name");
        aFirstName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        aFirstName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aFirstNameFocusGained(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(238, 134, 175));
        jLabel23.setText("First Name");

        jLabel24.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(238, 134, 175));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("ADOPTER INFORMATION");

        jLabel25.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(238, 134, 175));
        jLabel25.setText("Last Name");

        aLastName.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        aLastName.setForeground(new java.awt.Color(153, 153, 153));
        aLastName.setText("Enter Last Name");
        aLastName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        aLastName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                aLastNameFocusGained(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(120, 184, 198));

        jSeparator2.setForeground(new java.awt.Color(120, 184, 198));

        jLabel26.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(238, 134, 175));
        jLabel26.setText("Color");

        afColor.setEditable(false);
        afColor.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afColor.setForeground(new java.awt.Color(153, 153, 153));
        afColor.setText("Pet Color");
        afColor.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));

        adoptionDate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        adoptionDate.setForeground(new java.awt.Color(238, 134, 175));
        adoptionDate.setPreferredSize(new java.awt.Dimension(200, 35));

        jLabel29.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(238, 134, 175));
        jLabel29.setText("Date");

        jSeparator7.setForeground(new java.awt.Color(120, 184, 198));

        afSearch.setBackground(new java.awt.Color(238, 134, 175));
        afSearch.setPreferredSize(new java.awt.Dimension(150, 35));
        afSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                afSearchMouseClicked(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Search");

        javax.swing.GroupLayout afSearchLayout = new javax.swing.GroupLayout(afSearch);
        afSearch.setLayout(afSearchLayout);
        afSearchLayout.setHorizontalGroup(
            afSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        afSearchLayout.setVerticalGroup(
            afSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        afSubmit.setBackground(new java.awt.Color(238, 134, 175));
        afSubmit.setPreferredSize(new java.awt.Dimension(150, 35));
        afSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                afSubmitMouseClicked(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Submit");

        javax.swing.GroupLayout afSubmitLayout = new javax.swing.GroupLayout(afSubmit);
        afSubmit.setLayout(afSubmitLayout);
        afSubmitLayout.setHorizontalGroup(
            afSubmitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSubmitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        afSubmitLayout.setVerticalGroup(
            afSubmitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSubmitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        AdopterIDL.setForeground(new java.awt.Color(251, 203, 227));
        AdopterIDL.setText("jLabel13");

        PetIDL.setForeground(new java.awt.Color(251, 203, 227));
        PetIDL.setText("jLabel30");

        afClear.setBackground(new java.awt.Color(238, 134, 175));
        afClear.setPreferredSize(new java.awt.Dimension(150, 35));
        afClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                afClearMouseClicked(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Clear");

        javax.swing.GroupLayout afClearLayout = new javax.swing.GroupLayout(afClear);
        afClear.setLayout(afClearLayout);
        afClearLayout.setHorizontalGroup(
            afClearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afClearLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        afClearLayout.setVerticalGroup(
            afClearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afClearLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        afSearchTFA.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        afSearchTFA.setForeground(new java.awt.Color(153, 153, 153));
        afSearchTFA.setText("Enter Adopter ID");
        afSearchTFA.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        afSearchTFA.setPreferredSize(new java.awt.Dimension(200, 35));
        afSearchTFA.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                afSearchTFAFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                afSearchTFAFocusLost(evt);
            }
        });

        afSearchA.setBackground(new java.awt.Color(238, 134, 175));
        afSearchA.setPreferredSize(new java.awt.Dimension(150, 35));
        afSearchA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                afSearchAMouseClicked(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("Search");

        javax.swing.GroupLayout afSearchALayout = new javax.swing.GroupLayout(afSearchA);
        afSearchA.setLayout(afSearchALayout);
        afSearchALayout.setHorizontalGroup(
            afSearchALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSearchALayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        afSearchALayout.setVerticalGroup(
            afSearchALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(afSearchALayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel61.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(238, 134, 175));
        jLabel61.setText("Have adopted before?");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(aFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(aLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel22)
                                        .addGap(219, 219, 219))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(aEmail)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE))
                                    .addComponent(aPhone))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(afPetName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(afBreed, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(afSex, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(afWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18))
                                .addGap(77, 77, 77))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(AdopterIDL, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(62, 62, 62)
                                .addComponent(PetIDL, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(afPetType, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(afColor, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26))
                                .addGap(76, 76, 76))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(afClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adoptionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel61)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(afSearchTFA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afSearchA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(afSearchTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(afSearchA, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adoptionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(afSearchTFA, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel25)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(aFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(aEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(afSearchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(afSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afSex, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afPetType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(afColor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(afSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(afClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afPetName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afBreed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(AdopterIDL)
                            .addComponent(PetIDL))))
                .addGap(9, 9, 9))
        );

        javax.swing.GroupLayout AdoptionFormLayout = new javax.swing.GroupLayout(AdoptionForm);
        AdoptionForm.setLayout(AdoptionFormLayout);
        AdoptionFormLayout.setHorizontalGroup(
            AdoptionFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdoptionFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        AdoptionFormLayout.setVerticalGroup(
            AdoptionFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionFormLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pages.add(AdoptionForm, "card2");

        PetsPage.setBackground(new java.awt.Color(238, 134, 175));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 25)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MANAGE PETS");

        jPanel4.setBackground(new java.awt.Color(251, 203, 227));

        petsTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        petsTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        petsTable.setForeground(new java.awt.Color(238, 134, 175));
        petsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Pet ID", "Name", "Sex", "Type", "Breed", "Weight", "Color", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        petsTable.setGridColor(new java.awt.Color(120, 184, 198));
        petsTable.setSelectionBackground(new java.awt.Color(120, 184, 198));
        petsTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        petsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                petsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(petsTable);

        mpSearchTF.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        mpSearchTF.setForeground(new java.awt.Color(153, 153, 153));
        mpSearchTF.setText("Search");
        mpSearchTF.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        mpSearchTF.setPreferredSize(new java.awt.Dimension(150, 35));
        mpSearchTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                mpSearchTFFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                mpSearchTFFocusLost(evt);
            }
        });
        mpSearchTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mpSearchTFKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(mpSearchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(mpSearchTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout PetsPageLayout = new javax.swing.GroupLayout(PetsPage);
        PetsPage.setLayout(PetsPageLayout);
        PetsPageLayout.setHorizontalGroup(
            PetsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PetsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PetsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        PetsPageLayout.setVerticalGroup(
            PetsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PetsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pages.add(PetsPage, "card3");

        AdoptionsPage.setBackground(new java.awt.Color(238, 134, 175));

        jLabel32.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 25)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("ADOPTION RECORDS");

        jPanel11.setBackground(new java.awt.Color(251, 203, 227));

        recordsSearch.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        recordsSearch.setForeground(new java.awt.Color(153, 153, 153));
        recordsSearch.setText("Search");
        recordsSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        recordsSearch.setPreferredSize(new java.awt.Dimension(150, 35));
        recordsSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                recordsSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                recordsSearchFocusLost(evt);
            }
        });
        recordsSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                recordsSearchKeyReleased(evt);
            }
        });

        adoptionHistoryTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        adoptionHistoryTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        adoptionHistoryTable.setForeground(new java.awt.Color(238, 134, 175));
        adoptionHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Pet ID", "Adopter ID", "Adoption Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        adoptionHistoryTable.setGridColor(new java.awt.Color(120, 184, 198));
        adoptionHistoryTable.setSelectionBackground(new java.awt.Color(120, 184, 198));
        adoptionHistoryTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane5.setViewportView(adoptionHistoryTable);

        javax.swing.GroupLayout adoptionHistoryPageLayout = new javax.swing.GroupLayout(adoptionHistoryPage);
        adoptionHistoryPage.setLayout(adoptionHistoryPageLayout);
        adoptionHistoryPageLayout.setHorizontalGroup(
            adoptionHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        adoptionHistoryPageLayout.setVerticalGroup(
            adoptionHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
        );

        petHistoryTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        petHistoryTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        petHistoryTable.setForeground(new java.awt.Color(238, 134, 175));
        petHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Pet ID", "Pet Name", "Pet Sex", "Species", "Breed", "Weight", "Color", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        petHistoryTable.setGridColor(new java.awt.Color(120, 184, 198));
        petHistoryTable.setSelectionBackground(new java.awt.Color(120, 184, 198));
        petHistoryTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setViewportView(petHistoryTable);

        javax.swing.GroupLayout petHistoryPageLayout = new javax.swing.GroupLayout(petHistoryPage);
        petHistoryPage.setLayout(petHistoryPageLayout);
        petHistoryPageLayout.setHorizontalGroup(
            petHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
        );
        petHistoryPageLayout.setVerticalGroup(
            petHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
        );

        adopterHistoryTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        adopterHistoryTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        adopterHistoryTable.setForeground(new java.awt.Color(238, 134, 175));
        adopterHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Adopter ID", "First Name", "Last Name", "Email", "Phone", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        adopterHistoryTable.setGridColor(new java.awt.Color(120, 184, 198));
        adopterHistoryTable.setSelectionBackground(new java.awt.Color(120, 184, 198));
        adopterHistoryTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane6.setViewportView(adopterHistoryTable);

        javax.swing.GroupLayout adopterHistoryPageLayout = new javax.swing.GroupLayout(adopterHistoryPage);
        adopterHistoryPage.setLayout(adopterHistoryPageLayout);
        adopterHistoryPageLayout.setHorizontalGroup(
            adopterHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
        );
        adopterHistoryPageLayout.setVerticalGroup(
            adopterHistoryPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adoptionHistoryPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(petHistoryPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(adopterHistoryPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adoptionHistoryPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(petHistoryPage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(adopterHistoryPage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        adoptionHistoryB.setBackground(new java.awt.Color(238, 134, 175));
        adoptionHistoryB.setPreferredSize(new java.awt.Dimension(150, 35));
        adoptionHistoryB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adoptionHistoryBMouseClicked(evt);
            }
        });

        adoptionHistoryL.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        adoptionHistoryL.setForeground(new java.awt.Color(255, 255, 255));
        adoptionHistoryL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adoptionHistoryL.setText("Adoption History");

        javax.swing.GroupLayout adoptionHistoryBLayout = new javax.swing.GroupLayout(adoptionHistoryB);
        adoptionHistoryB.setLayout(adoptionHistoryBLayout);
        adoptionHistoryBLayout.setHorizontalGroup(
            adoptionHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adoptionHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adoptionHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        adoptionHistoryBLayout.setVerticalGroup(
            adoptionHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adoptionHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adoptionHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        petHistoryB.setBackground(new java.awt.Color(238, 134, 175));
        petHistoryB.setPreferredSize(new java.awt.Dimension(150, 35));
        petHistoryB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                petHistoryBMouseClicked(evt);
            }
        });

        petHistoryL.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        petHistoryL.setForeground(new java.awt.Color(255, 255, 255));
        petHistoryL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        petHistoryL.setText("Pet History");

        javax.swing.GroupLayout petHistoryBLayout = new javax.swing.GroupLayout(petHistoryB);
        petHistoryB.setLayout(petHistoryBLayout);
        petHistoryBLayout.setHorizontalGroup(
            petHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(petHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(petHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        petHistoryBLayout.setVerticalGroup(
            petHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(petHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(petHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        adopterHistoryB.setBackground(new java.awt.Color(238, 134, 175));
        adopterHistoryB.setPreferredSize(new java.awt.Dimension(150, 35));
        adopterHistoryB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adopterHistoryBMouseClicked(evt);
            }
        });

        adopterHistoryL.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 15)); // NOI18N
        adopterHistoryL.setForeground(new java.awt.Color(255, 255, 255));
        adopterHistoryL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adopterHistoryL.setText("Adopter History");

        javax.swing.GroupLayout adopterHistoryBLayout = new javax.swing.GroupLayout(adopterHistoryB);
        adopterHistoryB.setLayout(adopterHistoryBLayout);
        adopterHistoryBLayout.setHorizontalGroup(
            adopterHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adopterHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adopterHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        adopterHistoryBLayout.setVerticalGroup(
            adopterHistoryBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adopterHistoryBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adopterHistoryL, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(adoptionHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(petHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adopterHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(recordsSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recordsSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adoptionHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(petHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adopterHistoryB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout AdoptionsPageLayout = new javax.swing.GroupLayout(AdoptionsPage);
        AdoptionsPage.setLayout(AdoptionsPageLayout);
        AdoptionsPageLayout.setHorizontalGroup(
            AdoptionsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AdoptionsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1018, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        AdoptionsPageLayout.setVerticalGroup(
            AdoptionsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AdoptionsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pages.add(AdoptionsPage, "card4");

        SettingsPage.setBackground(new java.awt.Color(238, 134, 175));

        jLabel35.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 25)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("SETTINGS");

        settingsNav.setBackground(new java.awt.Color(255, 255, 255));

        editProfileTab.setBackground(new java.awt.Color(255, 255, 255));
        editProfileTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editProfileTabMouseClicked(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(238, 134, 175));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("EDIT PROFILE");

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/eprofile_icon.png"))); // NOI18N
        jLabel57.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout editProfileTabLayout = new javax.swing.GroupLayout(editProfileTab);
        editProfileTab.setLayout(editProfileTabLayout);
        editProfileTabLayout.setHorizontalGroup(
            editProfileTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfileTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        editProfileTabLayout.setVerticalGroup(
            editProfileTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfileTabLayout.createSequentialGroup()
                .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36)
                .addContainerGap())
        );

        changePasswordTab.setBackground(new java.awt.Color(255, 255, 255));
        changePasswordTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                changePasswordTabMouseClicked(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(238, 134, 175));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("CHANGE PASSWORD");

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/cpass_icon.png"))); // NOI18N
        jLabel58.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout changePasswordTabLayout = new javax.swing.GroupLayout(changePasswordTab);
        changePasswordTab.setLayout(changePasswordTabLayout);
        changePasswordTabLayout.setHorizontalGroup(
            changePasswordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changePasswordTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        changePasswordTabLayout.setVerticalGroup(
            changePasswordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changePasswordTabLayout.createSequentialGroup()
                .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
                .addContainerGap())
        );

        viewUsersTab.setBackground(new java.awt.Color(255, 255, 255));
        viewUsersTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewUsersTabMouseClicked(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(238, 134, 175));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("VIEW USERS");

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/vusers_icon.png"))); // NOI18N
        jLabel59.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout viewUsersTabLayout = new javax.swing.GroupLayout(viewUsersTab);
        viewUsersTab.setLayout(viewUsersTabLayout);
        viewUsersTabLayout.setHorizontalGroup(
            viewUsersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewUsersTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        viewUsersTabLayout.setVerticalGroup(
            viewUsersTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewUsersTabLayout.createSequentialGroup()
                .addComponent(jLabel59, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38)
                .addContainerGap())
        );

        javax.swing.GroupLayout settingsNavLayout = new javax.swing.GroupLayout(settingsNav);
        settingsNav.setLayout(settingsNavLayout);
        settingsNavLayout.setHorizontalGroup(
            settingsNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editProfileTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(changePasswordTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(viewUsersTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        settingsNavLayout.setVerticalGroup(
            settingsNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsNavLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(editProfileTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(changePasswordTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewUsersTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(251, 203, 227));
        jPanel15.setLayout(new java.awt.CardLayout());

        editProfilePage.setBackground(new java.awt.Color(251, 203, 227));

        jLabel39.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(238, 134, 175));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("EDIT PROFILE");

        jSeparator3.setForeground(new java.awt.Color(120, 184, 198));

        UserNameL.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        UserNameL.setForeground(new java.awt.Color(238, 134, 175));
        UserNameL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserNameL.setText("USER");

        usernameL.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        usernameL.setForeground(new java.awt.Color(238, 134, 175));
        usernameL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usernameL.setText("username");

        jSeparator4.setForeground(new java.awt.Color(120, 184, 198));

        jLabel42.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(238, 134, 175));
        jLabel42.setText("First Name");

        userFNTF.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        userFNTF.setForeground(new java.awt.Color(153, 153, 153));
        userFNTF.setText("Enter New First Name");
        userFNTF.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        userFNTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userFNTFFocusGained(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(238, 134, 175));
        jLabel43.setText("Last Name");

        userLNTF.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        userLNTF.setForeground(new java.awt.Color(153, 153, 153));
        userLNTF.setText("Enter New Last Name");
        userLNTF.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        userLNTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userLNTFFocusGained(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(238, 134, 175));
        jLabel44.setText("Username");

        userUNTF.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        userUNTF.setForeground(new java.awt.Color(153, 153, 153));
        userUNTF.setText("Enter New Username");
        userUNTF.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        userUNTF.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userUNTFFocusGained(evt);
            }
        });

        editProfileB.setBackground(new java.awt.Color(238, 134, 175));
        editProfileB.setPreferredSize(new java.awt.Dimension(150, 35));
        editProfileB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editProfileBMouseClicked(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Edit Profile");

        javax.swing.GroupLayout editProfileBLayout = new javax.swing.GroupLayout(editProfileB);
        editProfileB.setLayout(editProfileBLayout);
        editProfileBLayout.setHorizontalGroup(
            editProfileBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfileBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        editProfileBLayout.setVerticalGroup(
            editProfileBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfileBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout editProfilePageLayout = new javax.swing.GroupLayout(editProfilePage);
        editProfilePage.setLayout(editProfilePageLayout);
        editProfilePageLayout.setHorizontalGroup(
            editProfilePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfilePageLayout.createSequentialGroup()
                .addGroup(editProfilePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editProfilePageLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(editProfilePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator4)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserNameL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(editProfilePageLayout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addGroup(editProfilePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userUNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44)
                            .addComponent(userLNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43)
                            .addComponent(jLabel42)
                            .addComponent(userFNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 229, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(editProfilePageLayout.createSequentialGroup()
                .addGap(294, 294, 294)
                .addComponent(editProfileB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editProfilePageLayout.setVerticalGroup(
            editProfilePageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editProfilePageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserNameL, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameL, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userFNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userLNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userUNTF, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(editProfileB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jPanel15.add(editProfilePage, "card2");

        changePasswordPage.setBackground(new java.awt.Color(251, 203, 227));

        jLabel46.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(238, 134, 175));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("CHANGE PASSWORD");

        jSeparator5.setForeground(new java.awt.Color(120, 184, 198));

        UserNameL1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 12)); // NOI18N
        UserNameL1.setForeground(new java.awt.Color(238, 134, 175));
        UserNameL1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UserNameL1.setText("USER");

        usernameL1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        usernameL1.setForeground(new java.awt.Color(238, 134, 175));
        usernameL1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usernameL1.setText("username");

        jSeparator6.setForeground(new java.awt.Color(120, 184, 198));

        jLabel49.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(238, 134, 175));
        jLabel49.setText("Old Password");

        jLabel50.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(238, 134, 175));
        jLabel50.setText("New Password");

        jLabel51.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(238, 134, 175));
        jLabel51.setText("Confirm New Password");

        jPasswordField1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jPasswordField1.setForeground(new java.awt.Color(153, 153, 153));
        jPasswordField1.setText("Enter Old Password");
        jPasswordField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        jPasswordField1.setEchoChar('•');
        jPasswordField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField1FocusGained(evt);
            }
        });

        jPasswordField2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jPasswordField2.setForeground(new java.awt.Color(153, 153, 153));
        jPasswordField2.setText("Enter New Password");
        jPasswordField2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        jPasswordField2.setEchoChar('•');
        jPasswordField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField2FocusGained(evt);
            }
        });

        jPasswordField3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jPasswordField3.setForeground(new java.awt.Color(153, 153, 153));
        jPasswordField3.setText("Enter New Password");
        jPasswordField3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        jPasswordField3.setEchoChar('•');
        jPasswordField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField3FocusGained(evt);
            }
        });

        changePassB.setBackground(new java.awt.Color(238, 134, 175));
        changePassB.setPreferredSize(new java.awt.Dimension(150, 35));
        changePassB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                changePassBMouseClicked(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("Change Password");

        javax.swing.GroupLayout changePassBLayout = new javax.swing.GroupLayout(changePassB);
        changePassB.setLayout(changePassBLayout);
        changePassBLayout.setHorizontalGroup(
            changePassBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changePassBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        changePassBLayout.setVerticalGroup(
            changePassBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changePassBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout changePasswordPageLayout = new javax.swing.GroupLayout(changePasswordPage);
        changePasswordPage.setLayout(changePasswordPageLayout);
        changePasswordPageLayout.setHorizontalGroup(
            changePasswordPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changePasswordPageLayout.createSequentialGroup()
                .addGroup(changePasswordPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changePasswordPageLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(changePasswordPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator6)
                            .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserNameL1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(changePasswordPageLayout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addGroup(changePasswordPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel51)
                            .addComponent(jLabel50)
                            .addComponent(jLabel49)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                            .addComponent(jPasswordField2)
                            .addComponent(jPasswordField3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(changePasswordPageLayout.createSequentialGroup()
                .addGap(294, 294, 294)
                .addComponent(changePassB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
        );
        changePasswordPageLayout.setVerticalGroup(
            changePasswordPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changePasswordPageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UserNameL1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameL1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(changePassB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        jPanel15.add(changePasswordPage, "card3");

        viewUsersPage.setBackground(new java.awt.Color(251, 203, 227));

        userSearch.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        userSearch.setForeground(new java.awt.Color(153, 153, 153));
        userSearch.setText("Search");
        userSearch.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        userSearch.setPreferredSize(new java.awt.Dimension(200, 35));
        userSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userSearchFocusLost(evt);
            }
        });
        userSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                userSearchKeyReleased(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(238, 134, 175));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("VIEW USERS");

        usersTable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(120, 184, 198), 2, true));
        usersTable.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        usersTable.setForeground(new java.awt.Color(238, 134, 175));
        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "UserID", "First Name", "Last Name", "Email", "Username", "Password", "Role"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        usersTable.setGridColor(new java.awt.Color(120, 184, 198));
        usersTable.setSelectionBackground(new java.awt.Color(120, 184, 198));
        usersTable.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(usersTable);

        deleteUserp.setBackground(new java.awt.Color(238, 134, 175));
        deleteUserp.setPreferredSize(new java.awt.Dimension(150, 35));
        deleteUserp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteUserpMouseClicked(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel62.setText("Delete User");

        javax.swing.GroupLayout deleteUserpLayout = new javax.swing.GroupLayout(deleteUserp);
        deleteUserp.setLayout(deleteUserpLayout);
        deleteUserpLayout.setHorizontalGroup(
            deleteUserpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteUserpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        deleteUserpLayout.setVerticalGroup(
            deleteUserpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteUserpLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout viewUsersPageLayout = new javax.swing.GroupLayout(viewUsersPage);
        viewUsersPage.setLayout(viewUsersPageLayout);
        viewUsersPageLayout.setHorizontalGroup(
            viewUsersPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewUsersPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(viewUsersPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewUsersPageLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(viewUsersPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteUserp, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        viewUsersPageLayout.setVerticalGroup(
            viewUsersPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewUsersPageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteUserp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel15.add(viewUsersPage, "card4");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(settingsNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(settingsNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout SettingsPageLayout = new javax.swing.GroupLayout(SettingsPage);
        SettingsPage.setLayout(SettingsPageLayout);
        SettingsPageLayout.setHorizontalGroup(
            SettingsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SettingsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        SettingsPageLayout.setVerticalGroup(
            SettingsPageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SettingsPageLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Pages.add(SettingsPage, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Navigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Pages, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Navigation, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
            .addComponent(Pages, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adoptionFormTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adoptionFormTabMouseClicked
        // TODO add your handling code here:
        adoptionFormTabClicked();
    }//GEN-LAST:event_adoptionFormTabMouseClicked

    private void petsTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_petsTabMouseClicked
        // TODO add your handling code here:
        petsTabClicked();
    }//GEN-LAST:event_petsTabMouseClicked

    private void adoptionsTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adoptionsTabMouseClicked
        // TODO add your handling code here:
        adoptionsTabClicked();
    }//GEN-LAST:event_adoptionsTabMouseClicked

    private void settingsTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsTabMouseClicked
        // TODO add your handling code here:
        settingsTabClicked();
    }//GEN-LAST:event_settingsTabMouseClicked

    private void logoutTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutTabMouseClicked
        // TODO add your handling code here:
        int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to log out of PetPals?", "Log Out", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            Login login = new Login ();
            login.setVisible(true);
        }
    }//GEN-LAST:event_logoutTabMouseClicked

    private void afSearchTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_afSearchTFFocusGained
        // TODO add your handling code here:
        if (afSearchTF.getText().equals("Enter Pet ID or Name")){
            afSearchTF.setText("");
            afSearchTF.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_afSearchTFFocusGained

    private void afSearchTFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_afSearchTFFocusLost
        // TODO add your handling code here:
        if (afSearchTF.getText().equals("")){
            afSearchTF.setText("Enter Pet ID or Name");
            afSearchTF.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_afSearchTFFocusLost

    private void aAddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aAddressFocusGained
        // TODO add your handling code here:
        if (aAddress.getText().equals("Enter Address")){
            aAddress.setText("");
            aAddress.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_aAddressFocusGained

    private void aPhoneFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aPhoneFocusGained
        // TODO add your handling code here:
        if (aPhone.getText().equals("Enter Phone")){
            aPhone.setText("");
            aPhone.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_aPhoneFocusGained

    private void aEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aEmailFocusGained
        // TODO add your handling code here:
        if (aEmail.getText().equals("Enter Email")){
            aEmail.setText("");
            aEmail.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_aEmailFocusGained

    private void aFirstNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aFirstNameFocusGained
        // TODO add your handling code here:
        if (aFirstName.getText().equals("Enter First Name")){
            aFirstName.setText("");
            aFirstName.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_aFirstNameFocusGained

    private void aLastNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_aLastNameFocusGained
        // TODO add your handling code here:
        if (aLastName.getText().equals("Enter Last Name")){
            aLastName.setText("");
            aLastName.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_aLastNameFocusGained

    private void afSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afSearchMouseClicked
        // TODO add your handling code here:
        afSearch();
    }//GEN-LAST:event_afSearchMouseClicked

    private void afSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afSubmitMouseClicked
        // TODO add your handling code here:
        addAdopter();
    }//GEN-LAST:event_afSubmitMouseClicked

    private void afClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afClearMouseClicked
        // TODO add your handling code here:
        aFirstName.setText("");
        aLastName.setText("");
        aEmail.setText("");
        aPhone.setText("");
        aAddress.setText("");
        adoptionDate.setDate(null);
        afSearchTFA.setText("");
        afSearchTF.setText("");
        afPetName.setText("Pet Name");
        afSex.setText("Pet Sex");
        afPetType.setText("Type of Pet");
        afBreed.setText("Pet Breed");
        afWeight.setText("Pet Weight");
        afColor.setText("Pet Color");
    }//GEN-LAST:event_afClearMouseClicked

    private void petsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_petsTableMouseClicked
        // TODO add your handling code here:
//        fillFromPetsTable();
    }//GEN-LAST:event_petsTableMouseClicked

    private void mpSearchTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mpSearchTFFocusGained
        // TODO add your handling code here:
        if (mpSearchTF.getText().equals("Search")){
            mpSearchTF.setText("");
            mpSearchTF.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_mpSearchTFFocusGained

    private void mpSearchTFFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_mpSearchTFFocusLost
        // TODO add your handling code here:
        if (mpSearchTF.getText().equals("")){
            mpSearchTF.setText("Search");
            mpSearchTF.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_mpSearchTFFocusLost

    private void mpSearchTFKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mpSearchTFKeyReleased
        // TODO add your handling code here:
        DefaultTableModel pTable = (DefaultTableModel) petsTable.getModel();
        TableRowSorter<DefaultTableModel> pTable1 = new TableRowSorter<>(pTable);
        petsTable.setRowSorter(pTable1);
        pTable1.setRowFilter(RowFilter.regexFilter(mpSearchTF.getText()));
    }//GEN-LAST:event_mpSearchTFKeyReleased

    private void recordsSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recordsSearchFocusGained
        // TODO add your handling code here:
        if (recordsSearch.getText().equals("Search")){
            recordsSearch.setText("");
            recordsSearch.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_recordsSearchFocusGained

    private void recordsSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_recordsSearchFocusLost
        // TODO add your handling code here:
        if (recordsSearch.getText().equals("")){
            recordsSearch.setText("Search");
            recordsSearch.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_recordsSearchFocusLost

    private void recordsSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recordsSearchKeyReleased
        // TODO add your handling code here:
        DefaultTableModel pHTable = (DefaultTableModel) petHistoryTable.getModel();
        TableRowSorter<DefaultTableModel> pHTable1 = new TableRowSorter<>(pHTable);
        petHistoryTable.setRowSorter(pHTable1);
        pHTable1.setRowFilter(RowFilter.regexFilter(recordsSearch.getText()));

        DefaultTableModel aHTable = (DefaultTableModel) adopterHistoryTable.getModel();
        TableRowSorter<DefaultTableModel> aHTable1 = new TableRowSorter<>(aHTable);
        adopterHistoryTable.setRowSorter(aHTable1);
        aHTable1.setRowFilter(RowFilter.regexFilter(recordsSearch.getText()));

        DefaultTableModel adHTable = (DefaultTableModel) adoptionHistoryTable.getModel();
        TableRowSorter<DefaultTableModel> adHTable1 = new TableRowSorter<>(adHTable);
        adoptionHistoryTable.setRowSorter(adHTable1);
        adHTable1.setRowFilter(RowFilter.regexFilter(recordsSearch.getText()));
    }//GEN-LAST:event_recordsSearchKeyReleased

    private void adoptionHistoryBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adoptionHistoryBMouseClicked
        // TODO add your handling code here:
        adoptionHistoryTabClicked();
    }//GEN-LAST:event_adoptionHistoryBMouseClicked

    private void petHistoryBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_petHistoryBMouseClicked
        // TODO add your handling code here:
        adoptionHistoryPage.setVisible(false);
        petHistoryPage.setVisible(true);
        adopterHistoryPage.setVisible(false);

        adoptionHistoryB.setBackground(new Color(238,134,175));
        petHistoryB.setBackground(new Color(251,203,227));
        adopterHistoryB.setBackground(new Color(238,134,175));

        adoptionHistoryL.setForeground(Color.WHITE);
        petHistoryL.setForeground(new Color(238,134,175));
        adopterHistoryL.setForeground(Color.WHITE);

        showPetHistory();
    }//GEN-LAST:event_petHistoryBMouseClicked

    private void adopterHistoryBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adopterHistoryBMouseClicked
        // TODO add your handling code here:
        adoptionHistoryPage.setVisible(false);
        petHistoryPage.setVisible(false);
        adopterHistoryPage.setVisible(true);

        adoptionHistoryB.setBackground(new Color(238,134,175));
        petHistoryB.setBackground(new Color(238,134,175));
        adopterHistoryB.setBackground(new Color(251,203,227));

        adoptionHistoryL.setForeground(Color.WHITE);
        petHistoryL.setForeground(Color.WHITE);
        adopterHistoryL.setForeground(new Color(238,134,175));

        showAdopterHistory();
    }//GEN-LAST:event_adopterHistoryBMouseClicked

    private void editProfileTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editProfileTabMouseClicked
        // TODO add your handling code here:
        editProfileTabClicked();
    }//GEN-LAST:event_editProfileTabMouseClicked

    private void changePasswordTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordTabMouseClicked
        // TODO add your handling code here:
        changePasswordTabClicked();
    }//GEN-LAST:event_changePasswordTabMouseClicked

    private void viewUsersTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewUsersTabMouseClicked
        // TODO add your handling code here:
        //viewUsersTabClicked();
        JOptionPane.showMessageDialog(null, "Admin access only!");
    }//GEN-LAST:event_viewUsersTabMouseClicked

    private void userFNTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userFNTFFocusGained
        // TODO add your handling code here:
        if (userFNTF.getText().equals("Enter New First Name")){
            userFNTF.setText("");
            userFNTF.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_userFNTFFocusGained

    private void userLNTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userLNTFFocusGained
        // TODO add your handling code here:
        if (userLNTF.getText().equals("Enter New Last Name")){
            userLNTF.setText("");
            userLNTF.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_userLNTFFocusGained

    private void userUNTFFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userUNTFFocusGained
        // TODO add your handling code here:
        if (userUNTF.getText().equals("Enter New Username")){
            userUNTF.setText("");
            userUNTF.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_userUNTFFocusGained

    private void editProfileBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editProfileBMouseClicked
        // TODO add your handling code here:
        editUserProfile();
    }//GEN-LAST:event_editProfileBMouseClicked

    private void jPasswordField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1FocusGained
        // TODO add your handling code here:
        if (jPasswordField1.getText().equals("Enter Old Password")) {
            jPasswordField1.setText("");
            jPasswordField1.requestFocus();
            jPasswordField1.setEchoChar('•');
            jPasswordField1.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_jPasswordField1FocusGained

    private void jPasswordField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField2FocusGained
        // TODO add your handling code here:
        if (jPasswordField2.getText().equals("Enter New Password")) {
            jPasswordField2.setText("");
            jPasswordField2.requestFocus();
            jPasswordField2.setEchoChar('•');
            jPasswordField2.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_jPasswordField2FocusGained

    private void jPasswordField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField3FocusGained
        // TODO add your handling code here:
        if (jPasswordField3.getText().equals("Enter New Password")) {
            jPasswordField3.setText("");
            jPasswordField3.requestFocus();
            jPasswordField3.setEchoChar('•');
            jPasswordField3.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_jPasswordField3FocusGained

    private void changePassBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePassBMouseClicked
        // TODO add your handling code here:
        changePassword();
    }//GEN-LAST:event_changePassBMouseClicked

    private void userSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userSearchFocusGained
        // TODO add your handling code here:
        if (userSearch.getText().equals("Search")){
            userSearch.setText("");
            userSearch.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_userSearchFocusGained

    private void userSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userSearchFocusLost
        // TODO add your handling code here:
        if (userSearch.getText().equals("")){
            userSearch.setText("Search");
            userSearch.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_userSearchFocusLost

    private void userSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userSearchKeyReleased
        // TODO add your handling code here:
        DefaultTableModel uTable = (DefaultTableModel) usersTable.getModel();
        TableRowSorter<DefaultTableModel> uTable1 = new TableRowSorter<>(uTable);
        usersTable.setRowSorter(uTable1);
        uTable1.setRowFilter(RowFilter.regexFilter(userSearch.getText()));
    }//GEN-LAST:event_userSearchKeyReleased

    private void afSearchTFAFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_afSearchTFAFocusGained
        // TODO add your handling code here:
        if (afSearchTFA.getText().equals("Enter Adopter ID")){
            afSearchTFA.setText("");
            afSearchTFA.setForeground(new Color(238,134,175));
        }
    }//GEN-LAST:event_afSearchTFAFocusGained

    private void afSearchTFAFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_afSearchTFAFocusLost
        // TODO add your handling code here:
        if (afSearchTFA.getText().equals("")){
            afSearchTFA.setText("Enter Adopter ID");
            afSearchTFA.setForeground(new Color(153,153,153));
        }
    }//GEN-LAST:event_afSearchTFAFocusLost

    private void afSearchAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_afSearchAMouseClicked
        // TODO add your handling code here:
        afSearchA();
    }//GEN-LAST:event_afSearchAMouseClicked

    private void deleteUserpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteUserpMouseClicked
        // TODO add your handling code here:
        deleteUser();
    }//GEN-LAST:event_deleteUserpMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AdopterIDL;
    private javax.swing.JPanel AdoptionForm;
    private javax.swing.JPanel AdoptionsPage;
    private javax.swing.JPanel Navigation;
    private javax.swing.JPanel Pages;
    private javax.swing.JLabel PetIDL;
    private javax.swing.JPanel PetsPage;
    private javax.swing.JPanel SettingsPage;
    private javax.swing.JLabel UserNameL;
    private javax.swing.JLabel UserNameL1;
    private javax.swing.JTextArea aAddress;
    private javax.swing.JTextField aEmail;
    private javax.swing.JTextField aFirstName;
    private javax.swing.JTextField aLastName;
    private javax.swing.JTextField aPhone;
    private javax.swing.JPanel adopterHistoryB;
    private javax.swing.JLabel adopterHistoryL;
    private javax.swing.JPanel adopterHistoryPage;
    private javax.swing.JTable adopterHistoryTable;
    private com.toedter.calendar.JDateChooser adoptionDate;
    private javax.swing.JPanel adoptionFormTab;
    private javax.swing.JPanel adoptionHistoryB;
    private javax.swing.JLabel adoptionHistoryL;
    private javax.swing.JPanel adoptionHistoryPage;
    private javax.swing.JTable adoptionHistoryTable;
    private javax.swing.JPanel adoptionsTab;
    private javax.swing.JTextField afBreed;
    private javax.swing.JPanel afClear;
    private javax.swing.JTextField afColor;
    private javax.swing.JTextField afPetName;
    private javax.swing.JTextField afPetType;
    private javax.swing.JPanel afSearch;
    private javax.swing.JPanel afSearchA;
    private javax.swing.JTextField afSearchTF;
    private javax.swing.JTextField afSearchTFA;
    private javax.swing.JTextField afSex;
    private javax.swing.JPanel afSubmit;
    private javax.swing.JTextField afWeight;
    private javax.swing.JPanel changePassB;
    private javax.swing.JPanel changePasswordPage;
    private javax.swing.JPanel changePasswordTab;
    private javax.swing.JPanel deleteUserp;
    private javax.swing.JPanel editProfileB;
    private javax.swing.JPanel editProfilePage;
    private javax.swing.JPanel editProfileTab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPanel logoutTab;
    private javax.swing.JTextField mpSearchTF;
    private javax.swing.JPanel petHistoryB;
    private javax.swing.JLabel petHistoryL;
    private javax.swing.JPanel petHistoryPage;
    private javax.swing.JTable petHistoryTable;
    private javax.swing.JPanel petsTab;
    private javax.swing.JTable petsTable;
    private javax.swing.JTextField recordsSearch;
    private javax.swing.JPanel settingsNav;
    private javax.swing.JPanel settingsTab;
    private javax.swing.JTextField userFNTF;
    private javax.swing.JTextField userLNTF;
    private javax.swing.JTextField userSearch;
    private javax.swing.JTextField userUNTF;
    private javax.swing.JLabel usernameL;
    private javax.swing.JLabel usernameL1;
    private javax.swing.JTable usersTable;
    private javax.swing.JPanel viewUsersPage;
    private javax.swing.JPanel viewUsersTab;
    // End of variables declaration//GEN-END:variables
}
