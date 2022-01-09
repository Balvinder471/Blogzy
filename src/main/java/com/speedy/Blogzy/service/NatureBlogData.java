package com.speedy.Blogzy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("natureData")
public class NatureBlogData implements BlogData {
    private Map<String, String> blog;
    private Map<String, String> author;

    public NatureBlogData() {
        blog = new HashMap<>();
        author = new HashMap<>();
        blog.put("genre", "Nature");
        blog.put("title", "Power of Herbs");
        blog.put("desc", "The term “medicinal plant” include various types of plants used in herbalism (\"herbology\" or \"herbal medicine\"). It is the use of plants for medicinal purposes, and the study of such uses.\n" +
                "\n" +
                "The word “herb” has been derived from the Latin word, “herba” and an old French word “herbe”. Now a days, herb refers to any part of the plant like fruit, seed, stem, bark, flower, leaf, stigma or a root, as well as a non-woody plant. Earlier, the term “herb” was only applied to non-woody plants, including those that come from trees and shrubs. These medicinal plants are also used as food, flavonoid, medicine or perfume and also in certain spiritual activities.\n" +
                "\n" +
                "Plants have been used for medicinal purposes long before prehistoric period. Ancient Unani manuscripts Egyptian papyrus and Chinese writings described the use of herbs.  Evidence exist that Unani Hakims, Indian Vaids and European and Mediterranean cultures were using herbs for over 4000 years as medicine. Indigenous cultures such as Rome, Egypt, Iran, Africa and America used herbs in their healing rituals, while other developed traditional medical systems such as Unani, Ayurveda and Chinese Medicine in which herbal therapies were used systematically.\n" +
                "\n" +
                "Traditional systems of medicine continue to be widely practised on many accounts. Population rise, inadequate supply of drugs, prohibitive cost of treatments, side effects of several synthetic drugs and development of resistance to currently used drugs for infectious diseases have led to increased emphasis on the use of plant materials as a source of medicines for a wide variety of human ailments.");
        author.put("name", "Nature Lover");
        author.put("desc", "Loves to talk about Nature");
    }

    @Override
    public Map<String, String> getBlogDetails() {
        return blog;
    }

    @Override
    public Map<String, String> getAuthorDetails() {
        return author;
    }
}
