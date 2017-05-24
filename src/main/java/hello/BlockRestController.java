package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user on 5/22/2017.
 */

@RestController
@RequestMapping("/block")
public class BlockRestController {
    @Autowired
    private UserRepository repository;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> blockUser(@RequestBody Map<String, String> input) {
        User requestor = repository.findByEmail(input.get("requestor"));
        User target = repository.findByEmail(input.get("target"));

        if (requestor==null || target==null) {
            return ResponseEntity.ok().body(new HashMap() {{
                put("success", "false");
                put("message", "invalid user/s");
            }});
        }

        if (!requestor.getBlockList().contains(target)) {
            requestor.getBlockList().add(target);
        }
        repository.save(requestor);
        repository.flush();

        return ResponseEntity.ok().body(new HashMap() {{
            put("success", "true");
        }});
    }
}
