package muyu.trade.repository;

import muyu.trade.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

    User findUserByPhone(String phone);
}
