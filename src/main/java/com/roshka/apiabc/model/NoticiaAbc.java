package com.roshka.apiabc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticiaAbc {
    @JsonProperty("_website_urls")
    private List<String> websiteUrls;
    private HeadlinesAbc headlines;
    @JsonProperty("promo_items")
    private PromoItems promoItems;
    @JsonProperty("publish_date")
    private String publishDate;
    private HeadlinesAbc subheadlines;

    public List<String> getWebsiteUrls() {
        return websiteUrls;
    }

    public void setWebsiteUrls(List<String> websiteUrls) {
        this.websiteUrls = websiteUrls;
    }

    public HeadlinesAbc getHeadlines() {
        return headlines;
    }

    public void setHeadlines(HeadlinesAbc headlines) {
        this.headlines = headlines;
    }

    public PromoItems getPromoItems() {
        return promoItems;
    }

    public void setPromoItems(PromoItems promoItems) {
        this.promoItems = promoItems;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public HeadlinesAbc getSubheadlines() {
        return subheadlines;
    }

    public void setSubheadlines(HeadlinesAbc subheadlines) {
        this.subheadlines = subheadlines;
    }
}
