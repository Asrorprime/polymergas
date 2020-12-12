package uz.ecma.apppolymergasserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.ecma.apppolymergasserver.entity.User;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByChatId(Long chatId);

    Page<User> findAllByChatIdIsNotNull(Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update user set state=:state where id=:id", nativeQuery = true)
    void setState(@Param("id") UUID id, @Param("state") String state);

    Optional<User> findByPhoneNumberOrAdditionalPhone(String phoneNumber, String additionalPhone);

    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=true limit :size offset :page*:size", nativeQuery = true)
    List<User> findAllByEnabledIsTrueAndRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=true", nativeQuery = true)
    Integer findAllByEnabledIsTrueAndRolesNotInAndPhoneNumberCount(String searchName);

    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=false limit :size offset :page*:size", nativeQuery = true)
    List<User> findAllByEnabledIsFalseAndRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=false", nativeQuery = true)
    Integer findAllByEnabledIsFalseAndRolesNotInAndPhoneNumberCount(String searchName);

    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) limit :size offset :page*:size ", nativeQuery = true)
    List<User> findAllByRolesNotInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id!=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=1) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%')))", nativeQuery = true)
    Integer findAllByRolesNotInAndPhoneNumberOrderByCreatedAtDescCount(String searchName);


    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=true limit :size offset :page*:size ", nativeQuery = true)
    List<User> findAllByEnabledIsTrueAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=true", nativeQuery = true)
    Integer findAllByEnabledIsTrueAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsCount(String searchName);

    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=false limit :size offset :page*:size ", nativeQuery = true)
    List<User> findAllByEnabledIsFalseAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) and enabled=false", nativeQuery = true)
    Integer findAllByEnabledIsFalseAndRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneCount(String searchName);

    @Query(value = "select * from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%'))) limit :size offset :page*:size ", nativeQuery = true)
    List<User> findAllByRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneContainsIgnoreCaseOrFullNameContainsIgnoreCaseOrderByCreatedAtDesc(String searchName, int page, int size);

    @Query(value = "select count(*) from users inner join user_role ur on users.id = ur.user_id where (role_id=2 and (select count(*) from user_role where user_role.user_id=ur.user_id)=2) and ((lower(full_name) like concat('%',:searchName,'%')) or (lower(phone_number) like concat('%',:searchName,'%')) or (lower(additional_phone) like concat('%',:searchName,'%')))", nativeQuery = true)
    Integer findAllByRolesInAndPhoneNumberContainsIgnoreCaseOrAdditionalPhoneCount(String searchName);


    List<User> findAllByFullNameContainsIgnoreCaseOrPhoneNumberContainsIgnoreCase(String fullName, String phoneNumber);

    Integer countAllByCreatedAtBetween(Timestamp createdAt, Timestamp createdAt2);

    void deleteById(UUID uuid);
}
