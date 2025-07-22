package org.cmiApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.banque.PaymentBank;

import java.io.IOException;
import java.io.InputStream;

public class CmiApiServiceTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode getAccess(ObjectNode request) throws IOException {
        int targetId = request.get("id").asInt();

        try (InputStream is = CmiApiService.class.getClassLoader().getResourceAsStream("data/bankData.json")) {
            if (is == null) {
                throw new IOException("Fichier data/dataBank.json introuvable dans les ressources");
            }

            JsonNode root = mapper.readTree(is);
            JsonNode banksArray = root.get("banks");

            for (JsonNode bankNode : banksArray) {
                if (bankNode.get("id").asInt() == targetId) {
                    return (ObjectNode) bankNode;
                }
            }
        }
        return null;
    }

    public static ObjectNode userTransaction(ObjectNode transaction) throws IOException {
        int targetKey = transaction.get("key").asInt();
        String name = transaction.get("name").asText();
        String rib = transaction.get("rib").asText();
        String password = transaction.get("password").asText();
        System.out.println(transaction);

        InputStream is = CmiApiService.class.getClassLoader().getResourceAsStream("data/bankData.json");
            if (is == null) {
                throw new IOException("Fichier data/dataBank.json introuvable dans les ressources");
            }

            JsonNode root = mapper.readTree(is);
            JsonNode banksArray = root.get("banks");

            System.out.println(banksArray);

            for (JsonNode bankNode : banksArray) {
                if (bankNode.get("key").asInt() == targetKey) {

                    PaymentBank.authenticate(name, password);
                   System.out.println("payement success");
                    return PaymentBank.pay(name, rib);
                }
            }

        System.out.println("payement not success");

        return null;
    }
}
