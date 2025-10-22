package ma.projet.classes;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private Date dateDebut;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @OneToMany(mappedBy = "projet", fetch = FetchType.LAZY)
    private List<EmployeTache> employeTaches;

    public Projet() {
    }

    public Projet(String nom, Date dateDebut, Employe employe) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.employe = employe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public List<EmployeTache> getEmployeTaches() {
        return employeTaches;
    }

    public void setEmployeTaches(List<EmployeTache> employeTaches) {
        this.employeTaches = employeTaches;
    }
}