package org.cmiApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.banque.PaymentBank;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CmiApiService {
    private static final ObjectMapper mapper = new ObjectMapper();
    PaymentBank  paymentBank;

    CmiApiService(PaymentBank paymentBank) {
        this.paymentBank = paymentBank;
    }


    public  ObjectNode getAccess(ObjectNode request) throws IOException {

        System.out.println("1)- Vérification du droit d'accées au banque");
        int targetId = request.get("id").asInt();

        try (InputStream is = CmiApiService.class.getClassLoader().getResourceAsStream("data/bankData.json")) {
            if (is == null) {
                throw new IOException("Fichier data/dataBank.json introuvable dans les ressources");
            }

            JsonNode root = mapper.readTree(is);
            JsonNode banksArray = root.get("banks");

            for (JsonNode bankNode : banksArray) {
                if (bankNode.get("id").asInt() == targetId) {
                    System.out.println("2)- Droit d'accées vérifiées ");
                    System.out.println("Bank data: "+bankNode);
                    return (ObjectNode) bankNode;
                }
            }
        }
        return null;
    }

    public  ObjectNode userTransaction(ObjectNode transaction) throws IOException, InterruptedException {
        int targetKey = transaction.get("key").asInt();
        String name = transaction.get("name").asText();
        String rib = transaction.get("rib").asText();
        String password = transaction.get("password").asText();
        double amount = transaction.get("amount").asDouble();

        System.out.println("Transaction reçu: " +transaction);

        InputStream is = CmiApiService.class.getClassLoader().getResourceAsStream("data/bankData.json");
            if (is == null) {
                throw new IOException("Fichier data/dataBank.json introuvable dans les ressources");
            }

            JsonNode root = mapper.readTree(is);
            JsonNode banksArray = root.get("banks");

            for (JsonNode bankNode : banksArray) {
                System.out.println("1)- Vérifier le Key envoyé");
                if (bankNode.get("key").asInt() == targetKey) {
                    System.out.println("2)- Key correct");
                    String BankName = bankNode.get("name").asText();
                    System.out.println("3)- Authentification d'utilisateur");
                    PaymentBank.authenticate(name, password);

                    System.out.println("4)- Debut de l'opération du payement ");

                    return paymentBank.pay(BankName, rib, amount );
                }
            }

        System.out.println("payement not success");

        return null;
    }
}
