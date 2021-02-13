package com.sitebase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String current = format.format(new Date());
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> data0 = new HashMap<>();

        data0.put("category", "LIFE");
        data0.put("url", "http://localhost:8080");
        data0.put("date", current);
        data0.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559822138/theme9_v273a9.jpg");
        data0.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559822138/theme9_v273a9.jpg");
        data0.put("tags", new String[] {"books", "read"});
        data0.put("title", "Why books should be your priority?");
        data0.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna ");

        list.add(data0);

        Map<String, Object> data1 = new HashMap<>();

        data1.put("category", "CSS");
        data1.put("url", "http://localhost:8080");
        data1.put("date", current);
        data1.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_760/v1506079212/jekflix-capa_vfhuzh.png");
        data1.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1506079212/jekflix-capa_vfhuzh.png");
        data1.put("tags", new String[] {"css", "tips"});
        data1.put("title", "The quick brown fox jumps over a lazy dog");
        data1.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data1);

        Map<String, Object> data2 = new HashMap<>();

        data2.put("category", "CODE");
        data2.put("url", "http://localhost:8080");
        data2.put("date", current);
        data2.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559820489/js-code_n83m7a.jpg");
        data2.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559820489/js-code_n83m7a.jpg");
        data2.put("tags", new String[] {"platform", "javascript", "vscode"});
        data2.put("title", "A wonderful serenity has taken possession of my entire soul");
        data2.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data2);

        Map<String, Object> data3 = new HashMap<>();

        data3.put("category", "DIET");
        data3.put("url", "http://localhost:8080");
        data3.put("date", current);
        data3.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559822137/theme11_vei7iw.jpg");
        data3.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559822137/theme11_vei7iw.jpg");
        data3.put("tags", new String[] {"food", "tips"});
        data3.put("title", "Candy, candies, candy!");
        data3.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data3);

        Map<String, Object> data4 = new HashMap<>();

        data4.put("category", "TIPS");
        data4.put("url", "http://localhost:8080");
        data4.put("date", current);
        data4.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559821648/theme1_eoyjtl.jpg");
        data4.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559821648/theme1_eoyjtl.jpg");
        data4.put("tags", new String[] {"languages", "tips"});
        data4.put("title", "Trust me, it will work");
        data4.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data4);

        Map<String, Object> data5 = new HashMap<>();

        data5.put("category", "TUTORIAL");
        data5.put("url", "http://localhost:8080");
        data5.put("date", current);
        data5.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559821648/theme8_knvabs.jpg");
        data5.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559821648/theme8_knvabs.jpg");
        data5.put("tags", new String[] {"jedi", "doggo", "starwars"});
        data5.put("title", "How to turn your dog into a Jedi master");
        data5.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data5);

        Map<String, Object> data6 = new HashMap<>();

        data6.put("category", "CSS");
        data6.put("url", "http://localhost:8080");
        data6.put("date", current);
        data6.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559821647/theme2_ylcxxz.jpg");
        data6.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559821647/theme2_ylcxxz.jpg");
        data6.put("tags", new String[] {"css", "frontend"});
        data6.put("title", "Let flexbox work for you or perish");
        data6.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data6);

        Map<String, Object> data7 = new HashMap<>();

        data7.put("category", "MUSIC");
        data7.put("url", "http://localhost:8080");
        data7.put("date", current);
        data7.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559822138/theme10_xenudc.jpg");
        data7.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559822138/theme10_xenudc.jpg");
        data7.put("tags", new String[] {"crazy", "story"});
        data7.put("title", "A star has fallen from the sky and the cat ate it");
        data7.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data7);

        Map<String, Object> data8 = new HashMap<>();

        data8.put("category", "BLOG");
        data8.put("url", "http://localhost:8080");
        data8.put("date", current);
        data8.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559824021/theme12_e0vxlr.jpg");
        data8.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559824021/theme12_e0vxlr.jpg");
        data8.put("tags", new String[] {"jekyll", "wordpress", "blog"});
        data8.put("title", "Smoke alert");
        data8.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data8);

        Map<String, Object> data9 = new HashMap<>();

        data9.put("category", "TRAVEL");
        data9.put("url", "http://localhost:8080");
        data9.put("date", current);
        data9.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559824306/theme13_dshbqx.jpg");
        data9.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559824306/theme13_dshbqx.jpg");
        data9.put("tags", new String[] {"fate", "travel"});
        data9.put("title", "Do you believe that a spider can dance?");
        data9.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data9);

        Map<String, Object> data10 = new HashMap<>();

        data10.put("category", "DIET");
        data10.put("url", "http://localhost:8080");
        data10.put("date", current);
        data10.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559824575/theme14_gi2ypv.jpg");
        data10.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559824575/theme14_gi2ypv.jpg");
        data10.put("tags", new String[] {"cook", "cucine", "story"});
        data10.put("title", "A cook cries in the rain at night");
        data10.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data10);

        Map<String, Object> data11 = new HashMap<>();

        data11.put("category", "DIET");
        data11.put("url", "http://localhost:8080");
        data11.put("date", current);
        data11.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559824822/theme15_oqsl4z.jpg");
        data11.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559824822/theme15_oqsl4z.jpg");
        data11.put("tags", new String[] {"music", "band", "passion"});
        data11.put("title", "Grab your band and get out");
        data11.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data11);

        Map<String, Object> data12 = new HashMap<>();

        data12.put("category", "WORK");
        data12.put("url", "http://localhost:8080");
        data12.put("date", current);
        data12.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559825288/theme17_nlndhx.jpg");
        data12.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559825288/theme17_nlndhx.jpg");
        data12.put("tags", new String[] {"work", "career"});
        data12.put("title", "Passion is dangerous, go for it!");
        data12.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data12);

        Map<String, Object> data13 = new HashMap<>();

        data13.put("category", "LIFE");
        data13.put("url", "http://localhost:8080");
        data13.put("date", current);
        data13.put("imgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/v1559825145/theme16_o0seet.jpg");
        data13.put("OptImgUrl", "https://res.cloudinary.com/dm7h7e8xj/image/upload/c_scale,w_380/v1559825145/theme16_o0seet.jpg");
        data13.put("tags", new String[] {"life", "tips"});
        data13.put("title", "Birds can fly, but this you knew already");
        data13.put("description", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna");

        list.add(data13);

        model.addAttribute("data", list);

        return "home";
    }
}
