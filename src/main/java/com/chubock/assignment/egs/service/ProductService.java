package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.mapper.ProductMapper;
import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.ProductSearchModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import com.chubock.assignment.egs.repository.CategoryRepository;
import com.chubock.assignment.egs.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService extends AbstractService<Product, ProductModel> {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public Page<ProductModel> search(ProductSearchModel searchModel, Pageable pageable) {
        return productRepository.findAll(searchModel, pageable)
                .map(productMapper::toModelWithCategory);
    }

    @Transactional(readOnly = true)
    public Page<ProductModel> findAllByCategory(String categoryUid, Pageable pageable) {

        if (StringUtils.isBlank(categoryUid))
            throw new IllegalArgumentException("error.category.empty");

        Category category = Optional.of(categoryUid)
                .flatMap(categoryRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.category.notFound"));

        return productRepository.findByCategory(category, pageable)
                .map(this::findAllMapper);

    }

    @Transactional
    public void changeProductCategory(String productUid, String categoryUid) {

        Product product = getProduct(productUid);
        Category category = getCategory(categoryUid);

        product.setCategory(category);

    }

    @Transactional
    public void removeFromCategory(String productUid, String categoryUid) {

        Product product = getProduct(productUid);
        Category category = getCategory(categoryUid);

        if (category.equals(product.getCategory()))
            product.setCategory(null);

    }

    @Override
    @Transactional
    public ProductModel save(ProductModel model) {

        Product product = Optional.ofNullable(model.getUid())
                .flatMap(productRepository::findByUid)
                .orElseGet(Product::new);

        if (model.getUid() != null)
            product.setUid(model.getUid());

        product.setName(model.getName());
        product.setPrice(model.getPrice());

        Optional.ofNullable(model.getCategory())
                .map(CategoryModel::getUid)
                .flatMap(categoryRepository::findByUid)
                .ifPresent(product::setCategory);

        productRepository.save(product);

        return findOneMapper(product);
    }

    @Override
    public Optional<ProductModel> findOne(String uid) {
        return productRepository.findByUidWithCategoryAndComments(uid)
                .map(this::findOneMapper);
    }

    @Override
    AbstractRepository<Product> getRepository() {
        return productRepository;
    }

    @Override
    AbstractMapper<Product, ProductModel> getMapper() {
        return productMapper;
    }

    @Override
    ProductModel findOneMapper(Product entity) {
        return productMapper.toModelWithCategory(entity);
    }

    private Product getProduct(String uid) {

        if (StringUtils.isBlank(uid))
            throw new IllegalArgumentException("error.product.empty");

        return Optional.of(uid)
                .flatMap(productRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.product.notFound"));

    }

    private Category getCategory(String uid) {

        if (StringUtils.isBlank(uid))
            throw new IllegalArgumentException("error.product.empty");

        return Optional.of(uid)
                .flatMap(categoryRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.category.notFound"));

    }
}
