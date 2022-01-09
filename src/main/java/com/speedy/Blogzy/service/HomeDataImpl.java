package com.speedy.Blogzy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("homedata")
public class HomeDataImpl implements HomeData {

    private Map<String, String> blogs;

    public HomeDataImpl() {
        blogs = new HashMap<>();
        blogs.put("Day Dreamers","Duis id neque eros. Fusce erat augue, efficitur sit amet consectetur ut, pellentesque nec nulla. Etiam laoreet mi at bibendum.");
        blogs.put("Metaverse","Nullam ut placerat dolor, et vulputate elit. Praesent venenatis odio eget condimentum pretium. In vestibulum velit vel pharetra sagittis. Sed.");
        blogs.put("Medicinal herbs","Morbi vitae commodo nibh. Nunc lorem enim, dapibus ac augue et, egestas tempus eros. Aenean ac nibh semper, tincidunt tortor.");
        blogs.put("Cricket","Quisque pulvinar bibendum elit, a vestibulum mauris sagittis nec. Cras vehicula auctor aliquet. Aliquam ultricies iaculis enim varius semper.");
    }

    @Override
    public Map<String, String> getBlogs() {
        return blogs;
    }
}
