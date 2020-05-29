package unito.progetto.esame.model;


import org.hibernate.annotations.NaturalId;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class PaymentMethods implements Serializable {

    @Id
    @GeneratedValue
    private Integer mathodId;

    private String methodName;

    @NaturalId
    private Integer methodType;



    public PaymentMethods() {
    }

    public PaymentMethods(String methodName, Integer methodType) {
        this.methodName = methodName;
        this.methodType = methodType;
    }
}
