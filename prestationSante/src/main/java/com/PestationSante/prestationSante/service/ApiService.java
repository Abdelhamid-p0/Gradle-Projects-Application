package com.PestationSante.prestationSante.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {


    public ObjectNode traiterPaiementAPI(int idBank, String nom, String rib, String password) {
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

        int randomNumber = new java.util.Random().nextInt(9901) + 100;
        transaction.put("amount", randomNumber);


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
