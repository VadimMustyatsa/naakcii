package naakcii.by.api.subscriber;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {

    Subscriber findByEmail(String email);
}
