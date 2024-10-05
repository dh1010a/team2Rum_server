package server.domain.orderRoom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.orderRoom.model.OrderRoomStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 6494678977089006639L;

//    private Long idx;

    private Long orderIdx;

    private Long ownerMemberIdx;

    private int maxMemberCnt;

    private int memberCnt;

    private int totalPrice;

    private int currentPrice;

    private OrderRoomType type;

    private HashMap<Long, List<Long>> menuSelect;

    private HashMap<Long, Integer> menuAmount;

    private OrderRoomStatus status;

    private LocalDateTime createdAt;

    public void updateCurrentPrice(int price) {
        currentPrice += price;
    }

    public boolean plusMemberCnt() {
        if (memberCnt >= maxMemberCnt) {
            return false;
        }
        memberCnt++;
        return true;
    }

    public boolean minusMemberCnt() {
        if (memberCnt <= 0) {
            return false;
        }
        memberCnt--;
        return true;
    }

    public enum OrderRoomType {
        BY_PRICE, BY_MENU
    }

    public void updateOrderRoomType(String type) {
        if (type.equals("BY_PRICE")) {
            this.type = OrderRoomType.BY_PRICE;
        } else if (type.equals("BY_MENU")) {
            this.type = OrderRoomType.BY_MENU;
        }
    }

}
