
package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/Bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/saveBill")
    public ResponseEntity<BillResponseDTO> addBill(@RequestBody BillRequestDTO billDTO){
        BillResponseDTO billDTO1 = billService.addBill(billDTO);
        ResponseEntity<BillResponseDTO> responseEntity = new ResponseEntity<>(billDTO1, HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/getBills")
    public ResponseEntity<List<BillResponseDTO>> findAllBills(){
        List<BillResponseDTO> BillDTOS = billService.findAllBills();
        ResponseEntity<List<BillResponseDTO>> responseEntity = new ResponseEntity<>(BillDTOS, HttpStatus.OK);
        return responseEntity;
    }
}

