package server.domain.businessCard.mapper;

import org.apache.ibatis.annotations.Mapper;
import server.domain.businessCard.domain.BusinessCard;

import java.util.List;
import java.util.Map;

@Mapper
public interface BusinessCardMapper {

    List<BusinessCard> findAllByMemberIdx(Long memberIdx);

    BusinessCard findByMemberIdx(Long memberIdx);

    BusinessCard findByBusinessCardIdx(Long businessCardIdx);

    void save(BusinessCard businessCard);

    void delete(Long memberIdx);

    void updateBusinessCard(BusinessCard businessCard);

    BusinessCard findByIdxAndMemberIdx(Map<String, Object> map);

    List<BusinessCard> findAllFriendBusinessCards(Long memberIdx);



}
