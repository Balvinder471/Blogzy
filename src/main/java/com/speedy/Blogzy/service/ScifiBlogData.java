package com.speedy.Blogzy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("scifiData")
public class ScifiBlogData implements BlogData {

    private Map<String, String> blog;
    private Map<String, String> author;

    public ScifiBlogData() {
        blog = new HashMap<>();
        author = new HashMap<>();
        blog.put("genre", "Sci-Fi");
        blog.put("title", "Metaverse");
        blog.put("desc", "This vision of a 21st-century reality comes from Snow Crash, a 1992 book by science fiction writer Neal Stephenson. But wind forward to 2022, and one large and powerful company is certainly betting all on Protagonist’s escape route. In October 2021, Facebook rebranded itself as Meta, and founder Mark Zuckerberg set a goal for a billion people worldwide to join its version of the metaverse by the end of the 2020s. It committed at least $10 billion last year alone to make that a reality.\n" +
                "\n" +
                "Zuckerberg isn’t the only one latching on to a radical vision in which we go to work, seek entertainment and connect with each other not in a physical world, but in a virtual reality. The buzz surrounding the metaverse raises many questions. Chief among them are, what exactly is the metaverse, is it really just around the corner and, if so, do we actually want it?\n" +
                "\n" +
                "For the company now known as Meta, you can see the attraction in the metaverse. The firm currently makes its money selling advertising based on our interactions with Facebook, its core app. But that only gives the firm visibility over the parts of our lives that we choose to put on Facebook. If our entire lives – or at least a far greater proportion of them – are conducted online, the opportunity to make big bucks selling advertising becomes that much greater.\n" +
                "\n" +
                "Facebook’s metamorphosis certainly amped up the metaverse hype. Almost 160 companies mentioned the metaverse in their earnings statements in 2021, according to financial research firm Sentieo, 93 of them after the Facebook rebrand. “It’s a lot like when the ‘internet of things’ was first coming about, and the phrase started to be on everybody’s lips,” says Nick Kelly, who researches interaction design at Queensland University of Technology in Australia.");
        author.put("name", "Metaverse Enthusiast");
        author.put("desc", "Loves to talk about Sci-Fi");
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
