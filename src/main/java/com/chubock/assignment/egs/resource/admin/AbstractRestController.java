package com.chubock.assignment.egs.resource.admin;

import com.chubock.assignment.egs.model.AbstractModel;
import com.chubock.assignment.egs.service.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractRestController<T extends AbstractModel<?>> {

    abstract BaseService<T> getService();

    @GetMapping
    @Operation(summary = "Get all items page by page")
    public Page<? extends T> findAll(Pageable pageable) {
        return getService().findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one item via id")
    public T findOne(@PathVariable("id") String id) {
        return getService().findOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Add an item")
    public T save(@Validated @RequestBody T model) {
        model.setUid(null);
        return getService().save(model);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item via id")
    public T update(@PathVariable("id") String id, @Validated @RequestBody T model) {
        model.setUid(id);
        return getService().save(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item via id")
    public void delete(@PathVariable("id") String id) {
        getService().delete(id);
   }

}
