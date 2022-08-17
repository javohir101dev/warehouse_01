package uz.devops.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import uz.devops.domain.Users;

public interface UsersRepositoryWithBagRelationships {
    Optional<Users> fetchBagRelationships(Optional<Users> users);

    List<Users> fetchBagRelationships(List<Users> users);

    Page<Users> fetchBagRelationships(Page<Users> users);
}
