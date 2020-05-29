package unito.progetto.esame.service;


import unito.progetto.esame.model.PaymentCallback;
import unito.progetto.esame.model.PaymentDetail;

public interface PaymentService {

    PaymentDetail proceedPayment(PaymentDetail paymentDetail);
    String payuCallback(PaymentCallback paymentResponse);

}
