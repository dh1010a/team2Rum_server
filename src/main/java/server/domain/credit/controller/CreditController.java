package server.domain.credit.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.credit.domain.Credit;
import server.domain.credit.domain.CreditHistory;
import server.domain.credit.dto.CreditHistoryDtoConverter;
import server.domain.credit.dto.CreditHistoryResponseDto;
import server.domain.credit.dto.CreditRequestDto;
import server.domain.credit.dto.CreditResponseDto;
import server.domain.credit.service.CreditService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import java.util.List;

@RestController
@RequestMapping("/api/credit")
@Slf4j
@RequiredArgsConstructor
public class CreditController {

    private final CreditService creditService;

    @PostMapping
    @ApiOperation(value="신용카드 등록")
    public ApiResponse<?> uploadCredit(@RequestBody CreditRequestDto.UploadCreditRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 업로드 요청 : memberId = {}, credit = {}", loginMemberId, requestDto.getCreditName());
        return ApiResponse.onSuccess(creditService.upload(requestDto, loginMemberId));
    }

    @GetMapping("/all")
    @ApiOperation(value="신용카드 리스트 조회")
    public ApiResponse<?> getCreditList() {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(creditService.getCreditList(loginMemberId));
    }

    @DeleteMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idx", value = "삭제할 카드 Idx", required = true),
    })
    @ApiOperation(value="신용카드 삭제")
    public ApiResponse<?> deleteCredit(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(creditService.delete(idx, loginMemberId));
    }

    //카드 결제
    @PostMapping("/payCredit")
    public ResponseEntity<CreditResponseDto.CreditTaskSuccessResponseDto> payCredit(@RequestBody CreditRequestDto.PayCreditRequestDto requestDto) {

        Credit credit = creditService.findMemberIdxAndCreditNumber(getLoginMemberId(), requestDto.getCreditNumber());

        creditService.uploadCreditHistory(credit, requestDto.getAmountSum(), requestDto.getName());

        return ResponseEntity.ok(creditService.updateAmountSum(credit, requestDto.getAmountSum()));
    }

    //카드 결제 내역 조회
    @GetMapping("history")
    public ApiResponse<?> getCreditHistoryList(@RequestParam("creditIdx") Long creditIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 내역 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(creditService.getCreditHistoryList(creditIdx, loginMemberId));
    }

    @GetMapping("/history/detail")
    public ApiResponse<?> getCreditHistory(
            @RequestParam("creditIdx") Long creditIdx,
            @RequestParam("historyIdx") Long historyIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("신용카드 내역 상세 조회 요청 : loginMemberId = {}, creditIdx = {}, historyIdx = {}", loginMemberId, creditIdx, historyIdx);
        return ApiResponse.onSuccess(creditService.getCreditHistoryDetail(creditIdx, historyIdx, loginMemberId));
    }

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
