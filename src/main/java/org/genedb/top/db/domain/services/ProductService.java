package org.genedb.top.db.domain.services;

import org.genedb.top.db.domain.misc.MethodResult;
import org.genedb.top.db.domain.objects.Product;

import java.util.List;

public interface ProductService {

    List<Product> getProductList(boolean restrictToGeneLinked);

    MethodResult rationaliseProduct(Product newProduct, List<Product> products);

}
