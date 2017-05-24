package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 5/23/2017.
 */

@RestController
@RequestMapping("/messages")
public class MessagesRestController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    ResponseEntity<?> subscribe(@RequestBody Map<String, String> input) {
        User requestor = repository.findByEmail(input.get("requestor"));
        User target = repository.findByEmail(input.get("target"));

        if (requestor == null || target == null) {
            return ResponseEntity.ok().body(new HashMap() {{
                put("success", "false");
                put("message", "invalid user/s");
            }});
        }

        if (!requestor.getSubscriptions().contains(target)) {
            requestor.getSubscriptions().add(target);
        }
        repository.save(requestor);
        repository.flush();

        return ResponseEntity.ok().body(new HashMap() {{
            put("success", "true");
        }});
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    ResponseEntity<?> postMessage(@RequestBody Map<String, String> input) {
        User sender = repository.findByEmail(input.get("sender"));
        String text = input.get("text");

        if (sender == null) {
            return ResponseEntity.ok().body(new HashMap() {{
                put("success", "false");
                put("message", "invalid user");
            }});
        }

        List<String> recipients = new ArrayList(sender.getFriends().keySet());

        sender.getRecipients().forEach(r -> {
            recipients.remove(r.getEmail());
            recipients.add(r.getEmail());
        });

        Pattern emailPatt = Pattern.compile(
                "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");

        Matcher m = emailPatt.matcher(text);

        while (m.find()) {
            if (repository.findByEmail(m.group()) != null) {
                recipients.remove(m.group());
                recipients.add(m.group());
            }
        }

        sender.getBlockers().forEach(b-> recipients.remove(b.getEmail()));

        return ResponseEntity.ok().body(new HashMap() {{
            put("success", "true");
            put("recipients", recipients);
        }});
    }
}
