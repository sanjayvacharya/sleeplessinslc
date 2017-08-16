package com.welflex.example.baseproduct.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.welflex.example.baseproduct.dto.BaseProduct;
import com.welflex.example.baseproduct.dto.BaseProductOption;

@RestController
public class BaseProductResource {
  @RequestMapping(value = "/baseProduct/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public BaseProduct get(@PathVariable Long id) {

	List<BaseProductOption> productOptions = new ArrayList<>();

	productOptions.add(new BaseProductOption(1L, "Large"));
	productOptions.add(new BaseProductOption(2L, "Medium"));
	productOptions.add(new BaseProductOption(3L, "Small"));
	productOptions.add(new BaseProductOption(4L, "XLarge"));

	return new BaseProduct(9310301L, "Brio Milano Men's Blue and Grey Plaid Button-down Fashion Shirt",
	    "http://ak1.ostkcdn.com/images/products/9310301/Brio-Milano-Mens-Blue-GrayWhite-Black-Plaid-Button-Down-Fashion-Shirt-6ace5a36-0663-4ec6-9f7d-b6cb4e0065ba_600.jpg",
	    productOptions);
  }
}
