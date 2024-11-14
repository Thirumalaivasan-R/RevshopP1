package com.Revshop.revshop.model;



import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, length = 100) 
    private String name;

    @Column(nullable = true) 
    private String image;

    @Column(nullable = true, length = 500) 
    private String description;

    @Column(nullable = false) 
    private Double price;
    private Double discountprice;
    public Double getDiscountprice() {
		return discountprice;
	}

	public void setDiscountprice(Double discountprice) {
		this.discountprice = discountprice;
	}

	@Column(nullable = false) 
    private int quantity;

    @Column(nullable = false) 
    private int threshold;

    
    private String categoryname;

    @Column(nullable = true) 
    private LocalDate productAddedAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "seller_id", referencedColumnName = "sellerId")
    private Seller seller;


   
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    
    public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public LocalDate getProductAddedAt() {
        return productAddedAt;
    }

    public void setProductAddedAt(LocalDate localDate) {
        this.productAddedAt = localDate;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
