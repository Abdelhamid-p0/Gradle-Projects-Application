package com.banque.bmce;

import org.springframework.stereotype.Service;

@Service
public class TransactionOperation {
    public void payementOperation(String message) {

        System.out.println("payementOperation");
        System.out.println(message);
        System.out.println("Payement réussi");

    }
}
