package com.banque.bmce;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOperation {

    TransactionOperation transactionOperation ;
    KafkaTemplate<String, String> kafkaTemplate;

    KafkaOperation(TransactionOperation transactionOperation , KafkaTemplate<String, String> kafkaTemplate) {
        this.transactionOperation = transactionOperation ;
        this.kafkaTemplate = kafkaTemplate ;
    }

    @KafkaListener(topics = "cih",groupId = "my-group")
    public void listen(String message){

        transactionOperation.payementOperation(message);
        kafkaTemplate.send("status-success",message);

    }



}
