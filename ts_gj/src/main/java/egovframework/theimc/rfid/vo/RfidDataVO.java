package egovframework.theimc.rfid.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfidDataVO {
    
    private String mtId;          // 단말기 아이디
    private String rfidNo;        // RFID번호
    private LocalDateTime tagDt;  // 태그일시
    private LocalDateTime regDt;  // 등록일시
}