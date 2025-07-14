package com.PestationSante.prestationSante.controller;

import com.PestationSante.prestationSante.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class FormController {

    ApiService apiService;

     public FormController(ApiService apiService) {

         this.apiService = apiService;

     }
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
            ObjectNode paiementReussi = apiService.traiterPaiementAPI(idBank , nom, rib, password);

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




}