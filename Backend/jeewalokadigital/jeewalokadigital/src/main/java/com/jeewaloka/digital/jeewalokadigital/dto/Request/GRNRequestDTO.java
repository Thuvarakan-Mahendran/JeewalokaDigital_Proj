package com.jeewaloka.digital.jeewalokadigital.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GRNRequestDTO {
    private Long grnSupplierId;
    private String grnReceivedBy;
    private String grnStatus;
    private List<GRNItemRequestDTO> grnItems;
}
