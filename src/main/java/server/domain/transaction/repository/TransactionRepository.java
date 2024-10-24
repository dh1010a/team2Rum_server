package server.domain.transaction.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.domain.transaction.domain.Transaction;
import server.domain.transaction.mapper.TransactionMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final TransactionMapper transactionMapper;

    public void save(Transaction transaction) {
        transactionMapper.save(transaction);
    }

    // 거래내역 ID로 조회
    public Optional<Transaction> findByTransactionIdx(Long TransactionIdx) {
        Transaction transaction = transactionMapper.findByTransactionIdx(TransactionIdx);
        if(transaction != null) {
            return Optional.of(transaction);
        }

        return Optional.empty();
    }

    // 특정 카드의 거래내역 조회
    public List<Transaction> findTransactionsByCreditIdx(Long creditIdx) {
        return transactionMapper.findTransactionsByCreditIdx(creditIdx);
    }

    // 특정 회원의 모든 거래내역 조회
    public List<Transaction> findAllTransactionByMemberIdx(Long memberIdx) {
        return transactionMapper.findAllByMemberIdx(memberIdx);
    }

    // 거래내역 업데이트
    public Transaction updateTransaction(Transaction transaction) {
        transactionMapper.updateTransaction(transaction);
        return transaction;
    }


    // 거래내역 삭제
    public void delete(Long idx) {
        transactionMapper.delete(idx);
    }

    public boolean existsByTransactionIdxAndMemberIdx(Long idx, Long memberIdx) {
        Map<String, Object> map = Map.of("idx", idx, "memberIdx", memberIdx);
        Transaction transaction = transactionMapper.findByIdxAndMemberIdx(map);
        return transaction != null;
    }


    public List<Transaction> findAllByCreditIdx(Long creditIdx) {
        return transactionMapper.findTransactionsByCreditIdx(creditIdx);
    }
}
