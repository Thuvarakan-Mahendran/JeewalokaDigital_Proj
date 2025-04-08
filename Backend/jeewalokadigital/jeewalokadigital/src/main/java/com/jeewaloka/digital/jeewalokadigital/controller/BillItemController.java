package com.jeewaloka.digital.jeewalokadigital.controller;

import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillItemRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Request.BillRequestDTO;
import com.jeewaloka.digital.jeewalokadigital.dto.Response.BillItemResponseDTO;
import com.jeewaloka.digital.jeewalokadigital.service.BillItemService;
import com.jeewaloka.digital.jeewalokadigital.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/BillItem")
//@CrossOrigin
public class BillItemController {
    @Autowired
    private BillItemService billItemService;

    @PostMapping("/createBillItem")
    public ResponseEntity<BillItemResponseDTO> addBillItem(@RequestBody BillItemRequestDTO billItemDTO){
        BillItemResponseDTO billDTO1 = billItemService.addBillItem(billItemDTO);
        ResponseEntity<BillItemResponseDTO> responseEntity = new ResponseEntity<>(billDTO1, HttpStatus.CREATED);
        return responseEntity;
    }

    @PostMapping("/createBillItems")
    public String addBillItems(@RequestBody List<BillItemRequestDTO> billItemRequestDTOList){
        System.out.println(billItemRequestDTOList);
        billItemService.addBillItems(billItemRequestDTOList);
//        ResponseEntity<List<BillItemResponseDTO>> responseEntity = new ResponseEntity<>(billItemResponseDTOList,HttpStatus.CREATED);
        return "BillItems are created";
    }

    @DeleteMapping("/deleteBillItem/{id}")
    public void deleteBillItem(@PathVariable Long id){
        billItemService.deleteBillItem(id);
    }

    @GetMapping("/getBillItems")
    public ResponseEntity<List<BillItemResponseDTO>> getBillItems(){
        List<BillItemResponseDTO> billItemResponseDTOS = billItemService.getBillItems();
        ResponseEntity<List<BillItemResponseDTO>> responseEntity = new ResponseEntity<>(billItemResponseDTOS,HttpStatus.OK);
        return responseEntity;
    }
}
