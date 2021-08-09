package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.entity.Comment;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.mapper.CommentMapper;
import com.chubock.assignment.egs.model.CommentModel;
import com.chubock.assignment.egs.model.ProductModel;
import com.chubock.assignment.egs.model.UserModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import com.chubock.assignment.egs.repository.CommentRepository;
import com.chubock.assignment.egs.repository.ProductRepository;
import com.chubock.assignment.egs.repository.UserRepository;
import com.chubock.assignment.egs.service.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentService extends AbstractService<Comment, CommentModel> {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          ProductRepository productRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.commentMapper = commentMapper;
    }

    public Page<CommentModel> findByProduct(String uid, Pageable pageable) {

        if (StringUtils.isBlank(uid))
            throw new IllegalArgumentException("error.comment.product.notFound");

        Product product = Optional.of(uid)
                .flatMap(productRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.comment.product.notFound"));

        return commentRepository.findByProductWithUser(product, pageable)
                .map(this::findAllMapper);

    }

    @Override
    @Transactional
    public CommentModel save(CommentModel model) {

        if (StringUtils.isBlank(model.getText()))
            throw new IllegalArgumentException("error.comment.text.empty");

        if (model.getProduct() == null || StringUtils.isBlank(model.getProduct().getUid()))
            throw new IllegalArgumentException("error.comment.product.empty");

        if (model.getUser() == null || StringUtils.isBlank(model.getUser().getUid()))
            throw new IllegalArgumentException("error.comment.user.empty");

        Product product = Optional.of(model.getProduct())
                .map(ProductModel::getUid)
                .flatMap(productRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.comment.product.notFound"));

        User user = Optional.of(model.getUser())
                .map(UserModel::getUid)
                .flatMap(userRepository::findByUid)
                .orElseThrow(() -> new IllegalArgumentException("error.comment.user.notFound"));

        Comment comment = Comment.builder()
                .user(user)
                .product(product)
                .text(model.getText())
                .build();

        commentRepository.save(comment);

        return findOneMapper(comment);

    }

    @Override
    AbstractRepository<Comment> getRepository() {
        return commentRepository;
    }

    @Override
    AbstractMapper<Comment, CommentModel> getMapper() {
        return commentMapper;
    }
}
