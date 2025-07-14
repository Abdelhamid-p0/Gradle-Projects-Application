package com.PestationSante.prestationSante.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class FormController {

    /**
     * Affiche le formulaire de paiement
     */

    @GetMapping("/paiement")
    public String afficherFormulaire(Model model) {
        return "formulaire"; // Correspond à formulaire.html dans resources/templates
    }

    /**
     * Traite les données du formulaire de paiement
     */
    @PostMapping("/paiement")
    public String traiterPaiement(
            @RequestParam("nom") String nom,
            @RequestParam("rib") String rib,
            @RequestParam("password") String password,
            Model model) {

        try {
            // Validation des données
            if (nom == null || nom.trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom du titulaire est obligatoire");
            }

            if (rib == null || rib.trim().isEmpty()) {
                throw new IllegalArgumentException("Le RIB est obligatoire");
            }

            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Le mot de passe est obligatoire");
            }



            // Simulation du traitement du paiement
            //id extrait du rib
             int idBank = 1;
            ObjectNode paiementReussi = traiterPaiementAPI(idBank , nom, rib, password);

            if (paiementReussi != null) {
                // Paiement réussi
                System.out.println(paiementReussi);
                model.addAttribute("success", true);
                model.addAttribute("message", "Paiement effectué avec succès !");
                model.addAttribute("montant", paiementReussi.get("amount").asText());
                model.addAttribute("nom", nom);
            } else {
                // Paiement échoué
                model.addAttribute("success", false);
                model.addAttribute("message", "Échec du paiement. Vérifiez vos informations bancaires.");
            }

        } catch (Exception e) {
            // Gestion des erreurs
            model.addAttribute("success", false);
            model.addAttribute("message", "Erreur : " + e.getMessage());
        }

        return "formulaire"; // Retourne la même page avec les résultats
    }


    /**
     * Communique avec l'API CMI pour traiter le paiement
     */
    private ObjectNode traiterPaiementAPI(int idBank, String nom, String rib, String password) {
        try {
            // Étape 1 : Vérifier l'accès bancaire
            ObjectNode accessRequest = createAccessRequest(idBank);
            ObjectNode accessResponse = callBankAccessAPI(accessRequest);


            // Étape 2 : Créer la transaction
            ObjectNode transactionRequest = createTransactionRequest(nom, rib, password, accessResponse);
            ObjectNode transactionResponse = callTransactionAPI(transactionRequest);

            return transactionResponse;

        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du paiement : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crée la requête pour vérifier l'accès bancaire
     */
    private ObjectNode createAccessRequest(int id) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode request = mapper.createObjectNode();
        request.put("id", id);
        System.out.println(request.toString());

        return request;
    }

    /**
     * Appelle l'API d'accès bancaire
     */
    private ObjectNode callBankAccessAPI(ObjectNode request) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ObjectNode> entity = new HttpEntity<>(request, headers);

            // Appel à votre API avec le token "0000"
            String url = "http://localhost:8080/api/banks/0000";
            ResponseEntity<ObjectNode> response = restTemplate.postForEntity(url, entity, ObjectNode.class);

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API d'accès : " + e.getMessage());
            return null;
        }
    }

    /**
     * Crée la requête de transaction
     */
    private ObjectNode createTransactionRequest(String nom, String rib, String password, ObjectNode accessResponse) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();
        transaction.put("name", nom);
        transaction.put("rib", rib);
        transaction.put("password", password);

        // Ajouter les données d'accès si disponibles
        if (accessResponse != null && accessResponse.has("key")) {
            transaction.put("key", accessResponse.get("key").asText());
        }
        System.out.println(transaction);
        return transaction;
    }

    /**
     * Appelle l'API de transaction
     */
    private ObjectNode callTransactionAPI(ObjectNode transaction) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ObjectNode> entity = new HttpEntity<>(transaction, headers);

            String url = "http://localhost:8080/api/banks/transaction";
            ResponseEntity<ObjectNode> response = restTemplate.postForEntity(url, entity, ObjectNode.class);

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API de transaction : " + e.getMessage());
            return null;
        }
    }





}