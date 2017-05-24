package hello;

import org.hibernate.mapping.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 5/22/2017.
 */

@RestController
@RequestMapping("/friends")
public class FriendRestController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    ResponseEntity<?> getFriends(@RequestBody Map<String, String> input) {
        User user = repository.findByEmail(input.get("email"));

        if (user != null) {
            Map result = new LinkedHashMap() {
                {
                    put("sucess", "true");
                    put("friends", new ArrayList(user.getFriends().keySet()));
                    put("count", user.getFriends().size());
                }
            };
            return new ResponseEntity<Map>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/common", method = RequestMethod.POST)
    ResponseEntity<?> getCoomonFriends(@RequestBody Map<String, List> input) {
        List<String> emails = input.get("friends");

        if (!emails.isEmpty() && (emails.size() == 2)) {
            User user1 = repository.findByEmail(emails.get(0));
            User user2 = repository.findByEmail(emails.get(1));
            Set user1Friends = user1.getFriends().keySet();
            Set user2Friends = user2.getFriends().keySet();
            user2Friends.retainAll(user1Friends);
            List commonFriends = new ArrayList(user2Friends);

            Map result = new LinkedHashMap() {
                {
                    put("sucess", "true");
                    put("friends", commonFriends);
                    put("count", commonFriends.size());
                }
            };
            return new ResponseEntity<Map>(result, HttpStatus.OK);

        } else {
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    ResponseEntity<?> addFriend(@RequestBody Map<String, List> input) {

        List<String> emails = input.get("friends");
        if (!emails.isEmpty() && (emails.size() == 2)) {
            User user1 = getUser(emails.get(0));
            User user2 = getUser(emails.get(1));

            User blocked = null;

            if (user1.getBlockList()!=null && user1.getBlockList().contains(user2))
            {
                blocked = user2;
            }
            else if (user2.getBlockList()!=null && user2.getBlockList().contains(user1))
            {
                blocked = user1;
            }

            if (blocked != null) {
                String msg = String.format("user: %s is blocked", blocked.getEmail());
                return ResponseEntity.ok().body(new HashMap() {
                    {
                        put("success", "false");
                        put("message", msg);
                    }
                });
            }

            if (!user1.getFriends().containsKey(user2.getEmail())) {
                user1.getFriends().put(user2.getEmail(),user2);
            }
            if (!user2.getFriends().containsKey(user1.getEmail())) {
                user2.getFriends().put(user1.getEmail(), user1);
            }
            repository.save(user1);
            repository.save(user2);

            return ResponseEntity.ok().body(new HashMap() {{
                put("success", "true");
            }});
        } else {
            return ResponseEntity.ok().body(new HashMap() {
                {
                    put("success", "false");
                    put("message", "invalid input");
                }
            });
        }
    }

    /*  This method finds an user by email, or creates a new user if it does not exist */
    private User getUser(String email) {
        User user = repository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            repository.save(user);
        }

        return user;
    }
}