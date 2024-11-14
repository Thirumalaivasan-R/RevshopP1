package com.Revshop.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Revshop.revshop.model.Product;
import com.Revshop.revshop.model.Seller;
import com.Revshop.revshop.service.ProductService;
import com.Revshop.revshop.service.SellerLoginService;

import jakarta.servlet.http.HttpSession;
@Controller
public class ProductController {
	@Autowired
	private SellerLoginService sellerService;
	@Autowired
	private ProductService productService;
	@GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product()); 
        return "add-product"; 
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") Product product, HttpSession session, Model model) {
        Seller loggedInSeller = (Seller) session.getAttribute("loggedInSeller");
       
        if (loggedInSeller != null) {
        	String sellerName=loggedInSeller.getLastName();
        	
            product.setSeller(loggedInSeller); 
            System.out.println(loggedInSeller.getSellerId());
            productService.addProduct(product);  
            
            model.addAttribute("message", "Product added successfully!"); 
            return "redirect:/display-products?sellerId=" + loggedInSeller.getSellerId();  
        }
        model.addAttribute("loginError", "You must log in to add a product.");
        return "redirect:/seller-login";  
    }
    @GetMapping("/display-products")
    public String getProductsBySellerId(@RequestParam("sellerId") Long sellerId, Model model) {
        List<Product> productList = productService.getProductsBySellerId(sellerId);  
        model.addAttribute("products", productList);  
        
        return "display-products";  
    }
    @GetMapping("/edit-product")
    public String showEditProductForm(@RequestParam("id") Long productId, Model model) {
        Product product = productService.getProductById(productId); // Fetch product by ID
        model.addAttribute("product", product);
        return "edit-product";
    }

    
    @PostMapping("/edit-product")
    public String editProduct(@ModelAttribute("product") Product product, HttpSession session, Model model) {
        Seller loggedInSeller = (Seller) session.getAttribute("loggedInSeller");
        if (loggedInSeller != null) {
            product.setSeller(loggedInSeller);  
            productService.updateProduct(product); 

            model.addAttribute("message", "Product updated successfully!");  
            return "redirect:/display-products?sellerId=" + loggedInSeller.getSellerId(); 
        }
        model.addAttribute("loginError", "You must log in to edit a product.");
        return "loginSeller"; 
    }

    @DeleteMapping("/delete-product")
    public String deleteProduct(@RequestParam("id") Long productId,Model model)
    {
    	productService.deleteProduct(productId);
    	return "display-products";
    }
    @GetMapping("/search-products")
    public String searchProducts(@RequestParam("query") String query,
                                 @RequestParam("sellerId") Long sellerId,
                                 Model model) {
        List<Product> products = productService.searchProducts(query, sellerId);
        
        // Add products and message to the model
        model.addAttribute("products", products);
        model.addAttribute("message", products.isEmpty() ? "No products found." : null);
        
        return "display-products"; 
    }
    
    
    
 // Method to display products on the homepage
    @GetMapping("/homepage")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products); // Add product list to the model
        return "homepage";  // Refers to a Thymeleaf template named "homepage.html"
    }
    
    
        
    	@GetMapping("/searchall")
    	public String getAllProducts(@RequestParam("query") String query, Model model) {
    	    System.out.println("Search Query: " + query); // Debugging statement

    	    List<Product> products = productService.searchProductsByNameOrCategory(query);

    	    // Add products and message to the model
    	    model.addAttribute("products", products);
    	    model.addAttribute("message", products.isEmpty() ? "No products found." : null);

    	    return "display-all-products"; 
    	}

    

}
