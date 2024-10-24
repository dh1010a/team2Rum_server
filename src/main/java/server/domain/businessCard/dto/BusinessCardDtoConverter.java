package server.domain.businessCard.dto;

import server.domain.businessCard.domain.BusinessCard;

import java.util.List;

public class BusinessCardDtoConverter {

    public static BusinessCardResponseDto.BusinessCardInfoResponseDto convertToBusinessCardInfoResponseDto(BusinessCard businessCard) {
        return BusinessCardResponseDto.BusinessCardInfoResponseDto.builder()
                .idx(businessCard.getIdx())
                .name(businessCard.getName())
                .phoneNumber(businessCard.getPhoneNum())
                .tel_num(businessCard.getTelNum())
                .email(businessCard.getEmail())
                .position(businessCard.getPosition())
                .part(businessCard.getPart())
                .company(businessCard.getCompany())
                .address(businessCard.getAddress())
                .imgurl(businessCard.getImgUrl())
                .qrData(businessCard.getQrData())
                .build();
    }

    public static BusinessCardResponseDto.BusinessCardListResponseDto convertToBusinessCardListResponseDto(List<BusinessCard> BusinessCard) {
        if (BusinessCard == null) {
            return BusinessCardResponseDto.BusinessCardListResponseDto.builder()
                    .cnt(0)
                    .isSuccess(false)
                    .build();
        }

        return BusinessCardResponseDto.BusinessCardListResponseDto.builder()
                .cnt(BusinessCard.size())
                .businessCardList(BusinessCard.stream().map(BusinessCardDtoConverter::convertToBusinessCardInfoResponseDto).toList())
                .isSuccess(true)
                .build();
    }
}
