package com.quickkart.service;


import com.quickkart.entity.Productimages;
import com.quickkart.repository.ProductimagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProductimagesService {
	@Autowired
	private ProductimagesRepository productimagesRepository;


	public List<Productimages> getAllProducts() {
	return productimagesRepository.findAll();
	}


	public Optional<Productimages> getProductById(Long id) {
	return productimagesRepository.findById(id);
	}


	public Productimages saveProduct(Productimages productimages) {
	return productimagesRepository.save(productimages);
	}


	public void deleteProduct(Long id) {
	productimagesRepository.deleteById(id);
	}
}