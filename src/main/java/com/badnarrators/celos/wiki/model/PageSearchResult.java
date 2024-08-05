package com.badnarrators.celos.wiki.model;

import com.badnarrators.celos.wiki.entity.Page;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageSearchResult {

    private String id;
    private String title;
    private String category;
    private String content;

    private List<SmallPage> children;

    public PageSearchResult(Page page) {
        this(page, true);
    }
    public PageSearchResult(Page page, boolean excludePrivate) {
        List<SmallPage> children = new java.util.ArrayList<>();
        for (Page child : page.getChildren()) {
            if(child.isPrivate() && excludePrivate) {
                continue;
            }
            SmallPage smallPage = new SmallPage();
            smallPage.setId(child.getId());
            smallPage.setTitle(child.getTitle());
            children.add(smallPage);
        }
        this.setId(page.getId());
        this.setTitle(page.getTitle());
        this.setCategory(page.getCategory());
        this.setContent(page.getContent());
        this.setChildren(children);
    }
}
