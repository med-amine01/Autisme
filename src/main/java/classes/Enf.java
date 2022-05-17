package classes;

public class Enf {

    private int idenfant ;
    private String nom ;
    private String prenom;
    private String sex;
    private String parent;
    private int idgroupe;

    public Enf(int idenfant, String nom, String prenom, String sex, String parent, int idgroupe) {
        this.idenfant = idenfant;
        this.nom = nom;
        this.prenom = prenom;
        this.sex = sex;
        this.parent = parent;
        this.idgroupe = idgroupe;
    }


    public int getIdenfant() {
        return idenfant;
    }

    public void setIdenfant(int idenfant) {
        this.idenfant = idenfant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getIdgroupe() {
        return idgroupe;
    }

    public void setIdgroupe(int idgroupe) {
        this.idgroupe = idgroupe;
    }
}
