package server.domain.transaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.domain.transaction.domain.Transaction;
import server.domain.transaction.dto.TransactionDtoConverter;
import server.domain.transaction.dto.TransactionRequestDto;
import server.domain.transaction.dto.TransactionResponseDto;
import server.domain.transaction.repository.TransactionRepository;
import server.domain.member.domain.Member;
import server.domain.member.repository.MemberRepository;
import server.global.apiPayload.code.status.ErrorStatus;
import server.global.apiPayload.exception.handler.ErrorHandler;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MemberRepository memberRepository;

    // 거래 추가
    public TransactionResponseDto.TransactionTaskSuccessResponseDto upload(TransactionRequestDto.UploadTransactionRequestDto requestDto, String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .memberIdx(memberIdx)
                .time(LocalDateTime.now())
                .payMethod(Transaction.PayMethod.valueOf(requestDto.getPayMethod()))
                .amount(requestDto.getAmount())
                .memo(requestDto.getMemo())
                .category(Transaction.Category.valueOf(requestDto.getCategory()))
                .build();

        transactionRepository.save(transaction);

        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(transaction.getIdx())
                .build();
    }

   public Transaction getTransaction(Long idx, String loginMemberId) {
       Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
               .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

       Transaction transaction = transactionRepository.findByTransactionIdx(idx)
               .orElseThrow(() -> new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND));

       if(!transaction.getMemberIdx().equals(memberIdx)) {
              throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
       }

       return transaction;
    }

    // 특정 회원의 모든 거래 내역 조회
    public TransactionResponseDto.TransactionListResponseDto getAllTransactionList(String memberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return TransactionDtoConverter.convertToTransactionListResponseDto(transactionRepository.findAllTransactionByMemberIdx(memberIdx));
    }

    public TransactionResponseDto.TransactionTaskSuccessResponseDto delete(Long idx, String memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(!transactionRepository.existsByTransactionIdxAndMemberIdx(idx, member.getIdx())) {
            throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
        }

        transactionRepository.delete(idx);

        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(idx)
                .build();
    }

    public TransactionResponseDto.TransactionTaskSuccessResponseDto update(TransactionRequestDto.UpdateTransactionRequestDto requestDto, String loginMemberId) {
        Long memberIdx = memberRepository.getIdxByMemberId(loginMemberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Transaction transaction = transactionRepository.findByTransactionIdx(requestDto.getIdx())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND));

        if (!transaction.getMemberIdx().equals(memberIdx)) {
            throw new ErrorHandler(ErrorStatus.TRANSACTION_NOT_FOUND);
        }

//        transaction.setCreditIdx(requestDto.getCreditIdx());
//        transaction.setAccountIdx(requestDto.getAccountIdx());
        transaction.setPayMethod(Transaction.PayMethod.valueOf(requestDto.getPayMethod()));
        transaction.setAmount(requestDto.getAmount());
        transaction.setMemo(requestDto.getMemo());
        transaction.setCategory(Transaction.Category.valueOf(requestDto.getCategory()));
        transaction.setTranId(requestDto.getTranId());

        transactionRepository.updateTransaction(transaction);


        return TransactionResponseDto.TransactionTaskSuccessResponseDto.builder()
                .isSuccess(true)
                .idx(transaction.getIdx())
                .build();
    }
}