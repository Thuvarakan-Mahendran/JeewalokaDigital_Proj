package com.jeewaloka.digital.jeewalokadigital.service;

import com.jeewaloka.digital.jeewalokadigital.dto.GRRNDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.ItemDTO;

public interface GRRNService {


    ItemDTO getItemByName(String itemName);

    GRRNDTO updateGRRN(Long grrnId, GRRNDTO grrnDTO);

    GRRNDTO addGRRNWithItems(GRRNDTO grrnDTO); // Method to save GRRN and associated GRRNItems

}
