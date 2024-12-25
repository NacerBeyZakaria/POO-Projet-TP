import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// الفئة الرئيسية
public class GestionCabinetMedical {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // خدمات الإدارة
    private static PatientService patientService = new PatientService();
    private static MedecinService medecinService = new MedecinService();
    private static RendezVousService rendezVousService = new RendezVousService();
    private static DossierMedicalService dossierMedicalService = new DossierMedicalService();

    // إنشاء كائن السكرتير
    private static Secretary secretary = new Secretary("Alice", patientService, medecinService, rendezVousService);

    public static void main(String[] args) {
        int choix;
        do {
            afficherMenu();
            choix = lireEntier("Choisissez une option: ");
            switch (choix) {
                case 1:
                    ajouterPatient();
                    break;
                case 2:
                    afficherPatients();
                    break;
                case 3:
                    supprimerPatient();
                    break;
                case 4:
                    ajouterMedecin();
                    break;
                case 5:
                    afficherMedecins();
                    break;
                case 6:
                    supprimerMedecin();
                    break;
                case 7:
                    prendreRendezVous();
                    break;
                case 8:
                    afficherRendezVousParMedecin();
                    break;
                case 9:
                    afficherRendezVousParPatient();
                    break;
                case 10:
                    annulerRendezVous();
                    break;
                case 11:
                    ajouterDossierMedical();
                    break;
                case 12:
                    afficherDossiersParPatient();
                    break;
                case 13:
                    supprimerDossierMedical();
                    break;
                case 0:
                    System.out.println("Sortie du programme.");
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        } while (choix != 0);
    }

    private static void afficherMenu() {
        System.out.println("\n=== Gestion Cabinet Médical ===");
        System.out.println("1. Ajouter un patient");
        System.out.println("2. Afficher tous les patients");
        System.out.println("3. Supprimer un patient");
        System.out.println("4. Ajouter un médecin");
        System.out.println("5. Afficher tous les médecins");
        System.out.println("6. Supprimer un médecin");
        System.out.println("7. Prendre un rendez-vous");
        System.out.println("8. Afficher les rendez-vous par médecin");
        System.out.println("9. Afficher les rendez-vous par patient");
        System.out.println("10. Annuler un rendez-vous");
        System.out.println("11. Ajouter un dossier médical");
        System.out.println("12. Afficher les dossiers médicaux d'un patient");
        System.out.println("13. Supprimer un dossier médical");
        System.out.println("0. Quitter");
    }

    private static int lireEntier(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        int valeur = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return valeur;
    }

    private static double lireDouble(String message) {
        System.out.print(message);
        while (!scanner.hasNextDouble()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        double valeur = scanner.nextDouble();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return valeur;
    }

    // طرق إدارة المرضى
    private static void ajouterPatient() {
        System.out.println("\n--- Ajouter un Patient ---");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();

        // Utiliser le secrétaire pour ajouter le patient avec les détails de base
        Patient patient = secretary.ajouterPatient(nom, prenom, telephone, "", "", 0.0, 0.0, "", "");
        
        // Demander des détails supplémentaires si nécessaire
        System.out.print("Adresse (optionnel): ");
        String adresse = scanner.nextLine();
        if (!adresse.isEmpty()) {
            patient.setAdresse(adresse);
        }

        System.out.print("Date de Naissance (dd/MM/yyyy) (optionnel): ");
        String dateNaissance = scanner.nextLine();
        if (!dateNaissance.isEmpty()) {
            patient.setDateNaissance(dateNaissance);
        }

        String poidsStr = "";
        System.out.print("Poids (kg) (optionnel): ");
        poidsStr = scanner.nextLine();
        if (!poidsStr.isEmpty()) {
            try {
                double poids = Double.parseDouble(poidsStr);
                patient.setPoids(poids);
            } catch (NumberFormatException e) {
                System.out.println("Poids invalide. Valeur par défaut 0.0 kg utilisée.");
            }
        }

        String tailleStr = "";
        System.out.print("Taille (cm) (optionnel): ");
        tailleStr = scanner.nextLine();
        if (!tailleStr.isEmpty()) {
            try {
                double taille = Double.parseDouble(tailleStr);
                patient.setTaille(taille);
            } catch (NumberFormatException e) {
                System.out.println("Taille invalide. Valeur par défaut 0.0 cm utilisée.");
            }
        }

        System.out.print("Allergies (optionnel): ");
        String allergies = scanner.nextLine();
        if (!allergies.isEmpty()) {
            patient.setAllergies(allergies);
        }

        System.out.print("Tares (optionnel): ");
        String tares = scanner.nextLine();
        if (!tares.isEmpty()) {
            patient.setTares(tares);
        }

        System.out.println("Patient ajouté avec succès !");
    }

    private static void afficherPatients() {
        System.out.println("\n--- Liste des Patients ---");
        List<Patient> patients = patientService.getTousLesPatients();
        if (patients.isEmpty()) {
            System.out.println("Aucun patient enregistré.");
        } else {
            for (Patient p : patients) {
                System.out.println(p);
            }
        }
    }

    private static void supprimerPatient() {
        System.out.println("\n--- Supprimer un Patient ---");
        long id = lireLong("Entrez l'ID du patient à supprimer: ");
        Optional<Patient> patientOpt = patientService.getPatientParId(id);
        if (patientOpt.isPresent()) {
            patientService.supprimerPatient(id);
        } else {
            System.out.println("Patient non trouvé.");
        }
    }

    // طرق إدارة الأطباء
    private static void ajouterMedecin() {
        System.out.println("\n--- Ajouter un Médecin ---");
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        System.out.print("Spécialité: ");
        String specialite = scanner.nextLine();
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Medecin medecin = new Medecin(nom, prenom, specialite, telephone, email);
        medecinService.ajouterMedecin(medecin);
    }

    private static void afficherMedecins() {
        System.out.println("\n--- Liste des Médecins ---");
        List<Medecin> medecins = medecinService.getTousLesMedecins();
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin enregistré.");
        } else {
            for (Medecin m : medecins) {
                System.out.println(m);
            }
        }
    }

    private static void supprimerMedecin() {
        System.out.println("\n--- Supprimer un Médecin ---");
        long id = lireLong("Entrez l'ID du médecin à supprimer: ");
        Optional<Medecin> medecinOpt = medecinService.getMedecinParId(id);
        if (medecinOpt.isPresent()) {
            medecinService.supprimerMedecin(id);
        } else {
            System.out.println("Médecin non trouvé.");
        }
    }

    // طرق إدارة المواعيد
    private static void prendreRendezVous() {
        System.out.println("\n--- Prendre un Rendez-vous ---");
        afficherPatients();
        long patientId = lireLong("Entrez l'ID du patient: ");
        Optional<Patient> patientOpt = patientService.getPatientParId(patientId);
        if (!patientOpt.isPresent()) {
            System.out.println("Patient non trouvé.");
            return;
        }

        afficherMedecins();
        long medecinId = lireLong("Entrez l'ID du médecin: ");
        Optional<Medecin> medecinOpt = medecinService.getMedecinParId(medecinId);
        if (!medecinOpt.isPresent()) {
            System.out.println("Médecin non trouvé.");
            return;
        }

        System.out.print("Date et Heure du Rendez-vous (dd/MM/yyyy HH:mm): ");
        String dateHeureStr = scanner.nextLine();
        LocalDateTime dateHeure;
        try {
            dateHeure = LocalDateTime.parse(dateHeureStr, formatter);
        } catch (Exception e) {
            System.out.println("Format de date invalide.");
            return;
        }

        // Utiliser le secrétaire pour prendre le rendez-vous
        secretary.prendreRendezVous(patientId, medecinId, dateHeure);
    }

    private static void afficherRendezVousParMedecin() {
        System.out.println("\n--- Rendez-vous par Médecin ---");
        afficherMedecins();
        long medecinId = lireLong("Entrez l'ID du médecin: ");
        List<RendezVous> rdvs = rendezVousService.getRendezVousParMedecin(medecinId);
        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé pour ce médecin.");
        } else {
            for (RendezVous rdv : rdvs) {
                System.out.println(rdv);
            }
        }
    }

    private static void afficherRendezVousParPatient() {
        System.out.println("\n--- Rendez-vous par Patient ---");
        afficherPatients();
        long patientId = lireLong("Entrez l'ID du patient: ");
        List<RendezVous> rdvs = rendezVousService.getRendezVousParPatient(patientId);
        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé pour ce patient.");
        } else {
            for (RendezVous rdv : rdvs) {
                System.out.println(rdv);
            }
        }
    }

    private static void annulerRendezVous() {
        System.out.println("\n--- Annuler un Rendez-vous ---");
        long id = lireLong("Entrez l'ID du rendez-vous à annuler: ");
        Optional<RendezVous> rdvOpt = rendezVousService.getRendezVousParId(id);
        if (rdvOpt.isPresent()) {
            RendezVous rdv = rdvOpt.get();
            if (rdv.getStatut().equalsIgnoreCase("Annulé")) {
                System.out.println("Ce rendez-vous est déjà annulé.");
                return;
            }
            rdv.setStatut("Annulé");
            System.out.println("Rendez-vous annulé avec succès !");
        } else {
            System.out.println("Rendez-vous non trouvé.");
        }
    }

    // طرق إدارة السجلات الطبية
    private static void ajouterDossierMedical() {
        System.out.println("\n--- Ajouter un Dossier Médical ---");
        afficherPatients();
        long patientId = lireLong("Entrez l'ID du patient: ");
        Optional<Patient> patientOpt = patientService.getPatientParId(patientId);
        if (!patientOpt.isPresent()) {
            System.out.println("Patient non trouvé.");
            return;
        }

        afficherMedecins();
        long medecinId = lireLong("Entrez l'ID du médecin: ");
        Optional<Medecin> medecinOpt = medecinService.getMedecinParId(medecinId);
        if (!medecinOpt.isPresent()) {
            System.out.println("Médecin non trouvé.");
            return;
        }

        System.out.print("Date de Consultation (dd/MM/yyyy): ");
        String dateConsultation = scanner.nextLine();
        System.out.print("Observations: ");
        String observations = scanner.nextLine();
        System.out.print("Traitement: ");
        String traitement = scanner.nextLine();

        DossierMedical dossier = new DossierMedical(patientOpt.get(), medecinOpt.get(),
                dateConsultation, observations, traitement);
        dossierMedicalService.ajouterDossierMedical(dossier);

        // إضافة وصفة طبية
        System.out.print("Voulez-vous ajouter une ordonnance ? (oui/non): ");
        String reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("oui")) {
            System.out.print("Médicament: ");
            String medicament = scanner.nextLine();
            System.out.print("Dose: ");
            String dose = scanner.nextLine();
            System.out.print("Durée (jours): ");
            String duree = scanner.nextLine();
            String ordonnance = "Médicament: " + medicament + ", Dose: " + dose + ", Durée: " + duree;
            dossier.ajouterOrdonnance(ordonnance);
            System.out.println("Ordonnance ajoutée.");
        }

        // إضافة شهادة طبية
        System.out.print("Voulez-vous ajouter un certificat ? (oui/non): ");
        reponse = scanner.nextLine();
        if (reponse.equalsIgnoreCase("oui")) {
            System.out.print("Type: ");
            String type = scanner.nextLine();
            System.out.print("Description: ");
            String description = scanner.nextLine();
            String certificat = "Type: " + type + ", Description: " + description;
            dossier.ajouterCertificat(certificat);
            System.out.println("Certificat ajouté.");
        }
    }

    private static void afficherDossiersParPatient() {
        System.out.println("\n--- Dossiers Médicaux par Patient ---");
        afficherPatients();
        long patientId = lireLong("Entrez l'ID du patient: ");
        List<DossierMedical> dossiers = dossierMedicalService.getDossiersParPatient(patientId);
        if (dossiers.isEmpty()) {
            System.out.println("Aucun dossier médical trouvé pour ce patient.");
        } else {
            for (DossierMedical dm : dossiers) {
                System.out.println(dm);
            }
        }
    }

    private static void supprimerDossierMedical() {
        System.out.println("\n--- Supprimer un Dossier Médical ---");
        long id = lireLong("Entrez l'ID du dossier médical à supprimer: ");
        Optional<DossierMedical> dossierOpt = dossierMedicalService.getDossierMedicalParId(id);
        if (dossierOpt.isPresent()) {
            dossierMedicalService.supprimerDossierMedical(id);
        } else {
            System.out.println("Dossier médical non trouvé.");
        }
    }

    private static long lireLong(String message) {
        System.out.print(message);
        while (!scanner.hasNextLong()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            scanner.next();
        }
        long valeur = scanner.nextLong();
        scanner.nextLine(); // Consommer la nouvelle ligne
        return valeur;
    }
}

// ============================
// الفئات الأساسية
// ============================

class Patient {
    private static long idCounter = 1;
    private long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String dateNaissance;
    private double poids;
    private double taille;
    private String allergies;
    private String tares;

    public Patient(String nom, String prenom, String telephone, String adresse, String dateNaissance,
                  double poids, double taille, String allergies, String tares) {
        this.id = idCounter++;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.poids = poids;
        this.taille = taille;
        this.allergies = allergies;
        this.tares = tares;
    }

    // Getters

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public double getPoids() {
        return poids;
    }

    public double getTaille() {
        return taille;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getTares() {
        return tares;
    }

    // Setters

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setTares(String tares) {
        this.tares = tares;
    }

    @Override
    public String toString() {
        return "Patient ID: " + id +
                ", Nom: " + nom +
                ", Prénom: " + prenom +
                ", Téléphone: " + telephone +
                ", Adresse: " + (adresse.isEmpty() ? "N/A" : adresse) +
                ", Date de Naissance: " + (dateNaissance.isEmpty() ? "N/A" : dateNaissance) +
                ", Poids: " + (poids > 0 ? poids + " kg" : "N/A") +
                ", Taille: " + (taille > 0 ? taille + " cm" : "N/A") +
                ", Allergies: " + (allergies.isEmpty() ? "N/A" : allergies) +
                ", Tares: " + (tares.isEmpty() ? "N/A" : tares);
    }
}

class Medecin {
    private static long idCounter = 1;
    private long id;
    private String nom;
    private String prenom;
    private String specialite;
    private String telephone;
    private String email;

    public Medecin(String nom, String prenom, String specialite, String telephone, String email) {
        this.id = idCounter++;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Médecin ID: " + id +
                ", Nom: " + nom +
                ", Prénom: " + prenom +
                ", Spécialité: " + specialite +
                ", Téléphone: " + telephone +
                ", Email: " + email;
    }

    /**
     * Vérifie si le médecin est disponible à la date et l'heure spécifiées.
     *
     * @param dateHeure          La date et l'heure du rendez-vous.
     * @param rendezVousService  Le service de gestion des rendez-vous.
     * @return Vrai si disponible, faux sinon.
     */
    public boolean isAvailable(LocalDateTime dateHeure, RendezVousService rendezVousService) {
        List<RendezVous> rdvs = rendezVousService.getRendezVousParMedecin(this.id);
        for (RendezVous rdv : rdvs) {
            if (rdv.getDateHeure().equals(dateHeure) && rdv.getStatut().equalsIgnoreCase("Confirmé")) {
                return false;
            }
        }
        return true;
    }
}

class RendezVous {
    private static long idCounter = 1;
    private long id;
    private Patient patient;
    private Medecin medecin;
    private LocalDateTime dateHeure;
    private String statut; // Confirmé, Annulé, etc.

    public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String statut) {
        this.id = idCounter++;
        this.patient = patient;
        this.medecin = medecin;
        this.dateHeure = dateHeure;
        this.statut = statut;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "RendezVous ID: " + id +
                ", Patient: " + patient.getNom() + " " + patient.getPrenom() +
                ", Médecin: " + medecin.getNom() + " " + medecin.getPrenom() +
                ", Date et Heure: " + dateHeure.format(formatter) +
                ", Statut: " + statut;
    }
}

class DossierMedical {
    private static long idCounter = 1;
    private long id;
    private Patient patient;
    private Medecin medecin;
    private String dateConsultation;
    private String observations;
    private String traitement;
    private List<String> ordonnances;
    private List<String> certificats;

    public DossierMedical(Patient patient, Medecin medecin, String dateConsultation,
                         String observations, String traitement) {
        this.id = idCounter++;
        this.patient = patient;
        this.medecin = medecin;
        this.dateConsultation = dateConsultation;
        this.observations = observations;
        this.traitement = traitement;
        this.ordonnances = new ArrayList<>();
        this.certificats = new ArrayList<>();
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public String getDateConsultation() {
        return dateConsultation;
    }

    public String getObservations() {
        return observations;
    }

    public String getTraitement() {
        return traitement;
    }

    public List<String> getOrdonnances() {
        return ordonnances;
    }

    public List<String> getCertificats() {
        return certificats;
    }

    public void ajouterOrdonnance(String ordonnance) {
        ordonnances.add(ordonnance);
    }

    public void ajouterCertificat(String certificat) {
        certificats.add(certificat);
    }

    @Override
    public String toString() {
        return "Dossier Médical ID: " + id +
                ", Patient: " + patient.getNom() + " " + patient.getPrenom() +
                ", Médecin: " + medecin.getNom() + " " + medecin.getPrenom() +
                ", Date Consultation: " + dateConsultation +
                ", Observations: " + observations +
                ", Traitement: " + traitement +
                ", Ordonnances: " + (ordonnances.isEmpty() ? "N/A" : ordonnances) +
                ", Certificats: " + (certificats.isEmpty() ? "N/A" : certificats);
    }
}

// ============================
// خدمات الإدارة
// ============================

class PatientService {
    private List<Patient> patients = new ArrayList<>();

    public void ajouterPatient(Patient patient) {
        patients.add(patient);
        System.out.println("Patient ajouté avec succès !");
    }

    public List<Patient> getTousLesPatients() {
        return patients;
    }

    public Optional<Patient> getPatientParId(long id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst();
    }

    public void supprimerPatient(long id) {
        patients.removeIf(p -> p.getId() == id);
        System.out.println("Patient supprimé avec succès !");
    }
}

class MedecinService {
    private List<Medecin> medecins = new ArrayList<>();

    public void ajouterMedecin(Medecin medecin) {
        medecins.add(medecin);
        System.out.println("Médecin ajouté avec succès !");
    }

    public List<Medecin> getTousLesMedecins() {
        return medecins;
    }

    public Optional<Medecin> getMedecinParId(long id) {
        return medecins.stream().filter(m -> m.getId() == id).findFirst();
    }

    public void supprimerMedecin(long id) {
        medecins.removeIf(m -> m.getId() == id);
        System.out.println("Médecin supprimé avec succès !");
    }
}

class RendezVousService {
    private List<RendezVous> rendezVousList = new ArrayList<>();

    public void prendreRendezVous(RendezVous rendezVous) {
        rendezVousList.add(rendezVous);
        System.out.println("Rendez-vous pris avec succès !");
    }

    public List<RendezVous> getRendezVousParMedecin(long medecinId) {
        List<RendezVous> result = new ArrayList<>();
        for (RendezVous rdv : rendezVousList) {
            if (rdv.getMedecin().getId() == medecinId) {
                result.add(rdv);
            }
        }
        return result;
    }

    public List<RendezVous> getRendezVousParPatient(long patientId) {
        List<RendezVous> result = new ArrayList<>();
        for (RendezVous rdv : rendezVousList) {
            if (rdv.getPatient().getId() == patientId) {
                result.add(rdv);
            }
        }
        return result;
    }

    public Optional<RendezVous> getRendezVousParId(long id) {
        return rendezVousList.stream().filter(r -> r.getId() == id).findFirst();
    }

    public void annulerRendezVous(long id) {
        rendezVousList.removeIf(r -> r.getId() == id);
        System.out.println("Rendez-vous annulé avec succès !");
    }
}

class DossierMedicalService {
    private List<DossierMedical> dossiers = new ArrayList<>();

    public void ajouterDossierMedical(DossierMedical dossier) {
        dossiers.add(dossier);
        System.out.println("Dossier médical ajouté avec succès !");
    }

    public List<DossierMedical> getDossiersParPatient(long patientId) {
        List<DossierMedical> result = new ArrayList<>();
        for (DossierMedical dm : dossiers) {
            if (dm.getPatient().getId() == patientId) {
                result.add(dm);
            }
        }
        return result;
    }

    public Optional<DossierMedical> getDossierMedicalParId(long id) {
        return dossiers.stream().filter(d -> d.getId() == id).findFirst();
    }

    public void supprimerDossierMedical(long id) {
        dossiers.removeIf(d -> d.getId() == id);
        System.out.println("Dossier médical supprimé avec succès !");
    }
}

// ============================
// فئة السكرتير
// ============================

class Secretary {
    private String name;
    private PatientService patientService;
    private MedecinService medecinService;
    private RendezVousService rendezVousService;

    public Secretary(String name, PatientService patientService, MedecinService medecinService, RendezVousService rendezVousService) {
        this.name = name;
        this.patientService = patientService;
        this.medecinService = medecinService;
        this.rendezVousService = rendezVousService;
    }

    /**
     * Ajoute un nouveau patient avec les détails de base.
     *
     * @param nom           Le nom de famille du patient.
     * @param prenom        Le prénom du patient.
     * @param telephone     Le numéro de téléphone du patient.
     * @param adresse       L'adresse du patient.
     * @param dateNaissance La date de naissance du patient.
     * @param poids         Le poids du patient.
     * @param taille        La taille du patient.
     * @param allergies     Les allergies du patient.
     * @param tares         Les tares ou handicaps du patient.
     * @return L'objet Patient nouvellement ajouté.
     */
    public Patient ajouterPatient(String nom, String prenom, String telephone, String adresse,
                                  String dateNaissance, double poids, double taille,
                                  String allergies, String tares) {
        Patient patient = new Patient(nom, prenom, telephone, adresse, dateNaissance, poids, taille, allergies, tares);
        patientService.ajouterPatient(patient);
        return patient;
    }

    /**
     * Planifie un rendez-vous si le médecin est disponible à la date et l'heure données.
     *
     * @param patientId L'ID du patient.
     * @param medecinId L'ID du médecin.
     * @param dateHeure La date et l'heure du rendez-vous.
     */
    public void prendreRendezVous(long patientId, long medecinId, LocalDateTime dateHeure) {
        Optional<Patient> patientOpt = patientService.getPatientParId(patientId);
        if (!patientOpt.isPresent()) {
            System.out.println("Patient non trouvé.");
            return;
        }

        Optional<Medecin> medecinOpt = medecinService.getMedecinParId(medecinId);
        if (!medecinOpt.isPresent()) {
            System.out.println("Médecin non trouvé.");
            return;
        }

        Medecin medecin = medecinOpt.get();
        if (!medecin.isAvailable(dateHeure, rendezVousService)) {
            System.out.println("Le médecin n'est pas disponible à ce créneau.");
            return;
        }

        // Par défaut, le statut est "Confirmé" lors de la prise d'un rendez-vous
        String statut = "Confirmé";
        RendezVous rdv = new RendezVous(patientOpt.get(), medecin, dateHeure, statut);
        rendezVousService.prendreRendezVous(rdv);
        System.out.println("Rendez-vous pris avec succès !");
    }

    // Getters and Setters 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    
}
