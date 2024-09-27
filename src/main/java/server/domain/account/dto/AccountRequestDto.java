package server.domain.account.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

public class AccountRequestDto {

    @Data
    public static class UploadAccountRequestDto {
       public String amount;
       public String bankName;
       public String accountHolderName;
       public String accountNumber;
       public String accountSecret;

       @JsonCreator
         public UploadAccountRequestDto(
                @JsonProperty("amount") String amount,
                @JsonProperty("bankName") String bankName,
                @JsonProperty("accountHolderName") String accountHolderName,
                @JsonProperty("accountNumber") String accountNumber,
                @JsonProperty("accountSecret") String accountSecret
         ) {
              this.amount = amount;
              this.bankName = bankName;
              this.accountHolderName = accountHolderName;
              this.accountNumber = accountNumber;
              this.accountSecret = accountSecret;
         }
    }

    @Data
    public static class UpdateAccountAmountRequestDto {
        public Long idx;
        public String amount;


        @JsonCreator
        public UpdateAccountAmountRequestDto(
                @JsonProperty("idx") Long idx,
                @JsonProperty("amount") String amount
        ) {
            this.idx = idx;
            this.amount = amount;
        }
    }


}



