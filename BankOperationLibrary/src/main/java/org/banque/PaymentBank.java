package org.banque;

import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.digest.DigestUtils;



public class PaymentBank {

    private static final ObjectMapper mapper = new ObjectMapper(); // doit être réutilisé

    public static ObjectNode pay(String name, String rib) {
        System.out.println("Banque 1");

        int amount = new Random().nextInt(1000) + 1;

        System.out.println("Withdrawal from account of " + name + " in the amount of " + amount);

        // Création d'un ObjectNode via ObjectMapper
        ObjectNode response = mapper.createObjectNode();
        response.put("bank", "Banque 1");
        response.put("name", name);
        response.put("rib", rib);
        response.put("amount", amount);

        return response;
    }

    public static String authenticate(String name, String password) {
        // simulate hashing the password
        String hashed = DigestUtils.sha256Hex(password);
        System.out.println("Authenticating user " + name + " with hash: " + hashed);
        return hashed;
    }

}