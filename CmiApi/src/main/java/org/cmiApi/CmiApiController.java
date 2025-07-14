package org.cmiApi;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/banks")
public class CmiApiController {

        @PostMapping("/{token}")
        public ObjectNode getBankAccess(@PathVariable String token , @RequestBody ObjectNode request) throws IOException {
             if (token.equals("0000")) {
                 return  CmiApiService.getAccess(request);
             }
             /*
             *    public static ObjectNode getAccess(ObjectNode request) throws IOException {
        int targetId = request.get("id").asInt();

        try (InputStream is = CmiApiService.class.getClassLoader().getResourceAsStream("data/dataBank.json")) {
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
             *
             * */

            return null ; //Accées refusé
        }

        @PostMapping("/transaction")
        public ObjectNode createTransaction(@RequestBody ObjectNode transaction) throws IOException {

         return CmiApiService.userTransaction(transaction);

        }

}







