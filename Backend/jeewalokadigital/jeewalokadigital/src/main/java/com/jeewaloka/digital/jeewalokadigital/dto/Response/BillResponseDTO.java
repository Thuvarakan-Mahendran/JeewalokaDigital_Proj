package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import com.jeewaloka.digital.jeewalokadigital.enums.BillCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO{
    private Long BillNO;
    private Long userID;
    private Long retailerID;
    private Float total;
    private LocalDate date;
    private BillCategory billCategory;
//    private List<Long> billItemIDS;
}
