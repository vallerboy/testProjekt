package pl.oskarpolak;

import org.springframework.data.repository.CrudRepository;
import pl.oskarpolak.model.User;


/**
 * Created by OskarPraca on 2017-02-09.
 */
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}
