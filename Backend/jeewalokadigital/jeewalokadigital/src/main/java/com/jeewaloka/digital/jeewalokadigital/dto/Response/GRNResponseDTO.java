package com.jeewaloka.digital.jeewalokadigital.dto.Response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class GRNResponseDTO {
    private Long grnId;
    private String grnCode;
    private String grnReceivedBy;
    private String grnSupplierName;
    private String grnStatus;
    private List<GRNItemResponseDTO> grnItems;
}
