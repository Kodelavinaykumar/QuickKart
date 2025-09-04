package com.quickkart.config;

import com.quickkart.entity.Productimages;

import com.quickkart.repository.ProductimagesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ProductimagesSeeder implements CommandLineRunner {

    @Autowired
    private ProductimagesRepository productimagesRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productimagesRepository.count() == 0) {
            Productimages p1 = new Productimages();
            p1.setName("Nike Air Max");
            p1.setDescription("Running Shoes");
            p1.setPrice(8999.0);
            p1.setStock(50);
            p1.setImageUrl("http://localhost:8084/uploads/nike_air_max.jpg");

            Productimages p2 = new Productimages();
            p2.setName("Adidas Ultraboost");
            p2.setDescription("Comfortable Sneakers");
            p2.setPrice(10999.0);
            p2.setStock(40);
            p2.setImageUrl("http://localhost:8084/uploads/adidas_ultraboost.jpg");

            Productimages p3 = new Productimages();
            p3.setName("Puma RS-X");
            p3.setDescription("Stylish Casual Shoes");
            p3.setPrice(7999.0);
            p3.setStock(30);
            p3.setImageUrl("http://localhost:8084/uploads/puma_rsx.jpg");

            Productimages p4 = new Productimages();
            p4.setName("Reebok Classic");
            p4.setDescription("Retro Running Shoes");
            p4.setPrice(6999.0);
            p4.setStock(25);
            p4.setImageUrl("http://localhost:8084/uploads/reebok_classic.jpg");

            Productimages p5 = new Productimages();
            p5.setName("New Balance 574");
            p5.setDescription("Lifestyle Shoes");
            p5.setPrice(8499.0);
            p5.setStock(35);
            p5.setImageUrl("http://localhost:8084/uploads/nb_574.jpg");

            Productimages p6 = new Productimages();
            p6.setName("ASICS Gel-Kayano");
            p6.setDescription("Premium Running Shoes");
            p6.setPrice(11999.0);
            p6.setStock(20);
            p6.setImageUrl("http://localhost:8084/uploads/asics_gel_kayano.jpg");

            Productimages p7 = new Productimages();
            p7.setName("Converse Chuck Taylor");
            p7.setDescription("Classic High Tops");
            p7.setPrice(4999.0);
            p7.setStock(60);
            p7.setImageUrl("http://localhost:8084/uploads/converse_chuck.jpg");

            Productimages p8 = new Productimages();
            p8.setName("Vans Old Skool");
            p8.setDescription("Skateboarding Shoes");
            p8.setPrice(5599.0);
            p8.setStock(45);
            p8.setImageUrl("http://localhost:8080/uploads/vans_old_skool.jpg");

            productimagesRepository.save(p1);
            productimagesRepository.save(p2);
            productimagesRepository.save(p3);
            productimagesRepository.save(p4);
            productimagesRepository.save(p5);
            productimagesRepository.save(p6);
            productimagesRepository.save(p7);
            productimagesRepository.save(p8);
        }
    }

	public ProductimagesRepository getProductimagesRepository() {
		return productimagesRepository;
	}

	public void setProductimagesRepository(ProductimagesRepository productimagesRepository) {
		this.productimagesRepository = productimagesRepository;
	}
}
