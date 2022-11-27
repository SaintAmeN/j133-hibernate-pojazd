package pl.sda.hibernate;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pl.sda.hibernate.model.Pojazd;

import java.util.List;
import java.util.Scanner;

/**
 * @author Paweł Recław, AmeN
 * @project j133-zad-dom-pojazd
 * @created 27.11.2022
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Podaj komende (dodaj / lista / szukaj / usun / aktualizuj):");
        String komenda = JakasKlasa.scanner.nextLine();

        if (komenda.equalsIgnoreCase("dodaj")) {
            obslugaDodaj();
        } else if (komenda.equalsIgnoreCase("lista")) {
            obslugaLista();
        } else if (komenda.equalsIgnoreCase("szukaj")) {
            obslugaSzukaj();
        } else if (komenda.equalsIgnoreCase("usun")) {
            obslugaUsun();
        } else if (komenda.equalsIgnoreCase("aktualizuj")) {
            obslugaAktualizuj();
        } else {
            System.err.println("Nieznana komenda.");
        }
    }

    private static void obslugaAktualizuj() {
        System.out.println("Podaj identyfikator szukanego obiektu:");
        String identyfikator = JakasKlasa.scanner.nextLine();
        Long id = Long.parseLong(identyfikator);

        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pojazd pojazd = session.get(Pojazd.class, id);

            if (pojazd == null) {
                System.err.println("Rekord nie istnieje.");
            } else {
                Pojazd daneAktualizującePojazd = wczytajParametryPojazdu();
                daneAktualizującePojazd.setId(id);

                session.merge(daneAktualizującePojazd);
            }
            transaction.commit();
        } catch (Exception ioe) {
            System.err.println("Błąd aktualizacji rekordu w bazie danych");
        }
    }

    private static void obslugaUsun() {
        System.out.println("Podaj identyfikator usuwanego obiektu:");
        String identyfikator = JakasKlasa.scanner.nextLine();
        Long id = Long.parseLong(identyfikator);

        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Pojazd pojazd = session.get(Pojazd.class, id);

            if (pojazd == null) {
                System.err.println("Rekord nie istnieje.");
            } else {
                session.remove(pojazd);
                System.out.println("Usunięto rekord o identyfikatorze: " + id);
            }
            transaction.commit();
        } catch (Exception ioe) {
            System.err.println("Błąd usuwania rekordu z bazy danych");
        }
    }

    private static void obslugaSzukaj() {
        System.out.println("Podaj identyfikator szukanego obiektu:");
        String identyfikator = JakasKlasa.scanner.nextLine();
        Long id = Long.parseLong(identyfikator);

        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Pojazd pojazd = session.get(Pojazd.class, id);

            if (pojazd == null) {
                System.err.println("Rekord nie istnieje.");
            } else {
                System.out.println(pojazd);
            }
        } catch (Exception ioe) {
            System.err.println("Błąd szukania rekordu w bazie danych");
        }
    }

    private static void obslugaLista() {
        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            TypedQuery<Pojazd> zapytanie = session.createQuery("FROM Pojazd", Pojazd.class);
            List<Pojazd> lista = zapytanie.getResultList();

            // najkrotsza
//            lista.forEach(System.out::println);

            // najkrotsza - 1
//            lista.forEach((paramPojazd) -> {
//                System.out.println(paramPojazd);
//            });

            // najkrotsza - 2
//            lista.forEach(new Consumer<Pojazd>() {
//                @Override
//                public void accept(Pojazd paramPojazd) {
//                    System.out.println(paramPojazd);
//                }
//            });

            // prymitywna
            for (Pojazd pojazd : lista) {
                System.out.println(pojazd);
            }

        } catch (Exception ioe) {
            System.err.println("Błąd listowania rekordów z bazy danych");
        }
    }

    private static void obslugaDodaj() {
        Pojazd p = wczytajParametryPojazdu();

        try (Session session = HibernateUtil.INSTANCE.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(p);

            transaction.commit();
        } catch (Exception e) {
            System.err.println("Błąd dodawania do bazy danych");
        }
    }

    private static Pojazd wczytajParametryPojazdu() {
        System.out.println("Podaj markę:");
        String marka = JakasKlasa.scanner.nextLine();

        System.out.println("Podaj moc:");
        String mocString = JakasKlasa.scanner.nextLine();
        double moc = Double.parseDouble(mocString);

        System.out.println("Podaj kolor:");
        String kolor = JakasKlasa.scanner.nextLine();

        Integer rokProdukcji = null;
        do {
            System.out.println("Podaj rok produkcji:");
            String rokProdukcjiString = JakasKlasa.scanner.nextLine();
            rokProdukcji = Integer.parseInt(rokProdukcjiString);

            if (rokProdukcji < 1990 || rokProdukcji > 2020) {
                // ustawiamy null by wymusić powtórzenie pętli
                rokProdukcji = null;
            }
        } while (rokProdukcji == null);

        System.out.println("Czy jest elektryczny:");
        String elektrycznyString = JakasKlasa.scanner.nextLine();
        boolean elektryczny = Boolean.parseBoolean(elektrycznyString);

        return Pojazd.builder()
                .rokProdukcji(rokProdukcji)
                .elektryczny(elektryczny)
                .kolor(kolor)
                .marka(marka)
                .moc(moc)
                .build();
    }
}
