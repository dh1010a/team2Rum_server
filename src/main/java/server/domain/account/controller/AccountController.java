package server.domain.account.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.account.domain.Account;
import server.domain.account.dto.AccountHistoryRequestDto;
import server.domain.account.dto.AccountRequestDto;
import server.domain.account.dto.AccountResponseDto;
import server.domain.account.repository.AccountRepository;
import server.domain.account.service.AccountService;
import server.global.apiPayload.ApiResponse;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;
import server.global.util.SecurityUtil;

import javax.security.auth.login.AccountNotFoundException;


@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    private String getLoginMemberId() {
        return SecurityUtil.getLoginMemberId().orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @PostMapping
    public ApiResponse<?> uploadAccount(@RequestBody AccountRequestDto.UploadAccountRequestDto requestDto) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 업로드 요청 : memberId = {}, accountNumber = {}", loginMemberId, requestDto.accountNumber);
        return ApiResponse.onSuccess(accountService.upload(requestDto, loginMemberId));
    }

    @GetMapping("/all")
    public ApiResponse<?> getAccountList() {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 리스트 조회 요청 : loginMemberId = {}", loginMemberId);
        return ApiResponse.onSuccess(accountService.getAccountList(loginMemberId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteAccount(@RequestParam(value = "idx") Long idx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 삭제 요청 : loginMemberId = {}, idx = {}", loginMemberId, idx);
        return ApiResponse.onSuccess(accountService.delete(idx, loginMemberId));
    }

    /**
     * 계좌 송금 로직
     * @param requestDto
     * @return
     */
    @PostMapping("/sendAccount")
    public ResponseEntity<AccountResponseDto.AccountTaskSuccessResponseDto> sendAccount(@RequestBody AccountRequestDto.sendAccount requestDto) {
        String loginMemberId = getLoginMemberId();

        Account fromAccount = accountService.findByMemberIdxAndAccountNumber(loginMemberId, requestDto.fromAccountNumber);
        if (fromAccount == null) {
            // 오류 처리
            System.out.println("fromAccount = " + fromAccount);
        }
        Account toAccount = accountService.findByAccountNumber(requestDto.getToAccountNumber());
        AccountResponseDto.AccountTaskSuccessResponseDto accountTaskSuccessResponseDto = accountService.updateAmount(fromAccount, toAccount, requestDto.getAmount());

        // 히스토리 업로드
       accountService.uploadAccountHistory(fromAccount, toAccount, requestDto.getAmount(),requestDto.getName());

        return ResponseEntity.ok(accountTaskSuccessResponseDto);
    }

    // 계좌 히스토리 리스트 조회
    @GetMapping("/history")
    public ApiResponse<?> getAccountHistoryList(@RequestParam("accountIdx") Long accountIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 히스토리 리스트 조회 요청 : loginMemberId = {}, accountIdx = {}", loginMemberId, accountIdx);
        return ApiResponse.onSuccess(accountService.getAccountHistoryList(accountIdx, loginMemberId));
    }

    // 계좌의 특정 히스토리 조회
    @GetMapping("/history/detail")
    public ApiResponse<?> getAccountHistory(
            @RequestParam("accountIdx") Long accountIdx,
            @RequestParam("historyIdx") Long historyIdx) {
        String loginMemberId = getLoginMemberId();
        log.info("계좌 히스토리 조회 요청 : loginMemberId = {}, accountIdx = {}, historyIdx = {}",
                loginMemberId, accountIdx, historyIdx);
        return ApiResponse.onSuccess(accountService.getAccountHistoryDetail(accountIdx, historyIdx, loginMemberId));
    }




}
