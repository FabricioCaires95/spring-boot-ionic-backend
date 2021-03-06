package br.com.CourseSpringBoot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author fabricio
 */
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();

    private Double discount;
    private Integer amount;
    private Double price;

    public OrderItem() {

    }

    public OrderItem(Order order, Product product, Double discount, Integer amount, Double price) {
        super();
        id.setOrder(order);
        id.setProduct(product);
        this.discount = discount;
        this.amount = amount;
        this.price = price;
    }

    public double getSubTotal(){
        return (price - discount) * amount;
    }


    @JsonIgnore
    public Order getOrder() {
        return id.getOrder();
    }

    public void setOrder(Order order){
        id.setOrder(order);
    }


    public Product getProduct() {
        return id.getProduct();
    }

    public void setProduct(Product product){
        id.setProduct(product);
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt" , "BR"));

        builder.append(getProduct().getName());
        builder.append(", Qtde: ");
        builder.append(getAmount());
        builder.append(", Price: ");
        builder.append(nf.format(getPrice()));
        builder.append(", Total: ");
        builder.append(nf.format(getSubTotal()));
        builder.append("\n");

        return builder.toString();
    }
}
