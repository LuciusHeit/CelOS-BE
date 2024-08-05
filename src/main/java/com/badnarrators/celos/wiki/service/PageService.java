package com.badnarrators.celos.wiki.service;

import com.badnarrators.celos.message.service.DiscordService;
import com.badnarrators.celos.wiki.entity.Page;
import com.badnarrators.celos.wiki.model.PageSearchResult;
import com.badnarrators.celos.wiki.model.SmallPage;
import com.badnarrators.celos.wiki.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private DiscordService discordService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    //TODO: IMPORTANT: implement impagination and user control

    public PageSearchResult get(String id) {
        Optional<Page> page = pageRepository.findById(id);

        if (page.isEmpty()) {
            return null;
        }

        return new PageSearchResult(page.get());
    }

    public List<PageSearchResult> getByCategory(String category) {
        List<Page> pages = pageRepository.findByCategoryContainingIgnoreCase(category);
        return parseList(pages, true);
    }
    public List<PageSearchResult> getByTitle(String title) {
        List<Page> pages = pageRepository.findByTitleContainingIgnoreCase(title);
        return parseList(pages, true);
    }
    public List<PageSearchResult> getByParent(String id) {
        Optional<Page> page = pageRepository.findById(id);
        if (page.isEmpty()) {
            return null;
        }
        List<Page> pages = page.get().getChildren();
        return parseList(pages, true);
    }

    private List<PageSearchResult> parseList(List<Page> pages) {
        return parseList(pages, false);
    }
    private List<PageSearchResult> parseList(List<Page> pages, boolean excludePrivate) {
        List<PageSearchResult> results = new java.util.ArrayList<>();
        for (Page page : pages) {
            if(page.isPrivate() && excludePrivate) {
                continue;
            }
            results.add(new PageSearchResult(page));
        }
        return results;
    }

}
