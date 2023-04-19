package hello.login.domain.member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 * 인터페이스로 만들고 구현체를 따로 두는 것을 추천
 */
@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    /**
     * db에 저장되는 자동 등록 id가 아닌 login 아이디로 찾는 방법
     */
    public Optional<Member> findByLoginId(String loginId) {
        // Optional<T>는 null이 올 수 있는 값을 감싸는 Wrapper 클래스로, 참조하더라도 NPE가 발생하지 않도록 도와줌

        /* //아래의 코드와 같음
        List<Member> all = findAll();
        for(Member m : all){
            if(m.getLoginId().equals(loginId)){
                return Optional.of(m);
                //Optional<Member> -> Member 라면
                //return m;
            }
        }//null 이면
        return Optional.empty();
        */

        return findAll().stream() //filter() 마치 DB의 where절 같음
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}