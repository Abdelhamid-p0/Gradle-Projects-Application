package org.banque;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


@Service
public class PaymentBank {

    private static final ObjectMapper mapper = new ObjectMapper();

    BankTransaction bankTransaction;

    private String transactionStatus;


    PaymentBank(BankTransaction bankTransaction) {
        this.bankTransaction = bankTransaction;
    }


    public static String authenticate(String name, String password) {
        // simulate hashing the password
        String hashed = DigestUtils.sha256Hex(password);
        System.out.println("Authenticating user " + name + " with hash: " + hashed);
        return hashed;
    }



    public ObjectNode pay(String BankName, String rib, double amount) throws InterruptedException {

        String statusTransaction = "failed" ;

        System.out.println("Withdrawal from account of " + BankName + " in the amount of " + amount);

        //bankTransaction(kafka)
        this.transactionStatus = bankTransaction.bankTransactionPub(BankName , rib, amount);

        if (this.transactionStatus.equals("success")) {
            statusTransaction = "success";
        }

        // Création d'un ObjectNode via ObjectMapper
        ObjectNode response = mapper.createObjectNode();
        response.put("bankName", BankName);
        response.put("rib", rib);
        response.put("amount", amount);
        response.put("status", statusTransaction);

        return response;
    }

}