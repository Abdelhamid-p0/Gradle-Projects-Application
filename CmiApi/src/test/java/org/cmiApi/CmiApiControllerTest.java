package org.cmiApi;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/banks")
public class CmiApiControllerTest {


        @PostMapping("/{token}")
        public ObjectNode getBankAccess(@PathVariable String token , @RequestBody ObjectNode request) throws IOException {
             if (token.equals("0000")) {
                 return  cmiApiService.getAccess(request);
             }

            return null ; //Accées refusé
        }

        @PostMapping("/transaction")
        public ObjectNode createTransaction(@RequestBody ObjectNode transaction) throws IOException {

         return cmiApiService.userTransaction(transaction);

        }

}







