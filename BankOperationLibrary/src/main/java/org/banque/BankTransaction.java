package org.banque;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class BankTransaction {

        /*
        publier Transaction
        Consommer le status
         */

    private final KafkaTemplate<String, String> kafkaTemplate;
    private  String  topic ;
    private String   transactionStatus ;
    private final CountDownLatch latch = new CountDownLatch(1);

    public BankTransaction(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    public  String  bankTransactionPub(String BankName, String rib, double amount) throws InterruptedException {

        this.topic = BankName ;

        //publier Transaction
        TransactionEvent transactionEvent =  new TransactionEvent(rib, amount);
        kafkaTemplate.send(topic,transactionEvent.toString());

        this.transactionStatus = "in progress";
        //Attender la consommation du status
        latch.await(5, TimeUnit.SECONDS);

        if  (!this.transactionStatus.equals("success")) {
            this.transactionStatus = "failed";
        }

        return transactionStatus;

    }


    @KafkaListener(topics ="status-succes", groupId = "my-group")
    public String consume(String event) {

        this.transactionStatus = "succes";
        return event;
    }

}
