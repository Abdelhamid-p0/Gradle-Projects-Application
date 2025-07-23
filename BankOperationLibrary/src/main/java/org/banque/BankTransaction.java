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
    private final Object lock = new Object();


    public BankTransaction(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }



    public  String  bankTransactionPub(String BankName, String rib, double amount) throws InterruptedException {

        this.topic = BankName ;

        //publier Transaction
        System.out.println("5/a)- Publier la transaction");
        TransactionEvent transactionEvent =  new TransactionEvent(rib, amount);
        System.out.println("Transaction: " + transactionEvent);
        kafkaTemplate.send(topic,transactionEvent.toString());

        this.transactionStatus = "in progress";
        System.out.println("5/b)- Attendre la reponse du banque via kafkaListnner" );
        //Attender la consommation du status
        synchronized (lock) {
            long start = System.currentTimeMillis();
            long timeout = 5000; // 5 secondes max
            while ("in progress".equals(transactionStatus) && (System.currentTimeMillis() - start < timeout)) {
                lock.wait(100); // attend avec réveil régulier
            }
        }

        if  (this.transactionStatus.equals("success")) {
            System.out.println("6)- Opération réussi");
        }
        else {
            System.out.println("Transaction échoué");
        }

        return transactionStatus;

    }


    @KafkaListener(topics ="status-success", groupId = "my-group")
    public String consume(String event) {
        System.out.println("5/c)- Event entendue et consommer");
        System.out.println("event: " + event);
        synchronized (lock) {
            this.transactionStatus = "success";
            lock.notifyAll(); // réveille celui qui attend
        }
        return event;
    }

}
