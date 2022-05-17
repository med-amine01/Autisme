package com.example.autisme;

import classes.Enf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Enfant implements Initializable {
    @FXML
    private ToggleGroup genre;
    @FXML
    private RadioButton feminin;
    @FXML
    private RadioButton masculin;
    @FXML
    private TextField nomEnf;
    @FXML
    private TextField parentEnf;
    @FXML
    private TextField prenomEnf;
    @FXML
    private ComboBox<String> groupCB;


    //int -> Integer
    //double -> Double
    //float -> Float

    @FXML
    private TableView<Enf> tableEnfant;
    @FXML
    private TableColumn<Enf, Integer> tableID;
    @FXML
    private TableColumn<Enf, String> tableNOM;
    @FXML
    private TableColumn<Enf, String> tablePARENT;
    @FXML
    private TableColumn<Enf, String> tablePRENOM;
    @FXML
    private TableColumn<Enf, String> tableSEX;
    @FXML
    private TableColumn<Enf, Integer> tableGROUPE;
    Connection con;
    PreparedStatement pst,pst1;
    //-----------------------------------------------------------------------------------

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        ActualiserTableEnfant(getEnfant());
        setListeDeroulante();
    }


    public void connect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/autisme", "root","");
            //System.out.println("connecté");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }


    public ObservableList<Enf> getEnfant()
    {
        connect();//connexion base de donnée
        ObservableList<Enf> enfantList = FXCollections.observableArrayList();

        try
        {
            pst = con.prepareStatement("select * from enfant");
            ResultSet rs = pst.executeQuery(); // [('idenfant' , 1) , ('nom', 'salah')  ..... ]
            Enf Enfants;

            while (rs.next())
            {
                Enfants = new Enf(rs.getInt("idenfant") , rs.getString("nom"),
                                     rs.getString("prenom"), rs.getString("sex"),
                                      rs.getString("parent") , rs.getInt("idgroupe"));
                enfantList.add(Enfants);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return enfantList;
    }


    public void ActualiserTableEnfant(ObservableList<Enf> list)
    {
        tableID.setCellValueFactory(new PropertyValueFactory<>("idenfant"));
        tableNOM.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tablePRENOM.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        tableSEX.setCellValueFactory(new PropertyValueFactory<>("sex"));
        tablePARENT.setCellValueFactory(new PropertyValueFactory<>("parent"));
        tableGROUPE.setCellValueFactory(new PropertyValueFactory<>("idgroupe"));
        tableEnfant.setItems(list);
    }

    @FXML
    void clickAJOUTER(ActionEvent event) {

        try
        {
            String nom = nomEnf.getText();
            String prenom = prenomEnf.getText();
            String parent = parentEnf.getText();

            pst = con.prepareStatement("insert into enfant(nom,prenom,sex,parent,idgroupe) values( ?, ? , ? , ? , ? )");
            pst.setString(1,nom);
            pst.setString(2,prenom);
            pst.setString(3,getSex(event));
            pst.setString(4,parent);
            pst.setString(5,String.valueOf(getGroupe()));
            pst.executeUpdate();
            Message("Enfant ajouté !");
            ActualiserTableEnfant(getEnfant());

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void clickMODFIER(ActionEvent event) { //boutton
        try
        {
            String nom = nomEnf.getText();
            String prenom = prenomEnf.getText();
            String parent = parentEnf.getText();
            Enf enfant = tableEnfant.getSelectionModel().getSelectedItem();
            int idenfant = enfant.getIdenfant();

            pst = con.prepareStatement("update enfant set nom = ? , prenom = ? , sex = ? , parent = ?, idgroupe = ? where idenfant = ? ");
            pst.setString(1,nom);
            pst.setString(2,prenom);
            pst.setString(3,getSex(event));
            pst.setString(4,parent);
            pst.setString(5,String.valueOf(getGroupe()));
            pst.setString(6,String.valueOf(idenfant));

            pst.executeUpdate();
            Message("Enfant Modifier !");
            ActualiserTableEnfant(getEnfant());

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void clickSUPPRIMER(ActionEvent event) {
        try
        {
            Enf enfant = tableEnfant.getSelectionModel().getSelectedItem();
            int idenfant = enfant.getIdenfant();

            pst = con.prepareStatement("delete from enfant where idenfant = ?");
            pst.setString(1,String.valueOf(idenfant));
            pst.executeUpdate();

            Message("Enfant Supprimer !");
            ActualiserTableEnfant(getEnfant());

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void clickMODFIERTable(MouseEvent event) {

        //si il click sur le vide
        try
        {
            Enf enfant = tableEnfant.getSelectionModel().getSelectedItem();
            nomEnf.setText(enfant.getNom());
            prenomEnf.setText(enfant.getPrenom());
            parentEnf.setText(enfant.getParent());
            groupCB.getSelectionModel().select(indexInListGroupe(String.valueOf(enfant.getIdgroupe())));;
        }
        catch (Exception e)
        {
            Message("Veuillez sélectionner un enfant !");
        }
    }



    @FXML
    String getSex(ActionEvent event) {
        if(feminin.isSelected())
        {
            masculin.setSelected(false);
            return "feminin";
        }
        else if(masculin.isSelected())
        {
            feminin.setSelected(false);
            return "masculin";
        }
        return "masculin";
    }

    int getGroupe()
    {
        try
        {
            pst1 = con.prepareStatement("select idgroupe from groupe where nom = '" + groupCB.getSelectionModel().getSelectedItem()+"'");
            ResultSet rs = pst1.executeQuery();
            while (rs.next())
            {
                return rs.getInt("idgroupe");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 1;
    }

    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }


    //------------------------ list déroulante ------------------
    public void setListeDeroulante()
    {
        try {
            pst = con.prepareStatement("select nom from groupe");
            ResultSet rs = pst.executeQuery();


            while (rs.next())
            {
                groupCB.getItems().add(rs.getString("nom"));
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    //---------------------- indexInList du fournisseur selon id piece---------------
    public int indexInListGroupe(String id)
    {
        try
        {
            //bech yjib juste (idgroupe) piece mte3 nom groupe
            pst = con.prepareStatement("select nom from groupe where idgroupe = "+id);
            ResultSet rs1 = pst.executeQuery();
            String s ="";
            while (rs1.next())
            {
                s = rs1.getString("nom");
            }

            //bech yjib les pieces lkol w yee9if aand id
            ArrayList<String> idfo = new ArrayList<>();
            pst = con.prepareStatement("select nom from groupe");
            ResultSet rs = pst.executeQuery();
            while (rs.next())
            {
                idfo.add(rs.getString("nom"));
                if(rs.getString("nom").equals(s))
                {
                    return idfo.indexOf(s);// ----> yraja3 indice mte3ou fi wosset list !!!!!!!!!
                }
            }
            idfo.clear();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }


}