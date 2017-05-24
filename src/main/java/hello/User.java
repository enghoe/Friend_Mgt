package hello;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 5/21/2017.
 */

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;

    @JoinTable(name = "friends", joinColumns =
    @JoinColumn(name = "friend", referencedColumnName = "id", nullable = false))
    @ManyToMany
    private Map<String, User> friends = new HashMap<>();

    @JoinTable(name = "subscriptions", joinColumns =
            {@JoinColumn(name = "requestor", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "target", referencedColumnName = "id", nullable = false)}
    )
    @ManyToMany
    private Collection<User> subscriptions;
    @ManyToMany(mappedBy = "subscriptions")
    private Collection<User> recipients;
    @JoinTable(name = "block_list", joinColumns =
            {@JoinColumn(name = "requestor", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "target", referencedColumnName = "id", nullable = false)}
    )
    @ManyToMany
    private Collection<User> blockList;
    @ManyToMany(mappedBy = "blockList")
    private Collection<User> blockers;

    public Collection<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<User> recipients) {
        this.recipients = recipients;
    }

    public Collection<User> getBlockList() {
        return blockList;
    }

    public void setBlockList(Collection<User> blockList) {
        this.blockList = blockList;
    }

    public Collection<User> getBlockers() {
        return blockers;
    }

    public void setBlockers(Collection<User> blockers) {
        this.blockers = blockers;
    }

    public Map<String, User> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, User> friends) {
        this.friends = friends;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Collection<User> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
