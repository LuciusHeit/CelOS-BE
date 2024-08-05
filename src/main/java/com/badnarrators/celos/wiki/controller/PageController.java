package com.badnarrators.celos.wiki.controller;

import com.badnarrators.celos.wiki.model.PageSearchResult;
import com.badnarrators.celos.wiki.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/page")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("/{id}")
    public PageSearchResult get(@PathVariable String id) {
        return pageService.get(id);
    }

    @GetMapping("/title/{title}")
    public List<PageSearchResult> getByTitle(@PathVariable String title) {
        return pageService.getByTitle(title);
    }

    @GetMapping("/category/{category}")
    public List<PageSearchResult> getByCategory(@PathVariable String category) {
        return pageService.getByCategory(category);
    }

    @GetMapping("/parent/{id}")
    public List<PageSearchResult> getByParent(@PathVariable String id) {
        return pageService.getByParent(id);
    }

}
