package com.speedy.Blogzy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("fantasyData")
public class FantasyBlogData implements BlogData {
    private Map<String, String> blog;
    private Map<String, String> author;

    public FantasyBlogData() {
        blog = new HashMap<>();
        author = new HashMap<>();
        blog.put("genre", "Fantasy");
        blog.put("title", "Day Dreaming Facts");
        blog.put("desc", "Anecdotally, mind-wandering has been associated with creativity for centuries. But this link to creativity may depend on the type of mind-wandering you do, as a new study by the University of Calgary’s Julia Kam and her colleagues suggests.\n" +
                "\n" +
                "In this study, researchers used electroencephalogram technology to see what happens in our brains when we are engaged in different types of mind-wandering. To do that, they had people perform a mundane, repetitive task and interrupted them occasionally to see what they were thinking about, while continuously monitoring their brain activity.\n" +
                "\n" +
                "Some participants reported thoughts that Kam calls “constrained,” involving things like ruminating over a fight with a spouse or thinking about how to manage a work problem. While these thoughts were not related to the task at hand, they were still somewhat focused. Others reported thoughts that were “freely moving”—meaning, they skipped from thing to thing—perhaps daydreaming about a future vacation in Italy, then wondering if they needed a new bathing suit, then fantasizing about an old flame.\n" +
                "\n" +
                "When Kam and her colleagues matched people’s thoughts to their concurrent brain activity, they found signature patterns for different types of mind-wandering. In particular, freely moving thoughts were associated with increased alpha waves in the brain’s frontal cortex—a remarkable and novel finding, says Kam.");
        author.put("name", "Fantasy Lover");
        author.put("desc", "Loves to talk about miscellaneous topics");
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
