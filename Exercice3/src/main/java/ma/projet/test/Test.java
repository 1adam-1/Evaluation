package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        var ctx = new AnnotationConfigApplicationContext(HibernateUtil.class);
        FemmeService femmeService = ctx.getBean(FemmeService.class);
        HommeService hommeService = ctx.getBean(HommeService.class);
        MariageService mariageService = ctx.getBean(MariageService.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


        List<Femme> femmes = new ArrayList<>();
        femmes.add(new Femme("RAMI", "SALIMA", "0600000001", "Addr1", sdf.parse("03/09/1970")));
        femmes.add(new Femme("ALI", "AMAL", "0600000002", "Addr2", sdf.parse("03/09/1975")));
        femmes.add(new Femme("ALAOUI", "WAFA", "0600000003", "Addr3", sdf.parse("04/11/1980")));
        femmes.add(new Femme("ALAMI", "KARIMA", "0600000004", "Addr4", sdf.parse("03/09/1969")));
        femmes.add(new Femme("BENSAID", "NORA", "0600000005", "Addr5", sdf.parse("12/12/1985")));
        femmes.add(new Femme("CHAOUI", "LINA", "0600000006", "Addr6", sdf.parse("01/01/1990")));
        femmes.add(new Femme("DADI", "YASMIN", "0600000007", "Addr7", sdf.parse("15/05/1982")));
        femmes.add(new Femme("ELHANI", "SARA", "0600000008", "Addr8", sdf.parse("20/07/1978")));
        femmes.add(new Femme("FARAH", "HIBA", "0600000009", "Addr9", sdf.parse("30/03/1983")));
        femmes.add(new Femme("GHAZI", "MAYA", "0600000010", "Addr10", sdf.parse("25/08/1992")));

        for (Femme f : femmes) {
            femmeService.create(f);
        }

        List<Homme> hommes = new ArrayList<>();
        hommes.add(new Homme("SAID", "SAFI", "0700000001", "HAddr1", sdf.parse("10/10/1960")));
        hommes.add(new Homme("ELMA", "HASSAN", "0700000002", "HAddr2", sdf.parse("05/05/1965")));
        hommes.add(new Homme("KHALID", "OMAR", "0700000003", "HAddr3", sdf.parse("02/02/1970")));
        hommes.add(new Homme("RACHID", "YOUNES", "0700000004", "HAddr4", sdf.parse("12/12/1975")));
        hommes.add(new Homme("ZAHI", "AHMED", "0700000005", "HAddr5", sdf.parse("01/01/1980")));

        for (Homme h : hommes) {
            hommeService.create(h);
        }


        Homme safi = hommes.get(0);
        Femme salima = femmes.get(0);
        Femme amal = femmes.get(1);
        Femme wafa = femmes.get(2);
        Femme karima = femmes.get(3);

        mariageService.create(new Mariage(sdf.parse("03/09/1990"), null, 4, safi, salima));
        mariageService.create(new Mariage(sdf.parse("03/09/1995"), null, 2, safi, amal));
        mariageService.create(new Mariage(sdf.parse("04/11/2000"), null, 3, safi, wafa));
        mariageService.create(new Mariage(sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0, safi, karima));

        Homme h2 = hommes.get(1);
        mariageService.create(new Mariage(sdf.parse("01/01/2000"), null, 1, h2, femmes.get(4)));
        mariageService.create(new Mariage(sdf.parse("02/02/2001"), null, 0, h2, femmes.get(5)));
        mariageService.create(new Mariage(sdf.parse("03/03/2002"), null, 2, h2, femmes.get(6)));
        mariageService.create(new Mariage(sdf.parse("04/04/2003"), null, 1, h2, femmes.get(7)));

        femmeService.create(femmes.get(8));
        mariageService.create(new Mariage(sdf.parse("01/01/1999"), sdf.parse("01/01/2000"), 0, hommes.get(2), femmes.get(8)));
        mariageService.create(new Mariage(sdf.parse("05/05/2005"), null, 1, hommes.get(3), femmes.get(8)));

        System.out.println("Liste des femmes :");
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        for (Femme f : femmeService.findAll()) {
            System.out.printf("- %s %s (Né le %s)\n", f.getNom(), f.getPrenom(), f.getDateNaissance() != null ? out.format(f.getDateNaissance()) : "N/A");
        }
        System.out.println();

        Femme oldest = null;
        for (Femme f : femmeService.findAll()) {
            if (f.getDateNaissance() == null) continue;
            if (oldest == null || f.getDateNaissance().before(oldest.getDateNaissance())) {
                oldest = f;
            }
        }
        if (oldest != null) {
            System.out.println("Femme la plus âgée : " + oldest.getNom() + " " + oldest.getPrenom() + " (" + out.format(oldest.getDateNaissance()) + ")");
        }
        System.out.println();

        System.out.println("Épouses de SAFI SAID :");
        LocalDate start = LocalDate.of(1900, 1, 1);
        LocalDate end = LocalDate.of(2100, 1, 1);
        var epouses = hommeService.findEpousesBetweenDates(safi.getId(), start, end);
        int idx = 1;
        for (Femme f : epouses) {
            System.out.printf("%d. Femme : %s %s   Date Début : %s    Nbr Enfants : %d\n",
                    idx++, f.getNom(), f.getPrenom(), getMariageDateDebutFor(mariageService, safi.getId(), f.getId(), out), getNbrEnfantsFor(mariageService, safi.getId(), f.getId()));
        }
        System.out.println();

        int enfants = femmeService.nombreEnfantsEntreDates(salima.getId(), LocalDate.of(1980,1,1), LocalDate.of(2000,12,31));
        System.out.println("Nombre d'enfants de " + salima.getNom() + " entre 01/01/1980 et 31/12/2000 : " + enfants);
        System.out.println();

        System.out.println("Femmes mariées deux fois ou plus :");
        for (Femme f : femmeService.femmesMarieesAuMoinsDeuxFois()) {
            System.out.println("- " + f.getNom() + " " + f.getPrenom());
        }
        System.out.println();

        System.out.println("Hommes mariés à 4 femmes entre 01/01/1999 et 31/12/2010 :");
        var hommes4 = mariageService.findHommesWithAtLeastNFemmesBetweenDates(4, LocalDate.of(1999,1,1), LocalDate.of(2010,12,31));
        for (Homme h : hommes4) {
            System.out.println("- " + h.getNom() + " " + h.getPrenom());
        }
        System.out.println();

        System.out.println("Mariages détaillés de SAFI :");
        mariageService.afficherMariagesAvecDetails(safi.getId());

        ctx.close();
    }

    private static String getMariageDateDebutFor(MariageService ms, int hommeId, int femmeId, SimpleDateFormat out) {
        var mariages = ms.findAll();
        for (Mariage m : mariages) {
            if (m.getHomme() != null && m.getFemme() != null && m.getHomme().getId() == hommeId && m.getFemme().getId() == femmeId) {
                return m.getDateDebut() != null ? out.format(m.getDateDebut()) : "N/A";
            }
        }
        return "N/A";
    }

    private static int getNbrEnfantsFor(MariageService ms, int hommeId, int femmeId) {
        var mariages = ms.findAll();
        for (Mariage m : mariages) {
            if (m.getHomme() != null && m.getFemme() != null && m.getHomme().getId() == hommeId && m.getFemme().getId() == femmeId) {
                return m.getNbrEnfant();
            }
        }
        return 0;
    }
}
