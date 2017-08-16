package com.welflex.example.productgateway.rest;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.welflex.example.productgateway.dto.Product;
import com.welflex.example.productgateway.dto.ProductOption;

@RestController
public class ProductResource {
  private static final Logger LOGGER = Logger.getLogger(ProductResource.class);

  private RestTemplate restTemplate = new RestTemplate();

  @Value("${BASEPRODUCT_SERVICE_HOST:localhost}")
  private String baseProductServiceHost;
  @Value("${BASEPRODUCT_SERVICE_PORT:9090}")
  private int baseProductServicePort;

  @Value("${INVENTORY_SERVICE_HOST:localhost}")
  private String inventoryServiceHost;
  @Value("${INVENTORY_SERVICE_PORT:9091}")
  private String inventoryServicePort;

  @Value("${PRICE_SERVICE_HOST:localhost}")
  private String priceServiceHost;
  @Value("${PRICE_SERVICE_PORT:9094}")
  private String priceServicePort;

  @Value("${REVIEWS_SERVICE_HOST:localhost}")
  private String reviewsServiceHost;
  @Value("${REVIEWS_SERVICE_PORT:9093}")
  private String reviewsServicePort;

  @RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public Product get(@PathVariable Long id) {

	Product p = create(id);
	return p;
  }

  private Product create(Long id) {
	String baseProductUrl = String.format("http://%s:%s", baseProductServiceHost, baseProductServicePort);
	String inventoryUrl = String.format("http://%s:%s", inventoryServiceHost, inventoryServicePort);
	String priceUrl = String.format("http://%s:%s", priceServiceHost, priceServicePort);
	String reviewsUrl = String.format("http://%s:%s", reviewsServiceHost, reviewsServicePort);

	LOGGER.info("Requesting:" + id + " from URL:" + baseProductUrl + "[" + baseProductServiceHost + ","
	    + baseProductServicePort + "]");

	String baseProduct = restTemplate.getForObject(baseProductUrl + "/baseProduct/" + id, String.class);

	LOGGER.info("Base Product Retrieved:" + baseProduct);
	JSONObject jsonObj = new JSONObject(baseProduct);

	String appDescription = jsonObj.getString("description");
	String imageUrl = jsonObj.getString("imageUrl");

	Product p = new Product(id);

	p.setDescription(appDescription);
	p.setImageUrl(imageUrl);

	JSONArray jsonArray = jsonObj.getJSONArray("options");

	for (int i = 0; i < jsonArray.length(); i++) {
	  JSONObject option = jsonArray.getJSONObject(i);
	  Long productId = option.getLong("productId");
	  String optionDescription = option.getString("optionDescription");

	  ProductOption productOption = new ProductOption(productId, optionDescription);
	  String inventoryString = restTemplate.getForObject(inventoryUrl + "/productInventory/" + id, String.class);
	  productOption.setInventory(Integer.valueOf(inventoryString));
	  String priceString = restTemplate.getForObject(priceUrl + "/productPrice/" + id, String.class);
	  productOption.setPrice(Double.valueOf(priceString));

	  p.addOption(productOption);
	}

	String productPriceString = restTemplate.getForObject(priceUrl + "/productPrice/" + id, String.class);
	p.setPrice(Double.valueOf(productPriceString));

	String reviewsJSON = restTemplate.getForObject(reviewsUrl + "/productReviews/" + id, String.class);

	jsonObj = new JSONObject(reviewsJSON);
	jsonArray = jsonObj.getJSONArray("reviews");

	for (int i = 0; i < jsonArray.length(); i++) {
	  String review = jsonArray.getString(i);
	  p.addReview(review);
	}

	return p;
  }
}
