package org.cmiApi;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/banks")
public class CmiApiController {

        CmiApiService cmiApiService;
        CmiApiController(CmiApiService cmiApiService) {
            this.cmiApiService = cmiApiService;
        }

        @PostMapping("/{token}")
        public ObjectNode getBankAccess(@PathVariable String token , @RequestBody ObjectNode request) throws IOException {
             if (token.equals("0000")) {
                 return  cmiApiService.getAccess(request);
             }
             /*
             {
              id : *int
             }
             - token pour verifier que l application est inscrit chez CMI
             (On suppose que le seule site à pour id "0000")
             - La fonction getAccess prend id pour verifier que la banque voulu est souscrie
             + en mode active et pas en panne
              */
            return null ; //Accées refusé
        }

        @PostMapping("/transaction")
        public ObjectNode createTransaction(@RequestBody ObjectNode transaction) throws IOException, InterruptedException {
         /*
         *("key").asInt();
        ("name").asText();
        ("rib").asText();
        ("password").asText();
        *("amount").asDouble(); */
         return cmiApiService.userTransaction(transaction);
        }

}







