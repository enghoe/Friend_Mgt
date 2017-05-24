package hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by user on 5/21/2017.
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
