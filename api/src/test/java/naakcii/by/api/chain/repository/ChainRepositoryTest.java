package naakcii.by.api.chain.repository;

import naakcii.by.api.chain.repository.model.Chain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ChainRepositoryTest {

    @Autowired
    ChainRepository chainRepository;

    @Autowired
    TestEntityManager entityManager;

    private Chain firstChain;

    @Before
    public void setUp() {
        firstChain = new Chain("firstChain", "firstLink", true);
        Chain secondChain = new Chain("secondChain", "secondLink", false);
        Chain thirdChain = new Chain("thirdChain", "thirdLink", true);
        entityManager.persist(firstChain);
        entityManager.persist(secondChain);
        entityManager.persist(thirdChain);
        entityManager.flush();
    }

    @Test
    public void findAllByOrderByNameAsc() {
        List<Chain> chainList = chainRepository.findAllByOrderByNameAsc();
        assertThat(chainList.size()).isEqualTo(3);
        assertThat(chainList.get(0).getName()).isEqualTo("firstChain");
        assertThat(chainList.get(1).getName()).isEqualTo("secondChain");
        assertThat(chainList.get(2).getName()).isEqualTo("thirdChain");
    }

    @Test
    public void findAllByOrderByNameDesc() {
        List<Chain> chainList = chainRepository.findAllByOrderByNameDesc();
        assertThat(chainList.size()).isEqualTo(3);
        assertThat(chainList.get(0).getName()).isEqualTo("thirdChain");
        assertThat(chainList.get(1).getName()).isEqualTo("secondChain");
        assertThat(chainList.get(2).getName()).isEqualTo("firstChain");
    }

    @Test
    public void findAllByIsActiveTrueOrderByNameAsc() {
        List<Chain> chainList = chainRepository.findAllByIsActiveTrueOrderByNameAsc();
        assertThat(chainList.size()).isEqualTo(2);
        assertThat(chainList.get(0).getName()).isEqualTo("firstChain");
        assertThat(chainList.get(1).getName()).isEqualTo("thirdChain");
    }

    @Test
    public void findAllByIsActiveTrueOrderByNameDesc() {
        List<Chain> chainList = chainRepository.findAllByIsActiveTrueOrderByNameDesc();
        assertThat(chainList.size()).isEqualTo(2);
        assertThat(chainList.get(0).getName()).isEqualTo("thirdChain");
        assertThat(chainList.get(1).getName()).isEqualTo("firstChain");
    }

    @Test
    public void findByName() {
        Chain chain = chainRepository.findByName("firstChain");
        assertThat(chain.getLink()).isEqualTo("firstLink");
    }

    @Test
    public void softDelete() {
        chainRepository.softDelete(firstChain.getId());
        entityManager.refresh(firstChain);
        entityManager.flush();
        assertThat(firstChain.isActive()).isEqualTo(false);
    }
}