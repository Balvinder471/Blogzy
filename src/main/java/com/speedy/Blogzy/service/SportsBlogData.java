package com.speedy.Blogzy.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("sportsData")
public class SportsBlogData implements BlogData {

    private Map<String, String> blog;
    private Map<String, String> author;

    public SportsBlogData() {
        blog = new HashMap<>();
        author = new HashMap<>();
        blog.put("genre", "Sports");
        blog.put("title", "Cricket");
        blog.put("desc", "Cricket is a bat-and-ball game played between two teams of eleven players each on a field at the centre of which is a 22-yard (20-metre) pitch with a wicket at each end, each comprising two bails balanced on three stumps. The game proceeds when a player on the fielding team, called the bowler, \"bowls\" (propels) the ball from one end of the pitch towards the wicket at the other end, with an \"over\" being completed once they have legally done so six times. The batting side has one player at each end of the pitch, with the player at the opposite end of the pitch from the bowler aiming to strike the ball with a bat. The batting side scores runs either when the ball reaches the boundary of the field, or when the two batters swap ends of the pitch, which results in one run. The fielding side's aim is to prevent run-scoring and dismiss each batter (so they are \"out\", and are said to have \"lost their wicket\"). Means of dismissal include being bowled, when the bowled ball hits the stumps and dislodges the bails, and by the fielding side either catching a hit ball before it touches the ground, or hitting a wicket with the ball before a batter can cross the crease line in front of the wicket to complete a run. When ten batters have been dismissed, the innings ends and the teams swap roles. The game is adjudicated by two umpires, aided by a third umpire and match referee in international matches.\n" +
                "\n" +
                "Forms of cricket range from Twenty20, with each team batting for a single innings of 20 overs and the game generally lasting three hours, to Test matches played over five days. Traditionally cricketers play in all-white kit, but in limited overs cricket they wear club or team colours. In addition to the basic kit, some players wear protective gear to prevent injury caused by the ball, which is a hard, solid spheroid made of compressed leather with a slightly raised sewn seam enclosing a cork core layered with tightly wound string.");
        author.put("name", "Cricket Author");
        author.put("desc", "Loves to play cricket");
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
