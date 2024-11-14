package com.Revshop.revshop.dto;

import java.time.LocalDate;

public class OrderDTO {

	private Long productId;
    private String productName;
    private String productImage;
    private Double discountPrice;
    private int quantity;
    private LocalDate orderDate;
	public OrderDTO(Long productId, String productName, String productImage, Double discountPrice, int quantity,
			LocalDate orderDate) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.discountPrice = discountPrice;
		this.quantity = quantity;
		this.orderDate = orderDate;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public Double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
}